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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterchege.notetakingapp.core.api.responses.Note
import com.peterchege.notetakingapp.core.api.responses.User
import com.peterchege.notetakingapp.core.work.sync_notes.SyncNotesWorkManager
import com.peterchege.notetakingapp.domain.repository.AuthRepository
import com.peterchege.notetakingapp.domain.repository.OfflineFirstNoteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


sealed interface AllNotesScreenUiState {
    object Loading : AllNotesScreenUiState

    data class Success(
        val notes: List<Note>,
        val authUser: User?,
    ) : AllNotesScreenUiState

    data class Error(val message: String) : AllNotesScreenUiState
}

class AllNotesScreenViewModel(
    val noteRepository: OfflineFirstNoteRepository,
    val syncNotesWorkManager: SyncNotesWorkManager,
    val authRepository: AuthRepository,
) : ViewModel() {

    val isSyncing = syncNotesWorkManager.isSyncing
        .onStart { emit(false) }
        .catch { emit(false) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = false
        )

    val uiState = combine(
        noteRepository.getAllNotes(),
        authRepository.authUser
    ) { notes, authUser ->
        AllNotesScreenUiState.Success(notes = notes, authUser = authUser)
    }
        .onStart { AllNotesScreenUiState.Loading }
        .catch { AllNotesScreenUiState.Error(message = "An unexpected error occurred") }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = AllNotesScreenUiState.Loading,
        )

    fun syncNotes(authorId: String) {
        viewModelScope.launch {
            syncNotesWorkManager.startSyncingNotes(authorId)
        }
    }


    fun deleteNote(noteId: String) {
        viewModelScope.launch {
            noteRepository.deleteNoteById(noteId = noteId)
        }
    }


}