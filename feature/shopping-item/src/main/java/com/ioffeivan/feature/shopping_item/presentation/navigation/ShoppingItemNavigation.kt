package com.ioffeivan.feature.shopping_item.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.ioffeivan.feature.shopping_item.presentation.add_shopping_item.AddShoppingItemRoute
import com.ioffeivan.feature.shopping_item.presentation.add_shopping_item.AddShoppingItemViewModel
import com.ioffeivan.feature.shopping_item.presentation.shopping_items.ShoppingItemsRoute
import com.ioffeivan.feature.shopping_item.presentation.shopping_items.ShoppingItemsViewModel
import kotlinx.serialization.Serializable

@Serializable
data class ShoppingItemBaseRoute(
    val listId: Int,
    val listName: String,
)

@Serializable
data object ShoppingItemsRoute

@Serializable
data class AddShoppingItemRoute(val listId: Int)

fun NavController.navigateToShoppingItem(
    listId: Int,
    listName: String,
    navOptions: NavOptions? = null,
) {
    navigate(
        route = ShoppingItemBaseRoute(
            listId = listId,
            listName = listName,
        ),
        navOptions = navOptions,
    )
}

fun NavController.navigateToAddShoppingItem(
    listId: Int,
    navOptions: NavOptions? = null,
) {
    navigate(
        route = AddShoppingItemRoute(listId = listId),
        navOptions = navOptions,
    )
}

fun NavGraphBuilder.shoppingItem(
    navBackStackEntry: () -> NavBackStackEntry,
    onBack: () -> Unit,
    onAddShoppingItemClick: (Int) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    navigation<ShoppingItemBaseRoute>(
        startDestination = ShoppingItemsRoute,
    ) {
        composable<ShoppingItemsRoute>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700),
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700),
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            },
        ) {
            val parentEntry = remember { navBackStackEntry() }
            val (listId, listName) = parentEntry.toRoute<ShoppingItemBaseRoute>()

            ShoppingItemsRoute(
                onBack = onBack,
                onAddShoppingItemClick = { onAddShoppingItemClick(listId) },
                onShowSnackbar = onShowSnackbar,
                viewModel = hiltViewModel<ShoppingItemsViewModel, ShoppingItemsViewModel.Factory>(
                    key = "$listId|$listName"
                ) { factory ->
                    factory.create(
                        listId = listId,
                        listName = listName,
                    )
                },
            )
        }

        composable<AddShoppingItemRoute>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700),
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700),
                )
            },
        ) { entry ->
            val listId = entry.toRoute<AddShoppingItemRoute>().listId

            AddShoppingItemRoute(
                onBack = onBack,
                viewModel = hiltViewModel<AddShoppingItemViewModel, AddShoppingItemViewModel.Factory>(
                    key = "$listId"
                ) { factory ->
                    factory.create(listId = listId)
                },
            )
        }
    }
}