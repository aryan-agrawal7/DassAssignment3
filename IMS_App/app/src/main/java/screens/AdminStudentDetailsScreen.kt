package com.DASS_2024111023_2024117009.ims.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.DASS_2024111023_2024117009.ims.ui.theme.*

@Composable
fun AdminStudentDetailsScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize().background(BackgroundDark).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(16.dp))
        Text("Student Details", color = TealAccent, fontSize = 16.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(60.dp))

        Box(contentAlignment = Alignment.Center) {
            Surface(modifier = Modifier.size(140.dp), shape = RoundedCornerShape(32.dp), color = CardDark) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Badge, contentDescription = null, tint = TextGray, modifier = Modifier.size(60.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Surface(shape = RoundedCornerShape(16.dp), color = Color(0xFF042F2E)) {
            Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = CircleShape, color = TealAccent, modifier = Modifier.size(6.dp)) {}
                Spacer(modifier = Modifier.width(6.dp))
                Text("COMING SOON", color = TealAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Student Details Module", color = TextWhite, fontSize = 22.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            "This feature is currently under development. Stay tuned for a comprehensive student management experience.",
            color = TextGray, fontSize = 12.sp, textAlign = TextAlign.Center, lineHeight = 20.sp, modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Skeleton Preview
        Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.fillMaxWidth().height(80.dp)) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text("PREVIEW", color = TextGray.copy(alpha = 0.5f), fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.TopEnd).padding(12.dp))
                Row(modifier = Modifier.padding(16.dp).fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                    Surface(shape = CircleShape, color = BackgroundDark, modifier = Modifier.size(40.dp)) {}
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(shape = RoundedCornerShape(4.dp), color = BackgroundDark, modifier = Modifier.height(12.dp).width(120.dp)) {}
                        Surface(shape = RoundedCornerShape(4.dp), color = BackgroundDark, modifier = Modifier.height(8.dp).width(80.dp)) {}
                    }
                }
            }
        }
    }
}