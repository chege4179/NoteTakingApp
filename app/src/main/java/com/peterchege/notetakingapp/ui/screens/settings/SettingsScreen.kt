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

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.peterchege.notetakingapp.core.util.Constants
import com.peterchege.notetakingapp.ui.components.ErrorComponent
import com.peterchege.notetakingapp.ui.components.LoadingComponent
import com.peterchege.notetakingapp.ui.components.SettingsInfoCard
import com.peterchege.notetakingapp.ui.screens.destinations.AuthScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel


@Destination
@Composable
fun SettingsScreen(
    navigator: DestinationsNavigator
) {
    val viewModel = getViewModel<SettingsScreenViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SettingsScreenContent(
        uiState = uiState,
        onChangeTheme = viewModel::setTheme,
        onChangeSyncSetting = viewModel::setSyncSetting,
        navigateToAuthScreen = { navigator.navigate(AuthScreenDestination) }
    )

}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsScreenContent(
    uiState: SettingsScreenUiState,
    onChangeTheme: (String) -> Unit,
    onChangeSyncSetting: (Boolean) -> Unit,
    navigateToAuthScreen: () -> Unit,
) {
    Scaffold(
//        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = Modifier.background(MaterialTheme.colorScheme.onBackground),
                title = {
                    Text(text = "App Settings")
                },
            )
        },

    ) {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .padding(paddingValues = it)
                .padding(10.dp)
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            when (uiState) {
                is SettingsScreenUiState.Loading -> {
                    LoadingComponent()
                }

                is SettingsScreenUiState.Error -> {
                    ErrorComponent(
                        message = uiState.message,
                        retryCallback = { }
                    )
                }

                is SettingsScreenUiState.Success -> {
                    SettingsInfoCard(
                        title = "Dark Theme",
                        checked = uiState.theme == Constants.DARK_MODE,
                        onCheckedChange = {
                            if (it) {
                                onChangeTheme(Constants.DARK_MODE)

                            } else {
                                onChangeTheme(Constants.LIGHT_MODE)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    SettingsInfoCard(
                        title = "Sync Notes To The Cloud",
                        checked = uiState.syncSetting,
                        onCheckedChange = {
                            if (it) {
                                onChangeSyncSetting(!it)
                            } else {
                                navigateToAuthScreen()
                            }


                        }
                    )
                }
            }

        }

    }

}