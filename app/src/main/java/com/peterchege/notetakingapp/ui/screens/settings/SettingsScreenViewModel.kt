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
package com.peterchege.notetakingapp.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterchege.notetakingapp.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


sealed interface SettingsScreenUiState {
    object Loading:SettingsScreenUiState

    data class Success(val theme:String, val syncSetting:Boolean):SettingsScreenUiState

    data class Error(val message:String):SettingsScreenUiState
}
class SettingsScreenViewModel(
    val settingsRepository:SettingsRepository,
):ViewModel() {

    val uiState = combine(
        settingsRepository.getTheme(),
        settingsRepository.getSyncSetting()
    ){ theme,syncSetting ->
        SettingsScreenUiState.Success(theme = theme,syncSetting = syncSetting)
    }
        .onStart { SettingsScreenUiState.Loading }
        .catch { SettingsScreenUiState.Error(message = "Error loading settings") }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = SettingsScreenUiState.Loading
        )


    fun setTheme(theme:String){
        viewModelScope.launch {
            settingsRepository.setTheme(theme)
        }
    }

    fun setSyncSetting(syncSetting: Boolean){
        viewModelScope.launch {
            settingsRepository.setSyncSetting(syncSetting)
        }
    }
}