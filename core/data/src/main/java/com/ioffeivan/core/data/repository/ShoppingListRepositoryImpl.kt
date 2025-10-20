package com.ioffeivan.core.data.repository

import com.ioffeivan.core.common.Result
import com.ioffeivan.core.data.mapper.toDomain
import com.ioffeivan.core.data.mapper.toEntities
import com.ioffeivan.core.data.mapper.toShoppingListEntity
import com.ioffeivan.core.data.source.local.ShoppingListLocalDataSource
import com.ioffeivan.core.data.source.remote.ShoppingListRemoteDataSource
import com.ioffeivan.core.data.source.remote.model.ShoppingListsDto
import com.ioffeivan.core.data.sync.ShoppingListSyncStarter
import com.ioffeivan.core.database.dao.ShoppingListOutboxDao
import com.ioffeivan.core.database.model.ShoppingListEntity
import com.ioffeivan.core.database.model.ShoppingListOutboxEntity
import com.ioffeivan.core.database.model.ShoppingListOutboxOperation
import com.ioffeivan.core.domain.repository.ShoppingListRepository
import com.ioffeivan.core.model.CreateShoppingList
import com.ioffeivan.core.model.ShoppingLists
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import javax.inject.Inject

class ShoppingListRepositoryImpl @Inject constructor(
    private val shoppingListRemoteDataSource: ShoppingListRemoteDataSource,
    private val shoppingListLocalDataSource: ShoppingListLocalDataSource,
    private val shoppingListOutboxDao: ShoppingListOutboxDao,
    private val shoppingListSyncStarter: ShoppingListSyncStarter,
) : ShoppingListRepository {

    private val remoteShoppingListsFlow = MutableSharedFlow<Result<ShoppingLists>>(replay = 1)

    private val localShoppingListsFlow: Flow<Result<ShoppingLists>> =
        shoppingListLocalDataSource.observeAllShoppingLists()
            .map { result ->
                when (result) {
                    is Result.Success -> {
                        val domainItems = result.data.map(ShoppingListEntity::toDomain)
                        Result.Success(ShoppingLists(items = domainItems))
                    }

                    Result.Loading -> Result.Loading
                    is Result.Error -> Result.Error(result.message)
                }
            }

    private val shoppingLists = merge(remoteShoppingListsFlow, localShoppingListsFlow)

    override suspend fun refreshShoppingLists() {
        shoppingListRemoteDataSource.getAllShoppingLists()
            .collect { result ->
                when (result) {
                    is Result.Success -> {
                        val shoppingListsDto: ShoppingListsDto = result.data

                        if (shoppingListsDto.items.isEmpty()) {
                            remoteShoppingListsFlow.emit(
                                Result.Success(ShoppingLists(emptyList()))
                            )
                        } else {
                            val shoppingListsEntity = shoppingListsDto.toEntities()
                            shoppingListLocalDataSource.upsertShoppingLists(shoppingListsEntity)
                        }
                    }

                    is Result.Loading -> remoteShoppingListsFlow.emit(Result.Loading)
                    is Result.Error -> remoteShoppingListsFlow.emit(Result.Error(result.message))
                }
            }
    }

    override suspend fun createShoppingList(createShoppingList: CreateShoppingList) {
        val id = shoppingListLocalDataSource.upsertShoppingList(
            createShoppingList.toShoppingListEntity()
        )
        shoppingListOutboxDao.insertShoppingListOutbox(
            ShoppingListOutboxEntity(
                listId = id.toInt(),
                operation = ShoppingListOutboxOperation.CREATE,
            )
        )
        shoppingListSyncStarter.startSync()
    }

    override suspend fun deleteShoppingList(id: Int) {
        shoppingListOutboxDao.insertShoppingListOutbox(
            ShoppingListOutboxEntity(
                listId = id,
                operation = ShoppingListOutboxOperation.DELETE,
            )
        )
        shoppingListSyncStarter.startSync()
    }

    override fun observeShoppingLists(): Flow<Result<ShoppingLists>> {
        return shoppingLists
    }
}