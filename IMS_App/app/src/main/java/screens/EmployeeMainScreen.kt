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

sealed class EmpBottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : EmpBottomNavItem("emp_dashboard", "Dashboard", Icons.Default.Dashboard)
    object Leaves : EmpBottomNavItem("emp_leaves", "Leaves", Icons.AutoMirrored.Filled.EventNote)
    object Payroll : EmpBottomNavItem("emp_payroll", "Payroll", Icons.Default.Payments)
    object Profile : EmpBottomNavItem("emp_profile", "Profile", Icons.Default.Person)
}

@Composable
fun EmployeeMainScreen(onLogout: () -> Unit = {}) {
    val navController = rememberNavController()

    Scaffold(bottomBar = { EmpBottomNav(navController) }, containerColor = BackgroundDark) { innerPadding ->
        NavHost(navController = navController, startDestination = EmpBottomNavItem.Dashboard.route, modifier = Modifier.padding(innerPadding)) {
            composable(EmpBottomNavItem.Dashboard.route) { EmployeeDashboardScreen(navController) }
            composable(EmpBottomNavItem.Leaves.route) { EmployeeLeavesScreen(navController) }
            composable(EmpBottomNavItem.Payroll.route) { EmployeePayrollScreen(navController) }
            composable(EmpBottomNavItem.Profile.route) { EmployeeProfileScreen(onLogout) }

            composable("settings") { GeneralSettingsScreen(navController) }
            composable("messages") { MessagesScreen(navController) }
            composable("global_search") { GlobalSearchScreen(navController) }
            composable("emp_exit") { EmployeeExitScreen(navController, onLogout) }
            composable("emp_fill_payslip") { EmployeeFillPayslipScreen(navController) }
            composable("emp_apply_leave") { EmployeeApplyLeaveScreen(navController) }
            composable("news_list") { InstituteNewsScreen(navController, isAdmin = false) }
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
fun EmpBottomNav(navController: NavHostController) {
    val items = listOf(EmpBottomNavItem.Dashboard, EmpBottomNavItem.Leaves, EmpBottomNavItem.Payroll, EmpBottomNavItem.Profile)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val dashboardSubRoutes = listOf("news_list")

    // HIDDEN FOR ALL FORMS AND SUB-SCREENS
    if (currentRoute in listOf("settings", "messages", "emp_exit", "emp_fill_payslip", "emp_apply_leave", "global_search") ||
        currentRoute?.startsWith("news_detail") == true) return

    NavigationBar(containerColor = CardDark, contentColor = TextGray) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route || (item.route == EmpBottomNavItem.Dashboard.route && currentRoute in dashboardSubRoutes)
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) }, label = { Text(text = item.title) },
                selected = isSelected,
                onClick = { navController.navigate(item.route) { popUpTo(navController.graph.startDestinationId) { saveState = true }; launchSingleTop = true; restoreState = true } },
                colors = NavigationBarItemDefaults.colors(selectedIconColor = TealAccent, selectedTextColor = TealAccent, unselectedIconColor = TextGray, unselectedTextColor = TextGray, indicatorColor = Color.Transparent)
            )
        }
    }
}

@Composable
fun PlaceholderScreen(title: String) {
    Box(modifier = Modifier.fillMaxSize().background(BackgroundDark), contentAlignment = Alignment.Center) {
        Text(title, color = TealAccent, fontSize = 24.sp)
    }
}