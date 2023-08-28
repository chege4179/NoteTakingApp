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
package com.peterchege.notetakingapp.fake

import com.peterchege.notetakingapp.domain.models.AuthResult
import com.peterchege.notetakingapp.domain.models.User
import com.peterchege.notetakingapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class FakeAuthRepository :AuthRepository {
    override fun getAuthUser(): Flow<User?> {
        return flowOf(User(email = "peterkagure@gmail.com",name = "Peter Chege", userId = "3",imageUrl = ""))
    }

    override fun loginUser(email: String, password: String): Flow<AuthResult> {
        TODO("Not yet implemented")
    }

    override fun signUpUser(email: String, password: String): Flow<AuthResult> {
        TODO("Not yet implemented")
    }

    override fun signOutUser() {
        TODO("Not yet implemented")
    }

}