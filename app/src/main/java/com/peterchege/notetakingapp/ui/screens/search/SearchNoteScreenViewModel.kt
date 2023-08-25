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

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterchege.notetakingapp.domain.models.Note
import com.peterchege.notetakingapp.domain.repository.OfflineFirstNoteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

sealed interface SearchNotesScreenUiState {
    object Idle:SearchNotesScreenUiState

    object Searching:SearchNotesScreenUiState

    object NoResultsFound:SearchNotesScreenUiState

    data class ResultsFound(val notes:List<Note>):SearchNotesScreenUiState

    data class Error(val message:String):SearchNotesScreenUiState
}


class SearchNoteScreenViewModel (
    val savedStateHandle: SavedStateHandle,
    val noteRepository: OfflineFirstNoteRepository,
): ViewModel() {

    val query  = savedStateHandle.getStateFlow(key = "query", initialValue = "")
    private val searchResults = noteRepository.searchNotes(query.value)

    private val _uiState = MutableStateFlow<SearchNotesScreenUiState>(SearchNotesScreenUiState.Idle)
    val uiState = _uiState.asStateFlow()


//    val uiState = noteRepository.searchNotes(query.value)
//        .map {
//            if(it.isEmpty()){
//                SearchNotesScreenUiState.NoResultsFound
//            }else{
//                SearchNotesScreenUiState.ResultsFound(it)
//            }
//        }
//        .onStart { SearchNotesScreenUiState.Idle }
//        .catch { SearchNotesScreenUiState.Error(message = "An error occurred") }
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5000L),
//            initialValue = SearchNotesScreenUiState.Idle
//        )


    fun onChangeQuery(query:String){
        savedStateHandle["query"] = query
        viewModelScope.launch {
            try {
                if (query.isNotBlank()){
                    _uiState.value = SearchNotesScreenUiState.Searching
                    noteRepository.searchNotes(query).collectLatest {
                        if(it.isEmpty()){
                            _uiState.value = SearchNotesScreenUiState.NoResultsFound
                        }else{
                            _uiState.value = SearchNotesScreenUiState.ResultsFound(it)
                        }
                    }
                }else{
                    _uiState.value = SearchNotesScreenUiState.Idle
                }
            }catch (e:Throwable){
                _uiState.value = SearchNotesScreenUiState.Error(message = "An unexpected error occurred")
            }
        }

    }



}