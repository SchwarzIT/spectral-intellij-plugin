package com.schwarzit.spectralIntellijPlugin

class SpectralException : Exception {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
