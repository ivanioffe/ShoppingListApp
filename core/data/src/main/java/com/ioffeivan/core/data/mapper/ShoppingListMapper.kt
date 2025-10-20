package com.ioffeivan.core.data.mapper

import com.ioffeivan.core.data.source.remote.model.CreateShoppingListDto
import com.ioffeivan.core.data.source.remote.model.CreatedShoppingListDto
import com.ioffeivan.core.data.source.remote.model.ShoppingListDto
import com.ioffeivan.core.data.source.remote.model.ShoppingListsDto
import com.ioffeivan.core.database.model.ShoppingListEntity
import com.ioffeivan.core.model.CreateShoppingList
import com.ioffeivan.core.model.ShoppingList

fun CreateShoppingList.toShoppingListEntity(): ShoppingListEntity {
    return ShoppingListEntity(
        name = name,
    )
}

fun ShoppingList.toCreateShoppingListDto(): CreateShoppingListDto {
    return CreateShoppingListDto(
        name = name,
    )
}

fun CreatedShoppingListDto.toShoppingListEntity(
    shoppingList: ShoppingList,
): ShoppingListEntity {
    return ShoppingListEntity(
        id = shoppingList.id,
        serverId = this.id,
        name = shoppingList.name,
    )
}

fun ShoppingListEntity.toDomain(): ShoppingList {
    return ShoppingList(
        id = id,
        name = name,
    )
}

fun ShoppingListsDto.toEntities(): List<ShoppingListEntity> {
    return items.map(ShoppingListDto::toEntity)
}

private fun ShoppingListDto.toEntity(): ShoppingListEntity {
    return ShoppingListEntity(
        serverId = id,
        name = name,
    )
}