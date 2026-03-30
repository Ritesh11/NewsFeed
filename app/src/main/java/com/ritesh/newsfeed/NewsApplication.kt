package com.ritesh.newsfeed

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.ritesh.newsfeed.domain.syncmanager.AppLifecycleObserver
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class NewsApplication: Application(){
    @Inject
    lateinit var lifecycleObserver: AppLifecycleObserver

    override fun onCreate() {
        super.onCreate()
        // Start watching the app's foreground/background status
        ProcessLifecycleOwner.get().lifecycle.addObserver(lifecycleObserver)
    }
}