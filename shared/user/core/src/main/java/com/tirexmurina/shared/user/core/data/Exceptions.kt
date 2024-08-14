package com.tirexmurina.shared.user.core.data

class TokenEmptyException(errMsg: String) : Exception(errMsg)

class SharedPrefsCorruptedException(errMsg: String) : Exception(errMsg)

class UserCreationException(errMsg: String) : Exception(errMsg)

class UserNotFoundException(errMsg: String) : Exception(errMsg)

class UnauthorizedException(message: String) : Exception(message)

class ForbiddenException(message: String) : Exception(message)

class NotFoundException(message: String) : Exception(message)

class ResponseFault(message: String) : Exception(message)

class RequestFault(message: String) : Exception(message)

class NetworkFault(message: String) : Exception(message)

class IdEmptyException(errMsg: String) : Exception(errMsg)

class IdSharedPrefsCorruptedException(errMsg: String) : Exception(errMsg)