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

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.R
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.peterchege.notetakingapp.core.util.UiEvent
import com.peterchege.notetakingapp.domain.repository.NetworkStatus
import com.peterchege.notetakingapp.ui.screens.destinations.AllNotesScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.getViewModel


@Destination(start = true)
@Composable
fun AuthScreen(
    navigator: DestinationsNavigator,

    ) {
    val viewModel = getViewModel<AuthScreenViewModel>()
    val networkStatus by viewModel.networkStatus.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val authUser by viewModel.authUser.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = true) {
        if (authUser != null) {
            navigator.navigate(AllNotesScreenDestination)
        }
    }

    AuthScreenContent(
        navigator = navigator,
        networkStatus = networkStatus,
        eventFlow = viewModel.eventFlow,
        formState = formState,
        onChangeEmail = { viewModel.onChangeEmail(it) },
        onChangePassword = { viewModel.onChangePassword(it) },
        signUpUser = { viewModel.signUpUser() },
        loginUser = { viewModel.loginUser() },
        onChangePasswordVisibility = { viewModel.onChangePasswordVisibilty() },
    )


}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AuthScreenContent(
    navigator: DestinationsNavigator,
    networkStatus: NetworkStatus,
    eventFlow: SharedFlow<UiEvent>,
    formState: FormState,
    onChangeEmail: (String) -> Unit,
    onChangePassword: (String) -> Unit,
    signUpUser: () -> Unit,
    loginUser: () -> Unit,
    onChangePasswordVisibility: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {

        val snackbarHostState = SnackbarHostState()
        val keyboardController = LocalSoftwareKeyboardController.current

//        LaunchedEffect(key1 = networkStatus) {
//            when (networkStatus) {
//                is NetworkStatus.Unknown -> {
//
//                }
//
//                is NetworkStatus.Connected -> {
////                    snackbarHostState.showSnackbar(
////                        message = "Connected"
////                    )
//                }
//
//                is NetworkStatus.Disconnected -> {
//                    snackbarHostState.showSnackbar(
//                        message = "You are offline"
//                    )
//                }
//            }
//        }

        LaunchedEffect(key1 = true) {
            eventFlow.collectLatest { event ->
                when (event) {
                    is UiEvent.ShowSnackbar -> {
                        snackbarHostState.showSnackbar(
                            message = event.message
                        )
                    }

                    is UiEvent.Navigate -> {
                        navigator.navigate(event.route)

                    }
                }
            }
        }

        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
            ) {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Note Taking App",
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                    )

                    Spacer(modifier = Modifier.height(15.dp))
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = formState.email,
                        onValueChange = {
                            onChangeEmail(it)
                            //state.username = it
                        },
                        label = { Text("Email Address") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = formState.password,
                        onValueChange = {
                            onChangePassword(it)
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = if (!formState.isPasswordVisible)
                                    Icons.Filled.Visibility
                                else
                                    Icons.Filled.VisibilityOff,
                                contentDescription = "Visibility on",
                                modifier = Modifier
                                    .size(26.dp)
                                    .clickable {
                                        onChangePasswordVisibility()
                                    }
                            )

                        },
                        visualTransformation = if (!formState.isPasswordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        label = { Text("Password") }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        onClick = {
                            keyboardController?.hide()
                            loginUser()

                        }
                    ) {
                        Text("Login")
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        onClick = {
                            keyboardController?.hide()
                            signUpUser()

                        }
                    )
                    {
                        Text("Sign Up")
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                }
                if (formState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

            }

        }
    }


}