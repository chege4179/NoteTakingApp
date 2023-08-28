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

import com.peterchege.notetakingapp.domain.models.Note
import com.peterchege.notetakingapp.domain.repository.OfflineFirstNoteRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.atomic.AtomicBoolean

class FakeOfflineFirstNoteRepository : OfflineFirstNoteRepository {

    private val notes = mutableListOf<Note>(note1, note2, note3)

    override fun getAllNotes(): Flow<List<Note>> = flow {
        emit(notes)
    }

    override fun getNoteById(noteId: String): Flow<Note?> = flow {
        val note = notes.find { it.noteId == noteId }
        emit(note)
    }

    override fun searchNotes(query: String): Flow<List<Note>> = flow {
        val filteredNotes = notes.filter { it.noteTitle.contains(query, ignoreCase = true) }
        emit(filteredNotes)
    }

    override suspend fun addNote(note: Note) {
        notes.add(note)
    }

    override suspend fun deleteNoteById(noteId: String) {
        notes.removeAll { it.noteId == noteId }
    }

    override suspend fun syncNote(noteId: String) {
        delay(500)
    }

    override suspend fun updateSyncStatus(syncStatus: Boolean) {
        // Simulate updating sync status
        delay(500)
    }
}


val note1 = Note(
    noteId = "1",
    noteTitle = "Grocery List",
    noteContent = "Milk, eggs, bread, cheese, apples",
    noteColor = 9,
    noteAuthorId = "1234567890",
    noteCreatedAt = "2023-07-21T11:02:15Z",
    noteCreatedOn = "2023-07-21",
    isInSync = true,
    isDeleted = false,
)
val note2 = Note(
    noteId = "2",
    noteTitle = "To-Do List",
    noteContent = "Do laundry, clean the house, go to the gym",
    noteColor = 9,
    noteAuthorId = "1234567891",
    noteCreatedAt = "2023-07-20T11:02:15Z",
    noteCreatedOn = "2023-07-20",
    isInSync = true,
    isDeleted = false,
)
val note3 = Note(
    noteId = "3",
    noteTitle = "Work Notes",
    noteContent = "Meeting with client at 10am, presentation at 2pm",
    noteColor = 8,
    noteAuthorId = "1234567892",
    noteCreatedAt = "2023-07-19T11:02:15Z",
    noteCreatedOn = "2023-07-19",
    isInSync = true,
    isDeleted = false,
)


