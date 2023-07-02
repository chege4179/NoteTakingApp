package com.peterchege.notetakingapp.core.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.peterchege.notetakingapp.core.room.entites.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes")
    fun getAllCachedNotes():Flow<List<NoteEntity>>
}