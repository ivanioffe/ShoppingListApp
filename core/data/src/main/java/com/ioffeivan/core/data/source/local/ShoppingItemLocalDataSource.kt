package com.ioffeivan.core.data.source.local

import com.ioffeivan.core.common.Result
import com.ioffeivan.core.database.model.ShoppingItemEntity
import kotlinx.coroutines.flow.Flow

interface ShoppingItemLocalDataSource {

    fun observeShoppingItems(listId: Int): Flow<Result<List<ShoppingItemEntity>>>

    suspend fun upsertShoppingItems(shoppingItems: List<ShoppingItemEntity>)

    suspend fun upsertShoppingItem(shoppingItemEntity: ShoppingItemEntity): Long

    suspend fun deleteShoppingItem(id: Int)

    suspend fun getShoppingItem(id: Int): ShoppingItemEntity
}