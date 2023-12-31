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
package com.peterchege.notetakingapp.data

import com.peterchege.notetakingapp.core.datastore.repository.DefaultSettingsProvider
import com.peterchege.notetakingapp.domain.models.UserSettings
import com.peterchege.notetakingapp.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class SettingsRepositoryImpl(
    val defaultSettingsProvider: DefaultSettingsProvider,
) : SettingsRepository {

    override val userSettings: Flow<UserSettings> =
        combine(
            defaultSettingsProvider.getTheme(),
            defaultSettingsProvider.getSyncSetting()
        ) { theme, syncSetting ->
            UserSettings(theme = theme, syncSetting = syncSetting)
        }

    override suspend fun setTheme(themeValue: String) {
        return defaultSettingsProvider.setTheme(themeValue = themeValue)
    }

    override suspend fun setSyncSetting(syncSetting: Boolean) {
        return defaultSettingsProvider.setSyncSetting(syncSetting)
    }


}