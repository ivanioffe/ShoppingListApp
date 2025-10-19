package com.ioffeivan.core.domain.usecase

import com.ioffeivan.core.domain.repository.ShoppingItemRepository
import javax.inject.Inject

class RefreshShoppingItemsUseCase @Inject constructor(
    private val shoppingItemRepository: ShoppingItemRepository,
) {
    suspend operator fun invoke(listLocalId: Int, listServerId: Int) {
        return shoppingItemRepository.refreshShoppingItems(
            listLocalId = listLocalId,
            listServerId = listServerId,
        )
    }
}