package com.ioffeivan.feature.shopping_item.presentation.shopping_items

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ioffeivan.core.designsystem.component.AddFloatingActionButton
import com.ioffeivan.core.designsystem.icon.PrimaryIcon
import com.ioffeivan.core.designsystem.icon.PrimaryIcons
import com.ioffeivan.core.designsystem.preview.PreviewContainer
import com.ioffeivan.core.model.ShoppingItem
import com.ioffeivan.core.model.ShoppingItems
import com.ioffeivan.core.ui.LoadingScreen
import com.ioffeivan.core.ui.ObserveAsEventsWithLifecycle
import com.ioffeivan.core.ui.SwipeToDismissContainer
import com.ioffeivan.core.ui.onDebounceClick
import com.ioffeivan.feature.shopping_item.R
import com.ioffeivan.feature.shopping_item.presentation.shopping_items.component.ShoppingItemCard
import com.ioffeivan.feature.shopping_item.presentation.shopping_items.utils.ShoppingItemsPreviewParameterProvider

@Composable
fun ShoppingItemsRoute(
    onBack: () -> Unit,
    onAddShoppingItemClick: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    viewModel: ShoppingItemsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    ObserveAsEventsWithLifecycle(
        events = viewModel.shoppingItemsEvent,
        onEvent = { event ->
            when (event) {
                is ShoppingItemsEvent.ShowSnackbar -> {
                    onShowSnackbar(event.message, null)
                }
            }
        },
    )

    ShoppingItemsScreen(
        uiState = uiState,
        isRefreshing = isRefreshing,
        onBack = onBack,
        onRefresh = viewModel::refreshShoppingItems,
        onShoppingItemDelete = viewModel::deleteShoppingItem,
        onAddShoppingItemClick = onAddShoppingItemClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingItemsScreen(
    uiState: ShoppingItemsUiState,
    isRefreshing: Boolean,
    onBack: () -> Unit,
    onRefresh: () -> Unit,
    onShoppingItemDelete: (Int) -> Unit,
    onAddShoppingItemClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = uiState.title,
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onDebounceClick(onClick = onBack)) {
                        PrimaryIcon(
                            icon = PrimaryIcons.ArrowBack,
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            ShoppingItemsFAB(
                onClick = onDebounceClick(onClick = onAddShoppingItemClick),
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
                    ShoppingItemsScreenContent(
                        shoppingItems = uiState.shoppingItems.items,
                        onShoppingItemDelete = onShoppingItemDelete,
                        modifier = Modifier
                            .fillMaxSize(),
                    )
                }

                uiState.isEmpty -> {
                    ShoppingItemsScreenEmpty(
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
fun ShoppingItemsScreenContent(
    shoppingItems: List<ShoppingItem>,
    onShoppingItemDelete: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
    ) {
        items(
            items = shoppingItems,
            key = { it.id },
        ) { shoppingItem ->
            SwipeToDismissContainer(
                backgroundContent = { swipeToDismissBoxState ->
                    SwipeToDismissBackground(
                        swipeToDismissBoxState = swipeToDismissBoxState,
                    )
                },
                enableDismissFromStartToEnd = false,
                confirmValueChange = { it != SwipeToDismissBoxValue.StartToEnd },
                onEndToStart = { onShoppingItemDelete(shoppingItem.id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItem()
                    .padding(vertical = 4.dp),
            ) {
                ShoppingItemCard(
                    shoppingItem = shoppingItem,
                    modifier = Modifier
                        .fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
fun SwipeToDismissBackground(
    swipeToDismissBoxState: SwipeToDismissBoxState,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        when (swipeToDismissBoxState.dismissDirection) {
            SwipeToDismissBoxValue.EndToStart -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.Red)
                        .padding(16.dp),
                ) {
                    Text(
                        text = "Удалить",
                        style = MaterialTheme.typography.bodyLarge,
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    PrimaryIcon(
                        icon = PrimaryIcons.Delete,
                    )
                }
            }

            else -> {}
        }
    }
}

@Composable
fun ShoppingItemsScreenEmpty(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.shopping_items_empty_title),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.shopping_items_empty_message),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
fun ShoppingItemsFAB(
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

@Preview
@Composable
fun ShoppingItemsScreenContentPreviewLight(
    @PreviewParameter(ShoppingItemsPreviewParameterProvider::class)
    shoppingItems: ShoppingItems,
) {
    PreviewContainer {
        ShoppingItemsScreen(
            uiState = ShoppingItemsUiState(
                title = "Party",
                shoppingItems = shoppingItems,
                isLoading = false,
            ),
            isRefreshing = false,
            onShoppingItemDelete = {},
            onAddShoppingItemClick = {},
            onBack = {},
            onRefresh = {},
        )
    }
}

@Preview
@Composable
fun ShoppingItemsScreenContentPreviewDark(
    @PreviewParameter(ShoppingItemsPreviewParameterProvider::class)
    shoppingItems: ShoppingItems,
) {
    PreviewContainer(darkTheme = true) {
        ShoppingItemsScreen(
            uiState = ShoppingItemsUiState(
                title = "Party",
                shoppingItems = shoppingItems,
                isLoading = false,
            ),
            isRefreshing = false,
            onShoppingItemDelete = {},
            onAddShoppingItemClick = {},
            onBack = {},
            onRefresh = {},
        )
    }
}

@Preview
@Composable
fun ShoppingItemsScreenEmptyPreviewLight() {
    PreviewContainer {
        ShoppingItemsScreen(
            uiState = ShoppingItemsUiState(
                title = "Party",
                isEmpty = true,
                isLoading = false,
            ),
            isRefreshing = false,
            onShoppingItemDelete = {},
            onAddShoppingItemClick = {},
            onBack = {},
            onRefresh = {},
        )
    }
}

@Preview
@Composable
fun ShoppingItemsScreenEmptyPreviewDark() {
    PreviewContainer(darkTheme = true) {
        ShoppingItemsScreen(
            uiState = ShoppingItemsUiState(
                title = "Party",
                isEmpty = true,
                isLoading = false,
            ),
            isRefreshing = false,
            onShoppingItemDelete = {},
            onAddShoppingItemClick = {},
            onBack = {},
            onRefresh = {},
        )
    }
}