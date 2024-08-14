package com.tirexmurina.shared.loan.source.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tirexmurina.shared.loan.source.data.models.LoanLocalModel

@Dao
interface LoanDao {

    @Query("SELECT * FROM loans WHERE userLinkedId = :userId")
    suspend fun getLoansByUserId(userId: Long): List<LoanLocalModel>

    @Query("SELECT * FROM loans WHERE loanId = :loanId LIMIT 1")
    suspend fun getLoanById(loanId: Long): LoanLocalModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLoan(loan: LoanLocalModel)


}