package com.DASS_2024111023_2024117009.ims.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.DASS_2024111023_2024117009.ims.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeApplyLeaveScreen(navController: NavController) {
    var reason by remember { mutableStateOf("") }
    var leaveType by remember { mutableStateOf("Sick Leave") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var attachedFile by remember { mutableStateOf<String?>(null) }

    LazyColumn(modifier = Modifier.fillMaxSize().background(BackgroundDark).padding(20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        item {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextWhite) }
                Spacer(modifier = Modifier.width(8.dp))
                Text("Apply Leave", color = TextWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }

        item {
            Text("Leave Type", color = TextWhite, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = leaveType, onValueChange = { leaveType = it },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) },
                colors = TextFieldDefaults.colors(focusedContainerColor = InputFieldDark, unfocusedContainerColor = InputFieldDark, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Text("Start Date", color = TextWhite, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = startDate, onValueChange = { startDate = it }, placeholder = { Text("mm/dd/yyyy", color = TextGray) },
                trailingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null, tint = TextGray, modifier = Modifier.size(16.dp)) },
                colors = TextFieldDefaults.colors(focusedContainerColor = InputFieldDark, unfocusedContainerColor = InputFieldDark, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, focusedTextColor = TextWhite),
                shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Text("End Date", color = TextWhite, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = endDate, onValueChange = { endDate = it }, placeholder = { Text("mm/dd/yyyy", color = TextGray) },
                trailingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null, tint = TextGray, modifier = Modifier.size(16.dp)) },
                colors = TextFieldDefaults.colors(focusedContainerColor = InputFieldDark, unfocusedContainerColor = InputFieldDark, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, focusedTextColor = TextWhite),
                shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Reason", color = TextWhite, fontSize = 12.sp)
                Text("Optional", color = TextGray, fontSize = 10.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = reason, onValueChange = { reason = it }, placeholder = { Text("Please provide a detailed reason...", color = TextGray) },
                colors = TextFieldDefaults.colors(focusedContainerColor = InputFieldDark, unfocusedContainerColor = InputFieldDark, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, focusedTextColor = TextWhite),
                shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth().height(100.dp)
            )
        }

        item {
            Text("Attachments", color = TextWhite, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))

            // Upload Box
            Surface(
                shape = RoundedCornerShape(12.dp), color = InputFieldDark, modifier = Modifier.fillMaxWidth().height(120.dp),
                onClick = { attachedFile = "document_${System.currentTimeMillis()}.pdf" }
            ) {
                Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Surface(shape = CircleShape, color = CardDark, modifier = Modifier.size(40.dp)) {
                        Icon(Icons.Default.AttachFile, contentDescription = null, tint = TealAccent, modifier = Modifier.padding(10.dp))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Upload documents", color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("PDF, JPG or PNG (Max. 5MB)", color = TextGray, fontSize = 10.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Uploaded File
            if (attachedFile != null) {
                Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFF1E2126), modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(12.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFF2D333B), modifier = Modifier.size(32.dp)) {
                                Icon(Icons.Default.Description, contentDescription = null, tint = TextGray, modifier = Modifier.padding(6.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(attachedFile ?: "", color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text("1.2 MB", color = TextGray, fontSize = 10.sp)
                            }
                        }
                        IconButton(onClick = { attachedFile = null }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Remove", tint = TextGray)
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { 
                    val newLeave = com.DASS_2024111023_2024117009.ims.Leave(
                        id = "L${System.currentTimeMillis()}",
                        empId = com.DASS_2024111023_2024117009.ims.MockDatabase.currentUser?.id ?: "unknown",
                        type = leaveType, 
                        start = if (startDate.isEmpty()) "Pending" else startDate, 
                        end = if (endDate.isEmpty()) "Pending" else endDate,
                        reason = reason,
                        attachment = attachedFile
                    )
                    com.DASS_2024111023_2024117009.ims.MockDatabase.addLeave(newLeave)
                    navController.popBackStack() // Auto-returns to history, which will live-update!
                },
                colors = ButtonDefaults.buttonColors(containerColor = TealAccent), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Submit Request", color = Color.Black, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Your request will be forwarded to HR for first-level approval.",
                color = TextGray, fontSize = 10.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}