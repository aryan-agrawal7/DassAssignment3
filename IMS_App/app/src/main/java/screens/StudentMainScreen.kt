package com.DASS_2024111023_2024117009.ims.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.DASS_2024111023_2024117009.ims.ui.theme.*

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : BottomNavItem("dashboard", "Dashboard", Icons.Default.Dashboard)
    object Academics : BottomNavItem("academics", "Academics", Icons.AutoMirrored.Filled.MenuBook)
    object Schedule : BottomNavItem("schedule", "Schedule", Icons.Default.CalendarMonth)
    object Profile : BottomNavItem("profile", "Profile", Icons.Default.Person)
}

@Composable
fun StudentMainScreen(onLogout: () -> Unit) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { StudentBottomNav(navController) },
        containerColor = BackgroundDark
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Main Bottom Tabs
            composable(BottomNavItem.Dashboard.route) { StudentDashboardScreen(navController) }
            composable(BottomNavItem.Academics.route) { AcademicsScreen() }
            composable(BottomNavItem.Schedule.route) { ScheduleScreen() }
            composable(BottomNavItem.Profile.route) { StudentProfileScreen(onLogout) }

            // Hidden/Sub Screens
            composable("attendance") { AttendanceScreen(navController) }
            composable("settings") { GeneralSettingsScreen(navController) }
            composable("messages") { MessagesScreen(navController) }
            composable("news_list") { InstituteNewsScreen(navController, isAdmin = false) }
            composable("global_search") { GlobalSearchScreen(navController) }
            composable(
                route = "news_detail/{newsId}",
                arguments = listOf(navArgument("newsId") { type = NavType.StringType })
            ) { backStackEntry ->
                val newsId = backStackEntry.arguments?.getString("newsId") ?: ""
                InstituteNewsDetailScreen(navController, newsId, isAdmin = false)
            }
        }
    }
}

@Composable
fun StudentBottomNav(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Dashboard,
        BottomNavItem.Academics,
        BottomNavItem.Schedule,
        BottomNavItem.Profile
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val dashboardSubRoutes = listOf("news_list")

    // Hide bottom bar on sub-screens
    if (currentRoute in listOf("attendance", "settings", "messages", "global_search") ||
        currentRoute?.startsWith("news_detail") == true) return

    NavigationBar(containerColor = CardDark, contentColor = TextGray) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route || (item.route == BottomNavItem.Dashboard.route && currentRoute in dashboardSubRoutes)
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(text = item.title) },
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = TealAccent, selectedTextColor = TealAccent,
                    unselectedIconColor = TextGray, unselectedTextColor = TextGray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}