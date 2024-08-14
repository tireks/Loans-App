package com.tirexmurina.shared.database.core.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tirexmurina.shared.loan.source.data.LoanDao
import com.tirexmurina.shared.loan.source.data.models.LoanLocalModel
import com.tirexmurina.shared.user.source.data.UserDao
import com.tirexmurina.shared.user.source.data.models.UserLocalModel

@Database(
    entities = [LoanLocalModel::class, UserLocalModel::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun loanDao(): LoanDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}