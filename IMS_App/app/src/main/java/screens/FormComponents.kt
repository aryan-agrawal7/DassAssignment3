package com.DASS_2024111023_2024117009.ims.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.DASS_2024111023_2024117009.ims.ui.theme.*
import com.DASS_2024111023_2024117009.ims.FormField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuilderFieldCard(
    field: FormField,
    onFieldChange: (FormField) -> Unit,
    onDelete: () -> Unit
) {
    Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp)) {
            Icon(Icons.Default.DragIndicator, contentDescription = null, tint = TextGray, modifier = Modifier.padding(top = 12.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                TextField(
                    value = field.label, onValueChange = { onFieldChange(field.copy(label = it)) },
                    label = { Text("Field Label", color = TextGray) },
                    colors = TextFieldDefaults.colors(focusedContainerColor = InputFieldDark, unfocusedContainerColor = InputFieldDark, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, focusedTextColor = TextWhite),
                    shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    TextField(
                        value = field.type, onValueChange = {}, label = { Text("Field Type", color = TextGray) }, readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        colors = TextFieldDefaults.colors(focusedContainerColor = InputFieldDark, unfocusedContainerColor = InputFieldDark, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, focusedTextColor = TextWhite),
                        shape = RoundedCornerShape(8.dp), modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, true).fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(InputFieldDark)
                    ) {
                        listOf("Short Text", "Dropdown", "Number", "Long Text").forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption, color = TextWhite) },
                                onClick = {
                                    onFieldChange(field.copy(type = selectionOption))
                                    expanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Switch(
                            checked = field.isRequired,
                            onCheckedChange = { onFieldChange(field.copy(isRequired = it)) },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = TealAccent, uncheckedThumbColor = TextGray, uncheckedTrackColor = InputFieldDark),
                            modifier = Modifier.scale(0.8f)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Required", color = TextGray, fontSize = 12.sp)
                    }
                    if (!field.isImmutable) {
                        IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = TextGray, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuilderDropdownCard(
    field: FormField,
    onFieldChange: (FormField) -> Unit,
    onDelete: () -> Unit
) {
    Card(colors = CardDefaults.cardColors(containerColor = CardDark), modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp)) {
            Icon(Icons.Default.DragIndicator, contentDescription = null, tint = TextGray, modifier = Modifier.padding(top = 12.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                TextField(
                    value = field.label, onValueChange = { onFieldChange(field.copy(label = it)) },
                    label = { Text("Field Label", color = TextGray) },
                    colors = TextFieldDefaults.colors(focusedContainerColor = InputFieldDark, unfocusedContainerColor = InputFieldDark, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, focusedTextColor = TextWhite),
                    shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    TextField(
                        value = field.type, onValueChange = {}, label = { Text("Field Type", color = TextGray) }, readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        colors = TextFieldDefaults.colors(focusedContainerColor = InputFieldDark, unfocusedContainerColor = InputFieldDark, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, focusedTextColor = TextWhite),
                        shape = RoundedCornerShape(8.dp), modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, true).fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(InputFieldDark)
                    ) {
                        listOf("Short Text", "Dropdown", "Number", "Long Text").forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption, color = TextWhite) },
                                onClick = {
                                    onFieldChange(field.copy(type = selectionOption))
                                    expanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                Text("OPTIONS", color = TextGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                
                field.options.forEachIndexed { optIndex, optionStr ->
                    DropdownOptionRow(
                        text = optionStr,
                        onDeleteOption = {
                            val newOptions = field.options.toMutableList().apply { removeAt(optIndex) }
                            onFieldChange(field.copy(options = newOptions))
                        },
                        onOptionChange = { newValue ->
                            val newOptions = field.options.toMutableList().apply { set(optIndex, newValue) }
                            onFieldChange(field.copy(options = newOptions))
                        }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "+ Add Option",
                    color = TealAccent,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        val newOptions = field.options + "New Option"
                        onFieldChange(field.copy(options = newOptions))
                    }.padding(8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Switch(
                            checked = field.isRequired,
                            onCheckedChange = { onFieldChange(field.copy(isRequired = it)) },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = TealAccent, uncheckedThumbColor = TextGray, uncheckedTrackColor = InputFieldDark),
                            modifier = Modifier.scale(0.8f)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Required", color = TextGray, fontSize = 12.sp)
                    }
                    if (!field.isImmutable) {
                        IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = TextGray, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DropdownOptionRow(text: String, onDeleteOption: () -> Unit, onOptionChange: (String) -> Unit) {
    Surface(shape = RoundedCornerShape(4.dp), color = InputFieldDark, modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Icon(Icons.Default.DragIndicator, contentDescription = null, tint = TextGray, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(8.dp))
                TextField(
                    value = text,
                    onValueChange = onOptionChange,
                    colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            IconButton(onClick = onDeleteOption, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.Close, contentDescription = "Remove", tint = TextGray, modifier = Modifier.size(14.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormTextField(label: String, value: String, placeholderText: String, onValueChange: (String) -> Unit) {
    Column {
        Text(label, color = TextGray, fontSize = 10.sp, modifier = Modifier.padding(start = 4.dp, bottom = 4.dp))
        TextField(
            value = value, onValueChange = onValueChange,
            placeholder = { Text(placeholderText, color = Color.DarkGray, fontSize = 14.sp) }, // Added placeholder
            colors = TextFieldDefaults.colors(focusedContainerColor = InputFieldDark, unfocusedContainerColor = InputFieldDark, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
            shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth().height(50.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DynamicFormDropdown(label: String, options: List<String>, selectedValue: String, onValueChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Column {
        Text(label, color = TextGray, fontSize = 10.sp, modifier = Modifier.padding(start = 4.dp, bottom = 4.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedValue.ifEmpty { "Select Option" }, onValueChange = {}, readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = TextFieldDefaults.colors(focusedContainerColor = InputFieldDark, unfocusedContainerColor = InputFieldDark, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                shape = RoundedCornerShape(8.dp), modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, true).fillMaxWidth().height(50.dp)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(InputFieldDark)
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption, color = TextWhite) },
                        onClick = {
                            onValueChange(selectionOption)
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
    }
}
