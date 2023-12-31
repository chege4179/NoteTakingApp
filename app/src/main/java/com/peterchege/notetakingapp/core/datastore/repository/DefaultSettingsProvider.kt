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
package com.peterchege.notetakingapp.core.datastore.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.peterchege.notetakingapp.core.datastore.repository.PreferencesKey.SYNC_SETTING
import com.peterchege.notetakingapp.core.datastore.repository.PreferencesKey.THEME_OPTIONS
import com.peterchege.notetakingapp.core.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


interface DefaultSettingsProvider {
    suspend fun setTheme(themeValue: String)

    fun getTheme(): Flow<String>

    suspend fun setSyncSetting(syncSetting:Boolean)

    fun getSyncSetting():Flow<Boolean>
}

private object PreferencesKey {
    val THEME_OPTIONS = stringPreferencesKey(name = "theme_options")

    val SYNC_SETTING = booleanPreferencesKey(name ="sync_setting")
}
class DefaultSettingsProviderImpl(
    val dataStore: DataStore<Preferences>
):DefaultSettingsProvider {

    override suspend fun setTheme(themeValue: String) {
        dataStore.edit { preferences ->
            preferences[THEME_OPTIONS] = themeValue
        }
    }
    override fun getTheme(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[THEME_OPTIONS] ?: Constants.DARK_MODE
        }
    }

    override suspend fun setSyncSetting(syncSetting: Boolean) {
        dataStore.edit { preferences ->
            preferences[SYNC_SETTING] = syncSetting
        }
    }

    override fun getSyncSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[SYNC_SETTING] ?: false
        }
    }
}