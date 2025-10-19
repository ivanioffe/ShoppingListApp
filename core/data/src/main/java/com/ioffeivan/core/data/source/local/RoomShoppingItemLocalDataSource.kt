package com.ioffeivan.core.data.source.local

import com.ioffeivan.core.common.Result
import com.ioffeivan.core.common.toResultFlow
import com.ioffeivan.core.database.dao.ShoppingItemDao
import com.ioffeivan.core.database.model.ShoppingItemEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomShoppingItemLocalDataSource @Inject constructor(
    private val shoppingItemDao: ShoppingItemDao,
) : ShoppingItemLocalDataSource {

    override suspend fun upsertShoppingItems(shoppingItems: List<ShoppingItemEntity>) {
        shoppingItemDao.upsertShoppingItems(shoppingItems)
    }

    override suspend fun upsertShoppingItem(shoppingItemEntity: ShoppingItemEntity): Long {
        return shoppingItemDao.upsertShoppingItem(shoppingItemEntity)
    }

    override suspend fun deleteShoppingItem(id: Int) {
        shoppingItemDao.deleteShoppingItem(id)
    }

    override fun observeShoppingItems(listId: Int): Flow<Result<List<ShoppingItemEntity>>> {
        return shoppingItemDao.observeShoppingItems(listId).toResultFlow()
    }

    override suspend fun getShoppingItem(id: Int): ShoppingItemEntity {
        return shoppingItemDao.getShoppingItem(id)
    }
}