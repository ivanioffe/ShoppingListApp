package com.ioffeivan.core.data.source.remote

import com.ioffeivan.core.data.source.remote.model.AddedShoppingItemDto
import com.ioffeivan.core.data.source.remote.model.ShoppingItemsDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ShoppingItemApiService {

    @GET("GetShoppingList")
    suspend fun getShoppingItems(
        @Query("list_id") listId: Int,
    ): Response<ShoppingItemsDto>

    @POST("AddToShoppingList")
    suspend fun addShoppingItem(
        @Query("id") listId: Int,
        @Query("value") name: String,
        @Query("n") quantity: Int,
    ): Response<AddedShoppingItemDto>

    @POST("RemoveFromList")
    suspend fun deleteShoppingItem(
        @Query("list_id") listId: Int,
        @Query("item_id") itemId: Int,
    ): Response<Unit>


    // Preferred: send credentials in request body (POST with @Body) — more secure and RESTful,
    // but backend currently only accepts credentials via query parameters (@Query).
    /*@POST("AddToShoppingList")
    suspend fun addShoppingItem(
        @Body addShoppingItemDto: AddShoppingItemDto,
    ): Response<AddedShoppingItemDto>

    @POST("RemoveFromList")
    suspend fun deleteShoppingItem(
        @Body deleteShoppingItemDto: DeleteShoppingItemDto,
    ): Response<Unit>*/
}