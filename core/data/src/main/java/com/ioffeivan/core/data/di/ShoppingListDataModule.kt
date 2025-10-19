package com.ioffeivan.core.data.di

import com.ioffeivan.core.data.repository.ShoppingListRepositoryImpl
import com.ioffeivan.core.domain.repository.ShoppingListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface ShoppingListDataModuleBinder {

    @Singleton
    @Binds
    fun bindShoppingListRepository(
        impl: ShoppingListRepositoryImpl
    ): ShoppingListRepository

    @Binds
    fun bindShoppingListSyncRepository(
        impl: ShoppingListSyncRepositoryImpl
    ): ShoppingListSyncRepository
}