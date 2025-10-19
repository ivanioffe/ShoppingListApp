package com.ioffeivan.core.data.source.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateShoppingListDto(

    @SerialName("name")
    val name: String,
)