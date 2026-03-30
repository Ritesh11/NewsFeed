package com.ritesh.newsfeed.domain.syncmanager

import com.ritesh.newsfeed.data.util.Resource
import com.ritesh.newsfeed.domain.usecase.GetNewsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsSyncManager @Inject constructor(
    // Injecting the UseCase or Repository
    private val getNewsUseCase: GetNewsUseCase
) {
    private val syncScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var syncJob: Job? = null

    fun startSync() {
        if (syncJob?.isActive == true) return

        syncJob = syncScope.launch {
            while (isActive) {

                // Perform the background fetch
                // forceRefresh = true ensures we hit the API, not just the DB
                getNewsUseCase.execute("us", 1, true).first { resource ->
                    // This block executes when the first value is emitted
                    resource is Resource.Success || resource is Resource.Error
                }

                // Wait 15 minutes before the next hit
                delay(/*15  **/60 * 1000)

            }
        }
    }

    fun stopSync() {
        syncJob?.cancel()
    }
}