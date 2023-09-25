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
package com.peterchege.notetakingapp.data.remote

import com.peterchege.notetakingapp.core.api.NetworkResult
import com.peterchege.notetakingapp.core.api.requests.NoteBody
import com.peterchege.notetakingapp.core.api.requests.UpdateNoteBody
import com.peterchege.notetakingapp.core.api.responses.AddNoteResponse
import com.peterchege.notetakingapp.core.api.responses.DeleteNoteByIdResponse
import com.peterchege.notetakingapp.core.api.responses.GetNotesByUserIdResponse
import com.peterchege.notetakingapp.core.api.responses.UpdateNoteByIdResponse

interface RemoteNoteRepository {


    suspend fun getAllRemoteNotes(authorId: String): NetworkResult<GetNotesByUserIdResponse>

    suspend fun deleteNoteById(noteId: String): NetworkResult<DeleteNoteByIdResponse>
    suspend fun saveNoteRemote(noteBody:NoteBody): NetworkResult<AddNoteResponse>

    suspend fun updateNoteById(updateNote: UpdateNoteBody):NetworkResult<UpdateNoteByIdResponse>
}