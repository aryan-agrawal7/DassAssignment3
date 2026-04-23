package com.DASS_2024111023_2024117009.ims.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.core.net.toUri
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.DASS_2024111023_2024117009.ims.ui.theme.*
import com.DASS_2024111023_2024117009.ims.MockDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminEditNewsScreen(navController: NavController, newsId: String) {
    val context = LocalContext.current
    val allNews by MockDatabase.allNews.collectAsState()
    val existingNews = remember(newsId) { allNews.find { it.id == newsId } }

    if (existingNews == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("News item not found", color = TextWhite)
        }
        return
    }

    var title by remember { mutableStateOf(existingNews.title) }
    var content by remember { mutableStateOf(TextFieldValue(existingNews.content)) }
    var imageUri by remember { mutableStateOf<String?>(existingNews.imageUri) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri?.toString()
    }

    fun wrapSelection(prefix: String, suffix: String) {
        val sel = content.selection
        val text = content.text
        val newText: String
        val newSelection: TextRange
        if (!sel.collapsed && sel.start != sel.end) {
            val before = text.substring(0, sel.min)
            val selected = text.substring(sel.min, sel.max)
            val after = text.substring(sel.max)
            newText = "$before$prefix$selected$suffix$after"
            newSelection = TextRange(sel.max + prefix.length + suffix.length)
        } else {
            val cursor = sel.start
            val before = text.substring(0, cursor)
            val after = text.substring(cursor)
            val placeholder = "${prefix}text${suffix}"
            newText = "$before$placeholder$after"
            newSelection = TextRange(cursor + prefix.length, cursor + prefix.length + 4)
        }
        content = TextFieldValue(newText, newSelection)
    }

    LazyColumn(modifier = Modifier.fillMaxSize().background(BackgroundDark).padding(20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        item {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TealAccent) }
                Spacer(modifier = Modifier.width(8.dp))
                Text("Edit News", color = TextWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
        item {
            Text("Title", color = TextWhite, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = title, onValueChange = { title = it },
                placeholder = { Text("Enter an engaging headline...", color = Color.DarkGray) },
                colors = TextFieldDefaults.colors(focusedContainerColor = InputFieldDark, unfocusedContainerColor = InputFieldDark, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, focusedTextColor = TextWhite),
                shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            Text("Content", color = TextWhite, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Surface(shape = RoundedCornerShape(8.dp), color = InputFieldDark, modifier = Modifier.fillMaxWidth()) {
                Column {
                    Row(modifier = Modifier.fillMaxWidth().background(Color(0xFF2D333B)).padding(horizontal = 12.dp, vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { wrapSelection("**", "**") }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.FormatBold, contentDescription = "Bold", tint = TextWhite, modifier = Modifier.size(16.dp))
                        }
                        IconButton(onClick = { wrapSelection("*", "*") }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.FormatItalic, contentDescription = "Italic", tint = TextWhite, modifier = Modifier.size(16.dp))
                        }
                        IconButton(onClick = { wrapSelection("__", "__") }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.FormatUnderlined, contentDescription = "Underline", tint = TextWhite, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = { content = TextFieldValue("") }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.FormatClear, contentDescription = "Clear", tint = TextGray, modifier = Modifier.size(16.dp))
                        }
                    }
                    TextField(
                        value = content, onValueChange = { content = it },
                        placeholder = { Text("Write your news content here...", color = Color.DarkGray) },
                        colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, focusedTextColor = TextWhite),
                        modifier = Modifier.fillMaxWidth().height(160.dp)
                    )
                }
            }
        }
        item {
            Text("Featured Image", color = TextWhite, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            if (imageUri == null) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(140.dp).drawBehind {
                        drawRoundRect(
                            color = Color.DarkGray,
                            style = Stroke(width = 2.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)),
                            cornerRadius = CornerRadius(8.dp.toPx())
                        )
                    }.clickable { imagePickerLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(shape = CircleShape, color = Color(0xFF374151), modifier = Modifier.size(40.dp)) {
                            Icon(Icons.Default.Upload, contentDescription = null, tint = TextWhite, modifier = Modifier.padding(10.dp))
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Tap to change image", color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxWidth().height(180.dp)) {
                    val bitmap = remember(imageUri) {
                        runCatching {
                            val uri = imageUri!!.toUri()
                            context.contentResolver.openInputStream(uri)?.use { stream ->
                                BitmapFactory.decodeStream(stream)?.asImageBitmap()
                            }
                        }.getOrNull()
                    }
                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap,
                            contentDescription = "Selected Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(8.dp)).clickable { imagePickerLauncher.launch("image/*") }
                        )
                    }
                    IconButton(
                        onClick = { imageUri = null },
                        modifier = Modifier.align(Alignment.TopEnd).background(Color(0x88000000), CircleShape)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Remove image", tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    if (title.isNotBlank() && content.text.isNotBlank()) {
                        MockDatabase.updateNews(newsId, title, content.text, imageUri)
                        navController.popBackStack()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = TealAccent),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) { Text("Save Changes", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp) }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
