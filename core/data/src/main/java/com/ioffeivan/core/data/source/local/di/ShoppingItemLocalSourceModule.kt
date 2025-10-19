package com.ioffeivan.core.data.source.local.di

import com.ioffeivan.core.data.source.local.RoomShoppingItemLocalDataSource
import com.ioffeivan.core.data.source.local.ShoppingItemLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface ShoppingItemLocalSourceModuleBinder {

    @Binds
    fun bindShoppingItemLocalDataSource(
        impl: RoomShoppingItemLocalDataSource
    ): ShoppingItemLocalDataSource
}