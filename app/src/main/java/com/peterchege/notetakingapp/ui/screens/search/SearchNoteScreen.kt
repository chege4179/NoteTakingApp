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
package com.peterchege.notetakingapp.ui.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.peterchege.notetakingapp.ui.components.LoadingComponent
import com.peterchege.notetakingapp.ui.components.NoteCard
import com.peterchege.notetakingapp.ui.screens.destinations.NoteScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel

@Destination
@Composable
fun SearchNoteScreen(
    navigator: DestinationsNavigator,
) {
    val viewModel = getViewModel<SearchNoteScreenViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val query by viewModel.query.collectAsStateWithLifecycle()


    SearchNotesScreenContent(
        uiState = uiState,
        onChangeQuery = viewModel::onChangeQuery,
        navigateToNoteScreen ={ navigator.navigate(NoteScreenDestination(it)) } ,
        query = query
    )
}


@Composable
fun SearchNotesScreenContent(
    uiState: SearchNotesScreenUiState,
    onChangeQuery: (String) -> Unit,
    navigateToNoteScreen: (String) -> Unit,
    query: String,

    ) {

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            SearchBar(
                modifier = Modifier
                    .height(67.dp)
                    .clip(RoundedCornerShape(20.dp))
                    ,
                query = query,
                onQueryChange = { onChangeQuery(it) },
                shape = RoundedCornerShape(15.dp),
                onSearch = { },
                active = true,
                onActiveChange = {

                },
                placeholder = {
                    Text("Search Note")
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                },
                trailingIcon = {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                },
                content = {  }
            )
            Spacer(modifier = Modifier.height(5.dp))
            when (uiState) {
                is SearchNotesScreenUiState.Idle -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "Type something",
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.primary
                        )

                    }
                }

                is SearchNotesScreenUiState.Searching -> {
                    LoadingComponent()
                }

                is SearchNotesScreenUiState.NoResultsFound -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "No results found",
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.primary
                        )

                    }
                }
                is SearchNotesScreenUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "Error Occurred",
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                is SearchNotesScreenUiState.ResultsFound -> {
                    val notes = uiState.notes
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(items = notes, key = {note ->  note.noteId }) { note ->
                            NoteCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 5.dp),

                                note = note,
                                onNoteClick = { id ->
                                    navigateToNoteScreen(id)
                                },
                                onDeleteClick = {

                                },
                                isUserLoggedIn = false
                            )

                        }

                    }
                }
            }

        }


    }

}
