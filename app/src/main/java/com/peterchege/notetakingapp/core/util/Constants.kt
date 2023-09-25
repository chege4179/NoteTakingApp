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
package com.peterchege.notetakingapp.core.util

import com.peterchege.notetakingapp.ui.theme.BabyBlue
import com.peterchege.notetakingapp.ui.theme.LightGreen
import com.peterchege.notetakingapp.ui.theme.RedOrange
import com.peterchege.notetakingapp.ui.theme.RedPink
import com.peterchege.notetakingapp.ui.theme.Violet

object Constants {
    const val BASE_URL = "https://note-app-server-q4lr.onrender.com"

    const val DATABASE_NAME = "note_app.db"

    const val LIGHT_MODE = "LIGHT_MODE"
    const val DARK_MODE = "DARK_MODE"

    const val USER_PREFERENCES = "USER_PREFERENCES"

    const val DATABASE_ID = "64aab9c9af59e88bf58b"
    const val COLLECTION_ID = "64aabcdef0860b34137d"

    const val NOTIFICATION_CHANNEL = "notification_channel"


    val noteBackgroundColors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
}