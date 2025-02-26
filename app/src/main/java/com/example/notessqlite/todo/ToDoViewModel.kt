package com.example.notessqlite.todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ToDoViewModel(private val toDoRepository: ToDoRepository):ViewModel() {
    private val _todoList = MutableLiveData<List<ToDo>>()
    val todoList: LiveData<List<ToDo>> = _todoList

    private val _showNoToDo = MutableLiveData<Boolean>()
    val showNoToDo:LiveData<Boolean> = _showNoToDo

    init {
        loadToDos()
    }

    fun loadToDos() {
        viewModelScope.launch {
            val toDos = toDoRepository.getAllToDos()
            _todoList.value = toDos
            _showNoToDo.value = toDos.isEmpty()
        }
    }
    fun insertToDo(toDO:ToDo){
        viewModelScope.launch {
            toDoRepository.insertToDos(toDO)
            loadToDos()
        }
    }
    fun updateToDo(toDo:ToDo){
        viewModelScope.launch {
            toDoRepository.updateToDo(toDo)
            loadToDos()
        }
    }
    fun deleteToDo(todoId:Int){
        viewModelScope.launch {
            toDoRepository.deleteToDo(todoId)
            loadToDos()
        }
    }
}