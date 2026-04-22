package com.DASS_2024111023_2024117009.ims.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.*
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
import com.DASS_2024111023_2024117009.ims.MockDatabase
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(navController: NavController) {
    val totalStudents = MockDatabase.users.collectAsState().value.count { it.role == "Student" }
    val totalEmployees = MockDatabase.users.collectAsState().value.count { it.role == "Employee" }
    val pendingApprovals = MockDatabase.leaves.collectAsState().value.count { it.adminStatus == "Pending" && it.hrStatus == "Approved" }
    val latestNews = MockDatabase.allNews.collectAsState().value.sortedByDescending { it.order }.take(3)

    LazyColumn(modifier = Modifier.fillMaxSize().background(BackgroundDark).padding(20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    val greeting = remember { listOf("Good morning,", "Good afternoon,", "Good evening,", "Welcome back,", "Hello,").random() }
                    Text(greeting, color = TextWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(MockDatabase.currentUser?.name ?: "Admin", color = TealAccent, fontSize = 16.sp)
                }
                Row {
                    IconButton(onClick = { navController.navigate("admin_settings") }) { Icon(Icons.Default.Settings, contentDescription = "Settings", tint = TextGray) }
                    IconButton(onClick = { navController.navigate("messages") }) { Icon(Icons.AutoMirrored.Filled.Message, contentDescription = "Messages", tint = TextGray) }
                }
            }
        }
        item {
            TextField(
                value = "", 
                onValueChange = {}, 
                enabled = false,
                placeholder = { Text("Search news, records...", color = TextGray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextGray) },
                colors = TextFieldDefaults.colors(
                    disabledContainerColor = CardDark,
                    disabledIndicatorColor = Color.Transparent,
                    disabledTextColor = TextWhite
                ),
                shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()
                    .clickable { navController.navigate("global_search") }
            )
        }
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Latest News", color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                // FIXED: News routing applied here!
                Text("View All", color = TealAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { navController.navigate("news_list") })
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                latestNews.forEach { news ->
                    item {
                        NewsCardStub(news.author, news.time, news.title, news.content, Color(0xFF1976D2), onClick = { navController.navigate("news_detail/${news.id}") })
                    }
                }
            }
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("$pendingApprovals", color = TextWhite, fontSize = 48.sp, fontWeight = FontWeight.ExtraBold)
                    Text("PENDING APPROVALS", color = TealAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                }
            }
        }
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.weight(1f).height(100.dp)) {
                    Column(modifier = Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.Center) {
                        Text("$totalStudents", color = TextWhite, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                        Text("TOTAL STUDENTS", color = TealAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.weight(1f).height(100.dp)) {
                    Column(modifier = Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.Center) {
                        Text("$totalEmployees", color = TextWhite, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                        Text("TOTAL EMPLOYEES", color = TealAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        item {
            Text("Quick Actions", color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.fillMaxWidth()) {
                Column {
                    AdminQuickActionStub(Icons.Default.PersonAdd, "Start Student Admission", "Stubbed") { navController.navigate("admin_student_admission") }
                    HorizontalDivider(color = BackgroundDark, thickness = 2.dp)

                    AdminQuickActionStub(Icons.Default.EventAvailable, "Approve Leave Requests", "10 pending") { navController.navigate("admin_leave_approvals") }
                    HorizontalDivider(color = BackgroundDark, thickness = 2.dp)

                    // FIXED: Removed "Approve Employee Admission" completely!
                    AdminQuickActionStub(Icons.Default.BarChart, "Manage Finance", "Stub") { navController.navigate("admin_finance") }
                }
            }
        }
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2126)),
                shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth().clickable { navController.navigate("admin_customize_admission") }
            ) {
                Row(modifier = Modifier.padding(20.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("CUSTOMIZE", color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
                        Text("EMPLOYEE ADMISSION FORM", color = TealAccent, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextGray)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2126)),
                shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth().clickable { navController.navigate("admin_customize_student_admission") }
            ) {
                Row(modifier = Modifier.padding(20.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("CUSTOMIZE", color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
                        Text("STUDENT ADMISSION FORM", color = TealAccent, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextGray)
                }
            }
        }
        item {
            Text("Student Records", color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(12.dp))
            StudentRecordCardStub("Rajesh", "UG2024")
            Spacer(modifier = Modifier.height(16.dp))
            StudentRecordCardStub("Raju", "PG2025")
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun AdminQuickActionStub(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Row(modifier = Modifier.padding(16.dp).fillMaxWidth().clickable(onClick = onClick), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = TextGray, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, color = TealAccent, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(subtitle, color = TextGray, fontSize = 10.sp)
            }
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TealAccent, modifier = Modifier.size(16.dp))
    }
}

@Composable
fun StudentRecordCardStub(name: String, batch: String) {
    Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = CircleShape, color = Color(0xFF374151), modifier = Modifier.size(40.dp)) {
                    Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.Person, contentDescription = null, tint = TextWhite) }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(name, color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(batch, color = TextGray, fontSize = 12.sp)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(shape = RoundedCornerShape(8.dp), color = BackgroundDark) { Text("Batch Transfer", color = TealAccent, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) }
                Surface(shape = RoundedCornerShape(8.dp), color = BackgroundDark) { Text("Change Category", color = TealAccent, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Surface(shape = RoundedCornerShape(8.dp), color = BackgroundDark) { Text("Graduation facility", color = TextWhite, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) }
        }
    }
}