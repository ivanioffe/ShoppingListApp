package com.ioffeivan.core.data.source.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeleteShoppingItemDto(

    @SerialName("list_id")
    val listId: Int,

    @SerialName("item_id")
    val itemId: Int,
)