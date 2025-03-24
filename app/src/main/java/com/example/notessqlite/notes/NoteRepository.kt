package com.example.notessqlite.notes

import com.example.notessqlite.database.NoteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NoteRepository(private val noteDatabase: NoteDatabase) {
    suspend fun getAllNotes():List<Note> = withContext(Dispatchers.IO){
        noteDatabase.getAllNotes()
    }
    suspend fun insertNote(note: Note) = withContext(Dispatchers.IO){
        noteDatabase.insertNote(note)
    }
    suspend fun updateNote(note: Note) = withContext(Dispatchers.IO){
        noteDatabase.updateNote(note)
    }
    suspend fun deleteNote(noteId:Int) = withContext(Dispatchers.IO){
        noteDatabase.deleteNote(noteId)
    }
}