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
package com.peterchege.notetakingapp.ui.screens.add_note

import android.annotation.SuppressLint
import androidx.compose.animation.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.peterchege.notetakingapp.core.util.UiEvent
import com.peterchege.notetakingapp.ui.components.TransparentHintTextField
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.getViewModel

@Destination
@Composable
fun AddNoteScreen(
    navigator: DestinationsNavigator
) {
    val viewModel = getViewModel<AddNoteScreenViewModel>()
    val noteState by viewModel.noteState.collectAsStateWithLifecycle()
    val authUser by viewModel.authUser.collectAsStateWithLifecycle()



    AddNoteScreenContent(
        noteState = noteState,
        eventFlow = viewModel.eventFlow ,
        navigator = navigator,
        onChangeNoteTitle = { viewModel.onChangeNoteTitle(it) },
        onChangeNoteContent = { viewModel.onChangeNoteContent(it) },
        onChangeNoteColor = { viewModel.onChangeNoteColor(it) },
        saveNote = { viewModel.saveNote(authUser = authUser) }
    )

}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreenContent(
    noteState:NoteFormState,
    eventFlow:SharedFlow<UiEvent>,
    navigator:DestinationsNavigator,
    onChangeNoteTitle:(String) -> Unit,
    onChangeNoteContent:(String) -> Unit,
    onChangeNoteColor:(Int) -> Unit,
    saveNote:() -> Unit,
) {

    val titleState = noteState.noteTitle
    val contentState = noteState.noteContent
    val noteColor = noteState.noteColor
    val snackbarHostState = SnackbarHostState()

    val noteBackgroundAnimatable = remember {
        Animatable(
            Color(color = noteColor ?: 0)
        )
    }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        eventFlow.collectLatest { event ->
            when(event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is UiEvent.Navigate -> {
                    navigator.navigate(event.route)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                   saveNote()
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = "Save note"
                )
            }
        },

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(noteBackgroundAnimatable.value)
                .padding(16.dp)
        ) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Note.noteColors.forEach { color ->
//                    val colorInt = color.toArgb()
//                    Box(
//                        modifier = Modifier
//                            .size(50.dp)
//                            .shadow(15.dp, CircleShape)
//                            .clip(CircleShape)
//                            .background(color)
//                            .border(
//                                width = 3.dp,
//                                color = if (viewModel.noteColor.value == colorInt) {
//                                    Color.Black
//                                } else Color.Transparent,
//                                shape = CircleShape
//                            )
//                            .clickable {
//                                scope.launch {
//                                    noteBackgroundAnimatable.animateTo(
//                                        targetValue = Color(colorInt),
//                                        animationSpec = tween(
//                                            durationMillis = 500
//                                        )
//                                    )
//                                }
//
//                            }
//                    )
//                }
//            }
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = titleState,
                hint = "Note title",
                onValueChange = {
                    onChangeNoteTitle(it)
                },
                onFocusChange = {

                },
                isHintVisible = true,
                singleLine = true,
                textStyle = MaterialTheme.typography.labelMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = contentState,
                hint = "Write your note",
                onValueChange = {
                    onChangeNoteContent(it)
                },
                onFocusChange = {

                },
                isHintVisible = true,
                textStyle = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxHeight()
            )
        }
    }

}