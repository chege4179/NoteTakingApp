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
package com.peterchege.notetakingapp.core.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.peterchege.notetakingapp.core.room.entites.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes")
    fun getAllCachedNotes():Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE noteId = :noteId")
    fun getCachedNoteById(noteId:String):Flow<NoteEntity?>

    @Query("DELETE  FROM notes")
    suspend fun deleteAllCachedNotes()


    @Query("DELETE FROM notes WHERE noteId = :noteId")
    suspend fun deleteNoteById(noteId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCachedNote(noteEntity: NoteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBulkNotes(notes:List<NoteEntity>)


    @Query("SELECT * FROM notes WHERE isInSync =:isInSync")
    suspend fun getNotesBySyncStatus(isInSync:Boolean):List<NoteEntity>


    @Query("UPDATE notes SET noteAuthorId =:noteAuthorId")
    suspend fun updateNotesAuthorId(noteAuthorId:String)

    @Query("SELECT * FROM notes WHERE LOWER(noteTitle) LIKE '%' || LOWER(:query) || '%' ")
    fun searchNotes(query:String):Flow<List<NoteEntity>>

    @Query("UPDATE notes SET isInSync =:syncStatus")
    suspend fun updateNoteSyncStatus(syncStatus:Boolean)






}