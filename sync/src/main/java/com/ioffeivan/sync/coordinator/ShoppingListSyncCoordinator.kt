package com.ioffeivan.sync.coordinator

import com.ioffeivan.core.common.Result
import com.ioffeivan.core.data.mapper.toCreateShoppingListDto
import com.ioffeivan.core.data.mapper.toDomain
import com.ioffeivan.core.data.mapper.toShoppingListEntity
import com.ioffeivan.core.data.source.local.ShoppingListLocalDataSource
import com.ioffeivan.core.data.source.remote.ShoppingListRemoteDataSource
import kotlinx.coroutines.flow.filterNot
import javax.inject.Inject

class ShoppingListSyncCoordinator @Inject constructor(
    private val shoppingListRemoteDataSource: ShoppingListRemoteDataSource,
    private val shoppingListLocalDataSource: ShoppingListLocalDataSource,
) {
    suspend fun createShoppingList(id: Int) {
        val shoppingList = shoppingListLocalDataSource.getShoppingList(id).toDomain()

        shoppingListRemoteDataSource.createShoppingList(shoppingList.toCreateShoppingListDto())
            .filterNot { it is Result.Loading }
            .collect { result ->
                when (result) {
                    is Result.Success -> {
                        val shoppingListEntity = result.data.toShoppingListEntity(shoppingList)
                        shoppingListLocalDataSource.upsertShoppingList(
                            shoppingList = shoppingListEntity,
                        )
                    }

                    is Result.Error -> {
                        throw Exception()
                    }

                    else -> {}
                }
            }
    }

    suspend fun deleteShoppingList(id: Int) {
        val shoppingListEntity = shoppingListLocalDataSource.getShoppingList(id)
        val shoppingListServerId = shoppingListEntity.serverId ?: throw Exception()

        shoppingListRemoteDataSource.deleteShoppingList(id = shoppingListServerId)
            .filterNot { it is Result.Loading }
            .collect { result ->
                when (result) {
                    is Result.Success -> {
                        shoppingListLocalDataSource.deleteShoppingList(id = id)
                    }

                    is Result.Error -> {
                        throw Exception()
                    }

                    else -> {}
                }
            }
    }
}