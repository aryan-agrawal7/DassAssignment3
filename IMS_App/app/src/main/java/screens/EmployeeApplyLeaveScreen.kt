package com.DASS_2024111023_2024117009.ims.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import android.provider.OpenableColumns
import android.content.Context
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current
    var reason by remember { mutableStateOf("") }
    var leaveType by remember { mutableStateOf("Sick Leave") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var attachedUri by remember { mutableStateOf<Uri?>(null) }
    var attachedFileName by remember { mutableStateOf<String?>(null) }

    var expanded by remember { mutableStateOf(false) }
    val leaveTypes = listOf("Sick Leave", "Casual Leave", "Annual Leave", "Maternity/Paternity Leave")

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        attachedUri = uri
        uri?.let {
            attachedFileName = getFileName(context, it)
        }
    }

    val metrics = com.DASS_2024111023_2024117009.ims.MockDatabase.getLeaveMetrics(com.DASS_2024111023_2024117009.ims.MockDatabase.currentUser?.id ?: "")

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
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = leaveType, 
                    onValueChange = {}, 
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = InputFieldDark, 
                        unfocusedContainerColor = InputFieldDark, 
                        focusedIndicatorColor = Color.Transparent, 
                        unfocusedIndicatorColor = Color.Transparent, 
                        focusedTextColor = TextWhite, 
                        unfocusedTextColor = TextWhite
                    ),
                    shape = RoundedCornerShape(8.dp), 
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(InputFieldDark)
                ) {
                    leaveTypes.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type, color = TextWhite) },
                            onClick = {
                                leaveType = type
                                expanded = false
                            },
                            colors = MenuDefaults.itemColors()
                        )
                    }
                }
            }
        }

        item {
            Text("Start Date", color = TextWhite, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = startDate, onValueChange = { startDate = it }, placeholder = { Text("dd/mm/yyyy", color = TextGray) },
                trailingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null, tint = TextGray, modifier = Modifier.size(16.dp)) },
                colors = TextFieldDefaults.colors(focusedContainerColor = InputFieldDark, unfocusedContainerColor = InputFieldDark, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, focusedTextColor = TextWhite),
                shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Text("End Date", color = TextWhite, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = endDate, onValueChange = { endDate = it }, placeholder = { Text("dd/mm/yyyy", color = TextGray) },
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
                onClick = { filePickerLauncher.launch("*/*") }
            ) {
                Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Surface(shape = CircleShape, color = CardDark, modifier = Modifier.size(40.dp)) {
                        Icon(Icons.Default.AttachFile, contentDescription = null, tint = TealAccent, modifier = Modifier.padding(10.dp))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Upload documents", color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Any file type (Max. 5MB)", color = TextGray, fontSize = 10.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Uploaded File
            if (attachedFileName != null) {
                Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFF1E2126), modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.padding(12.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFF2D333B), modifier = Modifier.size(32.dp)) {
                                Icon(Icons.Default.Description, contentDescription = null, tint = TextGray, modifier = Modifier.padding(6.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(attachedFileName ?: "", color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text("Attached", color = TextGray, fontSize = 10.sp)
                            }
                        }
                        IconButton(onClick = { attachedUri = null; attachedFileName = null }, modifier = Modifier.size(24.dp)) {
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
                    if (startDate.isEmpty() || endDate.isEmpty()) {
                        Toast.makeText(context, "Please enter start and end dates", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    
                    if (metrics.balance <= 0) {
                        Toast.makeText(context, "Leave request limit reached (${metrics.total} total allowed)", Toast.LENGTH_LONG).show()
                        return@Button
                    }

                    val newLeave = com.DASS_2024111023_2024117009.ims.Leave(
                        id = "L${System.currentTimeMillis()}",
                        empId = com.DASS_2024111023_2024117009.ims.MockDatabase.currentUser?.id ?: "unknown",
                        type = leaveType, 
                        start = startDate, 
                        end = endDate,
                        reason = reason,
                        attachment = attachedUri?.toString(),
                        attachmentName = attachedFileName
                    )
                    com.DASS_2024111023_2024117009.ims.MockDatabase.addLeave(newLeave)
                    navController.popBackStack()
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

private fun getFileName(context: Context, uri: Uri): String? {
    var result: String? = null
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index != -1) result = it.getString(index)
            }
        }
    }
    if (result == null) {
        result = uri.path
        val cut = result?.lastIndexOf('/') ?: -1
        if (cut != -1) {
            result = result?.substring(cut + 1)
        }
    }
    return result
}