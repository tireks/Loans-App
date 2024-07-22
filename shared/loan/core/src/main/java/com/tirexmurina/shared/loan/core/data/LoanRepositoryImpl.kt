package com.tirexmurina.shared.loan.core.data


import com.tirexmurina.shared.loan.core.data.models.LoanConditionsModel
import com.tirexmurina.shared.loan.core.data.models.LoanRequestModel
import com.tirexmurina.shared.loan.core.data.remote.ForbiddenException
import com.tirexmurina.shared.loan.core.data.remote.LoanConditionsCannotFind
import com.tirexmurina.shared.loan.core.data.remote.LoanService
import com.tirexmurina.shared.loan.core.data.remote.NetworkFault
import com.tirexmurina.shared.loan.core.data.remote.NotFoundException
import com.tirexmurina.shared.loan.core.data.remote.RequestFault
import com.tirexmurina.shared.loan.core.data.remote.ResponseFault
import com.tirexmurina.shared.loan.core.data.remote.SingleLoanCannotFind
import com.tirexmurina.shared.loan.core.data.remote.UnauthorizedException
import com.tirexmurina.shared.loan.core.domain.entity.Loan
import com.tirexmurina.shared.loan.core.domain.repository.LoanRepository
import com.tirexmurina.util.core.exeptions.UnsuccessfulException
import retrofit2.Response
import java.io.IOException

class LoanRepositoryImpl(
    private val loanService: LoanService
) : LoanRepository {
    override suspend fun getLoans(): List<Loan> {
        return try {
            val response = loanService.getLoans()
            if (response.isSuccessful && response.code() == 200) {
                response.body() ?: emptyList()
            } else {
                handleErrorResponse(response)
            }
        } catch (e: IOException) {
            throw NetworkFault("Network error")
        } catch (e: Exception) {
            throw RequestFault("Request went wrong")
        }
    }

    override suspend fun getLoanById(id: Long): Loan {
        return try {
            val response = loanService.getLoanById(id)
            if (response.isSuccessful) {
                response.body() ?: throw SingleLoanCannotFind("Loan with ID $id not found")
            } else {
                handleErrorResponse(response)
            }
        } catch (e: IOException) {
            throw NetworkFault("Network error")
        } catch (e: Exception) {
            throw RequestFault("Request went wrong")
        }
    }

    override suspend fun getLoanConditions(): LoanConditionsModel {
        return try {
            val response = loanService.getLoanConditions()
            if (response.isSuccessful) {
                response.body() ?: throw LoanConditionsCannotFind("Loan conditions not found")
            } else {
                handleErrorResponse(response)
            }
        } catch (e: IOException) {
            throw NetworkFault("Network error")
        } catch (e: Exception) {
            throw RequestFault("Request went wrong")
        }
    }

    override suspend fun requestLoan(loan: LoanRequestModel) {
        try {
            val response = loanService.requestLoan(loan)
            if (response.isSuccessful) {
                if (response.code() != 200) {
                    throw UnsuccessfulException("Request unsuccessful")
                }
            } else {
                handleErrorResponse(response)
            }
        } catch (e: IOException) {
            throw NetworkFault("Network error")
        } catch (e: Exception) {
            throw RequestFault("Request went wrong")
        }
    }

    private fun <T> handleErrorResponse(response: Response<T>): Nothing {
        val errorMessage = response.errorBody()?.string()
        when (response.code()) {
            401 -> throw UnauthorizedException("Unauthorized: ${response.code()} $errorMessage")
            403 -> throw ForbiddenException("Forbidden: ${response.code()} $errorMessage")
            404 -> throw NotFoundException("Not Found: ${response.code()} $errorMessage")
            else -> throw ResponseFault("Something went wrong with response: ${response.code()} $errorMessage")
        }
    }
}