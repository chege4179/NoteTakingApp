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
package com.peterchege.notetakingapp.ui.screens.note

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.peterchege.notetakingapp.ui.components.CustomIconButton
import com.peterchege.notetakingapp.ui.components.LoadingComponent
import com.peterchege.notetakingapp.ui.screens.destinations.EditNoteScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel

@Destination
@Composable
fun NoteScreen(
    navigator: DestinationsNavigator,
    noteId: String,
) {
    val viewModel = getViewModel<NoteScreenViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NoteScreenContent(
        uiState = uiState,
        navigateToEditNoteScreen = {
            navigator.navigate(EditNoteScreenDestination(it))
        },
        deleteNote = {

        }
    )
}


@Composable
fun NoteScreenContent(
    uiState: NoteScreenUiState,
    navigateToEditNoteScreen: (String) -> Unit,
    deleteNote: () -> Unit,
) {


    when (uiState) {
        is NoteScreenUiState.Loading -> {
            LoadingComponent()
        }

        is NoteScreenUiState.Error -> {

        }

        is NoteScreenUiState.Success -> {
            val note = uiState.note
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(note.noteColor)),
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = note.noteTitle)
                        },
                        actions = {
                            CustomIconButton(
                                icon = Icons.Default.EditNote,
                                onClick = {
                                    navigateToEditNoteScreen(note.noteId)
                                }
                            )
                            CustomIconButton(
                                icon = Icons.Default.Delete,
                                onClick = {
                                    deleteNote()
                                }
                            )

                        }
                    )
                },
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues = it)
                        .padding(10.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 3.dp),
                        text = note.noteTitle,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = note.noteContent,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

            }


        }
    }

}