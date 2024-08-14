package com.tirexmurina.shared.user.source

import com.tirexmurina.shared.user.core.data.IdSharedPrefsCorruptedException
import com.tirexmurina.shared.user.core.data.UserCreationException
import com.tirexmurina.shared.user.core.data.UserNotFoundException
import com.tirexmurina.shared.user.core.data.models.AuthModel
import com.tirexmurina.shared.user.core.domain.repository.UserRepository
import com.tirexmurina.shared.user.source.data.UserDao
import com.tirexmurina.shared.user.source.data.models.UserModelAdapter
import com.tirexmurina.util.core.exeptions.DatabaseCorruptedException
import com.tirexmurina.util.source.data.IdDataStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
    private val userDao: UserDao,
    private val userModelAdapter: UserModelAdapter,
    private val idDataStore: IdDataStore,
    private val dispatcherIO: CoroutineDispatcher
) : UserRepository {

    override suspend fun register(authModel: AuthModel) {
        withContext(dispatcherIO) {
            try {
                val registrationResult = userDao.createUser(userModelAdapter.convert(authModel))
                //тут Long потому что Room только лонг возвращает
                if (registrationResult < 0) {
                    throw UserCreationException("User creation process gone wrong")
                }
                idDataStore.setUserId(registrationResult.toString())
                if (idDataStore.getUserId() != registrationResult.toString()) {
                    throw IdSharedPrefsCorruptedException("User id for session saving process gone wrong")
                }
            } catch (exception: Exception) {
                handleExceptions(exception)
            }
        }
    }

    override suspend fun login(authModel: AuthModel) {
        withContext(dispatcherIO) {
            try {
                val userId = userDao.getUserId(authModel.name, authModel.password)
                    ?: throw UserNotFoundException("User not found")
                idDataStore.setUserId(userId.toString())
                if (idDataStore.getUserId() != userId.toString()) {
                    throw IdSharedPrefsCorruptedException("User id for session saving process gone wrong")
                }
            } catch (exception: Exception) {
                handleExceptions(exception)
            }
        }
    }

    override suspend fun sessionAvailable(): Boolean {
        return withContext(dispatcherIO) {
            try {
                idDataStore.isUserIdSaved()
            } catch (exception: Exception) {
                handleExceptions(exception)
            }
        }
    }

    override suspend fun clearSession() {
        withContext(dispatcherIO) {
            try {
                idDataStore.clearUserId()
            } catch (exception: Exception) {
                handleExceptions(exception)
            }
        }
    }

    private fun handleExceptions(exception: Exception): Nothing {
        when (exception) {
            is UserCreationException -> {
                throw exception
            }

            is IdSharedPrefsCorruptedException -> {
                throw exception
            }

            else -> {
                throw DatabaseCorruptedException("Problems with database acquired")
            }
        }
    }

}