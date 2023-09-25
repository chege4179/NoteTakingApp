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
package com.peterchege.notetakingapp.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterchege.notetakingapp.core.api.NetworkResult
import com.peterchege.notetakingapp.core.api.requests.LoginBody
import com.peterchege.notetakingapp.core.api.requests.SignUpBody
import com.peterchege.notetakingapp.core.util.Constants
import com.peterchege.notetakingapp.core.util.UiEvent
import com.peterchege.notetakingapp.domain.repository.AuthRepository
import com.peterchege.notetakingapp.domain.repository.NetworkInfoRepository
import com.peterchege.notetakingapp.domain.repository.NetworkStatus
import com.peterchege.notetakingapp.domain.repository.SettingsRepository
import com.peterchege.notetakingapp.ui.screens.destinations.AllNotesScreenDestination
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class FormState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false
)

class AuthScreenViewModel(
    val authRepository: AuthRepository,
    val networkRepository: NetworkInfoRepository,
    val settingsRepository: SettingsRepository,

    ) : ViewModel() {

    val theme = settingsRepository.userSettings
        .map { it.theme }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = Constants.DARK_MODE
        )

    val networkStatus = networkRepository.networkStatus
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = NetworkStatus.Unknown
        )
    val authUser = authRepository.authUser
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )


    private val _formState = MutableStateFlow(FormState())
    val formState = _formState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    fun onChangeEmail(text: String) {
        _formState.value = _formState.value.copy(email = text)
    }

    fun onChangePassword(text: String) {
        _formState.value = _formState.value.copy(password = text)
    }

    fun onChangePasswordVisibilty() {
        _formState.value =
            _formState.value.copy(isPasswordVisible = !_formState.value.isPasswordVisible)
    }

    fun signUpUser() {
        _formState.value = _formState.value.copy(isLoading = true)
        viewModelScope.launch {
            val signUpBody = SignUpBody(
                email = _formState.value.email,
                password = _formState.value.password,
                fullName = "",
            )
            val response = authRepository.signUpUser(signUpBody)
            when (response) {
                is NetworkResult.Success -> {
                    _eventFlow.emit(UiEvent.ShowSnackbar(message = response.data.msg))
                    _formState.value = _formState.value.copy(isLoading = false)
                    if (response.data.success && response.data.user != null) {
                        authRepository.setAuthUser(response.data.user)
                        _eventFlow.emit(UiEvent.Navigate(route = AllNotesScreenDestination))
                    }
                }

                else -> {}
            }
        }
    }

    fun loginUser() {
        _formState.value = _formState.value.copy(isLoading = true)
        viewModelScope.launch {
            val loginBody = LoginBody(
                email = _formState.value.email,
                password = _formState.value.password
            )
            val response = authRepository.loginUser(loginBody)
            when(response){
                is NetworkResult.Success -> {
                    _formState.value = _formState.value.copy(isLoading = false)
                    _eventFlow.emit(UiEvent.ShowSnackbar(message = response.data.msg))
                    _formState.value = _formState.value.copy(isLoading = false)
                    if (response.data.success){
                        _eventFlow.emit(UiEvent.Navigate(route = AllNotesScreenDestination))
                    }
                }
                is NetworkResult.Error -> {
                    _formState.value = _formState.value.copy(isLoading = false)
                }
                is NetworkResult.Loading -> {

                }
            }
        }
    }
}