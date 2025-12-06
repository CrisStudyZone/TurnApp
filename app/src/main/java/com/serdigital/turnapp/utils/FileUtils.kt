package com.serdigital.turnapp.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

fun createImageFileUri(context: Context): Uri {
    val storageDir = File(context.cacheDir, "images")
    storageDir.mkdirs()
    val file = File.createTempFile(
        "client_${System.currentTimeMillis()}",
        ".jpg",
        storageDir
    )
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )
}
