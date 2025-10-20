package com.ioffeivan.core.data.source.local

import com.ioffeivan.core.common.Result
import com.ioffeivan.core.database.model.ShoppingListEntity
import kotlinx.coroutines.flow.Flow

interface ShoppingListLocalDataSource {

    fun observeAllShoppingLists(): Flow<Result<List<ShoppingListEntity>>>

    suspend fun upsertShoppingLists(shoppingLists: List<ShoppingListEntity>)

    suspend fun upsertShoppingList(shoppingList: ShoppingListEntity): Long

    suspend fun deleteShoppingList(id: Int)

    suspend fun getShoppingList(id: Int): ShoppingListEntity
}