package com.DASS_2024111023_2024117009.ims.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.WarningAmber
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
import androidx.compose.foundation.clickable
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import androidx.compose.ui.platform.LocalContext

@Composable
fun HRLeavesScreen(navController: NavController) {
    val leaves by MockDatabase.leaves.collectAsState()
    val users by MockDatabase.users.collectAsState()
    val context = LocalContext.current
    var filterStatus by remember { mutableStateOf("Pending") }

    val filteredLeaves = leaves.filter { it.hrStatus == filterStatus }

    LazyColumn(modifier = Modifier.fillMaxSize().background(BackgroundDark).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Text("Leave Approvals", color = TealAccent, fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
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
            item {
                LeaveCardStub(
                    initials = emp?.name?.take(2)?.uppercase() ?: "??",
                    name = emp?.name ?: "Unknown",
                    role = emp?.role ?: "Employee",
                    leaveType = leave.type,
                    duration = "${leave.start} - ${leave.end}",
                    history = "Applied on ${leave.appliedDate}",
                    hasWarning = false,
                    note = leave.reason,
                    hasAttachment = leave.attachment != null,
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
                    showActions = leave.hrStatus == "Pending",
                    onApprove = { MockDatabase.updateLeaveStatusHR(leave.id, "Approved") },
                    onReject = { MockDatabase.updateLeaveStatusHR(leave.id, "Rejected") }
                )
            }
        }
    }
}

@Composable
fun LeaveCardStub(
    initials: String, name: String, role: String, leaveType: String, duration: String, history: String,
    hasWarning: Boolean, note: String, hasAttachment: Boolean, attachmentName: String? = null,
    onOpenAttachment: () -> Unit = {},
    showActions: Boolean = true, onApprove: () -> Unit = {}, onReject: () -> Unit = {}
) {
    Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(shape = CircleShape, color = Color(0xFF374151), modifier = Modifier.size(40.dp)) {
                        Box(contentAlignment = Alignment.Center) { Text(initials, color = TextWhite, fontWeight = FontWeight.Bold) }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(name, color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text(role, color = TextGray, fontSize = 12.sp)
                    }
                }
                Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFF1E3A8A).copy(alpha = 0.3f)) {
                    Text(leaveType, color = Color(0xFF60A5FA), fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Surface(shape = RoundedCornerShape(8.dp), color = InputFieldDark, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("DURATION", color = TextGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(duration, color = TextWhite, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("HISTORY", color = TextGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (hasWarning) { Icon(Icons.Default.WarningAmber, contentDescription = null, tint = Color(0xFFFCA5A5), modifier = Modifier.size(14.dp)); Spacer(modifier = Modifier.width(4.dp)) }
                        Text(history, color = if (hasWarning) Color(0xFFFCA5A5) else TextWhite, fontSize = 12.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(note, color = TextGray, fontSize = 12.sp)

            if (hasAttachment) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp), 
                    color = BackgroundDark,
                    modifier = Modifier.clickable(onClick = onOpenAttachment)
                ) {
                    Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Description, contentDescription = null, tint = TealAccent, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(attachmentName ?: "View Attachment", color = TealAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

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