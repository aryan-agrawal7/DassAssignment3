package com.DASS_2024111023_2024117009.ims.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun RootNavigation() {
    val rootNavController = rememberNavController()

    NavHost(navController = rootNavController, startDestination = "login") {
        composable("login") { LoginScreen(rootNavController) }
        composable("student_root") { StudentMainScreen(onLogout = { rootNavController.navigate("login") { popUpTo(0) } }) }
        composable("hr_root") { HRMainScreen(onLogout = { rootNavController.navigate("login") { popUpTo(0) } }) }
        composable("employee_root") { EmployeeMainScreen(onLogout = { rootNavController.navigate("login") { popUpTo(0) } }) }
        composable("admin_root") { AdminMainScreen(onLogout = { rootNavController.navigate("login") { popUpTo(0) } }) }
    }
}