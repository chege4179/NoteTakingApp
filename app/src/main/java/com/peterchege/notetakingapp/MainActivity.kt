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
package com.peterchege.notetakingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.work.Configuration
import androidx.work.WorkerFactory
import com.peterchege.notetakingapp.core.util.Constants
import com.peterchege.notetakingapp.ui.screens.NavGraphs
import com.peterchege.notetakingapp.ui.screens.destinations.AllNotesScreenDestination
import com.peterchege.notetakingapp.ui.screens.login.LoginScreenViewModel
import com.peterchege.notetakingapp.ui.theme.NoteTakingAppTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity(), Configuration.Provider {

    private val viewModel by inject<LoginScreenViewModel>()
    private val workerFactory: WorkerFactory by inject()

    override val workManagerConfiguration: Configuration = Configuration.Builder()
        .setMinimumLoggingLevel(android.util.Log.DEBUG)
        .setWorkerFactory(workerFactory)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val authUser by viewModel.authUser.collectAsStateWithLifecycle()
            val theme by viewModel.theme.collectAsStateWithLifecycle()
            NoteTakingAppTheme(
                darkTheme = theme == Constants.DARK_MODE
            ) {
                // A surface container using the 'background' color from the theme
                Surface(

                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DestinationsNavHost(
                        navGraph = NavGraphs.root,
                        startRoute = AllNotesScreenDestination
                    )
                }
            }
        }
    }
}

