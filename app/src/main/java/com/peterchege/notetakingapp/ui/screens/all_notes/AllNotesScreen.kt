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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.peterchege.notetakingapp.domain.models.Note
import com.peterchege.notetakingapp.ui.components.CustomIconButton
import com.peterchege.notetakingapp.ui.components.NoteCard
import com.peterchege.notetakingapp.ui.screens.destinations.AddNoteScreenDestination
import com.peterchege.notetakingapp.ui.screens.destinations.SettingsScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel

@Destination
@Composable
fun AllNotesScreen(
    navigator: DestinationsNavigator,

    ) {
    val viewModel = getViewModel<AllNotesScreenViewModel>()
    val notes by viewModel.notes.collectAsStateWithLifecycle()

    AllNotesScreenContent(
        notes = notes,
        navigator = navigator,
        onDeleteNote = { viewModel.deleteNote(it) }
    )


}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllNotesScreenContent(
    notes: List<Note>,
    navigator: DestinationsNavigator,
    onDeleteNote:(String) -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navigator.navigate(AddNoteScreenDestination)
                }
            ) {
                Icon(
                    imageVector =  Icons.Filled.Add,
                    contentDescription = "Create Post"
                )

            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(10.dp)
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ){
                Text(
                    text = "Notes",
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.primary,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(0.5F),
                    horizontalArrangement = Arrangement.End,
                ){
                    CustomIconButton(
                        icon = Icons.Default.Settings,
                        onClick = {
                            navigator.navigate(SettingsScreenDestination)

                        })
                    CustomIconButton(
                        icon = Icons.Default.Search,
                        onClick = {

                        }
                    )
                }
            }
            if (notes.isEmpty()){
                Box(
                    modifier = Modifier.fillMaxSize()
                ){
                    Text(
                        text = "You have no notes yet",
                        modifier = Modifier.align(Alignment.Center),
                        style = TextStyle(color = MaterialTheme.colorScheme.primary),

                        )

                }
            }else{
                LazyVerticalGrid(columns = GridCells.Fixed(count = 2)){
                    items(items = notes) {
                        NoteCard(
                            note = it,
                            onDeleteClick = {
                                onDeleteNote(it.noteId)

                            }
                        )

                    }
                }
            }
        }



    }


}
