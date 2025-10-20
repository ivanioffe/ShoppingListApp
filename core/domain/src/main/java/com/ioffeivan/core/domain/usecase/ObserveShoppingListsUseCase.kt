package com.ioffeivan.core.domain.usecase

import com.ioffeivan.core.common.Result
import com.ioffeivan.core.domain.repository.ShoppingListRepository
import com.ioffeivan.core.model.ShoppingLists
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveShoppingListsUseCase @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository,
) {
    operator fun invoke(): Flow<Result<ShoppingLists>> {
        return shoppingListRepository.observeShoppingLists()
    }
}