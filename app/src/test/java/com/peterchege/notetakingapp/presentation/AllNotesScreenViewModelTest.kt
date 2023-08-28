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

import app.cash.turbine.test
import com.peterchege.notetakingapp.MainDispatcherRule
import com.peterchege.notetakingapp.fake.FakeAuthRepository
import com.peterchege.notetakingapp.fake.FakeOfflineFirstNoteRepository
import com.peterchege.notetakingapp.fake.FakeSyncNotesWorkManager
import com.peterchege.notetakingapp.ui.screens.all_notes.AllNotesScreenUiState
import com.peterchege.notetakingapp.ui.screens.all_notes.AllNotesScreenViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertIs

@RunWith(RobolectricTestRunner::class)
class AllNotesScreenViewModelTest {

    private val fakeNotesRepo = FakeOfflineFirstNoteRepository()
    private val fakeSyncNotesManager = FakeSyncNotesWorkManager()
    private val fakeAuthRepository = FakeAuthRepository()

    private lateinit var viewModel: AllNotesScreenViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        viewModel = AllNotesScreenViewModel(
            noteRepository = fakeNotesRepo,
            syncNotesWorkManager = fakeSyncNotesManager,
            authRepository = fakeAuthRepository,
        )
    }

    @Test
    fun test_stateFlow_is_collected_correctly() = runTest {
        assertIs<AllNotesScreenUiState.Loading>(viewModel.uiState.value)
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }
        assertIs<AllNotesScreenUiState.Success>(viewModel.uiState.value)

        collectJob.cancel()
    }

    @Test
    fun test_note_will_be_removed_when_deleted() = runTest {
        viewModel.uiState.test {
            val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect {} }
            val state = awaitItem()
            assertIs<AllNotesScreenUiState.Success>(state)
            assertEquals(expected = 3, actual = state.notes.size)
            viewModel.deleteNote("1")

            assertEquals(expected = 2, actual = state.notes.size)
            collectJob.cancel()
        }


    }
}