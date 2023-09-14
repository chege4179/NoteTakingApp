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

import com.peterchege.notetakingapp.MainDispatcherRule
import com.peterchege.notetakingapp.fake.FakeSettingsRepository
import com.peterchege.notetakingapp.ui.screens.settings.SettingsScreenUiState
import com.peterchege.notetakingapp.ui.screens.settings.SettingsScreenViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertIs

@Config(manifest= Config.NONE)
@RunWith(RobolectricTestRunner::class)
class SettingsScreenViewModelTest {

    private val fakeSettingsRepository = FakeSettingsRepository()
    private lateinit var viewModel: SettingsScreenViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        viewModel = SettingsScreenViewModel(
            settingsRepository = fakeSettingsRepository
        )
    }

    @Test
    fun test_stateFlow_is_collected_correctly() = runTest {
        assertIs<SettingsScreenUiState.Loading>(viewModel.uiState.value)
        val collectJob = launch(mainDispatcherRule.testDispatcher) {
            viewModel.uiState.collect {}
        }
        assertIs<SettingsScreenUiState.Success>(viewModel.uiState.value)

        collectJob.cancel()
    }
}