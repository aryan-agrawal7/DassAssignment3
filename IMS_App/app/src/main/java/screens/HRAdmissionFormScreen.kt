package com.DASS_2024111023_2024117009.ims.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.WorkOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.DASS_2024111023_2024117009.ims.ui.theme.*
import com.DASS_2024111023_2024117009.ims.MockDatabase
import com.DASS_2024111023_2024117009.ims.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HRAdmissionFormScreen(navController: NavController) {
    val template by MockDatabase.admissionTemplate.collectAsState()
    
    // State to store form responses (FieldName -> Value)
    var formResponses by remember { mutableStateOf(mapOf<String, String>()) }
    var generatedEmpId by remember { mutableStateOf(nextEmployeeId(MockDatabase.users.value)) }
    var createdEmpId by remember { mutableStateOf<String?>(null) }
    val newEmpPass = remember { "pass123" }
    var submitSuccess by remember { mutableStateOf(false) }

    LazyColumn(modifier = Modifier.fillMaxSize().background(BackgroundDark).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TealAccent) }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("Employee", color = TealAccent, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text("Admission", color = TealAccent, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        
        if (submitSuccess) {
            item {
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF065F46)), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("EMPLOYEE CREATED SUCCESSFULLY", color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Login ID: ${createdEmpId ?: generatedEmpId}", color = TealAccent, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Password: $newEmpPass", color = TealAccent, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { navController.popBackStack() },
                            colors = ButtonDefaults.buttonColors(containerColor = TealAccent),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Done", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            item {
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("GENERATED ID", color = TextGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(generatedEmpId, color = TealAccent, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Auto-assigned upon draft creation", color = TextGray, fontSize = 10.sp)
                    }
                }
            }
            
            item {
                Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.PersonOutline, contentDescription = null, tint = TealAccent, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Custom Admission Form", color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        template.forEach { field ->
                            if (field.type == "Dropdown") {
                                DynamicFormDropdown(
                                    label = field.label + if (field.isRequired) " *" else "",
                                    options = field.options,
                                    selectedValue = formResponses[field.id] ?: "",
                                    onValueChange = { formResponses = formResponses.toMutableMap().apply { put(field.id, it) } }
                                )
                            } else {
                                FormTextField(
                                    label = field.label + if (field.isRequired) " *" else "",
                                    value = formResponses[field.id] ?: "",
                                    placeholderText = "Enter ${field.label}",
                                    onValueChange = { formResponses = formResponses.toMutableMap().apply { put(field.id, it) } }
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        val fullNameField = template.find { it.label.contains("Name", ignoreCase = true) }
                        val deptField = template.find { it.label.contains("Department", ignoreCase = true) }
                        
                        val nameStr = if (fullNameField != null) formResponses[fullNameField.id] ?: "New Employee" else "New Employee"
                        val deptStr = if (deptField != null) formResponses[deptField.id] ?: "General" else "General"
                        val finalEmpId = ensureUniqueEmployeeId(generatedEmpId, MockDatabase.users.value)
                        
                        val newUser = User(
                            id = finalEmpId,
                            pass = newEmpPass,
                            role = "Employee",
                            name = nameStr,
                            department = deptStr
                        )
                        MockDatabase.addUser(newUser)
                        createdEmpId = finalEmpId
                        generatedEmpId = nextEmployeeId(MockDatabase.users.value)
                        submitSuccess = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TealAccent), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Text("Submit", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

private val employeeIdRegex = Regex("^EMP-(\\d+)$")

private fun nextEmployeeId(users: List<User>): String {
    val maxIdNumber = users
        .mapNotNull { employeeIdRegex.matchEntire(it.id)?.groupValues?.get(1)?.toIntOrNull() }
        .maxOrNull() ?: 1000
    return "EMP-${maxIdNumber + 1}"
}

private fun ensureUniqueEmployeeId(candidate: String, users: List<User>): String {
    val existingIds = users.map { it.id.uppercase() }.toSet()
    if (!existingIds.contains(candidate.uppercase())) return candidate

    var number = employeeIdRegex.matchEntire(candidate)?.groupValues?.get(1)?.toIntOrNull() ?: 1000
    var nextCandidate: String
    do {
        number += 1
        nextCandidate = "EMP-$number"
    } while (existingIds.contains(nextCandidate.uppercase()))

    return nextCandidate
}
