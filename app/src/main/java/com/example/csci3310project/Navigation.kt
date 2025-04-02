package com.example.csci3310project

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.login.takeuserface.CameraScreen
import com.example.login.screen.*

sealed class Screen(val route: String){
    object Login : Screen("login_screen")
    object Camera : Screen("camera_screen")
    object Register : Screen("register_screen")
    object ForgetPassword : Screen("forgetpw_screen")
    object ForgetPassword2 : Screen("forgetpw2_screen")
    object AvatarCreation: Screen("create_screen")

}
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(Screen.Camera.route) {
            CameraScreen(paddingValues = PaddingValues())
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
                //onBack = {navController.popBackStack()}
        }
    }
}
