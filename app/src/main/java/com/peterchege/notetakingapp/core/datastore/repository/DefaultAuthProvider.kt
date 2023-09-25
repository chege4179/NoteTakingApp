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

import android.content.Context
import androidx.datastore.dataStore
import com.peterchege.notetakingapp.core.api.responses.User
import com.peterchege.notetakingapp.core.datastore.serializers.AuthUserInfoSerializer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.userDataStore by dataStore("user.json", AuthUserInfoSerializer)

interface DefaultAuthProvider {

    val isUserLoggedIn: Flow<Boolean>

    val authUser:Flow<User?>

    suspend fun setLoggedInUser(user: User)

    suspend fun unsetLoggedInUser()
}


class DefaultAuthProviderImpl(
    private val context: Context
) :DefaultAuthProvider{
    override val isUserLoggedIn: Flow<Boolean> =
        context.userDataStore.data.map { user ->
            if(user == null){
                false
            }else{
                user.userId != ""
            }
        }

    override val authUser:Flow<User?> = context.userDataStore.data

    override suspend fun setLoggedInUser(user: User) {
        context.userDataStore.updateData {
            user
        }
    }
    override suspend fun unsetLoggedInUser() {
        context.userDataStore.updateData {
            null
        }
    }
}