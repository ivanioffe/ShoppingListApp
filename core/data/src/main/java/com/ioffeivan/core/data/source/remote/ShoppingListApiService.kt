package com.ioffeivan.core.data.source.remote

import com.ioffeivan.core.data.source.remote.model.CreatedShoppingListDto
import com.ioffeivan.core.data.source.remote.model.ShoppingListsDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ShoppingListApiService {

    @GET("GetAllMyShopLists")
    suspend fun getAllShoppingLists(): Response<ShoppingListsDto>

    @POST("CreateShoppingList")
    suspend fun createShoppingList(
        @Query("name") listName: String,
    ): Response<CreatedShoppingListDto>

    @POST("RemoveShoppingList")
    suspend fun deleteShoppingList(
        @Query("list_id") listId: Int,
    ): Response<Unit>

    // Preferred: send credentials in request body (POST with @Body) — more secure and RESTful,
    // but backend currently only accepts credentials via query parameters (@Query).
    /*@POST("CreateShoppingList")
    suspend fun createShoppingList(
        @Body createShoppingListDto: CreateShoppingListDto,
    ): Response<CreatedShoppingListDto>

    @POST("RemoveShoppingList")
    suspend fun deleteShoppingList(
        @Body listId: Int,
    ): Response<Unit>*/
}