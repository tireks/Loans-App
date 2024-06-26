package com.tirexmurina.shared.user.core.data.remote

class UnauthorizedException(message: String) : Exception(message)

class ForbiddenException(message: String) : Exception(message)

class NotFoundException(message: String) : Exception(message)

class ResponseFault(message: String) : Exception(message)

class RequestFault(message: String) : Exception(message)

class NetworkFault(message: String) : Exception(message)