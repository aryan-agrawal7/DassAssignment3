package com.DASS_2024111023_2024117009.ims.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.core.net.toUri
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.DASS_2024111023_2024117009.ims.ui.theme.*
import com.DASS_2024111023_2024117009.ims.MockDatabase
import com.DASS_2024111023_2024117009.ims.Comment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstituteNewsDetailScreen(navController: NavController, newsId: String, isAdmin: Boolean = false) {
    val allNews by MockDatabase.allNews.collectAsState()
    val users by MockDatabase.users.collectAsState()
    val adminName = remember(users) { users.find { it.role == "Admin" }?.name ?: "Alexander Pierce" }
    val news = allNews.find { it.id == newsId }
    var commentText by remember { mutableStateOf("") }
    
    if (news == null) {
        Box(modifier = Modifier.fillMaxSize().background(BackgroundDark), contentAlignment = Alignment.Center) {
            Text("News not found", color = TextWhite)
        }
        return
    }

    Scaffold(
        containerColor = BackgroundDark,
        // FIXED: Header is now a sticky topBar
        topBar = {
            Surface(color = BackgroundDark, modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TealAccent)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("News Detail", color = TealAccent, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        },
        bottomBar = {
            Surface(color = BackgroundDark, modifier = Modifier.fillMaxWidth().navigationBarsPadding().imePadding().padding(horizontal = 20.dp, vertical = 12.dp)) {
                Row(verticalAlignment = Alignment.Bottom) {
                    TextField(
                        value = commentText, onValueChange = { commentText = it },
                        placeholder = { Text("Add a comment...", color = TextGray) },
                        maxLines = 4,
                        colors = TextFieldDefaults.colors(focusedContainerColor = InputFieldDark, unfocusedContainerColor = InputFieldDark, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, focusedTextColor = TextWhite),
                        shape = RoundedCornerShape(24.dp), modifier = Modifier.weight(1f).defaultMinSize(minHeight = 50.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = {
                            if (commentText.isNotBlank()) {
                                MockDatabase.addNewsComment(
                                    newsId,
                                    Comment(
                                        id = "C${System.currentTimeMillis()}",
                                        authorName = MockDatabase.currentUser?.name ?: "Unknown User",
                                        content = commentText,
                                        time = SimpleDateFormat("MMM dd", Locale.getDefault()).format(Date())
                                    )
                                )
                                commentText = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = TealAccent), shape = RoundedCornerShape(24.dp), modifier = Modifier.height(50.dp), contentPadding = PaddingValues(horizontal = 20.dp)
                    ) {
                        Text("Post", color = Color.Black, fontWeight = FontWeight.Bold)
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, tint = Color.Black, modifier = Modifier.padding(start = 4.dp).size(16.dp))
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Text(news.title, color = TextWhite, fontSize = 24.sp, fontWeight = FontWeight.Bold, lineHeight = 30.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val isCurrentUserAdmin = MockDatabase.currentUser?.role == "Admin"
                    val displayAuthor = if (news.author.equals("Admin", ignoreCase = true) && !isCurrentUserAdmin) "Admin" else news.author
                    Surface(shape = CircleShape, color = Color(0xFFB45309), modifier = Modifier.size(36.dp)) {
                        Box(contentAlignment = Alignment.Center) { Text(displayAuthor.take(2).uppercase(), color = TextWhite, fontWeight = FontWeight.Bold) }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(displayAuthor, color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("${news.time} • ${news.likes} likes", color = TextGray, fontSize = 10.sp)
                    }
                }
            }
            item {
                // RTF-parsed content display
                val rtfAnnotated = buildRtfAnnotatedString(news.content, TextGray)
                Text(rtfAnnotated, fontSize = 14.sp, lineHeight = 22.sp)
                Spacer(modifier = Modifier.height(16.dp))
                // Show attached image if present
                if (news.imageUri != null) {
                    val context = LocalContext.current
                    val bitmap = remember(news.imageUri) {
                        runCatching {
                            context.contentResolver.openInputStream(news.imageUri.toUri())?.use { stream ->
                                BitmapFactory.decodeStream(stream)?.asImageBitmap()
                            }
                        }.getOrNull()
                    }
                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap,
                            contentDescription = "News Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(12.dp))
                        )
                    } else {
                        Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFF111827), modifier = Modifier.fillMaxWidth().height(180.dp)) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Image, contentDescription = null, tint = TealAccent, modifier = Modifier.size(36.dp))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
                // Thematic static element for symposium news
                if (news.title.contains("Tech Symposium", ignoreCase = true)) {
                    Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFF111827), modifier = Modifier.fillMaxWidth().height(160.dp)) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                            Text("SYMPOSIUM", color = TextWhite, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 2.sp)
                            Text("Safe work", color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            item {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { 
                            val uid = MockDatabase.currentUser?.id
                            if (uid != null) MockDatabase.toggleNewsLike(news.id, uid)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = if (MockDatabase.currentUser?.id in news.likedBy) TealAccent else CardDark)
                    ) {
                        Text(if (MockDatabase.currentUser?.id in news.likedBy) "Unlike" else "Like", color = if (MockDatabase.currentUser?.id in news.likedBy) Color.Black else TextWhite)
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = BackgroundDark, thickness = 2.dp)
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Discussion", color = TextWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(shape = CircleShape, color = CardDark) { Text(news.comments.size.toString(), color = TextGray, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) }
                }
            }
            news.comments.forEach { comment ->
                item {
                    val isCurrentUserAdmin = MockDatabase.currentUser?.role == "Admin"
                    val commentDisplayName = if (comment.authorName.equals(adminName, ignoreCase = true) && !isCurrentUserAdmin) "Admin" else comment.authorName
                    CommentStub(
                        name = commentDisplayName,
                        time = comment.time,
                        content = comment.content,
                        isAdmin = isAdmin && !comment.isDeleted,
                        color = Color(0xFF065F46),
                        onDelete = { MockDatabase.deleteNewsComment(news.id, comment.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun CommentStub(name: String, time: String, content: String, isAdmin: Boolean, color: Color, onDelete: () -> Unit = {}) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        Surface(shape = CircleShape, color = color, modifier = Modifier.size(32.dp)) {
            Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.Person, contentDescription = null, tint = TextWhite, modifier = Modifier.size(20.dp)) }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Surface(shape = RoundedCornerShape(12.dp), color = CardDark, modifier = Modifier.weight(1f)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(name, color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(time, color = TextGray, fontSize = 10.sp)
                    }
                    if (isAdmin) {
                        IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFFCA5A5), modifier = Modifier.size(14.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(content, color = TextGray, fontSize = 12.sp, lineHeight = 18.sp)
            }
        }
    }
}