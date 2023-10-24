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

interface NoteService {

    suspend fun loginUser(loginBody: LoginBody):NetworkResult<LoginResponse>

    suspend fun signUpUser(signUpBody: SignUpBody):NetworkResult<SignUpResponse>


    suspend fun addNote(noteBody: NoteBody):NetworkResult<AddNoteResponse>

    suspend fun getNotesByUserId(userId:String):NetworkResult<GetNotesByUserIdResponse>

    suspend fun updateNote(updateNote:UpdateNoteBody):NetworkResult<UpdateNoteByIdResponse>

    suspend fun deleteNoteById(noteId:String):NetworkResult<DeleteNoteByIdResponse>

    suspend fun deleteAllNotesById(userId:String):NetworkResult<DeleteNoteByIdResponse>

}