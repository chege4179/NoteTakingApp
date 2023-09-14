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
package com.peterchege.notetakingapp.presentation

import androidx.lifecycle.SavedStateHandle
import com.peterchege.notetakingapp.MainDispatcherRule
import com.peterchege.notetakingapp.fake.FakeOfflineFirstNoteRepository
import com.peterchege.notetakingapp.ui.screens.all_notes.AllNotesScreenUiState
import com.peterchege.notetakingapp.ui.screens.note.NoteScreenUiState
import com.peterchege.notetakingapp.ui.screens.note.NoteScreenViewModel
import com.peterchege.notetakingapp.ui.screens.search.SearchNoteScreenViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertIs


@RunWith(RobolectricTestRunner::class)
class NoteScreenViewModelTest {

    private val fakeNotesRepo = FakeOfflineFirstNoteRepository()
    lateinit var fakeSavedStateHandle : SavedStateHandle
    private lateinit var viewModel : NoteScreenViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()



    @Test
    fun verify_state_is_found_when_a_note_is_found() = runTest {
        viewModel = NoteScreenViewModel(
            noteRepository = fakeNotesRepo,
            savedStateHandle = SavedStateHandle().apply {
                set("noteId","1")
            }
        )
        assertIs<NoteScreenUiState.Loading>(viewModel.uiState.value)
        val collectJob = launch(mainDispatcherRule.testDispatcher) {
            viewModel.uiState.collect {}
        }
        assertIs<NoteScreenUiState.Success>(viewModel.uiState.value)
        collectJob.cancel()
    }

    @Test
    fun verify_state_is_not_found_when_a_note_is_not_found() = runTest {
        viewModel = NoteScreenViewModel(
            noteRepository = fakeNotesRepo,
            savedStateHandle = SavedStateHandle().apply {
                set("noteId","4")
            }
        )
        assertIs<NoteScreenUiState.Loading>(viewModel.uiState.value)
        val collectJob = launch(mainDispatcherRule.testDispatcher) {
            viewModel.uiState.collect {}
        }
        assertIs<NoteScreenUiState.Error>(viewModel.uiState.value)
        collectJob.cancel()
    }
}