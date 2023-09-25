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

import com.peterchege.notetakingapp.core.api.NetworkResult
import com.peterchege.notetakingapp.core.api.NoteService
import com.peterchege.notetakingapp.core.api.requests.LoginBody
import com.peterchege.notetakingapp.core.api.requests.SignUpBody
import com.peterchege.notetakingapp.core.api.responses.LoginResponse
import com.peterchege.notetakingapp.core.api.responses.SignUpResponse
import com.peterchege.notetakingapp.core.api.responses.User
import com.peterchege.notetakingapp.core.datastore.repository.DefaultAuthProvider
import com.peterchege.notetakingapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class AuthRepositoryImpl(
    private val defaultAuthProvider: DefaultAuthProvider,
    private val noteService: NoteService,
) : AuthRepository {

    override val authUser: Flow<User?> = defaultAuthProvider.authUser

    override val isUserLoggedIn: Flow<Boolean> = defaultAuthProvider.isUserLoggedIn

    override suspend fun loginUser(loginBody: LoginBody): NetworkResult<LoginResponse> {
        return noteService.loginUser(loginBody = loginBody)
    }

    override suspend fun setAuthUser(user: User?) {
        if (user == null){
            defaultAuthProvider.unsetLoggedInUser()
        }else{
            defaultAuthProvider.setLoggedInUser(user)
        }
    }

    override suspend fun signUpUser(signUpBody: SignUpBody): NetworkResult<SignUpResponse> {
        return noteService.signUpUser(signUpBody = signUpBody)
    }

    override suspend fun signOutUser() {
        defaultAuthProvider.unsetLoggedInUser()
    }

}