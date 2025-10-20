package com.ioffeivan.sync.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ioffeivan.core.database.dao.ShoppingListOutboxDao
import com.ioffeivan.core.database.model.ShoppingListOutboxOperation
import com.ioffeivan.sync.coordinator.ShoppingListSyncCoordinator
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
internal class ShoppingListSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val shoppingListSyncCoordinator: ShoppingListSyncCoordinator,
    private val shoppingListOutboxDao: ShoppingListOutboxDao,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            shoppingListOutboxDao.getAllShoppingListsOutbox()
                .forEach { outbox ->
                    when (outbox.operation) {
                        ShoppingListOutboxOperation.CREATE -> {
                            shoppingListSyncCoordinator.createShoppingList(outbox.listId)
                            shoppingListOutboxDao.deleteShoppingListOutbox(outbox.id)
                        }

                        // For Operation.DELETE we don't need to explicitly remove the outbox row.
                        // The FK (shopping_lists_outbox.list_id -> shopping_lists.id) is defined with
                        // ON DELETE CASCADE, so deleting the ShoppingList will automatically remove
                        // the related shopping_lists_outbox row in the database.
                        ShoppingListOutboxOperation.DELETE -> {
                            shoppingListSyncCoordinator.deleteShoppingList(outbox.listId)
                        }
                    }
                }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}