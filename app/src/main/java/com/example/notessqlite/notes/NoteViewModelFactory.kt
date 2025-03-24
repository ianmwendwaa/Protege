package com.example.notessqlite.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class NoteViewModelFactory(private val noteRepository: NoteRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)){
            @Suppress
            return NoteViewModel(noteRepository)as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}