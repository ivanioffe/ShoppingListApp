package com.ioffeivan.feature.shopping_list.presentation.shopping_lists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ioffeivan.core.common.Result
import com.ioffeivan.core.domain.usecase.DeleteShoppingListUseCase
import com.ioffeivan.core.domain.usecase.ObserveShoppingListsUseCase
import com.ioffeivan.core.domain.usecase.RefreshShoppingListsUseCase
import com.ioffeivan.core.ui.utils.withRefreshing
import dagger.Lazy
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
import javax.inject.Inject

@HiltViewModel
class ShoppingListsViewModel @Inject constructor(
    observeShoppingListsUseCase: ObserveShoppingListsUseCase,
    private val refreshShoppingListsUseCase: RefreshShoppingListsUseCase,
    private val deleteShoppingListUseCase: Lazy<DeleteShoppingListUseCase>,
) : ViewModel() {

    private val _shoppingListsEvent = Channel<ShoppingListsEvent>()
    val shoppingListsEvent = _shoppingListsEvent.receiveAsFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing get() = _isRefreshing.asStateFlow()

    val uiState = observeShoppingListsUseCase()
        .onStart { refreshShoppingListsUseCase() }
        .drop(1) // Skip first Loading or Error
        .onEach {
            when (it) {
                is Result.Error -> _shoppingListsEvent.send(ShoppingListsEvent.ShowSnackbar(it.message))
                else -> {}
            }
        }
        .runningFold(initial = ShoppingListsUiState()) { previousState, result ->
            when (result) {
                is Result.Success -> {
                    previousState.copy(
                        shoppingLists = result.data,
                        isEmpty = result.data.items.isEmpty(),
                        isLoading = false,
                    )
                }

                is Result.Error -> {
                    previousState.copy(
                        isLoading = false,
                    )
                }

                Result.Loading -> previousState
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000L),
            ShoppingListsUiState(),
        )

    fun refreshShoppingLists() {
        viewModelScope.launch(Dispatchers.IO) {
            withRefreshing(
                startRefreshingAction = { _isRefreshing.value = true },
                endRefreshingAction = { _isRefreshing.value = false },
            ) {
                refreshShoppingListsUseCase()
            }
        }
    }

    fun deleteShoppingList(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteShoppingListUseCase.get().invoke(id)
        }
    }
}

sealed interface ShoppingListsEvent {
    data class ShowSnackbar(val message: String) : ShoppingListsEvent
}