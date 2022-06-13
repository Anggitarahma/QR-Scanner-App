package com.anggitarha.qrscanner.model

//deklarasi interface NoteRepository dan function addNote
interface NoteRepository {
    fun addNote(note: Note)
}