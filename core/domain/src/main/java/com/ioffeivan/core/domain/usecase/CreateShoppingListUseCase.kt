package com.ioffeivan.core.domain.usecase

import com.ioffeivan.core.domain.repository.ShoppingListRepository
import com.ioffeivan.core.model.CreateShoppingList
import javax.inject.Inject

class CreateShoppingListUseCase @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository,
) {
    suspend operator fun invoke(createShoppingList: CreateShoppingList) {
        shoppingListRepository.createShoppingList(createShoppingList)
    }
}