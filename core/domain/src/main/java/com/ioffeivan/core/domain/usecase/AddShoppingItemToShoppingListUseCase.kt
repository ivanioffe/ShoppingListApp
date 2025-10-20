package com.ioffeivan.core.domain.usecase

import com.ioffeivan.core.domain.repository.ShoppingItemRepository
import com.ioffeivan.core.model.ShoppingItem
import javax.inject.Inject

class AddShoppingItemToShoppingListUseCase @Inject constructor(
    private val shoppingItemRepository: ShoppingItemRepository,
) {
    suspend operator fun invoke(shoppingItem: ShoppingItem) {
        shoppingItemRepository.addShoppingItem(shoppingItem)
    }
}