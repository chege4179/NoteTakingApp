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
package com.peterchege.notetakingapp.data.local

import com.peterchege.notetakingapp.core.api.responses.Note
import com.peterchege.notetakingapp.core.room.database.NoteTakingAppDatabase
import com.peterchege.notetakingapp.core.util.DispatcherProvider
import com.peterchege.notetakingapp.domain.mappers.toEntity
import com.peterchege.notetakingapp.domain.mappers.toExternalListModel
import com.peterchege.notetakingapp.domain.mappers.toExternalModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class LocalNoteRepositoryImpl(
    val db: NoteTakingAppDatabase,
    val defaultDispatcherProvider: DispatcherProvider,
) : LocalNoteRepository {
    override fun getLocalNotes(): Flow<List<Note>> {
        return db.noteDao.getAllCachedNotes()
            .map { it.toExternalListModel() }
            .flowOn(defaultDispatcherProvider.io)
    }

    override fun searchNotes(query: String): Flow<List<Note>> {
        return db.noteDao.searchNotes(query = query)
            .map { it.toExternalListModel() }
            .flowOn(defaultDispatcherProvider.io)
    }

    override fun getLocalNoteById(noteId: String): Flow<Note?> {
        return db.noteDao.getCachedNoteById(noteId = noteId)
            .map { it?.toExternalModel() }
            .flowOn(defaultDispatcherProvider.io)
    }

    override suspend fun deleteLocalNoteById(noteId: String) {
        withContext(defaultDispatcherProvider.io) {
            db.noteDao.deleteNoteById(noteId = noteId)
        }

    }

    override suspend fun addNote(note: Note) {
        withContext(defaultDispatcherProvider.io) {
            db.noteDao.insertCachedNote(noteEntity = note.toEntity(isInSync = false))
        }
    }

    override suspend fun getNotesBySyncStatus(isInSync: Boolean): List<Note> {
        return db.noteDao.getNotesBySyncStatus(isInSync = isInSync).toExternalListModel()
    }

    override suspend fun updateNoteAuthorId(noteAuthorId: String) {
        return db.noteDao.updateNotesAuthorId(noteAuthorId = noteAuthorId)
    }

    override suspend fun updateNoteSyncStatus(syncStatus: Boolean) {
        return db.noteDao.updateNoteSyncStatus(syncStatus)
    }
}