package com.ritesh.newsfeed

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication
import kotlin.jvm.java

class HiltTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        // This replaces NewsApplication with HiltTestApplication during tests
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}