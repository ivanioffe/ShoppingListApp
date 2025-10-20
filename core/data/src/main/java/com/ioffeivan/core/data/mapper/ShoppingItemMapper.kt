package com.ioffeivan.core.data.mapper

import com.ioffeivan.core.data.source.remote.model.AddShoppingItemDto
import com.ioffeivan.core.data.source.remote.model.AddedShoppingItemDto
import com.ioffeivan.core.data.source.remote.model.ShoppingItemDto
import com.ioffeivan.core.data.source.remote.model.ShoppingItemsDto
import com.ioffeivan.core.database.model.ShoppingItemEntity
import com.ioffeivan.core.model.ShoppingItem

fun ShoppingItem.toEntity(): ShoppingItemEntity {
    return ShoppingItemEntity(
        name = name,
        quantity = quantity,
        listId = listId,
    )
}

fun ShoppingItem.toAddShoppingItemDto(listId: Int): AddShoppingItemDto {
    return AddShoppingItemDto(
        name = name,
        quantity = quantity.toIntOrNull() ?: 0,
        listId = listId,
    )
}

fun AddedShoppingItemDto.toShoppingItemEntity(
    shoppingItem: ShoppingItem,
): ShoppingItemEntity {
    return ShoppingItemEntity(
        id = shoppingItem.id,
        serverId = id,
        name = shoppingItem.name,
        quantity = shoppingItem.quantity,
        listId = shoppingItem.listId,
    )
}

fun ShoppingItemEntity.toDomain(): ShoppingItem {
    return ShoppingItem(
        id = id,
        name = name,
        quantity = quantity,
        listId = listId,
    )
}

fun ShoppingItemsDto.toEntities(listId: Int): List<ShoppingItemEntity> {
    return items.map { it.toEntity(listId) }
}

private fun ShoppingItemDto.toEntity(listId: Int): ShoppingItemEntity {
    return ShoppingItemEntity(
        serverId = id,
        name = name,
        quantity = quantity,
        listId = listId,
    )
}