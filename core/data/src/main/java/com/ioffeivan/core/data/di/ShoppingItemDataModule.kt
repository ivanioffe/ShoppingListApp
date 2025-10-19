package com.ioffeivan.core.data.di

import com.ioffeivan.core.data.repository.ShoppingItemRepositoryImpl
import com.ioffeivan.core.domain.repository.ShoppingItemRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface ShoppingItemDataModuleBinder {

    @Singleton
    @Binds
    fun bindShoppingItemRepository(
        impl: ShoppingItemRepositoryImpl
    ): ShoppingItemRepository

    @Binds
    fun bindShoppingItemSyncRepository(
        impl: ShoppingItemSyncRepositoryImpl
    ): ShoppingItemSyncRepository
}