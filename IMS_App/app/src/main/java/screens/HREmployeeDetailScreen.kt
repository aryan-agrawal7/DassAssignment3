package com.DASS_2024111023_2024117009.ims.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.filled.*
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

@Composable
fun HREmployeeDetailScreen(navController: NavController) {
    LazyColumn(modifier = Modifier.fillMaxSize().background(BackgroundDark).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TealAccent) }
                Text("Employee Profile", color = TextWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(48.dp))
            }
        }
        item {
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(modifier = Modifier.size(100.dp), shape = CircleShape, color = CardDark) {
                    Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.Person, contentDescription = "Avatar", tint = TealAccent, modifier = Modifier.size(60.dp)) }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Alex Mercer", color = TextWhite, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("Senior Software Engineer • Engineering", color = TextGray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Surface(shape = RoundedCornerShape(16.dp), color = Color(0xFF1E3A8A).copy(alpha = 0.3f), border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E3A8A))) {
                    Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Verified, contentDescription = null, tint = Color(0xFF60A5FA), modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("PERMANENT", color = Color(0xFF60A5FA), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        item {
            DetailCard(title = "Employment Details", icon = Icons.Default.Badge) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Employee ID", color = TextGray, fontSize = 10.sp)
                        Text("EMP-2023-049", color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Role", color = TextGray, fontSize = 10.sp)
                        Text("Senior Engineer", color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Department", color = TextGray, fontSize = 10.sp)
                        Text("Engineering", color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Joining Date", color = TextGray, fontSize = 10.sp)
                        Text("12 Aug 2021", color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        item {
            DetailCard(title = "Salary", icon = Icons.Default.Payments) {
                Text("Grade L4", color = TextGray, fontSize = 10.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("$8,450", color = TextWhite, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Text("Net Monthly", color = TextGray, fontSize = 10.sp)
            }
        }
        item {
            DetailCard(title = "Leave Balance", icon = Icons.AutoMirrored.Filled.EventNote) {
                Text("Annual Quota", color = TextGray, fontSize = 10.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text("14", color = TextWhite, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        Text(" days", color = TextGray, fontSize = 14.sp, modifier = Modifier.padding(bottom = 4.dp))
                    }
                    Text("2 Pending", color = TextGray, fontSize = 12.sp)
                }
            }
        }
        item {
            DetailCard(title = "Personal Details", icon = Icons.Default.PersonOutline) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Email, contentDescription = null, tint = TextGray, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column { Text("alex.mercer@company.com", color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold); Text("Primary Email", color = TextGray, fontSize = 10.sp) }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Phone, contentDescription = null, tint = TextGray, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column { Text("+1 (555) 019-2834", color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold); Text("Mobile", color = TextGray, fontSize = 10.sp) }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = TextGray, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column { Text("492 Tech Boulevard, Suite 300\nSan Francisco, CA 94107", color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold); Text("Current Address", color = TextGray, fontSize = 10.sp) }
                }
            }
        }
        item {
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = TealAccent), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Icon(Icons.Default.Edit, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Edit Details", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun DetailCard(title: String, icon: ImageVector, content: @Composable () -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = TealAccent, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(title, color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}