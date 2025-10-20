package com.ioffeivan.feature.shopping_list.presentation.create_shopping_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ioffeivan.core.domain.usecase.CreateShoppingListUseCase
import com.ioffeivan.feature.shopping_list.presentation.create_shopping_list.mapper.toCreateShoppingList
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateShoppingListViewModel @Inject constructor(
    private val createShoppingListUseCase: Lazy<CreateShoppingListUseCase>,
) : ViewModel() {

    private val _createShoppingListUiState = MutableStateFlow(CreateShoppingListUiState())

    val createShoppingListUiState = _createShoppingListUiState
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000L),
            CreateShoppingListUiState(),
        )

    fun onShoppingListNameChange(shoppingListName: String) {
        _createShoppingListUiState.update {
            it.copy(name = shoppingListName)
        }
    }

    fun onCreateShoppingListClick() {
        viewModelScope.launch {
            createShoppingListUseCase.get().invoke(
                createShoppingList = _createShoppingListUiState.value.toCreateShoppingList()
            )
        }
    }
}