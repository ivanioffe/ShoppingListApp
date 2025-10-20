package com.ioffeivan.core.domain.usecase

import com.ioffeivan.core.domain.repository.ShoppingListRepository
import javax.inject.Inject

class RefreshShoppingListsUseCase @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository,
) {
    suspend operator fun invoke() {
        shoppingListRepository.refreshShoppingLists()
    }
}