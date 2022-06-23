package com.burakcanduzcan.contactslite.utils

import timber.log.Timber

class CustomDebugTree : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String {
        with(element) {
            return "($fileName:$lineNumber)$methodName()"
        }
    }
}