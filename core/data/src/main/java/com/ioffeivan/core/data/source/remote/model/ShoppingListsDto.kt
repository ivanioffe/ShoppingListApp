package com.ioffeivan.core.data.source.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShoppingListsDto(

    @SerialName("shop_list")
    val items: List<ShoppingListDto>,
)