package com.tirexmurina.shared.loan.core.data

class SingleLoanCannotFind(errMsg: String) : Exception(errMsg)
class LoanConditionsCannotFind(errMsg: String) : Exception(errMsg)

class RequestFault(errMsg: String) : Exception(errMsg)

class UnauthorizedException(message: String) : Exception(message)

class ForbiddenException(message: String) : Exception(message)

class NotFoundException(message: String) : Exception(message)

class ResponseFault(message: String) : Exception(message)

class NetworkFault(message: String) : Exception(message)