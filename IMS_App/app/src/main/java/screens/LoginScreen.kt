package com.DASS_2024111023_2024117009.ims.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.DASS_2024111023_2024117009.ims.ui.theme.*
import com.DASS_2024111023_2024117009.ims.MockDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    var identifier by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf("Student") }
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().background(BackgroundDark).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(48.dp))
        Icon(Icons.Default.AccountBalance, contentDescription = "Logo", tint = TealAccent, modifier = Modifier.size(48.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text("Institute Management", color = TextWhite, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Welcome back", color = TextGray, fontSize = 14.sp)

        Spacer(modifier = Modifier.height(32.dp))

        Card(colors = CardDefaults.cardColors(containerColor = CardDark), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Email or Username", color = TextGray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = identifier, onValueChange = { identifier = it },
                    placeholder = { Text("Enter your identifier", color = Color.DarkGray) },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = TextGray) },
                    colors = TextFieldDefaults.colors(focusedContainerColor = InputFieldDark, unfocusedContainerColor = InputFieldDark, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                    shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Password", color = TextGray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = password, onValueChange = { password = it },
                    placeholder = { Text("Enter password", color = Color.DarkGray) },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = TextGray) },
                    trailingIcon = { IconButton(onClick = { passwordVisible = !passwordVisible }) { Icon(if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = null, tint = TextGray) } },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    colors = TextFieldDefaults.colors(focusedContainerColor = InputFieldDark, unfocusedContainerColor = InputFieldDark, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                    shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Forgot password?", color = TextGray, fontSize = 12.sp, modifier = Modifier.align(Alignment.End))
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        // LIVE DB CHECK
                        val user = MockDatabase.users.value.find {
                            it.id.equals(identifier, ignoreCase = true) && it.pass == password && it.role == selectedRole
                        }

                        if (user != null) {
                            MockDatabase.currentUser = user // Set active session
                            val route = when (user.role) {
                                "Admin" -> "admin_root"
                                "Student" -> "student_root"
                                "HR" -> "hr_root"
                                "Employee" -> "employee_root"
                                else -> "login"
                            }
                            navController.navigate(route) { popUpTo("login") { inclusive = true } }
                        } else {
                            Toast.makeText(context, "Invalid Credentials or Role", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TealAccent), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("Login", color = Color.Black, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Card(colors = CardDefaults.cardColors(containerColor = CardDark), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("QUICK ROLE SWITCH", color = TextGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    RoleButton("Admin", Icons.Default.Security, selectedRole == "Admin", { selectedRole = "Admin" }, modifier = Modifier.weight(1f))
                    RoleButton("Student", Icons.Default.School, selectedRole == "Student", { selectedRole = "Student" }, modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    RoleButton("Employee", Icons.Default.Work, selectedRole == "Employee", { selectedRole = "Employee" }, modifier = Modifier.weight(1f))
                    RoleButton("HR", Icons.Default.Group, selectedRole == "HR", { selectedRole = "HR" }, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun RoleButton(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = onClick, modifier = modifier.height(40.dp), shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = if (isSelected) TealAccent.copy(alpha = 0.1f) else InputFieldDark, contentColor = if (isSelected) TealAccent else TextGray),
        border = BorderStroke(1.dp, if (isSelected) TealAccent else Color.Transparent)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(title, fontSize = 12.sp)
    }
}