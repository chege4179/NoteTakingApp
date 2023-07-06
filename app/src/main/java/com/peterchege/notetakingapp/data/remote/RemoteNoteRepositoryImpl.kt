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

import com.peterchege.notetakingapp.domain.models.Note
import kotlinx.coroutines.flow.Flow

class RemoteNoteRepositoryImpl(

):RemoteNoteRepository {
    override fun getAllRemoteNotes(authorId: String): Flow<List<Note>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllNotes(authorId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNoteById(noteId: String) {
        TODO("Not yet implemented")
    }


}