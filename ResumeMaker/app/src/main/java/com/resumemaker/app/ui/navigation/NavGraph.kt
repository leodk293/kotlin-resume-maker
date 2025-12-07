package com.resumemaker.app.ui.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.resumemaker.app.ui.screens.*
import com.resumemaker.app.ui.viewmodel.CVViewModel
import kotlinx.coroutines.flow.first

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Create : Screen("create")
    object Edit : Screen("edit/{cvId}") {
        fun createRoute(cvId: String) = "edit/$cvId"
    }
    object Detail : Screen("detail/{cvId}") {
        fun createRoute(cvId: String) = "detail/$cvId"
    }
}

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val viewModel: CVViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToCreate = {
                    navController.navigate(Screen.Create.route)
                },
                onNavigateToEdit = { cv ->
                    navController.navigate(Screen.Edit.createRoute(cv.id))
                },
                onNavigateToDetail = { cv ->
                    navController.navigate(Screen.Detail.createRoute(cv.id))
                }
            )
        }

        composable(Screen.Create.route) {
            CreateEditCVScreen(
                viewModel = viewModel,
                cvId = null,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.Edit.route,
            arguments = listOf(navArgument("cvId") { type = NavType.StringType })
        ) { backStackEntry ->
            val cvId = backStackEntry.arguments?.getString("cvId")
            CreateEditCVScreen(
                viewModel = viewModel,
                cvId = cvId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("cvId") { type = NavType.StringType })
        ) { backStackEntry ->
            val cvId = backStackEntry.arguments?.getString("cvId")
            var cv by remember { mutableStateOf<com.resumemaker.app.data.models.CV?>(null) }
            
            LaunchedEffect(cvId) {
                if (cvId != null) {
                    cv = viewModel.getCVById(cvId)
                }
            }
            
            cv?.let { currentCV ->
                CVDetailScreen(
                    cv = currentCV,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToEdit = {
                        navController.navigate(Screen.Edit.createRoute(currentCV.id))
                    }
                )
            }
        }
    }
}

