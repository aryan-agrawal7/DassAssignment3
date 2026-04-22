package com.DASS_2024111023_2024117009.ims.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp // FIXED IMPORT
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.WarningAmber
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
fun AdminFinanceScreen(navController: NavController) {
    LazyColumn(modifier = Modifier.fillMaxSize().background(BackgroundDark).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        item {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TealAccent) }
                Spacer(modifier = Modifier.width(8.dp))
                Text("Finance", color = TealAccent, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(40.dp))

            Surface(modifier = Modifier.size(100.dp), shape = CircleShape, color = Color(0xFF042F2E).copy(alpha = 0.5f)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.AccountBalance, contentDescription = null, tint = TealAccent, modifier = Modifier.size(40.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Coming Soon", color = TextWhite, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "A comprehensive financial control center is being built. Track fees, manage expenses, and analyze revenue flows.",
                color = TextGray, fontSize = 12.sp, textAlign = TextAlign.Center, lineHeight = 20.sp, modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(40.dp))
        }

        item {
            Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text("Fees Collection", color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("Current Term", color = TextGray, fontSize = 10.sp)
                        }
                        Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, tint = TextGray, modifier = Modifier.size(16.dp)) // FIXED HERE
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(60.dp)) {
                            CircularProgressIndicator(progress = { 0.65f }, color = TealAccent, trackColor = BackgroundDark, strokeWidth = 4.dp, modifier = Modifier.fillMaxSize())
                            Text("65%", color = TextWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(24.dp))
                        Column {
                            Text("COLLECTED", color = TextGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Text("$142,500", color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("PENDING", color = TextGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Text("$76,200", color = TextGray, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Top Defaulters", color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Icon(Icons.Default.WarningAmber, contentDescription = null, tint = TextGray, modifier = Modifier.size(16.dp))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    DefaulterRow("JS", "John Smith", "Grade 10 - A", "$1,250")
                    Spacer(modifier = Modifier.height(12.dp))
                    DefaulterRow("ED", "Emma Davis", "Grade 12 - B", "$980")
                    Spacer(modifier = Modifier.height(12.dp))
                    DefaulterRow("MW", "Michael Wood", "Grade 9 - C", "$850")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.fillMaxWidth().height(160.dp)) {
                Column(modifier = Modifier.padding(20.dp).fillMaxSize()) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text("Expense Overview", color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("YTD Trends", color = TextGray, fontSize = 10.sp)
                        }
                        Icon(Icons.Default.Payments, contentDescription = null, tint = TextGray, modifier = Modifier.size(16.dp))
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Row(modifier = Modifier.fillMaxWidth().height(60.dp), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.Bottom) {
                        Box(modifier = Modifier.width(30.dp).height(30.dp).background(BackgroundDark))
                        Box(modifier = Modifier.width(30.dp).height(45.dp).background(BackgroundDark))
                        Box(modifier = Modifier.width(30.dp).height(20.dp).background(BackgroundDark))
                        Box(modifier = Modifier.width(30.dp).height(55.dp).background(BackgroundDark))
                        Box(modifier = Modifier.width(30.dp).height(40.dp).background(BackgroundDark))
                    }
                    Row(modifier = Modifier.fillMaxWidth().padding(top = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Jan", color = TextGray, fontSize = 8.sp)
                        Text("Jun", color = TextGray, fontSize = 8.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun DefaulterRow(initials: String, name: String, grade: String, amount: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = CircleShape, color = BackgroundDark, modifier = Modifier.size(36.dp)) {
                Box(contentAlignment = Alignment.Center) { Text(initials, color = TextGray, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(name, color = TextGray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text(grade, color = TextGray, fontSize = 10.sp)
            }
        }
        Text(amount, color = TextGray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}