package com.example.notessqlite.todo

import com.example.notessqlite.database.ToDoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ToDoRepository(private val toDoDatabase: ToDoDatabase) {
    suspend fun getAllToDos():List<ToDo> = withContext(Dispatchers.IO){
        toDoDatabase.getAllToDos()
    }
    suspend fun insertToDos(toDo:ToDo) = withContext(Dispatchers.IO){
        toDoDatabase.insertToDo(toDo)
    }
    suspend fun deleteToDo(toDoId:Int) = withContext(Dispatchers.IO){
        toDoDatabase.deleteToDo(toDoId)
    }
    suspend fun updateToDo(toDo: ToDo) = withContext(Dispatchers.IO){
        toDoDatabase.updateToDo(toDo)
    }
}