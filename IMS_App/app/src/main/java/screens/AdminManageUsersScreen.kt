package com.DASS_2024111023_2024117009.ims.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.DASS_2024111023_2024117009.ims.ui.theme.*

@Composable
fun AdminManageUsersScreen(navController: NavController) {
    LazyColumn(modifier = Modifier.fillMaxSize().background(BackgroundDark).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Manage Users", color = TealAccent, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Start)
            Spacer(modifier = Modifier.height(60.dp))

            Surface(modifier = Modifier.size(120.dp), shape = CircleShape, color = Color(0xFF042F2E).copy(alpha = 0.5f)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Build, contentDescription = null, tint = TealAccent, modifier = Modifier.size(48.dp))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            Text("Coming Soon", color = TextWhite, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Advanced user-role management, granular privilege controls, and institutional hierarchy management are coming in a future update.",
                color = TextGray, fontSize = 12.sp, textAlign = TextAlign.Center, lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(40.dp))
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Planned Roles", color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFF042F2E)) {
                    Text("PREVIEW", color = TealAccent, fontSize = 8.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item { PlannedRoleCard(Icons.Default.Security, "Administrator", "Full system control, configuration, and security.", TealAccent) }
        item { Spacer(modifier = Modifier.height(12.dp)) }
        item { PlannedRoleCard(Icons.Default.Group, "Human Resources", "Employee lifecycle, payroll, and leave management.", Color(0xFF818CF8)) }
        item { Spacer(modifier = Modifier.height(12.dp)) }
        item { PlannedRoleCard(Icons.Default.Person, "Employee", "Personal records, academics, and payroll access.", Color(0xFFFDBA74)) }
        item { Spacer(modifier = Modifier.height(12.dp)) }
        item { PlannedRoleCard(Icons.Default.School, "Student", "Academic progress, schedule, and profile management.", TealAccent) }
    }
}

@Composable
fun PlannedRoleCard(icon: ImageVector, title: String, desc: String, iconColor: Color) {
    Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = CircleShape, color = BackgroundDark, modifier = Modifier.size(40.dp)) {
                Box(contentAlignment = Alignment.Center) { Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp)) }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(title, color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Surface(shape = RoundedCornerShape(4.dp), color = BackgroundDark) {
                        Text("SOON", color = TextGray, fontSize = 8.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(desc, color = TextGray, fontSize = 10.sp, lineHeight = 14.sp)
            }
        }
    }
}