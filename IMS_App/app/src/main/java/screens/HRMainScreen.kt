package com.DASS_2024111023_2024117009.ims.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.DASS_2024111023_2024117009.ims.ui.theme.*

sealed class HRBottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : HRBottomNavItem("hr_dashboard", "Dashboard", Icons.Default.Dashboard)
    object Leaves : HRBottomNavItem("hr_leaves", "Leaves", Icons.AutoMirrored.Filled.EventNote)
    object PaySlips : HRBottomNavItem("hr_payslips", "PaySlips", Icons.Default.Assessment)
    object Profile : HRBottomNavItem("hr_profile", "Profile", Icons.Default.Person)
}

@Composable
fun HRMainScreen(onLogout: () -> Unit = {}) {
    val navController = rememberNavController()

    Scaffold(bottomBar = { HRBottomNav(navController) }, containerColor = BackgroundDark) { innerPadding ->
        NavHost(navController = navController, startDestination = HRBottomNavItem.Dashboard.route, modifier = Modifier.padding(innerPadding)) {
            // Tabs
            composable(HRBottomNavItem.Dashboard.route) { HRDashboardScreen(navController) }
            composable(HRBottomNavItem.Leaves.route) { HRLeavesScreen(navController) }
            composable(HRBottomNavItem.PaySlips.route) { HRPayslipsScreen(navController) }
            composable(HRBottomNavItem.Profile.route) { HRProfileScreen(onLogout) }

            // Sub-screens
            composable("settings") { GeneralSettingsScreen(navController) }
            composable("messages") { MessagesScreen(navController) }
            composable("hr_employee_list") { HREmployeeListScreen(navController) }
            composable("hr_employee_detail") { HREmployeeDetailScreen(navController) }
            composable("hr_admission_form") { HRAdmissionFormScreen(navController) }
            composable("hr_payroll_setup") { HRPayrollSetupScreen(navController) }
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
fun HRBottomNav(navController: NavHostController) {
    val items = listOf(HRBottomNavItem.Dashboard, HRBottomNavItem.Leaves, HRBottomNavItem.PaySlips, HRBottomNavItem.Profile)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val dashboardSubRoutes = listOf("news_list")

    if (currentRoute in listOf("settings", "messages", "hr_employee_list", "hr_employee_detail", "hr_admission_form", "hr_payroll_setup", "global_search") ||
        currentRoute?.startsWith("news_detail") == true) return

    NavigationBar(containerColor = CardDark, contentColor = TextGray) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route || (item.route == HRBottomNavItem.Dashboard.route && currentRoute in dashboardSubRoutes)
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) }, label = { Text(text = item.title) },
                selected = isSelected,
                onClick = { navController.navigate(item.route) { popUpTo(navController.graph.startDestinationId) { saveState = true }; launchSingleTop = true; restoreState = true } },
                colors = NavigationBarItemDefaults.colors(selectedIconColor = TealAccent, selectedTextColor = TealAccent, unselectedIconColor = TextGray, unselectedTextColor = TextGray, indicatorColor = Color.Transparent)
            )
        }
    }
}