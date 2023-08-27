package com.peterchege.notetakingapp.ui.screens.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterchege.notetakingapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AccountScreenViewModel (
    val authRepository: AuthRepository
): ViewModel() {


    val authUser = authRepository.getAuthUser()
        .stateIn(
            scope = viewModelScope,
            started=  SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )


    fun logOutUser(){
        authRepository.signOutUser()
    }


}