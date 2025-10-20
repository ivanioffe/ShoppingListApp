package com.ioffeivan.feature.shopping_item.presentation.shopping_items

import com.ioffeivan.core.model.ShoppingItems

data class ShoppingItemsUiState(
    val title: String,
    val shoppingItems: ShoppingItems = ShoppingItems(emptyList()),
    val isEmpty: Boolean = false,
    val isLoading: Boolean = true,
)