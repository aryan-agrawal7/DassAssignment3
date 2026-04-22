package com.DASS_2024111023_2024117009.ims.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue

@Composable
fun HRPayslipsScreen(navController: NavController) {
    val payslips by MockDatabase.payslips.collectAsState()
    val users by MockDatabase.users.collectAsState()
    var filterStatus by remember { mutableStateOf("Pending") }

    val filteredPayslips = payslips.filter { it.status == filterStatus }

    LazyColumn(modifier = Modifier.fillMaxSize().background(BackgroundDark).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Text("Payslip Approvals", color = TealAccent, fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CardDark),
                modifier = Modifier.fillMaxWidth().clickable { navController.navigate("hr_payroll_setup") }
            ) {
                Row(modifier = Modifier.padding(20.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("CUSTOMIZE", color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
                        Text("PAYROLL FORM", color = TealAccent, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
                    }
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = TealAccent)
                }
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (filterStatus == "Pending") TealAccent else InputFieldDark,
                    modifier = Modifier.clickable { filterStatus = "Pending" }
                ) {
                    Text("Pending", color = if (filterStatus == "Pending") Color.Black else TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                }
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (filterStatus == "Approved") TealAccent else InputFieldDark,
                    modifier = Modifier.clickable { filterStatus = "Approved" }
                ) {
                    Text("Approved", color = if (filterStatus == "Approved") Color.Black else TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                }
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (filterStatus == "Rejected") TealAccent else InputFieldDark,
                    modifier = Modifier.clickable { filterStatus = "Rejected" }
                ) {
                    Text("Rejected", color = if (filterStatus == "Rejected") Color.Black else TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                }
            }
        }

        filteredPayslips.forEach { payslip ->
            val emp = users.find { it.id == payslip.empId }
            item {
                PayslipCardStub(
                    initials = emp?.name?.take(2)?.uppercase() ?: "??",
                    name = emp?.name ?: "Unknown",
                    role = emp?.role ?: "Employee",
                    date = payslip.month,
                    basic = "$${payslip.basic}",
                    allowances = "+$${payslip.allowances}",
                    deductions = "-$${payslip.deductions}",
                    net = "$${payslip.net}",
                    note = "System generated.",
                    showActions = payslip.status == "Pending",
                    onApprove = { MockDatabase.updatePayslipStatusHR(payslip.id, "Approved") },
                    onReject = { MockDatabase.updatePayslipStatusHR(payslip.id, "Rejected") }
                )
            }
        }
    }
}

@Composable
fun FilterTabStub(text: String, isSelected: Boolean) {
    Surface(
        shape = RoundedCornerShape(20.dp), color = if (isSelected) TealAccent.copy(alpha = 0.1f) else CardDark,
        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, TealAccent) else null
    ) {
        Text(text, color = if (isSelected) TealAccent else TextGray, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
    }
}

@Composable
fun PayslipCardStub(
    initials: String, name: String, role: String, date: String, basic: String,
    allowances: String, deductions: String, net: String, note: String, showActions: Boolean = true,
    onApprove: () -> Unit = {}, onReject: () -> Unit = {}
) {
    Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(shape = CircleShape, color = Color(0xFF374151), modifier = Modifier.size(40.dp)) {
                        Box(contentAlignment = Alignment.Center) { Text(initials, color = TealAccent, fontWeight = FontWeight.Bold) }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(name, color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text(role, color = TextGray, fontSize = 12.sp)
                    }
                }
                Surface(shape = RoundedCornerShape(4.dp), color = InputFieldDark) {
                    Text(date, color = TextGray, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Surface(shape = RoundedCornerShape(8.dp), color = InputFieldDark, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Basic Salary", color = TextGray, fontSize = 10.sp)
                            Text(basic, color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Allowances", color = TextGray, fontSize = 10.sp)
                            Text(allowances, color = TealAccent, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Deductions", color = TextGray, fontSize = 10.sp)
                            Text(deductions, color = Color(0xFFEF4444), fontSize = 14.sp, fontWeight = FontWeight.Bold) // Red
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Net Salary", color = TextGray, fontSize = 10.sp)
                            Text(net, color = TealAccent, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(note, color = TextGray, fontSize = 12.sp)

            if (showActions) {
                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = onApprove, colors = ButtonDefaults.buttonColors(containerColor = TealAccent), shape = RoundedCornerShape(8.dp), modifier = Modifier.weight(1f).height(40.dp)) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Approve", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    Button(onClick = onReject, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F2323)), shape = RoundedCornerShape(8.dp), modifier = Modifier.weight(1f).height(40.dp)) {
                        Icon(Icons.Default.Cancel, contentDescription = null, tint = Color(0xFFFCA5A5), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Reject", color = Color(0xFFFCA5A5), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}