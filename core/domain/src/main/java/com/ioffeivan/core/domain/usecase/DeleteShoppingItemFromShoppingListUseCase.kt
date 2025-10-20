package com.ioffeivan.core.domain.usecase

import com.ioffeivan.core.domain.repository.ShoppingItemRepository
import javax.inject.Inject

class DeleteShoppingItemFromShoppingListUseCase @Inject constructor(
    private val shoppingItemRepository: ShoppingItemRepository,
) {
    suspend operator fun invoke(id: Int) {
        shoppingItemRepository.deleteShoppingItem(id)
    }
}