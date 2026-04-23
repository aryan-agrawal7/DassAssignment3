package com.DASS_2024111023_2024117009.ims.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.foundation.clickable
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import androidx.compose.ui.platform.LocalContext

@Composable
fun AdminLeaveApprovalScreen(navController: NavController) {
    val leaves by MockDatabase.leaves.collectAsState()
    val users by MockDatabase.users.collectAsState()
    val context = LocalContext.current
    var filterStatus by remember { mutableStateOf("Pending") }

    val filteredLeaves = leaves.filter { it.hrStatus == "Approved" && it.adminStatus == filterStatus }

    LazyColumn(modifier = Modifier.fillMaxSize().background(BackgroundDark).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TealAccent) }
                Spacer(modifier = Modifier.width(8.dp))
                Text("Final Leave Approvals", color = TextWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Pending", "Approved", "Rejected").forEach { status ->
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (filterStatus == status) TealAccent else InputFieldDark,
                        modifier = Modifier.clickable { filterStatus = status }
                    ) {
                        Text(status, color = if (filterStatus == status) Color.Black else TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                    }
                }
            }
        }
        
        filteredLeaves.forEach { leave ->
            val emp = users.find { it.id == leave.empId }
            val empMetrics = MockDatabase.getLeaveMetrics(leave.empId)
            item {
                AdminLeaveCardStub(
                    initials = emp?.name?.take(2)?.uppercase() ?: "??", 
                    name = emp?.name ?: "Unknown", 
                    role = emp?.role ?: "Employee",
                    leaveType = leave.type, 
                    leaveIcon = Icons.Default.LocalHospital,
                    duration = "${leave.start} - ${leave.end}",
                    note = leave.reason,
                    usedRequests = empMetrics.approved, 
                    totalRequests = empMetrics.total, 
                    attachmentName = leave.attachmentName,
                    onOpenAttachment = {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = leave.attachment?.toUri()
                                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                            }
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            android.widget.Toast.makeText(context, "Cannot open file: ${e.localizedMessage}", android.widget.Toast.LENGTH_SHORT).show()
                        }
                    },
                    avatarColor = Color(0xFF1E3A8A),
                    showActions = filterStatus == "Pending",
                    onApprove = { MockDatabase.updateLeaveStatusAdmin(leave.id, "Approved") },
                    onReject = { MockDatabase.updateLeaveStatusAdmin(leave.id, "Rejected") }
                )
            }
        }
    }
}

@Composable
fun AdminLeaveCardStub(
    initials: String, name: String, role: String, leaveType: String, leaveIcon: ImageVector,
    duration: String, note: String, usedRequests: Int, totalRequests: Int, 
    attachmentName: String? = null, onOpenAttachment: () -> Unit = {}, avatarColor: Color,
    showActions: Boolean = true, onApprove: () -> Unit = {}, onReject: () -> Unit = {}
) {
    Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Surface(shape = CircleShape, color = if (initials.isNotEmpty()) avatarColor else Color.Transparent, modifier = Modifier.size(40.dp)) {
                        Box(contentAlignment = Alignment.Center) {
                            if (initials.isNotEmpty()) {
                                Text(initials, color = TextWhite, fontWeight = FontWeight.Bold)
                            } else {
                                Icon(Icons.Default.Person, contentDescription = null, tint = TealAccent, modifier = Modifier.fillMaxSize())
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(name, color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text(role, color = TextGray, fontSize = 10.sp, lineHeight = 14.sp)
                    }
                }
                Surface(shape = RoundedCornerShape(16.dp), color = Color(0xFF042F2E).copy(alpha = 0.5f), border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF064E3B))) {
                    Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = TealAccent, modifier = Modifier.size(10.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("HR Approved", color = TealAccent, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(leaveIcon, contentDescription = null, tint = TextGray, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(leaveType, color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.AutoMirrored.Filled.EventNote, contentDescription = null, tint = TextGray, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(duration, color = TextGray, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(note, color = TextGray, fontSize = 12.sp, lineHeight = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Past Requests", color = TextGray, fontSize = 10.sp)
                Text("$usedRequests/$totalRequests used", color = TealAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(4.dp))

            LinearProgressIndicator(
                progress = { if (totalRequests > 0) usedRequests.toFloat() / totalRequests.toFloat() else 0f },
                color = if (usedRequests > totalRequests / 2) Color(0xFFFDBA74) else TealAccent,
                trackColor = BackgroundDark,
                modifier = Modifier.fillMaxWidth().height(4.dp)
            )

            if (attachmentName != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp), 
                    color = Color(0xFF1E2126), 
                    modifier = Modifier.fillMaxWidth().clickable(onClick = onOpenAttachment)
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Description, contentDescription = null, tint = TealAccent, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(attachmentName, color = TealAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (showActions) {
                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                    TextButton(onClick = onReject) { Text("Reject", color = Color(0xFFFCA5A5), fontWeight = FontWeight.Bold) }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = onApprove,
                        colors = ButtonDefaults.buttonColors(containerColor = TealAccent), shape = RoundedCornerShape(12.dp), modifier = Modifier.height(40.dp)
                    ) { Text("Approve", color = Color.Black, fontWeight = FontWeight.Bold) }
                }
            }
        }
    }
}