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
import app.cash.turbine.test
import com.peterchege.notetakingapp.MainDispatcherRule
import com.peterchege.notetakingapp.fake.FakeOfflineFirstNoteRepository
import com.peterchege.notetakingapp.fake.note3
import com.peterchege.notetakingapp.ui.screens.all_notes.AllNotesScreenViewModel
import com.peterchege.notetakingapp.ui.screens.search.SearchNoteScreenViewModel
import com.peterchege.notetakingapp.ui.screens.search.SearchNotesScreenUiState
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertIs

@RunWith(RobolectricTestRunner::class)
class SearchScreenViewModelTest {

    private val fakeNotesRepo = FakeOfflineFirstNoteRepository()
    private val fakeSavedStateHandle :SavedStateHandle = SavedStateHandle().apply {
        set("query","")
    }
    private lateinit var viewModel : SearchNoteScreenViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp(){
        viewModel = SearchNoteScreenViewModel(
            noteRepository = fakeNotesRepo,
            savedStateHandle = fakeSavedStateHandle
        )
    }

    @Test
    fun verify_initial_state_is_loading() = runTest {
        assert(viewModel.uiState.value is SearchNotesScreenUiState.Idle)
    }

    @Test
    fun verify_state_will_be_idle_given_an_empty_string() = runTest{
        viewModel.onChangeQuery("")
        assert(viewModel.uiState.value is SearchNotesScreenUiState.Idle)
    }

    @Test
    fun verify_state_will_be_searching_given_a_non_empty_string_that_has_results() = runTest {
        viewModel.uiState.test {
            assertIs<SearchNotesScreenUiState.Idle>(awaitItem())
            viewModel.onChangeQuery("Wo")
            assertIs<SearchNotesScreenUiState.Searching>(awaitItem())
            val state = awaitItem()
            assertIs<SearchNotesScreenUiState.ResultsFound>(state)
            assertEquals(state.notes[0].noteTitle, note3.noteTitle)
        }
    }
    @Test
    fun verify_state_will_be_searching_given_a_non_empty_string_that_has_no_results() = runTest {
        viewModel.uiState.test {
            assert(awaitItem() is SearchNotesScreenUiState.Idle)
            viewModel.onChangeQuery("ffff")
            assert(awaitItem() is SearchNotesScreenUiState.Searching)
            assert(awaitItem() is SearchNotesScreenUiState.NoResultsFound)
        }
    }
    @Test
    fun verify_query_is_saved_in_saved_state_handle() = runTest {
        assertEquals("",viewModel.query.value)
        viewModel.onChangeQuery("note")
        assertEquals(expected = "note",viewModel.query.value)

    }
}