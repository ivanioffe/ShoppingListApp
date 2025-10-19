package com.ioffeivan.core.data.source.remote

import com.ioffeivan.core.common.Result
import com.ioffeivan.core.data.source.remote.model.CreateShoppingListDto
import com.ioffeivan.core.data.source.remote.model.CreatedShoppingListDto
import com.ioffeivan.core.data.source.remote.model.ShoppingListsDto
import kotlinx.coroutines.flow.Flow

interface ShoppingListRemoteDataSource {

    fun getAllShoppingLists(): Flow<Result<ShoppingListsDto>>

    fun createShoppingList(
        createShoppingListDto: CreateShoppingListDto,
    ): Flow<Result<CreatedShoppingListDto>>

    fun deleteShoppingList(id: Int): Flow<Result<Unit>>
}