package com.tirexmurina.util.core.exeptions

import java.lang.Exception

class SuccessfulThrowable(message : String) : Throwable(message)

class UnsuccessfulException(message: String) : Exception(message)