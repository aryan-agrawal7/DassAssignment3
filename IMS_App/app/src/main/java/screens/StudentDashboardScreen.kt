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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.DASS_2024111023_2024117009.ims.ui.theme.*
import com.DASS_2024111023_2024117009.ims.MockDatabase
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDashboardScreen(navController: NavController) {
    LazyColumn(modifier = Modifier.fillMaxSize().background(BackgroundDark).padding(20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    val greeting = remember { listOf("Good morning,", "Good afternoon,", "Good evening,", "Welcome back,", "Hello,").random() }
                    Text(greeting, color = TextWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(MockDatabase.currentUser?.name ?: "Student", color = TealAccent, fontSize = 16.sp)
                }
                Row {
                    IconButton(onClick = { navController.navigate("settings") }) { Icon(Icons.Default.Settings, contentDescription = "Settings", tint = TextGray) }
                    IconButton(onClick = { navController.navigate("messages") }) { Icon(Icons.AutoMirrored.Filled.Message, contentDescription = "Messages", tint = TextGray) }
                }
            }
        }
        item {
            TextField(
                value = "", onValueChange = {}, placeholder = { Text("Search news, records...", color = TextGray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextGray) },
                enabled = false,
                colors = TextFieldDefaults.colors(disabledContainerColor = CardDark, disabledIndicatorColor = Color.Transparent, disabledTextColor = TextWhite),
                shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().clickable { navController.navigate("global_search") }
            )
        }
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Latest News", color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("View All", color = TealAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { navController.navigate("news_list") })
            }
            Spacer(modifier = Modifier.height(12.dp))
            val latestNews = MockDatabase.allNews.collectAsState().value.sortedByDescending { it.order }.take(3)
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
                    Text("UG2024", color = TextWhite, fontSize = 36.sp, fontWeight = FontWeight.ExtraBold)
                    Text("BATCH", color = TealAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.weight(1f).height(100.dp).clickable { navController.navigate("attendance") }) {
                    Column(modifier = Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.Center) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("92%", color = TextWhite, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TealAccent, modifier = Modifier.size(20.dp))
                        }
                        Text("ATTENDANCE", color = TealAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.weight(1f).height(100.dp)) {
                    Column(modifier = Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.Center) {
                        Text("8.67", color = TextWhite, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                        Text("CGPA", color = TealAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("NEXT CLASS", color = TealAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Adv. Algorithms", color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = TextGray, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Room 402", color = TextGray, fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(16.dp))
                        Icon(Icons.Default.Schedule, contentDescription = null, tint = TextGray, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("10:30 AM", color = TextGray, fontSize = 12.sp)
                    }
                }
            }
        }
        item {
            Text("Active Courses", color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.fillMaxWidth()) {
                Column {
                    ActiveCourseStub("Machine Learning", "CS401 • Prof. Sarah Jenkins", Icons.Default.Psychology, Color(0x333538))
                    HorizontalDivider(color = BackgroundDark, thickness = 2.dp) // FIXED HERE
                    ActiveCourseStub("Database Systems", "CS302 • Prof. David Chen", Icons.Default.Storage, Color(0x333538))
                }
            }
        }
    }
}

@Composable
fun NewsCardStub(author: String, time: String, title: String, snippet: String, iconColor: Color, onClick: () -> Unit = {}) {
    Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.width(280.dp).clickable(onClick = onClick)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = CircleShape, color = iconColor.copy(alpha = 0.2f), modifier = Modifier.size(24.dp)) { Icon(Icons.Default.Campaign, contentDescription = null, tint = iconColor, modifier = Modifier.padding(4.dp)) }
                Spacer(modifier = Modifier.width(8.dp))
                Column { Text(author, color = TextWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold); Text(time, color = TextGray, fontSize = 8.sp) }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(title, color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(snippet, color = TextGray, fontSize = 12.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
fun ActiveCourseStub(title: String, subtitle: String, icon: ImageVector, iconTint: Color) {
    Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Surface(shape = RoundedCornerShape(8.dp), color = iconTint.copy(alpha = 0.1f), modifier = Modifier.size(40.dp)) { Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.padding(8.dp)) }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(subtitle, color = TextGray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Surface(shape = RoundedCornerShape(16.dp), color = BackgroundDark) { Text("Change", color = TextWhite, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) }
        }
    }
}