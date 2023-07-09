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
package com.peterchege.notetakingapp.core.di

import com.peterchege.notetakingapp.ui.screens.add_note.AddNoteScreenViewModel
import com.peterchege.notetakingapp.ui.screens.all_notes.AllNotesScreenViewModel
import com.peterchege.notetakingapp.ui.screens.auth.AuthScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel<AuthScreenViewModel> {
        AuthScreenViewModel(
            authRepository = get(),
            networkRepository = get(),
            settingsRepository = get(),
        )
    }
    viewModel<AllNotesScreenViewModel> {
        AllNotesScreenViewModel(
            noteRepository = get()
        )
    }
    viewModel {
        AddNoteScreenViewModel(
            authRepository = get(),
            noteRepository = get(),
            dispatcherProvider = get(),

        )
    }
}