package com.DASS_2024111023_2024117009.ims.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.DASS_2024111023_2024117009.ims.MockDatabase
import com.DASS_2024111023_2024117009.ims.ui.theme.*

@Composable
fun GlobalSearchScreen(navController: NavController) {
    var query by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val role = MockDatabase.currentUser?.role ?: "Student"

    // Auto-focus the search bar when screen opens
    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    Column(modifier = Modifier.fillMaxSize().background(BackgroundDark)) {
        Surface(color = CardDark, modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TealAccent)
                }
                TextField(
                    value = query, onValueChange = { query = it },
                    placeholder = { Text("Search anywhere...", color = TextGray) },
                    colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, focusedTextColor = TextWhite),
                    modifier = Modifier.fillMaxWidth().focusRequester(focusRequester)
                )
            }
        }

        LazyColumn(modifier = Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item { Text("Quick Navigation", color = TextGray, fontSize = 12.sp, fontWeight = FontWeight.Bold) }

            // Dynamic routing based on role
            if (query.contains("news", ignoreCase = true) || query.isEmpty()) {
                item { SearchResultItem("Institute News", "View latest broadcasts") { navController.navigate("news_list") } }
            }
            if (role == "Admin" && (query.contains("news", ignoreCase = true) || query.contains("create", ignoreCase = true) || query.isEmpty())) {
                item { SearchResultItem("Create News", "Broadcast a new announcement") { navController.navigate("admin_create_news") } }
            }
            
            if (role == "Employee" && (query.contains("leave", ignoreCase = true) || query.contains("apply", ignoreCase = true) || query.isEmpty())) {
                item { SearchResultItem("Apply Leave", "Submit a new leave request") { navController.navigate("emp_apply_leave") } }
            }
            if (role == "Employee" && (query.contains("pay", ignoreCase = true) || query.isEmpty())) {
                item { SearchResultItem("Fill Payslip", "Submit monthly payroll data") { navController.navigate("emp_fill_payslip") } }
            }
            if (role == "Employee" && (query.contains("exit", ignoreCase = true) || query.isEmpty())) {
                item { SearchResultItem("Exit Interview", "Resign and delete data") { navController.navigate("emp_exit") } }
            }

            if (role == "HR" && (query.contains("admission", ignoreCase = true) || query.contains("add", ignoreCase = true) || query.isEmpty())) {
                item { SearchResultItem("Employee Admission", "Add a new employee") { navController.navigate("hr_admission_form") } }
            }
            if (role == "HR" && (query.contains("directory", ignoreCase = true) || query.contains("employee", ignoreCase = true) || query.isEmpty())) {
                item { SearchResultItem("Employee Directory", "Search for an employee") { navController.navigate("hr_employee_list") } }
            }
            if (role == "HR" && (query.contains("customize", ignoreCase = true) || query.contains("payroll", ignoreCase = true) || query.isEmpty())) {
                item { SearchResultItem("Customize Payslip", "Add or remove fields") { navController.navigate("hr_payroll_setup") } }
            }

            if (role == "Admin" && (query.contains("customize", ignoreCase = true) || query.contains("admission", ignoreCase = true) || query.isEmpty())) {
                item { SearchResultItem("Customize Employee Admission", "Modify employee admission form") { navController.navigate("admin_customize_admission") } }
                item { SearchResultItem("Customize Student Admission", "Modify student admission form") { navController.navigate("admin_customize_student_admission") } }
            }
            if (role == "Admin" && (query.contains("leave", ignoreCase = true) || query.contains("approve", ignoreCase = true) || query.isEmpty())) {
                item { SearchResultItem("Leave Approvals", "Final approval for leaves") { navController.navigate("admin_leave_approvals") } }
            }

            if ((query.contains("set", ignoreCase = true) || query.isEmpty())) {
                item { SearchResultItem("Settings", "Configure your account") { navController.navigate(if (role=="Admin") "admin_settings" else "settings") } }
            }
        }
    }
}

@Composable
fun SearchResultItem(title: String, subtitle: String, onClick: () -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(title, color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(subtitle, color = TextGray, fontSize = 12.sp)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TealAccent)
        }
    }
}