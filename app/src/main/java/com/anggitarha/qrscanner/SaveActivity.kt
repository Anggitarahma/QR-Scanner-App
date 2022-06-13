package com.anggitarha.qrscanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.anggitarha.qrscanner.model.InternalFileRepository
import com.anggitarha.qrscanner.model.Note
import com.anggitarha.qrscanner.model.NoteRepository

class SaveActivity : AppCompatActivity() {

    //deklarasi variabel repo dari NoteRepository
    private val repo: NoteRepository by lazy { InternalFileRepository(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save)

        //deklarasi function bagi komponen pada save activity
        val fileName = findViewById<EditText>(R.id.editFileName)
        val Teks = findViewById<EditText>(R.id.editTeks)
        val buttonSaveFile = findViewById<Button>(R.id.btnSaveInStorage)

        //inisialisasi function buttonSaveFile saat tombol ditekan
        buttonSaveFile.setOnClickListener {
            if (fileName.text.isNotEmpty()){ //jika fileName tidak kosong, maka eksekusi
                try {
                    repo.addNote(
                        Note(
                            fileName.text.toString(),
                            Teks.text.toString()
                        )
                    )  //memanggil function addNote, ubah parameter fileNama dan Teks kedalam string
                    Toast.makeText(this, "File Succesfully Saved!", Toast.LENGTH_LONG).show() //jika sukses maka file disimpan
                } catch (e:Exception){
                    Toast.makeText(this, "Save File Failed", Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                }
                fileName.text.clear()
                Teks.text.clear()
            } else {
                Toast.makeText(this, "Please provide a Filename", Toast.LENGTH_LONG).show()
                //jika parameter fileNama tidak diisi maka akan diajukan pengisian nama file
            }
        }
    }
}