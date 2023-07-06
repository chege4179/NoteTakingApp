package com.peterchege.notetakingapp.core.di

import com.peterchege.notetakingapp.core.work.add_note.AddNoteWorkManager
import com.peterchege.notetakingapp.core.work.add_note.AddNoteWorkManagerImpl
import com.peterchege.notetakingapp.core.work.sync_notes.SyncNotesWorkManager
import com.peterchege.notetakingapp.core.work.sync_notes.SyncNotesWorkManagerImpl
import org.koin.dsl.module


val workModule = module {


    single<AddNoteWorkManager> {
        AddNoteWorkManagerImpl()
    }

    single<SyncNotesWorkManager> {
        SyncNotesWorkManagerImpl()
    }

}