package com.anggitarha.qrscanner

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode

class MainActivity : AppCompatActivity() {
    //deklarasi fungsi untuk permission request camera
    companion object{
        private  const val CAMERA_REQ = 101
    }

    //deklarasi function codeScanner dari library CodeScanner
    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //deklarasi function bagi komponen yang berada pada activity main
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)
        val kodeqr = findViewById<TextView>(R.id.textQR)
        val CardCopy = findViewById<CardView>(R.id.cardCopy)
        val CardSave = findViewById<CardView>(R.id.cardSave)
        val CardShare = findViewById<CardView>(R.id.cardShare)

        //mengeksekusi function getPermission
        getPermission()

        codeScanner = CodeScanner(this, scannerView)

        // deklarasi parameter yang digunakan untuk fungsi codeScanner
        codeScanner.camera = CodeScanner.CAMERA_BACK //menentukan kamera yang digunakan
        codeScanner.formats = CodeScanner.ALL_FORMATS //menentukan format barcode
        codeScanner.autoFocusMode = AutoFocusMode.SAFE //mengatur auto fokus kamera
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true // mengatur penggunaan auto focus
        codeScanner.isFlashEnabled = false // mengatur penggunaan flash

        // menentukan callback
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                kodeqr.text = it.text //memasukkan hasil scan kedalam parameter kodeqr yang disimpan dalam bentuk teks
            }
        }
        codeScanner.errorCallback = ErrorCallback { // penanganan error
            runOnUiThread {
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

        //inisialisasi function CardCopy saat ditekan
        CardCopy.setOnClickListener {
            val clipboard : ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip : ClipData = ClipData.newPlainText("simple text", kodeqr.text.toString())
            clipboard.setPrimaryClip(clip)
            //deklarasi clipboard service dan parameter clip untuk menyimpan teks dari parameter kodeqr, dikonversi ke string dan disimpan dalam clipboardmanager
            Toast.makeText(this, "copied to clipboard", Toast.LENGTH_SHORT).show()
            //menampilkan teks bahwa teks hasil scan berhasil di copy ke clipboard
        }

        //inisialisasi function CardShare saat ditekan
        CardShare.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, kodeqr.text.toString())
                type = "text/plain"
            } //menyimpan hasil parameter kodeqr dan dikonversi ke strng
            val shareIntent = Intent.createChooser(sendIntent, null) //memilih media yang akan dikirimkan parameter sendIntent
            startActivity(shareIntent) //memulai aktifitas shareIntent
        }

        //inisialisasi function CardSave saat ditekan
        CardSave.setOnClickListener {
            val intentBiasa = Intent(this@MainActivity, SaveActivity::class.java)
            startActivity(intentBiasa)
            //deklarasi parameter intentBiasa untuk membuka activity SaveActivity
        }
    }

    //deklarasi function getPermission()
    private fun getPermission() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQ)
        }
        //meminta request permission untuk menjalankan camera pada perangkat android saat dijalankan pada android
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
}