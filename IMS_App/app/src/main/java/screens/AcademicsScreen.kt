package com.DASS_2024111023_2024117009.ims.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.DASS_2024111023_2024117009.ims.ui.theme.*

@Composable
fun AcademicsScreen() {
    LazyColumn(modifier = Modifier.fillMaxSize().background(BackgroundDark).padding(20.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
        item { Text("Academics", color = TealAccent, fontSize = 24.sp, fontWeight = FontWeight.Bold) }

        item {
            Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("CUMULATIVE GPA", color = TextGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text("8.84", color = TextWhite, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                        Text(" / 10.0", color = TextGray, fontSize = 14.sp, modifier = Modifier.padding(bottom = 6.dp))
                    }
                    Text("↗ +0.12 from last semester", color = TealAccent, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column { Text("TOTAL CREDITS", color = TextGray, fontSize = 10.sp, fontWeight = FontWeight.Bold); Text("112", color = TextWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold) }
                        Column {
                            Text("BATCH RANK", color = TextGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text("14", color = TextWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                Text("/320", color = TextGray, fontSize = 12.sp, modifier = Modifier.padding(bottom = 2.dp))
                            }
                        }
                    }
                }
            }
        }

        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Current Courses", color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Surface(color = Color(0xFF1E88E5), shape = RoundedCornerShape(8.dp)) { Text("FALL 2024", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)) }
            }
            Spacer(modifier = Modifier.height(12.dp))
            CourseCard("CS301", "Advanced Data Structures", "4.0")
            Spacer(modifier = Modifier.height(12.dp))
            CourseCard("MATH204", "Discrete Mathematics II", "3.0")
        }

        // NEW: Examinations Coming Soon Section
        item {
            Text("Examinations", color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                // Background Faded Content
                Column(modifier = Modifier.alpha(0.3f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.fillMaxWidth().height(70.dp)) {}
                    Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.fillMaxWidth().height(70.dp)) {}
                }
                // Foreground Badge
                Surface(color = Color(0xFF2A2D35), shape = RoundedCornerShape(24.dp), shadowElevation = 4.dp) {
                    Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.HourglassEmpty, contentDescription = null, tint = TextGray, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("COMING SOON", color = TextGray, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun CourseCard(code: String, title: String, credits: String) {
    Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(code, color = TealAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Surface(shape = RoundedCornerShape(12.dp), color = BackgroundDark) { Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextGray, modifier = Modifier.size(20.dp)) }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(title, color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                Column { Text("CREDITS", color = TextGray, fontSize = 8.sp); Text(credits, color = TextWhite, fontSize = 14.sp) }
                Text("View Details", color = TealAccent, fontSize = 12.sp)
            }
        }
    }
}