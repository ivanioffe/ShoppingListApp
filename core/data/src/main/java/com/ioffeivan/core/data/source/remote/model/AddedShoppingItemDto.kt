package com.ioffeivan.core.data.source.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddedShoppingItemDto(

    @SerialName("item_id")
    val id: Int,
)