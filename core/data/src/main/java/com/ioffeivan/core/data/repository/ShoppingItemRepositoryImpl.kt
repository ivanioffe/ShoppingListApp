package com.ioffeivan.core.data.repository

import com.ioffeivan.core.common.Result
import com.ioffeivan.core.data.mapper.toDomain
import com.ioffeivan.core.data.mapper.toEntities
import com.ioffeivan.core.data.mapper.toEntity
import com.ioffeivan.core.data.source.local.ShoppingItemLocalDataSource
import com.ioffeivan.core.data.source.local.ShoppingListLocalDataSource
import com.ioffeivan.core.data.source.remote.ShoppingItemRemoteDataSource
import com.ioffeivan.core.data.sync.ShoppingItemSyncStarter
import com.ioffeivan.core.database.dao.ShoppingItemOutboxDao
import com.ioffeivan.core.database.model.ShoppingItemEntity
import com.ioffeivan.core.database.model.ShoppingItemOutboxEntity
import com.ioffeivan.core.database.model.ShoppingItemOutboxOperation
import com.ioffeivan.core.domain.repository.ShoppingItemRepository
import com.ioffeivan.core.model.ShoppingItem
import com.ioffeivan.core.model.ShoppingItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import javax.inject.Inject

class ShoppingItemRepositoryImpl @Inject constructor(
    private val shoppingItemRemoteDataSource: ShoppingItemRemoteDataSource,
    private val shoppingItemLocalDataSource: ShoppingItemLocalDataSource,
    private val shoppingItemOutboxDao: ShoppingItemOutboxDao,
    private val shoppingListLocalDataSource: ShoppingListLocalDataSource,
    private val shoppingItemSyncStarter: ShoppingItemSyncStarter,
) : ShoppingItemRepository {

    private val remoteShoppingItemsFlow = MutableSharedFlow<Result<ShoppingItems>>(replay = 1)

    override suspend fun refreshShoppingItems(listId: Int) {
        val shoppingListServerId = shoppingListLocalDataSource.getShoppingList(listId).serverId

        shoppingItemRemoteDataSource.getShoppingItems(shoppingListServerId ?: 0)
            .collect { result ->
                when (result) {
                    is Result.Error -> remoteShoppingItemsFlow.emit(Result.Error(result.message))

                    Result.Loading -> remoteShoppingItemsFlow.emit(Result.Loading)

                    is Result.Success -> {
                        shoppingItemLocalDataSource.upsertShoppingItems(
                            result.data.toEntities(listId)
                        )
                    }
                }
            }
    }

    override suspend fun addShoppingItem(shoppingItem: ShoppingItem) {
        val id = shoppingItemLocalDataSource.upsertShoppingItem(shoppingItem.toEntity())
        shoppingItemOutboxDao.insertShoppingItemOutbox(
            shoppingItemOutboxEntity = ShoppingItemOutboxEntity(
                itemId = id.toInt(),
                operation = ShoppingItemOutboxOperation.ADD,
            )
        )
        shoppingItemSyncStarter.startSync()
    }

    override suspend fun deleteShoppingItem(id: Int) {
        shoppingItemOutboxDao.insertShoppingItemOutbox(
            ShoppingItemOutboxEntity(
                itemId = id,
                operation = ShoppingItemOutboxOperation.DELETE,
            )
        )
        shoppingItemSyncStarter.startSync()
    }

    override fun observeShoppingItems(listId: Int): Flow<Result<ShoppingItems>> {
        return merge(
            remoteShoppingItemsFlow,
            shoppingItemLocalDataSource.observeShoppingItems(listId)
                .map { result ->
                    when (result) {
                        is Result.Error -> Result.Error(result.message)
                        Result.Loading -> Result.Loading
                        is Result.Success -> {
                            val shoppingItems =
                                ShoppingItems(items = result.data.map(ShoppingItemEntity::toDomain))
                            Result.Success(shoppingItems)
                        }
                    }
                },
        )
    }
}