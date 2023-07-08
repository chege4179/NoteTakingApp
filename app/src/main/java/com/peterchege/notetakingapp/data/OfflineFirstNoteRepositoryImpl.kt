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

import com.peterchege.notetakingapp.core.util.DispatcherProvider
import com.peterchege.notetakingapp.data.local.LocalNoteRepository
import com.peterchege.notetakingapp.data.remote.RemoteNoteRepository
import com.peterchege.notetakingapp.domain.models.Note
import com.peterchege.notetakingapp.domain.repository.OfflineFirstNoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class OfflineFirstNoteRepositoryImpl (
    val localNoteRepository: LocalNoteRepository,
    val remoteNoteRepository: RemoteNoteRepository,
    val dispatcherProvider: DispatcherProvider
):OfflineFirstNoteRepository {

    override fun getAllNotes(): Flow<List<Note>> {
        return localNoteRepository.getLocalNotes().flowOn(dispatcherProvider.io)
    }

    override suspend fun addNote(note: Note) {
        withContext(dispatcherProvider.io){
            localNoteRepository.addNote(note = note)
        }
    }

    override suspend fun deleteNoteById(noteId: String) {
        withContext(dispatcherProvider.io){
            localNoteRepository.deleteLocalNoteById(noteId = noteId)
        }
    }

}