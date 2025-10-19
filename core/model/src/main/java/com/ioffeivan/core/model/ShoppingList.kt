package com.ioffeivan.core.model

// NOTE (temporary):
// serverId was added here to support syncing and lookup logic in the shopping-item module.
// This is an interim solution to avoid cross-module coupling.
// Planned next steps:
//   1) Create a shared :core:data module containing reusable data models and mappers.
//   2) Move ShoppingList and related DTO/entity classes into :core:data.
//   3) Remove serverId from UI/domain types if it does not belong there,
//      and use dedicated DTOs/entities for persistence.
data class ShoppingList(
    val id: Int,
    val name: String,
    val serverId: Int?,
)