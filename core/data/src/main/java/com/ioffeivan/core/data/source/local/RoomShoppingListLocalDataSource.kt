package com.ioffeivan.core.data.source.local

import com.ioffeivan.core.common.Result
import com.ioffeivan.core.common.toResultFlow
import com.ioffeivan.core.database.dao.ShoppingListDao
import com.ioffeivan.core.database.model.ShoppingListEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomShoppingListLocalDataSource @Inject constructor(
    private val shoppingListDao: ShoppingListDao,
) : ShoppingListLocalDataSource {

    override fun observeAllShoppingLists(): Flow<Result<List<ShoppingListEntity>>> {
        return shoppingListDao.observeAllShoppingLists().toResultFlow()
    }

    override suspend fun upsertShoppingLists(shoppingLists: List<ShoppingListEntity>) {
        shoppingListDao.upsertShoppingLists(shoppingLists)
    }

    override suspend fun upsertShoppingList(shoppingList: ShoppingListEntity): Long {
        return shoppingListDao.upsertShoppingList(shoppingList)
    }

    override suspend fun deleteShoppingList(id: Int) {
        shoppingListDao.deleteShoppingList(id)
    }

    override suspend fun getShoppingList(id: Int): ShoppingListEntity {
        return shoppingListDao.getShoppingList(id)
    }
}