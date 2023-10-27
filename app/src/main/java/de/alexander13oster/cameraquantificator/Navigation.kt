package de.alexander13oster.cameraquantificator

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import de.alexander13oster.cameraquantificator.scanner.mlkit.MlKitCameraScreen
import java.util.Base64

const val SCAN_RESULT = "scan_result"

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = NavigationRoute.MAIN_SCREEN.route,
    ) {
        composable(
            route = NavigationRoute.MAIN_SCREEN.route,
        ) {
            MainScreen(
                onNavigateToMlKitScanner = {
                    navController.navigate(NavigationRoute.ML_KIT_SCREEN.route)
                }
            )
        }
        composable(
            route = "${NavigationRoute.RESULT_SCREEN.route}/{$SCAN_RESULT}",
            arguments = listOf(
                navArgument(SCAN_RESULT) { type = NavType.StringType },
            ),
        ) {
            ResultScreen(
                it.arguments
                    ?.getString(SCAN_RESULT)
                    ?.decodeFromBase64String()
                    .orEmpty()
            )
        }
        composable(
            route = NavigationRoute.ML_KIT_SCREEN.route,
        ) {
            MlKitCameraScreen(
                onScanResult = {
                    navController.navigate(
                        "${NavigationRoute.RESULT_SCREEN.route}/${it.encodeToBase64String()}"
                    )
                }
            )
        }
    }
}

private fun String.encodeToBase64String() = Base64.getEncoder().encodeToString(this.toByteArray())

private fun String.decodeFromBase64String() = String(Base64.getDecoder().decode(this))
