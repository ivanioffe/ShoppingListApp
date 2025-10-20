package com.ioffeivan.sync.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import com.ioffeivan.core.database.dao.ShoppingItemOutboxDao
import com.ioffeivan.core.database.model.ShoppingItemOutboxOperation
import com.ioffeivan.sync.coordinator.ShoppingItemSyncCoordinator
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
internal class ShoppingItemSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val shoppingItemSyncCoordinator: ShoppingItemSyncCoordinator,
    private val shoppingItemOutboxDao: ShoppingItemOutboxDao,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            shoppingItemOutboxDao.getAllShoppingItemsOutbox()
                .forEach { outbox ->
                    when (outbox.operation) {
                        ShoppingItemOutboxOperation.ADD -> {
                            shoppingItemSyncCoordinator.addShoppingItem(outbox.itemId)
                            shoppingItemOutboxDao.deleteShoppingItemOutbox(outbox.id)
                        }

                        // For Operation.DELETE we don't need to explicitly remove the outbox row.
                        // The FK (shopping_items_outbox.item_id -> shopping_items.id) is defined with
                        // ON DELETE CASCADE, so deleting the ShoppingItem will automatically remove
                        // the related shopping_items_outbox row in the database.
                        ShoppingItemOutboxOperation.DELETE -> {
                            shoppingItemSyncCoordinator.deleteShoppingItem(outbox.itemId)
                        }
                    }
                }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        fun startUpSyncWork(): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<ShoppingItemSyncWorker>()
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .build()
        }
    }
}