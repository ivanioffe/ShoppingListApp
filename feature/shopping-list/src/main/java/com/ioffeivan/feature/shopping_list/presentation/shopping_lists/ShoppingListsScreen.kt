package com.ioffeivan.feature.shopping_list.presentation.shopping_lists

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ioffeivan.core.designsystem.component.AddFloatingActionButton
import com.ioffeivan.core.designsystem.icon.PrimaryIcon
import com.ioffeivan.core.designsystem.icon.PrimaryIcons
import com.ioffeivan.core.model.ShoppingList
import com.ioffeivan.core.ui.LoadingScreen
import com.ioffeivan.core.ui.ObserveAsEventsWithLifecycle
import com.ioffeivan.core.ui.onDebounceClick
import com.ioffeivan.feature.shopping_list.R
import com.ioffeivan.feature.shopping_list.presentation.shopping_lists.component.ShoppingListItem

@Composable
fun ShoppingListsRoute(
    onShoppingListClick: (ShoppingList) -> Unit,
    onCreateShoppingListClick: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    viewModel: ShoppingListsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    ObserveAsEventsWithLifecycle(
        events = viewModel.shoppingListsEvent,
        onEvent = { event ->
            when (event) {
                is ShoppingListsEvent.ShowSnackbar -> {
                    onShowSnackbar(event.message, null)
                }
            }
        },
    )

    ShoppingListsScreen(
        uiState = uiState,
        isRefreshing = isRefreshing,
        onRefresh = viewModel::refreshShoppingLists,
        onShoppingListClick = onShoppingListClick,
        onCreateShoppingListClick = onCreateShoppingListClick,
        onDeleteShoppingListClick = viewModel::deleteShoppingList,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListsScreen(
    uiState: ShoppingListsUiState,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onShoppingListClick: (ShoppingList) -> Unit,
    onCreateShoppingListClick: () -> Unit,
    onDeleteShoppingListClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.shopping_lists_content_title))
                },
            )
        },
        floatingActionButton = {
            ShoppingListsFAB(
                onClick = onDebounceClick(onClick = onCreateShoppingListClick),
                isVisible = !uiState.isLoading,
            )
        },
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier
                .padding(innerPadding),
        ) {
            when {
                uiState.isLoading -> {
                    LoadingScreen(
                        modifier = Modifier
                            .fillMaxSize(),
                    )
                }

                !uiState.isEmpty -> {
                    ShoppingListsScreenContent(
                        shoppingLists = uiState.shoppingLists.items,
                        onShoppingListClick = onShoppingListClick,
                        onDeleteShoppingListClick = onDeleteShoppingListClick,
                        modifier = Modifier
                            .fillMaxSize(),
                    )
                }

                uiState.isEmpty -> {
                    ShoppingListsScreenEmpty(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 8.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun ShoppingListsScreenContent(
    shoppingLists: List<ShoppingList>,
    onShoppingListClick: (ShoppingList) -> Unit,
    onDeleteShoppingListClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(
            items = shoppingLists,
            key = { it.id },
        ) { shoppingList ->
            ShoppingListItem(
                shoppingList = shoppingList,
                onClick = onDebounceClick(onClick = onShoppingListClick),
                onDeleteClick = onDeleteShoppingListClick,
            )

            HorizontalDivider()
        }
    }
}

@Composable
fun ShoppingListsScreenEmpty(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PrimaryIcon(
            icon = PrimaryIcons.ShoppingCartOff,
            modifier = Modifier
                .size(125.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.shopping_lists_empty_title),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.shopping_lists_empty_message),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
fun ShoppingListsFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isVisible: Boolean = true,
) {
    AnimatedVisibility(
        visible = isVisible,
        modifier = modifier,
        enter = fadeIn() + scaleIn(),
    ) {
        AddFloatingActionButton(
            onClick = onClick,
        )
    }
}