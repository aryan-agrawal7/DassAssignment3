package com.DASS_2024111023_2024117009.ims.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.*
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import java.util.Locale
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
import com.DASS_2024111023_2024117009.ims.Payslip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeFillPayslipScreen(navController: NavController) {
    val context = LocalContext.current
    val currentTemplate by MockDatabase.payslipTemplate.collectAsState()
    var month by remember { mutableStateOf("") }
    
    // Map to store values for each dynamic field id
    var fieldValues by remember { mutableStateOf(mapOf<String, String>()) }

    LazyColumn(modifier = Modifier.fillMaxSize().background(BackgroundDark).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextWhite) }
                Spacer(modifier = Modifier.width(8.dp))
                Text("Fill Payroll Form", color = TextWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
        item {
            Text("Monthly Disbursement", color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Review and adjust details for the current payroll cycle.", color = TextGray, fontSize = 14.sp)
        }
        item {
            Text("Month", color = TextWhite, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = month, onValueChange = { month = it },
                placeholder = { Text("Select Month", color = Color.DarkGray) },
                colors = TextFieldDefaults.colors(focusedContainerColor = InputFieldDark, unfocusedContainerColor = InputFieldDark, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()
            )
        }

        items(currentTemplate.size) { index ->
            val field = currentTemplate[index]
            val value = fieldValues[field.id] ?: ""

            Column {
                Text(field.label + if (field.isRequired) " *" else "", color = TextWhite, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = value, onValueChange = { fieldValues = fieldValues + (field.id to it) },
                    placeholder = { Text(if (field.type == "Number") "$ 0.00" else "Enter ${field.label}", color = Color.DarkGray) },
                    trailingIcon = if (field.isImmutable) { { Icon(Icons.Default.Lock, contentDescription = null, tint = TextGray, modifier = Modifier.size(16.dp)) } } else null,
                    colors = TextFieldDefaults.colors(focusedContainerColor = InputFieldDark, unfocusedContainerColor = InputFieldDark, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                    shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth().height(if (field.type == "Long Text") 100.dp else 56.dp)
                )
                if (field.isImmutable) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Immutable baseline as per contract.", color = TextGray, fontSize = 10.sp)
                }
            }
        }

        val basicVal = fieldValues["p1"]?.toDoubleOrNull() ?: 0.0
        val allowVal = fieldValues["p2"]?.toDoubleOrNull() ?: 0.0
        val deductVal = fieldValues["p3"]?.toDoubleOrNull() ?: 0.0
        val netVal = basicVal + allowVal - deductVal

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                border = BorderStroke(1.dp, TealAccent.copy(alpha = 0.3f)), modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(20.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("Net Salary Preview", color = TealAccent, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("$${String.format(Locale.getDefault(), "%.2f", netVal)}", color = TextWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                    Icon(Icons.Default.Wallet, contentDescription = null, tint = TealAccent, modifier = Modifier.size(24.dp))
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { 
                    // VALIDATION
                    if (month.isEmpty()) {
                        Toast.makeText(context, "Please select/enter the month", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    
                    val missingFields = currentTemplate.filter { it.isRequired && (fieldValues[it.id]?.isBlank() ?: true) }
                    if (missingFields.isNotEmpty()) {
                        Toast.makeText(context, "Please fill required fields: ${missingFields.joinToString { it.label }}", Toast.LENGTH_LONG).show()
                        return@Button
                    }

                    val newPayslip = Payslip(
                        id = java.util.UUID.randomUUID().toString(),
                        empId = MockDatabase.currentUser?.id ?: "unknown",
                        month = month,
                        basic = fieldValues["p1"] ?: "0",
                        allowances = fieldValues["p2"] ?: "0",
                        deductions = fieldValues["p3"] ?: "0",
                        net = String.format(Locale.getDefault(), "%.2f", netVal),
                        status = "Pending"
                    )
                    MockDatabase.addPayslip(newPayslip)
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(containerColor = TealAccent), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().height(50.dp)
            ) { Text("Submit for Approval", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp) }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}