package com.ioffeivan.feature.shopping_item.presentation.add_shopping_item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ioffeivan.core.domain.usecase.AddShoppingItemToShoppingListUseCase
import com.ioffeivan.feature.shopping_item.presentation.add_shopping_item.mapper.toShoppingItem
import dagger.Lazy
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = AddShoppingItemViewModel.Factory::class)
class AddShoppingItemViewModel @AssistedInject constructor(
    private val addShoppingItemUseCase: Lazy<AddShoppingItemToShoppingListUseCase>,
    @Assisted val listId: Int,
) : ViewModel() {

    private val _addShoppingItemUiState = MutableStateFlow(AddShoppingItemUiState())
    val addShoppingItemUiState = _addShoppingItemUiState
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000L),
            AddShoppingItemUiState(),
        )

    fun onShoppingItemNameChange(name: String) {
        _addShoppingItemUiState.update {
            it.copy(name = name)
        }
    }

    fun onShoppingItemQuantityChange(quantity: String) {
        if (isValidQuantity(quantity)) {
            _addShoppingItemUiState.update {
                it.copy(quantity = quantity)
            }
        }
    }

    fun onAddShoppingItemClick() {
        viewModelScope.launch(Dispatchers.IO) {
            addShoppingItemUseCase.get().invoke(
                shoppingItem = _addShoppingItemUiState.value.toShoppingItem(listId),
            )
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            listId: Int,
        ): AddShoppingItemViewModel
    }
}