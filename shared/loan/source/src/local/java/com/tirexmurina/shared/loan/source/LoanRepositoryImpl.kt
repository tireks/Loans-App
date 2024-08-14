package com.tirexmurina.shared.loan.source

import com.tirexmurina.shared.loan.core.data.models.LoanConditionsModel
import com.tirexmurina.shared.loan.core.data.models.LoanRequestModel
import com.tirexmurina.shared.loan.core.domain.entity.Loan
import com.tirexmurina.shared.loan.core.domain.repository.LoanRepository
import com.tirexmurina.shared.loan.source.data.LoanConditionsGenerator
import com.tirexmurina.shared.loan.source.data.LoanDao
import com.tirexmurina.shared.loan.source.data.models.LoanModelAdapter
import com.tirexmurina.shared.user.core.data.IdEmptyException
import com.tirexmurina.util.core.exeptions.DatabaseCorruptedException
import com.tirexmurina.util.source.data.IdDataStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class LoanRepositoryImpl(
    private val loanDao: LoanDao,
    private val idDataStore: IdDataStore,
    private val loanModelAdapter: LoanModelAdapter,
    private val loanConditionsGenerator: LoanConditionsGenerator,
    private val dispatcherIO: CoroutineDispatcher
) : LoanRepository {

    override suspend fun getLoans(): List<Loan> {
        return withContext(dispatcherIO) {
            try {
                val userId = idDataStore.getUserId()
                if (userId.isNullOrEmpty()) {
                    throw IdEmptyException("some problem with session's user id required")
                }
                loanDao.getLoansByUserId(userId.toLong()).map { loanModelAdapter.convert(it) }
            } catch (exception: IdEmptyException) {
                handleExceptions(exception)
            }
        }
    }

    override suspend fun getLoanById(id: Long): Loan {
        return withContext(dispatcherIO) {
            try {
                val userId = idDataStore.getUserId()
                if (userId.isNullOrEmpty()) {
                    throw IdEmptyException("some problem with session's user id required")
                }
                loanModelAdapter.convert(loanDao.getLoanById(id))
            } catch (exception: IdEmptyException) {
                handleExceptions(exception)
            }
        }
    }

    override suspend fun getLoanConditions(): LoanConditionsModel =
        loanConditionsGenerator.generate()

    override suspend fun requestLoan(loan: LoanRequestModel) {
        withContext(dispatcherIO) {
            try {
                val userId = idDataStore.getUserId()
                if (userId.isNullOrEmpty()) {
                    throw IdEmptyException("some problem with session's user id required")
                }
                val loanObject = loanModelAdapter.generate(loan, userId.toInt())
                loanDao.insertLoan(loanObject)
            } catch (exception: IdEmptyException) {
                handleExceptions(exception)
            }
        }
    }

    private fun handleExceptions(exception: Exception): Nothing {
        when (exception) {
            is IdEmptyException -> {
                throw exception
            }

            else -> {
                throw DatabaseCorruptedException("Problems with database acquired")
            }
        }
    }
}