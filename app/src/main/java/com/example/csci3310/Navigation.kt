package com.example.csci3310

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.afer_login.dataFetch.User
import com.example.login.takeuserface.CameraScreen
import com.example.login.takeuserface.ImageReviewScreen
import com.example.login.takeuserface.AvatarColorPreviewScreen
import com.example.login.screen.*

sealed class Screen(val route: String){
    object Login : Screen("login_screen")
    object Camera : Screen("camera_screen")
    object Register : Screen("register_screen")
    object ForgetPassword : Screen("forgetpw_screen")
    object ForgetPassword2 : Screen("forgetpw2_screen")
    object AvatarCreation: Screen("create_screen")
    object ImageReview: Screen("image_review_screen/{imageUri}")
    object AvatarColorPreview: Screen("avatar_color_preview_screen")
}

@Composable
fun BeforeLoginNavigation(onLoginSuccess: (User) -> Unit) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController, onLoginSuccess)
        }
        composable(Screen.Camera.route) {
            CameraScreen(navController)
        }
        composable(Screen.Register.route) {
            registerScreen(navController)
        }
        composable(Screen.ForgetPassword.route) {
            ForgetPasswordScreen(navController)
        }
        composable(Screen.ForgetPassword2.route) {
            ForgetPassword2Screen(navController)
        }
        composable(Screen.AvatarCreation.route) {
            CreateAvatar(navController)
        }
        // Add route for image review screen with URI parameter
        composable(
            route = "image_review_screen/{imageUri}",
            arguments = listOf(
                navArgument("imageUri") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val imageUriString = backStackEntry.arguments?.getString("imageUri") ?: ""
            ImageReviewScreen(navController, imageUriString)
        }
        
        // Add route for avatar color preview screen
        composable(Screen.AvatarColorPreview.route) {
            AvatarColorPreviewScreen(navController)
        }
    }
}

