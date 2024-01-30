package com.example.pengaduan.helper

import android.content.Context
import android.net.Uri
import android.os.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class FileDownloader(
    private val context: Context
) {
    fun downloadAndSaveFile(url: String, fileName: String, onComplete: (Uri?) -> Unit){
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val inputStream = URL(url).openStream()
                val directory = getDownloadDirectory()
                val file = File(directory, fileName)
                val outputStream = FileOutputStream(file)

                inputStream.use { input ->
                    outputStream.use { output ->
                        input.copyTo(output)
                    }
                }

                onComplete(Uri.fromFile(file))
            } catch (e : Exception) {
                onComplete(null)
            }
        }
    }

    private fun getDownloadDirectory(): File {
        val downloadDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "pengaduan"
        )
        if (!downloadDir.exists()){
            downloadDir.mkdirs()
        }

        return downloadDir
    }
}