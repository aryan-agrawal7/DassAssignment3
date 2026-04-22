package com.DASS_2024111023_2024117009.ims.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.DASS_2024111023_2024117009.ims.ui.theme.*
import com.DASS_2024111023_2024117009.ims.MockDatabase
import com.DASS_2024111023_2024117009.ims.FormField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminEmployeeAdmissionCustomizeScreen(navController: NavController) {
    val currentTemplate by MockDatabase.admissionTemplate.collectAsState()
    var editableFields by remember { mutableStateOf(currentTemplate) }

    Scaffold(
        containerColor = BackgroundDark,
        // FIXED: Header is now sticky at the top
        topBar = {
            Surface(color = BackgroundDark, modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TealAccent)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Employee Admission Form Customize", color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Text("Build Form Structure", color = TextWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            items(editableFields.size) { index ->
                val field = editableFields[index]
                if (field.type == "Dropdown") {
                    BuilderDropdownCard(
                        field = field,
                        onFieldChange = { updatedField ->
                            editableFields = editableFields.toMutableList().apply { set(index, updatedField) }
                        },
                        onDelete = {
                            editableFields = editableFields.toMutableList().apply { removeAt(index) }
                        }
                    )
                } else {
                    BuilderFieldCard(
                        field = field,
                        onFieldChange = { updatedField ->
                            editableFields = editableFields.toMutableList().apply { set(index, updatedField) }
                        },
                        onDelete = {
                            editableFields = editableFields.toMutableList().apply { removeAt(index) }
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Canvas(modifier = Modifier.fillMaxWidth().height(1.dp)) {
                    drawLine(
                        color = Color.DarkGray, start = Offset(0f, 0f), end = Offset(size.width, 0f),
                        strokeWidth = 2f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )
                }

                Surface(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp).clickable {
                        editableFields = editableFields + FormField(
                            id = "f${System.currentTimeMillis()}",
                            label = "New Field",
                            type = "Short Text",
                            isRequired = false
                        )
                    },
                    color = Color.Transparent, shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = TextGray)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Add New Field", color = TextGray, fontSize = 14.sp)
                    }
                }

                Canvas(modifier = Modifier.fillMaxWidth().height(1.dp)) {
                    drawLine(
                        color = Color.DarkGray, start = Offset(0f, 0f), end = Offset(size.width, 0f),
                        strokeWidth = 2f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        MockDatabase.admissionTemplate.value = editableFields
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TealAccent),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(52.dp)
                ) {
                    Text("Save & Publish", color = Color.Black, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
