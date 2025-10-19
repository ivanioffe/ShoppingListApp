package com.ioffeivan.core.domain.usecase

import com.ioffeivan.core.common.Result
import com.ioffeivan.core.domain.repository.ShoppingItemRepository
import com.ioffeivan.core.model.ShoppingItems
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveShoppingItemsUseCase @Inject constructor(
    private val shoppingItemRepository: ShoppingItemRepository,
) {
    operator fun invoke(listId: Int): Flow<Result<ShoppingItems>> {
        return shoppingItemRepository.observeShoppingItems(listId)
    }
}