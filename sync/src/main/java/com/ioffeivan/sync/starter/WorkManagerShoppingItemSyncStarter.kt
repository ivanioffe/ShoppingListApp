package com.ioffeivan.sync.starter

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.ioffeivan.core.data.sync.ShoppingItemSyncStarter
import com.ioffeivan.sync.worker.ShoppingItemSyncWorker

class WorkManagerShoppingItemSyncStarter(
    private val workManager: WorkManager,
) : ShoppingItemSyncStarter {

    override fun startSync() {
        val request = OneTimeWorkRequestBuilder<ShoppingItemSyncWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        workManager
            .beginUniqueWork(
                uniqueWorkName = SHOPPING_ITEM_SYNC_WORK_NAME,
                existingWorkPolicy = ExistingWorkPolicy.APPEND,
                request = request,
            )
            .enqueue()
    }
}

internal const val SHOPPING_ITEM_SYNC_WORK_NAME = "ShoppingItemSyncWork"