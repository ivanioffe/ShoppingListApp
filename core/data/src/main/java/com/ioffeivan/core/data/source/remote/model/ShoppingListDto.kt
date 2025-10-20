package com.ioffeivan.core.data.source.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShoppingListDto(

    @SerialName("id")
    val id: Int,

    @SerialName("name")
    val name: String,
)