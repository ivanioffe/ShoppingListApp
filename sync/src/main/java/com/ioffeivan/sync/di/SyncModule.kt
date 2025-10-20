package com.ioffeivan.sync.di

import android.content.Context
import androidx.work.WorkManager
import com.ioffeivan.core.data.sync.ShoppingItemSyncStarter
import com.ioffeivan.core.data.sync.ShoppingListSyncStarter
import com.ioffeivan.sync.starter.WorkManagerShoppingItemSyncStarter
import com.ioffeivan.sync.starter.WorkManagerShoppingListSyncStarter
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class SyncModuleProvider {

    @Singleton
    @Provides
    fun provideWorkManager(@ApplicationContext context: Context) = WorkManager.getInstance(context)
}

@InstallIn(SingletonComponent::class)
@Module
interface SyncModuleBinder {

    @Binds
    fun bindShoppingListSyncStarter(
        impl: WorkManagerShoppingListSyncStarter
    ): ShoppingListSyncStarter

    @Binds
    fun bindShoppingItemSyncStarter(
        impl: WorkManagerShoppingItemSyncStarter
    ): ShoppingItemSyncStarter
}