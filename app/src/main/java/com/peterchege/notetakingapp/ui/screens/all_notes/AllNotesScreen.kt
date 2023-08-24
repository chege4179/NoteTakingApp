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
package com.peterchege.notetakingapp.ui.screens.all_notes

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.peterchege.notetakingapp.core.util.pullRefresh.PullRefreshIndicator
import com.peterchege.notetakingapp.core.util.pullRefresh.pullRefresh
import com.peterchege.notetakingapp.core.util.pullRefresh.rememberPullRefreshState
import com.peterchege.notetakingapp.ui.components.CustomIconButton
import com.peterchege.notetakingapp.ui.components.ErrorComponent
import com.peterchege.notetakingapp.ui.components.LoadingComponent
import com.peterchege.notetakingapp.ui.components.NoteCard
import com.peterchege.notetakingapp.ui.screens.destinations.AddNoteScreenDestination
import com.peterchege.notetakingapp.ui.screens.destinations.NoteScreenDestination
import com.peterchege.notetakingapp.ui.screens.destinations.SearchNoteScreenDestination
import com.peterchege.notetakingapp.ui.screens.destinations.SettingsScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel

@RootNavGraph(start = true)
@Destination
@Composable
fun AllNotesScreen(
    navigator: DestinationsNavigator,
) {
    val viewModel = getViewModel<AllNotesScreenViewModel>()
    val isSyncing by viewModel.isSyncing.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    AllNotesScreenContent(
        uiState = uiState,
        onDeleteNote = viewModel::deleteNote,
        isSyncing = isSyncing,
        syncNotes = viewModel::syncNotes,
        onNoteClick = {
            navigator.navigate(NoteScreenDestination(it))
        },
        navigateToSearchScreen = {
            navigator.navigate(SearchNoteScreenDestination)
        },
        navigateToAddNoteScreen ={
            navigator.navigate(AddNoteScreenDestination)
        },
        navigateToSettingsScreen =  {
            navigator.navigate(SettingsScreenDestination)
        }
    )


}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllNotesScreenContent(
    isSyncing: Boolean,
    syncNotes: (String) -> Unit,
    uiState: AllNotesScreenUiState,
    navigateToSettingsScreen:() -> Unit,
    navigateToSearchScreen:() -> Unit,
    navigateToAddNoteScreen:() -> Unit,
    onDeleteNote: (String) -> Unit,
    onNoteClick:(String) -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "My Notes")
                },
                actions = {
                    CustomIconButton(
                        icon = Icons.Default.Settings,
                        onClick = {
                            navigateToSettingsScreen()


                        })
                    CustomIconButton(
                        icon = Icons.Default.Search,
                        onClick = {
                            navigateToSearchScreen()
                        }
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navigateToAddNoteScreen()
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Create Post"
                )

            }
        }
    ) {
        when (uiState) {
            is AllNotesScreenUiState.Loading -> {
                LoadingComponent()
            }

            is AllNotesScreenUiState.Error -> {
                ErrorComponent(
                    message = uiState.message,
                    retryCallback = { }
                )
            }

            is AllNotesScreenUiState.Success -> {
                val pullRefreshState = rememberPullRefreshState(
                    refreshing = isSyncing,
                    onRefresh = {
                        if (uiState.authUser != null) {
                            syncNotes(uiState.authUser.userId)
                        }
                    })
                val notes = uiState.notes
                Box(
                    modifier = Modifier
                        .pullRefresh(pullRefreshState)
                        .fillMaxSize()
                        .padding(paddingValues = it)
                        .padding(10.dp)
                ) {
                    PullRefreshIndicator(
                        refreshing = true,
                        state = pullRefreshState,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        if (notes.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text(
                                    text = "You have no notes yet",
                                    modifier = Modifier.align(Alignment.Center),
                                    style = TextStyle(color = MaterialTheme.colorScheme.primary),

                                    )

                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                            ) {
                                items(items = notes, key = { it.noteId }) {
                                    NoteCard(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 5.dp),
                                        note = it,
                                        onDeleteClick = {
                                            onDeleteNote(it.noteId)

                                        },
                                        onNoteClick = {
                                            onNoteClick(it)
                                        }
                                    )

                                }
                            }

                        }
                    }
                }
            }
        }
    }


}
