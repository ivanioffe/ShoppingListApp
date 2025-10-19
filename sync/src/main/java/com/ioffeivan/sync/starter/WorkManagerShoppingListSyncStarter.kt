package com.ioffeivan.sync.starter

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.ioffeivan.core.data.sync.ShoppingListSyncStarter
import com.ioffeivan.sync.worker.ShoppingListSyncWorker

class WorkManagerShoppingListSyncStarter(
    private val workManager: WorkManager,
) : ShoppingListSyncStarter {

    override fun startSync() {
        val request = OneTimeWorkRequestBuilder<ShoppingListSyncWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        workManager
            .beginUniqueWork(
                uniqueWorkName = SHOPPING_LIST_SYNC_WORK_NAME,
                existingWorkPolicy = ExistingWorkPolicy.APPEND,
                request = request,
            )
            .enqueue()
    }
}

internal const val SHOPPING_LIST_SYNC_WORK_NAME = "ShoppingListSyncWork"