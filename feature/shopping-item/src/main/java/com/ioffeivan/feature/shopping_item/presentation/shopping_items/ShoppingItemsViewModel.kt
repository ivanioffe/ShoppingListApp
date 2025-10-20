package com.ioffeivan.feature.shopping_item.presentation.shopping_items

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ioffeivan.core.common.Result
import com.ioffeivan.core.domain.usecase.DeleteShoppingItemFromShoppingListUseCase
import com.ioffeivan.core.domain.usecase.ObserveShoppingItemsUseCase
import com.ioffeivan.core.domain.usecase.RefreshShoppingItemsUseCase
import com.ioffeivan.core.ui.utils.withRefreshing
import dagger.Lazy
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ShoppingItemsViewModel.Factory::class)
class ShoppingItemsViewModel @AssistedInject constructor(
    observeShoppingItemsUseCase: ObserveShoppingItemsUseCase,
    private val refreshShoppingItemsUseCase: RefreshShoppingItemsUseCase,
    private val deleteShoppingItemUseCase: Lazy<DeleteShoppingItemFromShoppingListUseCase>,
    @Assisted val listId: Int,
    @Assisted val listName: String,
    @Assisted val listServerId: Int?,
) : ViewModel() {

    private val _shoppingItemsEvent = Channel<ShoppingItemsEvent>()
    val shoppingItemsEvent = _shoppingItemsEvent.receiveAsFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing
        get() = _isRefreshing.asStateFlow()

    val uiState = observeShoppingItemsUseCase(listId)
        .onStart {
            if (listServerId != null) {
                refreshShoppingItemsUseCase(
                    listId = listId,
                )
            }
        }
        .drop(if (listServerId != null) 1 else 0) // Skip first Loading or Error
        .onEach { result ->
            when (result) {
                is Result.Error -> {
                    _shoppingItemsEvent.send(ShoppingItemsEvent.ShowSnackbar(result.message))
                }

                else -> {}
            }
        }.runningFold(initial = ShoppingItemsUiState(title = listName)) { previousState, result ->
            when (result) {
                is Result.Success -> {
                    previousState.copy(
                        shoppingItems = result.data,
                        isEmpty = result.data.items.isEmpty(),
                        isLoading = false,
                    )
                }

                is Result.Error -> {
                    previousState.copy(
                        isLoading = false,
                    )
                }

                else -> previousState
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000L),
            ShoppingItemsUiState(title = listName),
        )

    fun refreshShoppingItems() {
        viewModelScope.launch(Dispatchers.IO) {
            withRefreshing(
                startRefreshingAction = { _isRefreshing.value = true },
                endRefreshingAction = { _isRefreshing.value = false },
            ) {
                if (listServerId != null) {
                    refreshShoppingItemsUseCase(
                        listId = listId,
                    )
                }
            }
        }
    }

    fun deleteShoppingItem(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteShoppingItemUseCase.get().invoke(id)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            listId: Int,
            listName: String,
            listServerId: Int?,
        ): ShoppingItemsViewModel
    }
}

sealed interface ShoppingItemsEvent {
    data class ShowSnackbar(val message: String) : ShoppingItemsEvent
}