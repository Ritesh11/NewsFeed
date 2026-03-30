package com.ritesh.newsfeed.domain.syncmanager

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import javax.inject.Inject

class AppLifecycleObserver @Inject constructor(
    private val syncManager: NewsSyncManager
) : DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        // App came to foreground
        syncManager.startSync()
    }

    override fun onStop(owner: LifecycleOwner) {
        // App went to background
        syncManager.stopSync()
    }
}