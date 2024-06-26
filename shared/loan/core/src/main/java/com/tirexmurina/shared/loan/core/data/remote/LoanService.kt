package com.tirexmurina.shared.loan.core.data.remote


import com.tirexmurina.shared.loan.core.data.models.LoanConditionsModel
import com.tirexmurina.shared.loan.core.data.models.LoanRequestModel
import com.tirexmurina.shared.loan.core.domain.entity.Loan
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface LoanService {

    @POST("/loans")
    suspend fun requestLoan(
        @Body request: LoanRequestModel
    ): Response<Loan>

    @GET("/loans/all")
    suspend fun getLoans(): Response<List<Loan>>

    @GET("/loans/{id}")
    suspend fun getLoanById(
        @Path("id") id: Long
    ): Response<Loan>

    @GET("/loans/conditions")
    suspend fun getLoanConditions(): Response<LoanConditionsModel>

}
