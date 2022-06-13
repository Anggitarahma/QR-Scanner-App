package com.anggitarha.qrscanner.model

import android.content.Context
import java.io.File

class InternalFileRepository(var context: Context) :
    //inisialisasi interface NoteRepository
    NoteRepository {
    private fun noteFile(fileName: String): File = File(noteDirectory(), fileName)
    private fun noteDirectory(): String = context.filesDir.absolutePath
    //deklarasi parameter noteDirectory untuk penyimpanan file

    override fun addNote(note: Note) {
        //inisialisasi function addNote untuk menyimpan file berdasarkan parameter fileName dan noteText
        context.openFileOutput(note.fileName, Context.MODE_PRIVATE).use { output ->
            output.write(note.noteText.toByteArray())
        }
    }

}