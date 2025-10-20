package com.ioffeivan.shoppinglistapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ioffeivan.feature.login.presentation.navigation.LoginRoute
import com.ioffeivan.feature.login.presentation.navigation.login
import com.ioffeivan.feature.shopping_item.presentation.navigation.ShoppingItemBaseRoute
import com.ioffeivan.feature.shopping_item.presentation.navigation.navigateToAddShoppingItem
import com.ioffeivan.feature.shopping_item.presentation.navigation.navigateToShoppingItem
import com.ioffeivan.feature.shopping_item.presentation.navigation.shoppingItem
import com.ioffeivan.feature.shopping_list.presentation.navigation.ShoppingListBaseRoute
import com.ioffeivan.feature.shopping_list.presentation.navigation.navigateToCreateShoppingList
import com.ioffeivan.feature.shopping_list.presentation.navigation.shoppingList

@Composable
fun AppNavGraph(
    navController: NavHostController,
    isLoggedIn: Boolean,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) ShoppingListBaseRoute else LoginRoute,
        modifier = modifier,
    ) {
        login(
            onShowSnackbar = onShowSnackbar,
        )

        shoppingList(
            onShoppingListClick = { shoppingList ->
                navController.navigateToShoppingItem(
                    listId = shoppingList.id,
                    listName = shoppingList.name,
                )
            },
            onCreateShoppingListClick = navController::navigateToCreateShoppingList,
            onBack = navController::popBackStack,
            onShowSnackbar = onShowSnackbar,
        )

        shoppingItem(
            navBackStackEntry = { navController.getBackStackEntry<ShoppingItemBaseRoute>() },
            onBack = navController::popBackStack,
            onAddShoppingItemClick = navController::navigateToAddShoppingItem,
            onShowSnackbar = onShowSnackbar,
        )
    }
}