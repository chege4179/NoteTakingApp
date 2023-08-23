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

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.peterchege.notetakingapp.core.datastore.repository.DefaultSettingsProvider
import com.peterchege.notetakingapp.core.datastore.repository.DefaultSettingsProviderImpl
import com.peterchege.notetakingapp.core.util.Constants
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val datastoreModule = module {

    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create(
            produceFile = {
                androidContext().preferencesDataStoreFile(Constants.USER_PREFERENCES)
            }
        )
    }

    single<DefaultSettingsProvider>{
        DefaultSettingsProviderImpl(dataStore = get())
    }



}