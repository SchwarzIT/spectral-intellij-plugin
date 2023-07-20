package com.schwarzit.spectralIntellijPlugin

import com.intellij.openapi.diagnostic.Logger

inline fun <reified T> T.getLogger(): Logger {
    if (T::class.isCompanion) {
        return Logger.getInstance(T::class.java.enclosingClass)
    }
    return Logger.getInstance(T::class.java)
}