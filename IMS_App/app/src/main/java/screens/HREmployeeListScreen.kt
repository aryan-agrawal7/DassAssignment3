package com.DASS_2024111023_2024117009.ims.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HREmployeeListScreen(navController: NavController) {
    LazyColumn(modifier = Modifier.fillMaxSize().background(BackgroundDark).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TealAccent) }
                Spacer(modifier = Modifier.width(8.dp))
                Text("Employees", color = TealAccent, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }
        item {
            TextField(
                value = "", onValueChange = {}, placeholder = { Text("Search employees...", color = TextGray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextGray) },
                colors = TextFieldDefaults.colors(focusedContainerColor = CardDark, unfocusedContainerColor = CardDark, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent),
                shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = false, onCheckedChange = {}, colors = CheckboxDefaults.colors(uncheckedColor = TextGray, checkedColor = TealAccent))
                    Text("0 selected", color = TextWhite, fontSize = 14.sp)
                }
                Text("Bulk Actions", color = TealAccent, fontSize = 14.sp)
            }
        }
        // FIXED HERE: Passed navController so clicking routes to detail screen
        item { EmployeeListItem("Sarah Chen", "Senior Product Manager", "Product & Design", true, navController) }
        item { EmployeeListItem("Marcus Johnson", "Director of Engineering", "Engineering", true, navController) }
        item { EmployeeListItem("Elena Rodriguez", "UX Researcher", "Product & Design", false, navController) }
    }
}

@Composable
fun EmployeeListItem(name: String, role: String, dept: String, isActive: Boolean, navController: NavController) {
    var isChecked by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = CardDark),
        modifier = Modifier.fillMaxWidth().clickable { navController.navigate("hr_employee_detail") }
    ) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = isChecked, onCheckedChange = { isChecked = it }, colors = CheckboxDefaults.colors(uncheckedColor = TextGray, checkedColor = TealAccent))

            Surface(shape = CircleShape, color = InputFieldDark, modifier = Modifier.size(48.dp)) {
                Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.Person, contentDescription = null, tint = TextGray) }
            }
            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(name, color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Surface(shape = RoundedCornerShape(8.dp), color = if (isActive) Color(0xFF10B981).copy(alpha = 0.2f) else Color(0xFFEF4444).copy(alpha = 0.2f)) {
                        Text(if (isActive) "ACTIVE" else "ON LEAVE", color = if (isActive) Color(0xFF10B981) else Color(0xFFEF4444), fontSize = 8.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                }
                Text(role, color = TextGray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(dept, color = TextGray, fontSize = 10.sp)
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextGray)
                }
            }
        }
    }
}