package com.ioffeivan.core.data.source.local.di

import com.ioffeivan.core.data.source.local.RoomShoppingListLocalDataSource
import com.ioffeivan.core.data.source.local.ShoppingListLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface ShoppingListLocalSourceModuleBinder {

    @Binds
    fun bindShoppingListLocalDataSource(
        impl: RoomShoppingListLocalDataSource
    ): ShoppingListLocalDataSource
}