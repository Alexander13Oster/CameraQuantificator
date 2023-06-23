package de.alexander13oster.cameraquantificator

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = "main_screen"
    ) {
        composable(route = "main_screen") {
            MainScreen(
                onNavigateToCameraX = {
                    navController.navigate("camera_x_camera_screen")
                }
            )
        }
        composable(route = "camera_x_camera_screen") {
            CameraXCameraScreen(
                navigateToResult = { result ->
                    val encodedResult = Uri.encode(result)
                    navController.navigate("result_screen/$encodedResult")
                }
            )
        }
        composable(
            route = "result_screen/{$resultArg}",
            arguments = listOf(
                navArgument(resultArg) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            backStackEntry.arguments?.getString(resultArg)?.let { ResultScreen(result = it) }
        }
    }
}
