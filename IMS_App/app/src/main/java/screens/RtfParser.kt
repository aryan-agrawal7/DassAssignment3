package com.DASS_2024111023_2024117009.ims.screens

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle

/**
 * Parses simple RTF markers into a styled AnnotatedString.
 * Supported markers:
 *   **text**   → Bold
 *   *text*     → Italic
 *   __text__   → Underline
 *
 * Markers are processed in priority order (** before *) to avoid conflicts.
 */
fun buildRtfAnnotatedString(text: String, baseColor: Color): AnnotatedString {
    return buildAnnotatedString {
        var i = 0
        while (i < text.length) {
            when {
                // Bold: **text**
                text.startsWith("**", i) -> {
                    val endIdx = text.indexOf("**", i + 2)
                    if (endIdx != -1) {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = baseColor)) {
                            append(text.substring(i + 2, endIdx))
                        }
                        i = endIdx + 2
                    } else {
                        append(text[i].toString())
                        i++
                    }
                }
                // Underline: __text__
                text.startsWith("__", i) -> {
                    val endIdx = text.indexOf("__", i + 2)
                    if (endIdx != -1) {
                        withStyle(SpanStyle(textDecoration = TextDecoration.Underline, color = baseColor)) {
                            append(text.substring(i + 2, endIdx))
                        }
                        i = endIdx + 2
                    } else {
                        append(text[i].toString())
                        i++
                    }
                }
                // Italic: *text*
                text[i] == '*' -> {
                    val endIdx = text.indexOf('*', i + 1)
                    if (endIdx != -1) {
                        withStyle(SpanStyle(fontStyle = FontStyle.Italic, color = baseColor)) {
                            append(text.substring(i + 1, endIdx))
                        }
                        i = endIdx + 1
                    } else {
                        append(text[i].toString())
                        i++
                    }
                }
                else -> {
                    append(text[i].toString())
                    i++
                }
            }
        }
    }
}
