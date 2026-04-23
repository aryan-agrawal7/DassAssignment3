package com.DASS_2024111023_2024117009.ims.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.DASS_2024111023_2024117009.ims.ui.theme.*

@Composable
fun StudentProfileScreen(onLogout: () -> Unit) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.fillMaxSize().background(BackgroundDark).padding(20.dp).verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text("Profile", color = TealAccent, fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))

        Spacer(modifier = Modifier.height(30.dp))
        Surface(modifier = Modifier.size(100.dp), shape = CircleShape, color = CardDark) {
            Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.Person, contentDescription = "Avatar", tint = TealAccent, modifier = Modifier.size(60.dp)) }
        }

        val user = com.DASS_2024111023_2024117009.ims.MockDatabase.currentUser
        val customData = user?.customData ?: emptyMap()
        val email = customData.entries.find { it.key.contains("Email", true) }?.value ?: "Not provided"
        val phone = customData.entries.find { it.key.contains("Phone", true) }?.value ?: "Not provided"

        Spacer(modifier = Modifier.height(16.dp))
        Text(user?.name ?: "Student Name", color = TextWhite, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("Student", color = TextGray, fontSize = 14.sp)

        Spacer(modifier = Modifier.height(32.dp))

        Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Contact Information", color = TealAccent, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Email, contentDescription = null, tint = TealAccent)
                    Spacer(modifier = Modifier.width(16.dp))
                    Column { Text("EMAIL ADDRESS", color = TextGray, fontSize = 10.sp); Text(email, color = TextWhite, fontSize = 14.sp) }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Phone, contentDescription = null, tint = TealAccent)
                    Spacer(modifier = Modifier.width(16.dp))
                    Column { Text("PHONE NUMBER", color = TextGray, fontSize = 10.sp); Text(phone, color = TextWhite, fontSize = 14.sp) }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text("Course / Batch", color = TextGray, fontSize = 12.sp)
                Text(user?.department ?: "UG 2024", color = TealAccent, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Text("ID / Roll Number", color = TextGray, fontSize = 12.sp)
                Text(user?.id ?: "2024111003", color = TealAccent, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Account Actions", color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(12.dp))

        ActionCardStub("Edit Profile", Icons.Default.Edit)
        Spacer(modifier = Modifier.height(12.dp))
        ActionCardStub("Change Password", Icons.Default.Lock)

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { onLogout() },
            colors = ButtonDefaults.buttonColors(containerColor = TealAccent), shape = RoundedCornerShape(12.dp), modifier = Modifier.width(160.dp).height(48.dp)
        ) {
            Text("Logout", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun ActionCardStub(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = TealAccent)
                Spacer(modifier = Modifier.width(16.dp))
                Text(title, color = TextWhite, fontSize = 16.sp)
            }
            Surface(shape = RoundedCornerShape(8.dp), color = BackgroundDark) {
                Text("Coming soon", color = TextGray, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
            }
        }
    }
}