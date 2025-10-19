package com.ioffeivan.core.data.source.remote

import com.ioffeivan.core.common.Result
import com.ioffeivan.core.data.source.remote.model.AddShoppingItemDto
import com.ioffeivan.core.data.source.remote.model.AddedShoppingItemDto
import com.ioffeivan.core.data.source.remote.model.DeleteShoppingItemDto
import com.ioffeivan.core.data.source.remote.model.ShoppingItemsDto
import kotlinx.coroutines.flow.Flow

interface ShoppingItemRemoteDataSource {

    fun addShoppingItem(
        addShoppingItemDto: AddShoppingItemDto,
    ): Flow<Result<AddedShoppingItemDto>>

    suspend fun deleteShoppingItem(
        deleteShoppingItemDto: DeleteShoppingItemDto,
    ): Flow<Result<Unit>>

    fun getShoppingItems(listId: Int): Flow<Result<ShoppingItemsDto>>
}