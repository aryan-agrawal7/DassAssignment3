package com.DASS_2024111023_2024117009.ims.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.DASS_2024111023_2024117009.ims.ui.theme.*
import com.DASS_2024111023_2024117009.ims.MockDatabase
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.core.net.toUri
import androidx.compose.ui.draw.clip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstituteNewsScreen(navController: NavController, isAdmin: Boolean = false) {
    val allNews by MockDatabase.allNews.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    
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
                    Text("Institute News", color = TealAccent, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }
        },
        floatingActionButton = {
            if (isAdmin) {
                FloatingActionButton(
                    onClick = { navController.navigate("admin_create_news") },
                    containerColor = TealAccent,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Create News", tint = Color.Black)
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                TextField(
                    value = searchQuery, onValueChange = { searchQuery = it },
                    placeholder = { Text("Search news...", color = TextGray) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextGray) },
                    colors = TextFieldDefaults.colors(focusedContainerColor = CardDark, unfocusedContainerColor = CardDark, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, focusedTextColor = TextWhite),
                    shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()
                )
            }

            val sortedNews = allNews.sortedByDescending { it.order }
            val filteredNews = if (searchQuery.isBlank()) sortedNews
                else sortedNews.filter { it.title.contains(searchQuery, ignoreCase = true) || it.content.contains(searchQuery, ignoreCase = true) || it.author.contains(searchQuery, ignoreCase = true) }

            filteredNews.forEach { news ->
                item {
                    val displayAuthor = if (news.author.equals("Admin", ignoreCase = true) && MockDatabase.currentUser?.role != "Admin") "Admin" else news.author
                    val displayInitials = if (news.author.equals("Admin", ignoreCase = true) && MockDatabase.currentUser?.role != "Admin") "AD" else news.author.take(2).uppercase()
                    FullNewsCard(
                        author = displayAuthor,
                        initials = displayInitials,
                        time = news.time,
                        title = news.title,
                        content = news.content,
                        likes = news.likes.toString(),
                        comments = news.comments.size.toString(),
                        imageUri = news.imageUri,
                        isAdmin = isAdmin,
                        searchQuery = searchQuery,
                        avatarColor = Color(0xFF1E3A8A),
                        onClick = { navController.navigate("news_detail/${news.id}") },
                        onEdit = {
                            if (isAdmin) navController.navigate("admin_edit_news/${news.id}")
                        },
                        onDelete = {
                            if (isAdmin) MockDatabase.deleteNews(news.id)
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun FullNewsCard(
    author: String, initials: String, time: String, title: String, content: String,
    likes: String, comments: String, imageUri: String? = null, isUrgent: Boolean = false,
    isAdmin: Boolean = false, avatarColor: Color,
    searchQuery: String = "",
    onClick: () -> Unit, onEdit: () -> Unit = {}, onDelete: () -> Unit = {}
) {
    Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Surface(shape = CircleShape, color = avatarColor, modifier = Modifier.size(40.dp)) {
                        Box(contentAlignment = Alignment.Center) { Text(initials, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp) }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(author, color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text(time, color = TextGray, fontSize = 10.sp)
                    }
                }
                if (isAdmin) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onEdit, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = TealAccent, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFFCA5A5), modifier = Modifier.size(16.dp))
                        }
                    }
                } else if (isUrgent) {
                    Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFF3F2323)) {
                        Text("URGENT", color = Color(0xFFFCA5A5), fontSize = 8.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            // RTF highlight: bold title with query highlight
            HighlightedText(text = title, query = searchQuery, baseStyle = TextStyle(color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(8.dp))
            HighlightedText(text = content, query = searchQuery, baseStyle = TextStyle(color = TextGray, fontSize = 12.sp, lineHeight = 18.sp), maxLines = 4)
            if (imageUri != null) {
                val context = LocalContext.current
                val bitmap = remember(imageUri) {
                    runCatching {
                        context.contentResolver.openInputStream(imageUri.toUri())?.use { stream ->
                            BitmapFactory.decodeStream(stream)?.asImageBitmap()
                        }
                    }.getOrNull()
                }
                Spacer(modifier = Modifier.height(12.dp))
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap,
                        contentDescription = "News Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().height(140.dp).clip(RoundedCornerShape(8.dp))
                    )
                } else {
                    Surface(shape = RoundedCornerShape(8.dp), color = BackgroundDark, modifier = Modifier.fillMaxWidth().height(140.dp)) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Image, contentDescription = null, tint = TealAccent, modifier = Modifier.size(32.dp))
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.FavoriteBorder, contentDescription = "Like", tint = TextGray, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(likes, color = TextGray, fontSize = 10.sp)
                Spacer(modifier = Modifier.width(16.dp))
                Icon(Icons.Default.ChatBubbleOutline, contentDescription = "Comment", tint = TextGray, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(comments, color = TextGray, fontSize = 10.sp)
            }
        }
    }
}

/**
 * RTF-style highlighted text composable.
 * Renders [text] with all occurrences of [query] highlighted in yellow (like MS Word Find).
 * Non-matching segments use [baseStyle].
 */
@Composable
fun HighlightedText(
    text: String,
    query: String,
    baseStyle: TextStyle = TextStyle.Default,
    maxLines: Int = Int.MAX_VALUE
) {
    if (query.isBlank()) {
        Text(text = text, style = baseStyle, maxLines = maxLines, overflow = TextOverflow.Ellipsis)
        return
    }
    val annotated = buildAnnotatedString {
        val lowerText = text.lowercase()
        val lowerQuery = query.lowercase()
        var lastIndex = 0
        var matchStart = lowerText.indexOf(lowerQuery, lastIndex)
        while (matchStart >= 0) {
            // Normal segment before match
            if (matchStart > lastIndex) {
                withStyle(SpanStyle(color = baseStyle.color, fontWeight = baseStyle.fontWeight, fontSize = baseStyle.fontSize)) {
                    append(text.substring(lastIndex, matchStart))
                }
            }
            // Highlighted match segment — yellow background, dark text, bold
            withStyle(SpanStyle(
                background = Color(0xFFFFEB3B),
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = baseStyle.fontSize
            )) {
                append(text.substring(matchStart, matchStart + query.length))
            }
            lastIndex = matchStart + query.length
            matchStart = lowerText.indexOf(lowerQuery, lastIndex)
        }
        // Remaining text after last match
        if (lastIndex < text.length) {
            withStyle(SpanStyle(color = baseStyle.color, fontWeight = baseStyle.fontWeight, fontSize = baseStyle.fontSize)) {
                append(text.substring(lastIndex))
            }
        }
    }
    Text(text = annotated, maxLines = maxLines, overflow = TextOverflow.Ellipsis)
}