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
package com.peterchege.notetakingapp.core.api

import com.peterchege.notetakingapp.core.api.requests.LoginBody
import com.peterchege.notetakingapp.core.api.requests.NoteBody
import com.peterchege.notetakingapp.core.api.requests.SignUpBody
import com.peterchege.notetakingapp.core.api.requests.UpdateNoteBody
import com.peterchege.notetakingapp.core.api.responses.AddNoteResponse
import com.peterchege.notetakingapp.core.api.responses.DeleteNoteByIdResponse
import com.peterchege.notetakingapp.core.api.responses.GetNotesByUserIdResponse
import com.peterchege.notetakingapp.core.api.responses.LoginResponse
import com.peterchege.notetakingapp.core.api.responses.SignUpResponse
import com.peterchege.notetakingapp.core.api.responses.UpdateNoteByIdResponse
import com.peterchege.notetakingapp.core.util.Constants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.util.InternalAPI

@OptIn(InternalAPI::class)
class NoteServiceImpl(
    val client: HttpClient,
) : NoteService {

    override suspend fun loginUser(loginBody: LoginBody): NetworkResult<LoginResponse> {
        return safeApiCall {
            client.post{
                url(urlString = "${Constants.BASE_URL}/user/login")
                contentType(ContentType.Application.Json)
                body = loginBody
            }.body()
        }
    }

    override suspend fun signUpUser(signUpBody: SignUpBody): NetworkResult<SignUpResponse> {
        return safeApiCall {
            client.post{
                url(urlString = "${Constants.BASE_URL}/user/signup")
                contentType(ContentType.Application.Json)
                body = signUpBody
            }.body()
        }
    }

    override suspend fun addNote(noteBody: NoteBody): NetworkResult<AddNoteResponse> {
        return safeApiCall {
            client.post{
                url(urlString = "${Constants.BASE_URL}/note/add")
                contentType(ContentType.Application.Json)
                body = noteBody
            }.body()
        }
    }

    override suspend fun deleteNoteById(noteId: String): NetworkResult<DeleteNoteByIdResponse> {
        return safeApiCall {
            client.delete{
                url(urlString = "${Constants.BASE_URL}/note/delete/${noteId}")
                contentType(ContentType.Application.Json)
            }.body()
        }
    }

    override suspend fun getNotesByUserId(userId: String): NetworkResult<GetNotesByUserIdResponse> {
        return safeApiCall {
            client.get {
                url(urlString = "${Constants.BASE_URL}/note/all/${userId}")
                contentType(ContentType.Application.Json)
            }.body()
        }
    }

    override suspend fun updateNote(updateNote: UpdateNoteBody): NetworkResult<UpdateNoteByIdResponse> {
        return safeApiCall {
            client.put{
                url(urlString = "${Constants.BASE_URL}/note/update")
                contentType(ContentType.Application.Json)
                body = updateNote
            }.body()
        }
    }
}