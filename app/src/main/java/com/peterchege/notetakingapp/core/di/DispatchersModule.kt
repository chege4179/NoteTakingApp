package com.peterchege.notetakingapp.core.di

import com.peterchege.notetakingapp.core.util.DefaultDispatcherProvider
import com.peterchege.notetakingapp.core.util.DispatcherProvider
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val dispatchersModule = module {

    single<DispatcherProvider> {
        DefaultDispatcherProvider()
    }

}