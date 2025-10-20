package com.ioffeivan.core.domain.repository

import com.ioffeivan.core.common.Result
import com.ioffeivan.core.model.CreateShoppingList
import com.ioffeivan.core.model.ShoppingLists
import kotlinx.coroutines.flow.Flow

interface ShoppingListRepository {

    suspend fun refreshShoppingLists()

    suspend fun createShoppingList(createShoppingList: CreateShoppingList)

    suspend fun deleteShoppingList(id: Int)

    fun observeShoppingLists(): Flow<Result<ShoppingLists>>
}