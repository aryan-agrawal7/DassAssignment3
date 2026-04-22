package com.DASS_2024111023_2024117009.ims.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.automirrored.filled.ReceiptLong // FIXED IMPORT
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.DASS_2024111023_2024117009.ims.ui.theme.*
import com.DASS_2024111023_2024117009.ims.MockDatabase
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeDashboardScreen(navController: NavController) {
    val myLeaves = MockDatabase.leaves.collectAsState().value.filter { it.empId == MockDatabase.currentUser?.id }
    val annualLeaveQuota = 21
    val approvedDays = myLeaves.filter { it.overallStatus == "Approved" }.sumOf { leaveDurationDays(it.start, it.end) }
    val remainingLeaves = (annualLeaveQuota - approvedDays).coerceAtLeast(0)
    val latestLeave = latestLeaveForEmployee(myLeaves)
    
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
                    Text(MockDatabase.currentUser?.name ?: "Employee", color = TealAccent, fontSize = 16.sp)
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
                placeholder = { Text("Search news, records...", color = TextGray) },
                enabled = false, // Disable typing to act as a button
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextGray) },
                colors = TextFieldDefaults.colors(
                    disabledContainerColor = CardDark,
                    disabledIndicatorColor = Color.Transparent,
                    disabledTextColor = TextWhite
                ),
                shape = RoundedCornerShape(12.dp), 
                modifier = Modifier
                    .fillMaxWidth()
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
            Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(24.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("$remainingLeaves", color = TextWhite, fontSize = 48.sp, fontWeight = FontWeight.ExtraBold)
                        Text("LEAVES REMAINING", color = TextGray, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    }
                    // ... icon
                }
            }
        }
        if (latestLeave != null) {
            item {
                val statusColor = when (latestLeave.overallStatus) {
                    "Approved" -> TealAccent
                    "Rejected" -> Color(0xFFFCA5A5)
                    else -> Color(0xFFFDBA74)
                }
                val statusBg = when (latestLeave.overallStatus) {
                    "Approved" -> Color(0xFF042F2E)
                    "Rejected" -> Color(0xFF3F2323)
                    else -> Color(0xFF431407)
                }
                Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("Latest Request", color = TextGray, fontSize = 12.sp)
                            Surface(shape = RoundedCornerShape(12.dp), color = statusBg) {
                                Text(latestLeave.overallStatus.uppercase(), color = statusColor, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(latestLeave.type, color = TextWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text("${latestLeave.start} - ${latestLeave.end}", color = TextGray, fontSize = 12.sp)
                    }
                }
            }
        }
        item {
            Text("Quick Access", color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                // FIXED WARNING HERE:
                QuickAccessCard("Payslips", "View history", Icons.AutoMirrored.Filled.ReceiptLong, TealAccent, Modifier.weight(1f)) { navigateToTab("emp_payroll") }
                QuickAccessCard("Leaves", "Apply & track", Icons.AutoMirrored.Filled.EventNote, Color(0xFF60A5FA), Modifier.weight(1f)) { navigateToTab("emp_leaves") }
            }
        }
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2A1616)),
                shape = RoundedCornerShape(16.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF451A1A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Exit Institute", color = Color(0xFFFCA5A5), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Initiate formal offboarding process.", color = TextGray, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { navController.navigate("emp_exit") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F2323)), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Text("Start Process", color = Color(0xFFFCA5A5), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

private fun latestLeaveForEmployee(leaves: List<com.DASS_2024111023_2024117009.ims.Leave>): com.DASS_2024111023_2024117009.ims.Leave? {
    val latestByAppliedDate = leaves
        .mapNotNull { leave -> parseDashboardDate(leave.appliedDate)?.let { it to leave } }
        .maxByOrNull { it.first }
        ?.second
    return latestByAppliedDate ?: leaves.lastOrNull()
}

private fun leaveDurationDays(start: String, end: String): Int {
    val startMillis = parseDashboardDate(start)
    val endMillis = parseDashboardDate(end)
    if (startMillis == null || endMillis == null || endMillis < startMillis) return 1
    val oneDayMs = 24 * 60 * 60 * 1000L
    return (((endMillis - startMillis) / oneDayMs) + 1).toInt()
}

private fun parseDashboardDate(value: String): Long? {
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)
    return runCatching { formatter.parse(value)?.time }.getOrNull()
}

@Composable
fun QuickAccessCard(title: String, subtitle: String, icon: ImageVector, iconTint: Color, modifier: Modifier, onClick: () -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = modifier.height(120.dp).clickable(onClick = onClick)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Surface(shape = RoundedCornerShape(12.dp), color = iconTint, modifier = Modifier.size(40.dp)) {
                Icon(icon, contentDescription = null, tint = Color.Black, modifier = Modifier.padding(8.dp))
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(title, color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(subtitle, color = TextGray, fontSize = 10.sp)
        }
    }
}