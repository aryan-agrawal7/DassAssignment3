package com.DASS_2024111023_2024117009.ims.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.DASS_2024111023_2024117009.ims.ui.theme.*
import com.DASS_2024111023_2024117009.ims.MockDatabase

@Composable
fun EmployeeLeavesScreen(navController: NavController) {
    val myLeaves = MockDatabase.leaves.collectAsState().value.filter { it.empId == MockDatabase.currentUser?.id }.reversed()
    val metrics = MockDatabase.getLeaveMetrics(MockDatabase.currentUser?.id ?: "")

    Scaffold(
        containerColor = BackgroundDark,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("emp_apply_leave") },
                containerColor = TealAccent,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Apply Leave", tint = Color.Black)
            }
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                Text("Leave History", color = TextWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
            }

            // Stats Row
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    PayrollStatCard("Total", "${metrics.total}", Modifier.weight(1f), TextWhite)
                    PayrollStatCard("Balance", "${metrics.balance}", Modifier.weight(1f), TealAccent)
                    PayrollStatCard("Pending", "${metrics.pending}", Modifier.weight(1f), Color(0xFFFDBA74))
                }
            }

            // History List
            myLeaves.forEach { leave ->
                item {
                    LeaveHistoryCard(leave.type, "${leave.start} - ${leave.end}", leave.overallStatus, leave.hrStatus, leave.adminStatus)
                }
            }
        }
    }
}

@Composable
fun LeaveHistoryCard(title: String, dates: String, overallStatus: String, hrStatus: String, adminStatus: String) {
    val statusColor = when (overallStatus) {
        "Approved" -> TealAccent
        "Rejected" -> Color(0xFFFCA5A5)
        else -> Color(0xFFFDBA74)
    }
    val statusBg = when (overallStatus) {
        "Approved" -> Color(0xFF042F2E)
        "Rejected" -> Color(0xFF3F2323)
        else -> Color(0xFF431407)
    }

    Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Column {
                    Text(title, color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(dates, color = TextGray, fontSize = 12.sp)
                }
                Surface(shape = RoundedCornerShape(16.dp), color = statusBg) {
                    Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Surface(shape = CircleShape, color = statusColor, modifier = Modifier.size(6.dp)) {}
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(overallStatus, color = statusColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Dual Approval Status
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                StatusBadge("HR Status", hrStatus)
                StatusBadge("Admin Status", adminStatus)
            }
        }
    }
}

@Composable
fun StatusBadge(label: String, status: String) {
    val color = when (status) {
        "Approved" -> TealAccent
        "Rejected" -> Color(0xFFFCA5A5)
        else -> Color(0xFFFDBA74)
    }
    val icon = when (status) {
        "Approved" -> Icons.Default.CheckCircle
        "Rejected" -> Icons.Default.Cancel
        else -> Icons.Default.Schedule
    }
    
    Column {
        Text(label, color = TextGray, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(status, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}