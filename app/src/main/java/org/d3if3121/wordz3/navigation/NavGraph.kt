package org.d3if3121.wordz3.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.d3if3121.wordz3.ui.screen.DetailScreen
import org.d3if3121.wordz3.ui.screen.KEY_ID_CATATAN
import org.d3if3121.wordz3.ui.screen.MainScreen
import org.d3if3121.wordz3.ui.screen.QuizScreen2


@Composable
fun SetupNavGraph(navController: NavHostController = rememberNavController()){
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ){
        composable(route = Screen.Home.route){
            MainScreen(navController)
        }

        composable(route = Screen.FormBaru.route){
            DetailScreen(navController)
        }

        composable(
            route = Screen.FormUbah.route,
            arguments = listOf(
                navArgument(KEY_ID_CATATAN){
                    type = NavType.LongType
                }
            )
        ){ navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getLong(KEY_ID_CATATAN)
            DetailScreen(navController, id)
        }

        composable(route = Screen.Quiz.route){
            QuizScreen2(navController)
        }
    }
}