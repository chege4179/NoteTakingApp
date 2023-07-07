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

import com.peterchege.notetakingapp.core.room.database.NoteTakingAppDatabase
import com.peterchege.notetakingapp.domain.mappers.toEntity
import com.peterchege.notetakingapp.domain.mappers.toExternalListModel
import com.peterchege.notetakingapp.domain.mappers.toExternalModel
import com.peterchege.notetakingapp.domain.models.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalNoteRepositoryImpl(
    val db:NoteTakingAppDatabase
) :LocalNoteRepository {
    override fun getLocalNotes(): Flow<List<Note>> {
        return db.noteDao.getAllCachedNotes().map { it.toExternalListModel() }
    }

    override fun getLocalNoteById(noteId: String): Flow<Note?> {
       return db.noteDao.getCachedNoteById(noteId = noteId).map { it?.toExternalModel() }
    }

    override suspend fun deleteLocalNoteById() {
        return db.noteDao.deleteAllCachedNotes()
    }

    override suspend fun addNote(note: Note) {
        return db.noteDao.insertCachedNote(noteEntity = note.toEntity())
    }
}