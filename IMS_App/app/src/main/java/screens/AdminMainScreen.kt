package com.DASS_2024111023_2024117009.ims.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
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

sealed class AdminBottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : AdminBottomNavItem("admin_dashboard", "Dashboard", Icons.Default.Dashboard)
    object Student : AdminBottomNavItem("admin_student", "Student", Icons.Default.School)
    object Users : AdminBottomNavItem("admin_users", "Users", Icons.Default.Assessment)
    object Profile : AdminBottomNavItem("admin_profile", "Profile", Icons.Default.Person)
}

@Composable
fun AdminMainScreen(onLogout: () -> Unit = {}) {
    val navController = rememberNavController()

    Scaffold(bottomBar = { AdminBottomNav(navController) }, containerColor = BackgroundDark) { innerPadding ->
        NavHost(navController = navController, startDestination = AdminBottomNavItem.Dashboard.route, modifier = Modifier.padding(innerPadding)) {
            composable(AdminBottomNavItem.Dashboard.route) { AdminDashboardScreen(navController) }
            composable(AdminBottomNavItem.Student.route) { AdminStudentDetailsScreen(navController) }
            composable(AdminBottomNavItem.Users.route) { AdminManageUsersScreen(navController) }
            composable(AdminBottomNavItem.Profile.route) { AdminProfileScreen(onLogout) }

            composable("admin_settings") { AdminSettingsScreen(navController) }
            composable("admin_student_admission") { AdminStudentAdmissionScreen(navController) }
            composable("messages") { MessagesScreen(navController) }
            composable("admin_customize_admission") { AdminEmployeeAdmissionCustomizeScreen(navController) }
            composable("admin_customize_student_admission") { AdminStudentAdmissionCustomizeScreen(navController) }
            composable("admin_leave_approvals") { AdminLeaveApprovalScreen(navController) }
            composable("admin_finance") { AdminFinanceScreen(navController) }
            composable("news_list") { InstituteNewsScreen(navController, isAdmin = true) }
            composable("admin_create_news") { AdminCreateNewsScreen(navController) }
            composable("global_search") { GlobalSearchScreen(navController) }
            // NEW ROUTE
            composable(
                route = "news_detail/{newsId}",
                arguments = listOf(navArgument("newsId") { type = NavType.StringType })
            ) { backStackEntry ->
                val newsId = backStackEntry.arguments?.getString("newsId") ?: ""
                InstituteNewsDetailScreen(navController, newsId, isAdmin = true)
            }
        }
    }
}

@Composable
fun AdminBottomNav(navController: NavHostController) {
    val items = listOf(AdminBottomNavItem.Dashboard, AdminBottomNavItem.Student, AdminBottomNavItem.Users, AdminBottomNavItem.Profile)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Hide bottom bar on sub-screens
    if (currentRoute in listOf("admin_settings", "messages", "admin_create_news", "admin_customize_admission", "admin_customize_student_admission", "admin_student_admission", "admin_leave_approvals", "admin_finance", "global_search") ||
        currentRoute?.startsWith("news_detail") == true) return

    // FIXED: Keeps Dashboard highlighted for sub-routes
    val dashboardSubRoutes = listOf("news_list", "admin_finance", "admin_customize_admission", "admin_customize_student_admission", "admin_leave_approvals", "admin_student_admission")

    NavigationBar(containerColor = CardDark, contentColor = TextGray) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route || (item.route == AdminBottomNavItem.Dashboard.route && currentRoute in dashboardSubRoutes)

            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) }, label = { Text(text = item.title) },
                selected = isSelected,
                onClick = { navController.navigate(item.route) { popUpTo(navController.graph.startDestinationId) { saveState = true }; launchSingleTop = true; restoreState = true } },
                colors = NavigationBarItemDefaults.colors(selectedIconColor = TealAccent, selectedTextColor = TealAccent, unselectedIconColor = TextGray, unselectedTextColor = TextGray, indicatorColor = Color.Transparent)
            )
        }
    }
}