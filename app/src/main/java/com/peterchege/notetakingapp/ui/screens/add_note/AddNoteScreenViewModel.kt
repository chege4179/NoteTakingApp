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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterchege.notetakingapp.core.util.DispatcherProvider
import com.peterchege.notetakingapp.core.util.UiEvent
import com.peterchege.notetakingapp.core.util.generateFormatDate
import com.peterchege.notetakingapp.core.work.sync_notes.SyncNotesWorkManager
import com.peterchege.notetakingapp.domain.models.Note
import com.peterchege.notetakingapp.domain.models.User
import com.peterchege.notetakingapp.domain.repository.AuthRepository
import com.peterchege.notetakingapp.domain.repository.OfflineFirstNoteRepository
import com.peterchege.notetakingapp.ui.screens.destinations.AllNotesScreenDestination
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID


data class NoteFormState(
    val noteTitle: String = "",
    val noteContent: String = "",
    val noteColor: Int? = null,
    val isNoteTitleHintVisible:Boolean = true,
    val isNoteContentHintVisible:Boolean = true

    )

class AddNoteScreenViewModel(
    val dispatcherProvider: DispatcherProvider,
    val authRepository: AuthRepository,
    val noteRepository: OfflineFirstNoteRepository,

    ) : ViewModel() {

    val authUser = authRepository.getAuthUser()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )


    private val _noteState = MutableStateFlow(NoteFormState())
    val noteState = _noteState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    fun onChangeNoteTitle(noteTitle: String) {
        _noteState.value = _noteState.value.copy(noteTitle = noteTitle)
    }

    fun onChangeNoteContent(noteContent: String) {
        _noteState.value = _noteState.value.copy(noteContent = noteContent)
    }

    fun onChangeNoteColor(noteColor: Int) {
        _noteState.value = _noteState.value.copy(noteColor = noteColor)
    }
    fun onChangeNoteTitleHintVisiblity(visibility:Boolean){
        _noteState.value = _noteState.value.copy(isNoteTitleHintVisible = visibility)
    }
    fun onChangeNoteContentHintVisiblity(visibility:Boolean){
        _noteState.value = _noteState.value.copy(isNoteContentHintVisible = visibility)
    }

    fun saveNote(authUser: User?) {

        viewModelScope.launch {
            val noteId = withContext(dispatcherProvider.io) {
                UUID.randomUUID().toString()
            }

            val note = Note(
                noteId = noteId,
                noteContent = _noteState.value.noteContent,
                noteTitle = _noteState.value.noteTitle,
                noteCreatedAt = "",
                noteCreatedOn = generateFormatDate(date = LocalDate.now()),
                noteColor = _noteState.value.noteColor ?: 0,
                noteAuthorId = authUser?.userId ?: "",
                isInSync = true,

            )
            try {
                noteRepository.addNote(note = note)
                _eventFlow.emit(UiEvent.ShowSnackbar(message = "Note saved successfully"))
                _noteState.value = _noteState.value.copy(
                    noteTitle = "",
                    noteContent = "",
                )
                _eventFlow.emit(UiEvent.Navigate(AllNotesScreenDestination))
            }catch (e:Throwable){
                Timber.tag("Note creation error").d(e)
                _eventFlow.emit(UiEvent.ShowSnackbar(message =
                e.message ?: "An error occurred while saving the note"))
            }

        }
    }

}