package com.DASS_2024111023_2024117009.ims.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Public
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

// Global state to persist settings just for this screen
val generalDropdownState = mutableStateMapOf<String, String>()
val generalSwitchState = mutableStateMapOf<String, Boolean>()

@Composable
fun GeneralSettingsScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    val localDropdownState = remember { mutableStateMapOf<String, String>().apply { putAll(generalDropdownState) } }
    val localSwitchState = remember { mutableStateMapOf<String, Boolean>().apply { putAll(generalSwitchState) } }

    Column(modifier = Modifier.fillMaxSize().background(BackgroundDark).padding(20.dp).verticalScroll(scrollState)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TealAccent)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Settings", color = TealAccent, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("General Settings", color = TextWhite, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))

        Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Public, contentDescription = null, tint = TealAccent, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Localization", color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(16.dp))
                SettingsDropdown("Language", "English (US)", localDropdownState)
                Spacer(modifier = Modifier.height(12.dp))
                SettingsDropdown("Country", "United States", localDropdownState)
                Spacer(modifier = Modifier.height(12.dp))
                SettingsDropdown("Currency", "USD (\$)", localDropdownState)
                Spacer(modifier = Modifier.height(12.dp))
                SettingsDropdown("Time Zone", "Pacific Time (PT)", localDropdownState)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.NotificationsNone, contentDescription = null, tint = TealAccent, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Notifications", color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(16.dp))
                SettingsSwitch("Push Alerts", "Receive urgent notifications directly on your device.", true, localSwitchState)
                Spacer(modifier = Modifier.height(16.dp))
                SettingsSwitch("Email Alerts", "Daily summaries and important system announcements.", true, localSwitchState)
                Spacer(modifier = Modifier.height(16.dp))
                SettingsSwitch("Approval Updates", "Get notified when a request requires your action.", false, localSwitchState)
                Spacer(modifier = Modifier.height(16.dp))
                SettingsSwitch("News Updates", "Institute newsletters and community updates.", false, localSwitchState)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { 
                generalDropdownState.putAll(localDropdownState)
                generalSwitchState.putAll(localSwitchState)
                navController.popBackStack()
            },
            colors = ButtonDefaults.buttonColors(containerColor = TealAccent),
            shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().height(50.dp)
        ) { Text("Save Changes", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp) }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsDropdown(label: String, initialValue: String, stateMap: MutableMap<String, String>) {
    var selectedValue by remember { mutableStateOf(stateMap[label] ?: generalDropdownState[label] ?: initialValue) }
    Column {
        Text(label, color = TextGray, fontSize = 10.sp, modifier = Modifier.padding(start = 4.dp, bottom = 4.dp))
        TextField(
            value = selectedValue, onValueChange = {}, readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) },
            colors = TextFieldDefaults.colors(focusedContainerColor = InputFieldDark, unfocusedContainerColor = InputFieldDark, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, focusedTextColor = TextWhite),
            shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth().height(50.dp)
        )
    }
    stateMap[label] = selectedValue
}

@Composable
private fun SettingsSwitch(title: String, subtitle: String, initialChecked: Boolean, stateMap: MutableMap<String, Boolean>) {
    var checked by remember { mutableStateOf(stateMap[title] ?: generalSwitchState[title] ?: initialChecked) }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f).padding(end = 16.dp)) {
            Text(title, color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(subtitle, color = TextGray, fontSize = 10.sp, lineHeight = 14.sp)
        }
        Switch(
            checked = checked, onCheckedChange = { checked = it; stateMap[title] = it },
            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = TealAccent, uncheckedThumbColor = TextGray, uncheckedTrackColor = InputFieldDark)
        )
    }
}