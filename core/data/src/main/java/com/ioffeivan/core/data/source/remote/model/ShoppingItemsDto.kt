package com.ioffeivan.core.data.source.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShoppingItemsDto(

    @SerialName("item_list")
    val items: List<ShoppingItemDto>,
)