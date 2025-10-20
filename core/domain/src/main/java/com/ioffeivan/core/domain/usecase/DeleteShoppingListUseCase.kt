package com.ioffeivan.core.domain.usecase

import com.ioffeivan.core.domain.repository.ShoppingListRepository
import javax.inject.Inject

class DeleteShoppingListUseCase @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository,
) {
    suspend operator fun invoke(id: Int) {
        return shoppingListRepository.deleteShoppingList(id)
    }
}