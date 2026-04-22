package com.DASS_2024111023_2024117009.ims.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.DASS_2024111023_2024117009.ims.ui.theme.*

@Composable
fun MessagesScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().background(BackgroundDark).padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TealAccent)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Messages", color = TealAccent, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(40.dp))

        Box {
            Surface(modifier = Modifier.size(100.dp), shape = CircleShape, color = Color(0xFF042F2E).copy(alpha = 0.5f)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.ChatBubbleOutline, contentDescription = null, tint = TealAccent, modifier = Modifier.size(40.dp))
                }
            }
            Surface(modifier = Modifier.size(16.dp).align(Alignment.TopEnd).offset(x = (-8).dp, y = 8.dp), shape = CircleShape, color = TealAccent) {}
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Coming Soon", color = TextWhite, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "We're building a seamless way for you to stay connected with students and staff. Secure messaging is coming to IMS soon.",
            color = TextGray, fontSize = 12.sp, textAlign = TextAlign.Center, lineHeight = 20.sp, modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Faded Mock Stubs
        Column(modifier = Modifier.alpha(0.3f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            MessageStub("Registrar Office", "10:42 AM", "Please review the updated semester schedule.", Icons.Default.Business)
            MessageStub("Prof. Khanna", "Yesterday", "The meeting has been rescheduled to Thursday at 3 PM.", Icons.Default.Person)
            MessageStub("CS301 Study Group", "Mon", "Anyone have the notes from the last lecture on algorithms?", Icons.Default.Group)
        }
    }
}

@Composable
fun MessageStub(name: String, time: String, message: String, icon: ImageVector) {
    Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = CircleShape, color = BackgroundDark, modifier = Modifier.size(40.dp)) {
                Box(contentAlignment = Alignment.Center) { Icon(icon, contentDescription = null, tint = TextGray, modifier = Modifier.size(20.dp)) }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(name, color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text(time, color = TextGray, fontSize = 10.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(message, color = TextGray, fontSize = 12.sp, maxLines = 2, lineHeight = 16.sp)
            }
        }
    }
}