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
import com.DASS_2024111023_2024117009.ims.News
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminCreateNewsScreen(navController: NavController) {
    val context = LocalContext.current

    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var content by remember { mutableStateOf(TextFieldValue("")) }
    var imageUri by remember { mutableStateOf<String?>(null) }

    // Real image picker — opens device gallery
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri?.toString()
    }

    // RTF helper: wraps selected text (or inserts placeholder) with the given markers
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
                Text("Create News", color = TextWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
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
            Text("Category", color = TextWhite, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = category, onValueChange = { category = it },
                placeholder = { Text("Enter Category (e.g. Alerts, Academics, Events)", color = Color.Gray) },
                colors = TextFieldDefaults.colors(focusedContainerColor = InputFieldDark, unfocusedContainerColor = InputFieldDark, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, focusedTextColor = TextWhite),
                shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            // RTF Content editor with functional toolbar
            Text("Content", color = TextWhite, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Tip: Use toolbar below to format — B = **bold**, I = *italic*, U = __underline__", color = TextGray, fontSize = 9.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Surface(shape = RoundedCornerShape(8.dp), color = InputFieldDark, modifier = Modifier.fillMaxWidth()) {
                Column {
                    // RTF Toolbar — buttons are functional
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
                        // Stub icons (decorative)
                        Icon(Icons.AutoMirrored.Filled.FormatListBulleted, contentDescription = null, tint = TextGray, modifier = Modifier.size(16.dp))
                        Icon(Icons.Default.FormatListNumbered, contentDescription = null, tint = TextGray, modifier = Modifier.size(16.dp))
                        Icon(Icons.Default.Link, contentDescription = null, tint = TextGray, modifier = Modifier.size(16.dp))
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
                // Upload box — tapping opens real device gallery
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
                        Text("Tap to upload image", color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("PNG, JPG or GIF (max. 5MB)", color = TextGray, fontSize = 10.sp)
                    }
                }
            } else {
                // Actual uploaded image preview
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
                            modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(8.dp))
                        )
                    } else {
                        Surface(shape = RoundedCornerShape(8.dp), color = InputFieldDark, modifier = Modifier.fillMaxSize()) {
                            Box(contentAlignment = Alignment.Center) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.Image, contentDescription = null, tint = TealAccent, modifier = Modifier.size(36.dp))
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Image selected", color = TealAccent, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                    // Delete / remove image button at top-right
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
                    if (title.isNotBlank() && content.text.isNotBlank() && category.isNotBlank()) {
                        val newNews = News(
                            id = java.util.UUID.randomUUID().toString(),
                            title = title,
                            content = content.text,
                            author = "Admin",
                            time = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date()),
                            likes = 0,
                            imageUri = imageUri
                        )
                        MockDatabase.addNews(newNews)
                        navController.popBackStack()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = TealAccent),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) { Text("Publish", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp) }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}