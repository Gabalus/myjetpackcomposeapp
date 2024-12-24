package com.example.myjetpackcomposeapp.ui.screens

import AddItemScreen
import MyViewModelFactory
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myjetpackcomposeapp.viewmodel.MainViewModel
import com.example.myjetpackcomposeapp.viewmodel.UiState

// AppScreen.kt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScreen(
    viewModel: MainViewModel = viewModel(factory = MyViewModelFactory(LocalContext.current))
) {
    val uiState by viewModel.uiState.collectAsState()
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Аптека") }
            )
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "category_screen",
            modifier = Modifier.padding(padding),
            enterTransition = {
                EnterTransition.None
            },
            exitTransition = {
                ExitTransition.None
            }
        ) {
            composable("category_screen") {
                CategoryScreen(
                    uiState = uiState,
                    onCategoryClick = {
                        viewModel.onCategorySelected(it)
                        navController.navigate("items_screen?categoryId=${it.categoryId}")
                    },
                    onLogin = { user, pass -> viewModel.authenticateUser(user, pass) },
                    onLogout = { viewModel.deauthenticateUser() },
                    onAddCategory = { categoryName ->
                        viewModel.addCategory(categoryName)
                    }
                )
            }
            composable(
                route = "items_screen?categoryId={categoryId}",
                arguments = listOf(navArgument("categoryId") { type = NavType.IntType })
            ) {

                ItemListScreen(
                    uiState = uiState,
                    onItemClick = { item ->
                        viewModel.onItemSelected(item.itemId)
                        navController.navigate("detail_screen")
                    },
                    onAddItemScreenRequested = {
                        navController.navigate("add_item_screen")
                    }
                )
            }
            composable("detail_screen") {
                DetailScreen(
                    uiState = uiState,
                    onSave = { updatedItem ->
                        viewModel.updateItemDetail(updatedItem)
                        navController.popBackStack()
                    },
                    onDelete = { deletedItem ->
                        viewModel.deleteItem(deletedItem)
                        navController.popBackStack()
                    },
                            onCancelClick = {
                        navController.popBackStack()
                    }
                )
            }
            composable("add_item_screen") {
                AddItemScreen(
                    currentCategory = uiState.selectedCategory,
                    onAddClick = { catId, name ->
                        viewModel.addNewItem(catId, name)
                        navController.popBackStack()
                    },
                    onCancelClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
