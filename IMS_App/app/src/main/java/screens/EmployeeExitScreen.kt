package com.DASS_2024111023_2024117009.ims.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.WarningAmber
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

@Composable
fun EmployeeExitScreen(navController: NavController) {
    var confirmLeave by remember { mutableStateOf(false) }
    var confirmIrreversible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(BackgroundDark).padding(20.dp)) {
        // Top Bar
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextGray)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Exit Process", color = TextWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Warning Icon
        Surface(shape = CircleShape, color = Color(0xFF3F2323), modifier = Modifier.size(48.dp)) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.WarningAmber, contentDescription = null, tint = Color(0xFFFCA5A5))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Irreversible Action", color = TextWhite, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "This action will permanently terminate your association with the institute. You will lose access to all institutional resources, payroll history, and records immediately. This cannot be undone.",
            color = TextGray, fontSize = 14.sp, lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Checkbox 1
        Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = confirmLeave, onCheckedChange = { confirmLeave = it },
                    colors = CheckboxDefaults.colors(uncheckedColor = TextGray, checkedColor = Color(0xFFEF4444))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("I confirm I want to leave the institute", color = TextWhite, fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Checkbox 2
        Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = confirmIrreversible, onCheckedChange = { confirmIrreversible = it },
                    colors = CheckboxDefaults.colors(uncheckedColor = TextGray, checkedColor = Color(0xFFEF4444))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("This is final, I understand I will be removed permanently and this action is irreversible", color = TextWhite, fontSize = 14.sp, lineHeight = 20.sp)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Exit Button
        Button(
            onClick = { 
                val uid = com.DASS_2024111023_2024117009.ims.MockDatabase.currentUser?.id
                if (uid != null) com.DASS_2024111023_2024117009.ims.MockDatabase.deleteUser(uid)
                com.DASS_2024111023_2024117009.ims.MockDatabase.currentUser = null
                navController.navigate("login") { popUpTo(0) } // Logs out permanently
            },
            enabled = confirmLeave && confirmIrreversible, // Only enables if both boxes are checked!
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE53935), // Deep red color
                disabledContainerColor = Color(0xFFE53935).copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Final Exit", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}