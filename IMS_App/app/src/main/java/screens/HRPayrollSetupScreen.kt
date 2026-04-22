package com.DASS_2024111023_2024117009.ims.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
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
import com.DASS_2024111023_2024117009.ims.FormField

@Composable
fun HRPayrollSetupScreen(navController: NavController) {
    val currentTemplate by MockDatabase.payslipTemplate.collectAsState()
    var editableFields by remember { mutableStateOf(currentTemplate) }

    LazyColumn(modifier = Modifier.fillMaxSize().background(BackgroundDark).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TealAccent) }
                    Column {
                        Text("Payroll Form", color = TealAccent, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Text("Setup", color = TealAccent, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Form Fields", color = TextWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        items(editableFields.size) { index ->
            val field = editableFields[index]
            DraggableFieldCard(
                field = field,
                onFieldChange = { updatedField ->
                    editableFields = editableFields.toMutableList().apply { set(index, updatedField) }
                },
                onDelete = {
                    editableFields = editableFields.toMutableList().apply { removeAt(index) }
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = { 
                    editableFields = editableFields + FormField(
                        id = "p${System.currentTimeMillis()}",
                        label = "New Field",
                        type = "Text",
                        isRequired = false
                    )
                },
                border = BorderStroke(1.dp, CardDark),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = TealAccent, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Field", color = TealAccent, fontWeight = FontWeight.Bold)
            }
        }
        item {
            Button(
                onClick = { MockDatabase.payslipTemplate.value = editableFields; navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = TealAccent), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Icon(Icons.Default.Save, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save Template", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DraggableFieldCard(
    field: FormField,
    onFieldChange: (FormField) -> Unit,
    onDelete: () -> Unit
) {
    Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp)) {
            Icon(Icons.Default.DragIndicator, contentDescription = null, tint = TextGray, modifier = Modifier.padding(top = 12.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                TextField(
                    value = field.label, onValueChange = { onFieldChange(field.copy(label = it)) },
                    placeholder = { Text("Field Label", color = Color.DarkGray) },
                    colors = TextFieldDefaults.colors(focusedContainerColor = InputFieldDark, unfocusedContainerColor = InputFieldDark, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                    shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth(),
                    enabled = !field.isImmutable // Prevent renaming immutable fields
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = field.type, onValueChange = { onFieldChange(field.copy(type = it)) },
                    placeholder = { Text("Field Type", color = Color.DarkGray) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) },
                    colors = TextFieldDefaults.colors(focusedContainerColor = InputFieldDark, unfocusedContainerColor = InputFieldDark, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                    shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth(),
                    enabled = !field.isImmutable // Prevent redefining immutable fields
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = field.isRequired, 
                            onCheckedChange = { onFieldChange(field.copy(isRequired = it)) }, 
                            colors = CheckboxDefaults.colors(checkedColor = TealAccent, uncheckedColor = TextGray), 
                            modifier = Modifier.size(24.dp),
                            enabled = !field.isImmutable
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Required", color = TextGray, fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(16.dp))
                        Icon(if (field.isImmutable) Icons.Default.Lock else Icons.Default.Edit, contentDescription = null, tint = TextGray, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (field.isImmutable) "Immutable" else "Mutable", color = TextGray, fontSize = 12.sp)
                    }
                    if (!field.isImmutable) {
                        IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Cancel, contentDescription = "Delete", tint = Color.Red)
                        }
                    }
                }
            }
        }
    }
}