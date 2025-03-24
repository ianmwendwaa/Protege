package com.example.notessqlite.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NoteViewModel(private val noteRepo:NoteRepository):ViewModel() {
    private val _notesList = MutableLiveData<List<Note>>()
    val notesList:LiveData<List<Note>> = _notesList

    private val _showNoNotes = MutableLiveData<Boolean>()
    val showNoNotes:LiveData<Boolean> = _showNoNotes

    init {
        loadNotes()
    }
    fun loadNotes(){
        viewModelScope.launch {
            val notes = noteRepo.getAllNotes()
            _notesList.value = notes
            _showNoNotes.value = notes.isEmpty()
        }
    }
    fun insertNote(note:Note){
        viewModelScope.launch {
            noteRepo.insertNote(note)
            loadNotes()
        }
    }
    fun updateNote(note: Note){
        viewModelScope.launch {
            noteRepo.updateNote(note)
            loadNotes()
        }
    }
    fun deleteNote(noteId:Int){
        viewModelScope.launch {
            noteRepo.deleteNote(noteId)
            loadNotes()
        }
    }
}
