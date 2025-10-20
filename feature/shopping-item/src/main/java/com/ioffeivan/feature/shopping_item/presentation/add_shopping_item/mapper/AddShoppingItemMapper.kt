package com.ioffeivan.feature.shopping_item.presentation.add_shopping_item.mapper

import com.ioffeivan.core.model.ShoppingItem
import com.ioffeivan.feature.shopping_item.presentation.add_shopping_item.AddShoppingItemUiState

fun AddShoppingItemUiState.toShoppingItem(
    listId: Int,
): ShoppingItem {
    return ShoppingItem(
        name = name.trim(),
        quantity = quantity.trim(),
        listId = listId,
    )
}