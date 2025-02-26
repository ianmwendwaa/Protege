package com.example.notessqlite.todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ToDoViewModelFactory(private val toDoRepository: ToDoRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ToDoViewModel::class.java)){
            @Suppress
            ("UNCHECKED CAST")
            return ToDoViewModel(toDoRepository)as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}