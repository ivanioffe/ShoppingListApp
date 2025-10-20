package com.ioffeivan.feature.shopping_item.presentation.shopping_items.utils

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.ioffeivan.core.model.ShoppingItem
import com.ioffeivan.core.model.ShoppingItems

class ShoppingItemsPreviewParameterProvider : PreviewParameterProvider<ShoppingItems> {
    override val values: Sequence<ShoppingItems>
        get() = sequenceOf(
            ShoppingItems(
                listOf(
                    ShoppingItem(
                        id = 1,
                        name = "Tomatoes",
                        quantity = "1.7 kg",
                        listId = 1,
                    ),
                    ShoppingItem(
                        id = 2,
                        name = "Apples",
                        quantity = "4 pcs",
                        listId = 1,
                    ),
                    ShoppingItem(
                        id = 3,
                        name = "Coffee",
                        quantity = "250 g",
                        listId = 1,
                    ),
                    ShoppingItem(
                        id = 4,
                        name = "Cheese",
                        quantity = "300 g",
                        listId = 1,
                    ),
                    ShoppingItem(
                        id = 5,
                        name = "Bananas",
                        quantity = "1 bunch",
                        listId = 1,
                    ),
                    ShoppingItem(
                        id = 6,
                        name = "Milk",
                        quantity = "500 ml",
                        listId = 1,
                    ),
                    ShoppingItem(
                        id = 7,
                        name = "Eggs",
                        quantity = "12 pcs",
                        listId = 1,
                    ),
                    ShoppingItem(
                        id = 8,
                        name = "Butter",
                        quantity = "200 g",
                        listId = 1,
                    ),
                    ShoppingItem(
                        id = 9,
                        name = "Yogurt",
                        quantity = "1500 ml",
                        listId = 1,
                    ),
                    ShoppingItem(
                        id = 10,
                        name = "Olive oil",
                        quantity = "500 ml",
                        listId = 1,
                    ),
                    ShoppingItem(
                        id = 11,
                        name = "Spaghetti",
                        quantity = "500 g",
                        listId = 1,
                    ),
                    ShoppingItem(
                        id = 12,
                        name = "Cucumbers",
                        quantity = "0.6 kg",
                        listId = 1,
                    ),
                    ShoppingItem(
                        id = 13,
                        name = "Potatoes",
                        quantity = "1.1 kg",
                        listId = 1,
                    ),
                    ShoppingItem(
                        id = 14,
                        name = "Bread",
                        quantity = "1 loaf",
                        listId = 1,
                    ),
                    ShoppingItem(
                        id = 15,
                        name = "Chicken breast",
                        quantity = "400 g",
                        listId = 1,
                    ),
                )
            )
        )
}