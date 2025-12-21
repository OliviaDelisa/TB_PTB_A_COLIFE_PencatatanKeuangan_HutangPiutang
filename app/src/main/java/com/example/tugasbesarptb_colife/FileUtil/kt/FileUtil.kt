package com.example.tugasbesarptb_colife.FileUtil.kt

import android.content.Context
import android.net.Uri
import java.io.File

object FileUtil {
    fun from(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)!!
        val file = File.createTempFile("struk_", ".jpg", context.cacheDir)
        file.outputStream().use { inputStream.copyTo(it) }
        return file
    }
}
