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

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterchege.notetakingapp.data.local.LocalNoteRepository
import com.peterchege.notetakingapp.domain.models.Note
import com.peterchege.notetakingapp.domain.repository.OfflineFirstNoteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


sealed interface NoteScreenUiState {
    data class Success(val note: Note):NoteScreenUiState

    object Loading:NoteScreenUiState

    data class Error(val message:String):NoteScreenUiState

}
class NoteScreenViewModel (
    val savedStateHandle:SavedStateHandle,
    val noteRepository: OfflineFirstNoteRepository
): ViewModel() {
    private val noteId = savedStateHandle.get<String>("noteId") ?: ""


    val uiState = noteRepository.getNoteById(noteId)
        .map { note ->
            if (note == null){
                NoteScreenUiState.Error(message = "Note not found")
            }else{
                NoteScreenUiState.Success(note = note)
            }
        }
        .onStart { NoteScreenUiState.Loading }
        .catch { NoteScreenUiState.Error(message ="An unexpected error occurred") }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = NoteScreenUiState.Loading
        )

    fun deleteNote(){
        viewModelScope.launch{
            noteRepository.deleteNoteById(noteId)
        }
    }


}