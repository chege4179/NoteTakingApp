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

import com.google.firebase.auth.FirebaseAuth
import com.peterchege.notetakingapp.core.work.sync_notes.SyncNotesWorkManager
import com.peterchege.notetakingapp.domain.models.AuthResult
import com.peterchege.notetakingapp.domain.models.User
import com.peterchege.notetakingapp.domain.repository.AuthRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow

class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
    private val syncNotesWorkManager: SyncNotesWorkManager,

    ) : AuthRepository {
    override fun getAuthUser(): Flow<User?> = flow {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            emit(null)
        } else {
            val uris =  currentUser.providerData.map {

                it.photoUrl.toString()
            }
            val uri = if (uris.isEmpty()) "" else uris[1]

            val user = User(
                name = currentUser.displayName ?: "",
                email = currentUser.email ?: "",
                userId = currentUser.uid,
                imageUrl = uri,
            )
            emit(user)
        }
    }

    override fun loginUser(email: String, password: String): Flow<AuthResult> = callbackFlow {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(
                        AuthResult(msg = "Sign In Successful", success = true)
                    )
                    val currentUser = auth.currentUser
                    if (currentUser != null){
                        syncNotesWorkManager.startSyncingNotes(currentUser.uid)
                    }
                } else {
                    trySend(
                        AuthResult(
                            msg = task.exception?.message ?: "Authentication failed.",
                            success = false
                        )
                    )

                }
            }

        awaitClose {  }

    }

    override fun signUpUser(email: String, password: String): Flow<AuthResult> = callbackFlow {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    trySend(
                        AuthResult(
                            msg = "Sign Up Successful", success = true)
                    )
                } else {
                    println("Task, ${task.exception?.message}")
                    trySend(
                        AuthResult(
                            msg = task.exception?.message ?: "Authentication failed.",
                            success = false
                        )
                    )

                }
            }
        awaitClose {  }

    }

    override fun signOutUser() {
        auth.signOut()

    }

}