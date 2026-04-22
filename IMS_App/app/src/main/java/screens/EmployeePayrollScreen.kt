package com.DASS_2024111023_2024117009.ims.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.DASS_2024111023_2024117009.ims.ui.theme.*
import com.DASS_2024111023_2024117009.ims.MockDatabase
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun EmployeePayrollScreen(navController: NavController) {
    val myPayslips by MockDatabase.payslips.collectAsState()
    val filteredPayslips = myPayslips.filter { it.empId == MockDatabase.currentUser?.id }.reversed()

    val totalCount = filteredPayslips.size
    val pendingCount = filteredPayslips.count { it.status == "Pending" }
    val lastApproved = filteredPayslips.firstOrNull { it.status == "Approved" }?.month ?: "None"

    LazyColumn(modifier = Modifier.fillMaxSize().background(BackgroundDark).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Text("Payslip History", color = TextWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
        }

        // Stats Row
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                PayrollStatCard("Total Payslips", "$totalCount", Modifier.weight(1f), TextWhite)
                PayrollStatCard("Last Approved", lastApproved, Modifier.weight(1.2f), TextWhite)
                PayrollStatCard("Pending", "$pendingCount", Modifier.weight(0.8f), if (pendingCount > 0) Color(0xFFFDBA74) else TextGray)
            }
        }

        // Fill Form Button
        item {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Surface(
                    shape = RoundedCornerShape(8.dp), color = TealAccent,
                    onClick = { navController.navigate("emp_fill_payslip") }
                ) {
                    Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        // FIXED: Made icon and text Color.Black for high contrast
                        Icon(Icons.Default.PostAdd, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Fill Payslip Form", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // History List
        items(filteredPayslips.size) { index ->
            val p = filteredPayslips[index]
            PayslipHistoryCard(
                title = p.month,
                subtitle = "ID: ${p.id.take(8).uppercase()}",
                amount = "$${p.net}",
                status = p.status.uppercase()
            )
        }
    }
}

@Composable
fun PayrollStatCard(title: String, value: String, modifier: Modifier, valueColor: Color) {
    Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = modifier.height(70.dp)) {
        Column(modifier = Modifier.padding(12.dp).fillMaxSize(), verticalArrangement = Arrangement.Center) {
            Text(title, color = TextGray, fontSize = 10.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, color = valueColor, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PayslipHistoryCard(title: String, subtitle: String, amount: String, status: String, isAmount: Boolean = false) {
    val statusColor = when (status) {
        "APPROVED" -> TealAccent
        "PENDING" -> Color(0xFFFDBA74) // Orange
        "REJECTED" -> Color(0xFFFCA5A5) // Red
        else -> TextGray
    }
    val statusBg = when (status) {
        "APPROVED" -> Color(0xFF042F2E)
        "PENDING" -> Color(0xFF431407)
        "REJECTED" -> Color(0xFF3F2323)
        else -> BackgroundDark
    }

    Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(title, color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Surface(shape = RoundedCornerShape(12.dp), color = statusBg) {
                    Text(status, color = statusColor, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(subtitle, color = TextGray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(if (isAmount) "Amount" else "Net Pay", color = TextGray, fontSize = 10.sp)
            Text(
                text = amount, color = TextWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold,
                textDecoration = if (status == "REJECTED") TextDecoration.LineThrough else TextDecoration.None
            )
        }
    }
}