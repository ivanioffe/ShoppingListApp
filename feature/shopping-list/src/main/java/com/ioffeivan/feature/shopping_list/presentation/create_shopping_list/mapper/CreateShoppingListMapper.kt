package com.ioffeivan.feature.shopping_list.presentation.create_shopping_list.mapper

import com.ioffeivan.core.model.CreateShoppingList
import com.ioffeivan.feature.shopping_list.presentation.create_shopping_list.CreateShoppingListUiState

fun CreateShoppingListUiState.toCreateShoppingList(): CreateShoppingList {
    return CreateShoppingList(
        name = name.trim(),
    )
}