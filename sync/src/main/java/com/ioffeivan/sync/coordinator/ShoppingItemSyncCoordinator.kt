package com.ioffeivan.sync.coordinator

import com.ioffeivan.core.common.Result
import com.ioffeivan.core.data.mapper.toAddShoppingItemDto
import com.ioffeivan.core.data.mapper.toDomain
import com.ioffeivan.core.data.mapper.toShoppingItemEntity
import com.ioffeivan.core.data.source.local.ShoppingItemLocalDataSource
import com.ioffeivan.core.data.source.local.ShoppingListLocalDataSource
import com.ioffeivan.core.data.source.remote.ShoppingItemRemoteDataSource
import com.ioffeivan.core.data.source.remote.model.DeleteShoppingItemDto
import javax.inject.Inject

class ShoppingItemSyncCoordinator @Inject constructor(
    private val shoppingItemRemoteDataSource: ShoppingItemRemoteDataSource,
    private val shoppingItemLocalDataSource: ShoppingItemLocalDataSource,
    private val shoppingListLocalDataSource: ShoppingListLocalDataSource,
) {
    suspend fun addShoppingItem(id: Int) {
        val shoppingItem = shoppingItemLocalDataSource.getShoppingItem(id).toDomain()
        val shoppingListServerId =
            shoppingListLocalDataSource.getShoppingList(shoppingItem.listId).serverId
                ?: throw Exception()

        shoppingItemRemoteDataSource.addShoppingItem(
            shoppingItem.toAddShoppingItemDto(shoppingListServerId)
        ).collect { result ->
            when (result) {
                is Result.Success -> {
                    val shoppingItemEntity = result.data.toShoppingItemEntity(
                        shoppingItem = shoppingItem,
                    )
                    shoppingItemLocalDataSource.upsertShoppingItem(
                        shoppingItemEntity = shoppingItemEntity,
                    )
                }

                is Result.Error -> {
                    throw Exception()
                }

                else -> {}
            }
        }
    }

    suspend fun deleteShoppingItem(id: Int) {
        val shoppingItemEntity = shoppingItemLocalDataSource.getShoppingItem(id)
        val shoppingListServerId =
            shoppingListLocalDataSource.getShoppingList(shoppingItemEntity.listId).serverId
                ?: throw Exception()
        val shoppingItemServerId = shoppingItemEntity.serverId ?: throw Exception()

        shoppingItemRemoteDataSource.deleteShoppingItem(
            deleteShoppingItemDto = DeleteShoppingItemDto(
                listId = shoppingListServerId,
                itemId = shoppingItemServerId,
            )
        ).collect { result ->
            when (result) {
                is Result.Success -> {
                    shoppingItemLocalDataSource.deleteShoppingItem(id)
                }

                is Result.Error -> {
                    throw Exception()
                }

                else -> {}
            }
        }
    }
}