/*
 * Copyright 2023 Note Taking App by Peter Chege
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.peterchege.notetakingapp.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.peterchege.notetakingapp.domain.models.Note

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun NoteCardPreview(){
    Scaffold(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(7.dp)) {
            LazyVerticalGrid(columns = GridCells.Fixed(count = 2)){
                items(10){
                    NoteCard(
                        modifier = Modifier.padding(5.dp),
                        note = Note(
                            noteId = "1",
                            noteTitle = "Sample Note",
                            noteContent = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer mattis eu odio vitae venenatis. Integer maximus felis quis consequat iaculis. Ut auctor accumsan ligula eu pretium. Sed aliquam metus quis nisl accumsan volutpat. Donec elementum neque sed luctus accumsan. Proin justo ipsum, sagittis eu massa at, hendrerit pretium neque. Duis rhoncus feugiat sapien pellentesque vehicula. In pharetra orci augue. Proin tincidunt augue venenatis enim interdum convallis. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer mattis eu odio vitae venenatis. Integer maximus felis quis consequat iaculis. Ut auctor accumsan ligula eu pretium. Sed aliquam metus quis nisl accumsan volutpat. Donec elementum neque sed luctus accumsan. Proin justo ipsum, sagittis eu massa at, hendrerit pretium neque. Duis rhoncus feugiat sapien pellentesque vehicula. In pharetra orci augue. Proin tincidunt augue venenatis enim interdum convallis.",
                            noteColor = 0xFFE0C090.toInt(),
                            noteAuthorId = "user123",
                            noteCreatedAt = "2023-07-08",
                            noteCreatedOn = "2023-07-08 10:00:00",
                            isInSync = true
                        ),
                        onDeleteClick = {  },
                        onNoteClick = {  },
                        isUserLoggedIn = true
                    )
                }

            }
        }
    }
}

@Composable
fun NoteCard(
    note: Note,
    isUserLoggedIn:Boolean,
    onNoteClick:(String) -> Unit,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 10.dp,
    cutCornerSize: Dp = 30.dp,
    onDeleteClick: () -> Unit
) {
    Box(
        modifier = modifier.clickable { onNoteClick(note.noteId) }
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val clipPath = Path().apply {
                lineTo(x = size.width - cutCornerSize.toPx(), y = 0f)
                lineTo(x = size.width, y = cutCornerSize.toPx())
                lineTo(x = size.width, size.height)
                lineTo(x = 0f, size.height)
                close()
            }

            clipPath(clipPath) {
                drawRoundRect(
                    color = Color(note.noteColor),
                    size = size,
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )
                drawRoundRect(
                    color = Color(
                        ColorUtils.blendARGB(note.noteColor, 0x000000, 0.2f)
                    ),
                    topLeft = Offset(size.width - cutCornerSize.toPx(), -100f),
                    size = Size(cutCornerSize.toPx() + 100f, cutCornerSize.toPx() + 100f),
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(end = 32.dp)
        ) {
            Text(
                text = note.noteTitle,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = note.noteContent,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 10,
                overflow = TextOverflow.Ellipsis
            )
        }
        Row(
            modifier = Modifier.align(Alignment.BottomEnd),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ){
            if (isUserLoggedIn){
                Icon(
                    imageVector = if(note.isInSync)
                        Icons.Outlined.Done else Icons.Default.Refresh,
                    contentDescription = "In Sync",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            IconButton(
                onClick = onDeleteClick,
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete note",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }

    }
}