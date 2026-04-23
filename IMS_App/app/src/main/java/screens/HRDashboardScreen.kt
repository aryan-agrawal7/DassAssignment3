package com.DASS_2024111023_2024117009.ims.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.DASS_2024111023_2024117009.ims.ui.theme.*
import com.DASS_2024111023_2024117009.ims.MockDatabase
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HRDashboardScreen(navController: NavController) {
    val cyanCardColor = Color(0xFF34D399)
    val users by MockDatabase.users.collectAsState()
    val leaves by MockDatabase.leaves.collectAsState()
    val payslips by MockDatabase.payslips.collectAsState()

    val pendingLeaves = leaves.filter { it.hrStatus == "Pending" }.takeLast(3).asReversed()
    val pendingPayroll = payslips.filter { it.status == "Pending" }
    val pendingPayrollCount = pendingPayroll.size
    val pendingPayrollAmount = pendingPayroll.sumOf { it.net.toAmountOrZero() }
    val hasPendingPayroll = pendingPayrollCount > 0
    val pendingPayrollColor = if (hasPendingPayroll) Color(0xFFEF4444) else Color(0xFF10B981)
    val pendingPayrollChipText = if (pendingPayrollCount == 1) "1 Pending HR Approval" else "$pendingPayrollCount Pending HR Approvals"

    fun navigateToTab(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.startDestinationId) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize().background(BackgroundDark).padding(20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    val greeting = remember { listOf("Good morning,", "Good afternoon,", "Good evening,", "Welcome back,", "Hello,").random() }
                    Text(greeting, color = TextWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(MockDatabase.currentUser?.name ?: "HR", color = TealAccent, fontSize = 16.sp)
                }
                Row {
                    IconButton(onClick = { navController.navigate("settings") }) { Icon(Icons.Default.Settings, contentDescription = "Settings", tint = TextGray) }
                    IconButton(onClick = { navController.navigate("messages") }) { Icon(Icons.AutoMirrored.Filled.Message, contentDescription = "Messages", tint = TextGray) }
                }
            }
        }
        item {
            TextField(
                value = "", 
                onValueChange = {}, 
                enabled = false,
                placeholder = { Text("Search news, records...", color = TextGray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextGray) },
                colors = TextFieldDefaults.colors(
                    disabledContainerColor = CardDark,
                    disabledIndicatorColor = Color.Transparent,
                    disabledTextColor = TextWhite
                ),
                shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()
                    .clickable { navController.navigate("global_search") }
            )
        }
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Latest News", color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("View All", color = TealAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { navController.navigate("news_list") })
            }
            Spacer(modifier = Modifier.height(12.dp))
            val latestNews = MockDatabase.allNews.collectAsState().value.sortedByDescending { it.order }.take(3)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                latestNews.forEach { news ->
                    item {
                        NewsCardStub(news.author, news.time, news.title, news.content, Color(0xFF1976D2), onClick = { navController.navigate("news_detail/${news.id}") })
                    }
                }
            }
        }
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = cyanCardColor), shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth().height(160.dp).clickable { navController.navigate("hr_admission_form") }
            ) {
                Box(modifier = Modifier.fillMaxSize().padding(20.dp)) {
                    Column {
                        Icon(Icons.Default.PersonAdd, contentDescription = null, tint = Color(0xFF042F2E), modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("New Employee Admission", color = Color(0xFF042F2E), fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Start onboarding process for new hires.", color = Color(0xFF064E3B), fontSize = 12.sp)
                    }
                    Surface(shape = CircleShape, color = Color(0xFF10B981), modifier = Modifier.align(Alignment.BottomEnd).size(40.dp)) {
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color(0xFF042F2E), modifier = Modifier.padding(8.dp))
                    }
                }
            }
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Employee Directory", color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text("View All", color = TealAccent, fontSize = 12.sp, modifier = Modifier.clickable { navController.navigate("hr_employee_list") })
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    val employeeList = users.filter { it.role == "Employee" }.take(3)
                    if (employeeList.isEmpty()) {
                        Text("No employees found", color = TextGray, fontSize = 12.sp)
                    } else {
                        employeeList.forEachIndexed { index, user ->
                            val initials = user.name.split(" ").filter { it.isNotEmpty() }.take(2).joinToString("") { it[0].toString().uppercase() }
                            EmployeeMiniStub(initials, user.name, "${user.department} • ${user.role}", Color(0xFF1E88E5))
                            if (index < employeeList.size - 1) {
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }
                }
            }
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Payments, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Pending Payroll", color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                        Text("View All", color = TealAccent, fontSize = 12.sp, modifier = Modifier.clickable { navigateToTab("hr_payslips") })
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("PENDING PAYROLL", color = TextGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                        Text(formatPayrollAmount(pendingPayrollAmount), color = pendingPayrollColor, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                        Surface(shape = RoundedCornerShape(16.dp), color = pendingPayrollColor.copy(alpha = 0.12f)) {
                            Text(pendingPayrollChipText, color = pendingPayrollColor, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp))
                        }
                    }
                }
            }
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // FIXED: Applied the AutoMirrored EventNote here!
                            Icon(Icons.AutoMirrored.Filled.EventNote, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Leave Requests", color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                        Text("View All", color = TealAccent, fontSize = 12.sp, modifier = Modifier.clickable { navigateToTab("hr_leaves") })
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    if (pendingLeaves.isEmpty()) {
                        Surface(shape = RoundedCornerShape(12.dp), color = InputFieldDark, modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "No pending leave requests",
                                color = Color(0xFF10B981),
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
                            )
                        }
                    } else {
                        pendingLeaves.forEachIndexed { index, leave ->
                            val employeeName = users.find { it.id == leave.empId }?.name ?: leave.empId
                            LeavePreviewStub(employeeName, "${leave.start} - ${leave.end}", leave.type)
                            if (index < pendingLeaves.lastIndex) {
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun String.toAmountOrZero(): Double {
    return replace(",", "").toDoubleOrNull() ?: 0.0
}

private fun formatPayrollAmount(amount: Double): String {
    return if (amount % 1.0 == 0.0) {
        "$${amount.toInt()}"
    } else {
        String.format(Locale.US, "$%.2f", amount)
    }
}

@Composable
fun LeavePreviewStub(name: String, dates: String, type: String) {
    Surface(shape = RoundedCornerShape(12.dp), color = InputFieldDark, modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(name, color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(dates, color = TextGray, fontSize = 12.sp)
            }
            Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFF374151)) {
                Text(type, color = TextGray, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
            }
        }
    }
}

@Composable
fun EmployeeMiniStub(initials: String, name: String, subtitle: String, color: Color) {
    Surface(shape = RoundedCornerShape(12.dp), color = InputFieldDark, modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = CircleShape, color = color, modifier = Modifier.size(40.dp)) {
                Box(contentAlignment = Alignment.Center) { Text(initials, color = Color.White, fontWeight = FontWeight.Bold) }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(name, color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(subtitle, color = TextGray, fontSize = 12.sp)
            }
        }
    }
}