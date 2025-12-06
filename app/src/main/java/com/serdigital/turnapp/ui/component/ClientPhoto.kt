package com.serdigital.turnapp.ui.component

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.serdigital.turnapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

@Composable
fun ClientPhoto(
    imageUrl: String?,
    modifier: Modifier = Modifier
) {
    // Hardcodeamos las imágenes de placeholder y error
    val placeholder: Painter = painterResource(R.drawable.placeholder_client)
    val errorImage: Painter = painterResource(R.drawable.error_image)

    var bitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }

    // Descargar la imagen si hay URL
    LaunchedEffect(imageUrl) {
        if (!imageUrl.isNullOrBlank()) {
            isLoading = true
            isError = false
            try {
                val bmp = withContext(Dispatchers.IO) {
                    val connection = URL(imageUrl).openStream()
                    BitmapFactory.decodeStream(connection)
                }
                bitmap = bmp
            } catch (e: Exception) {
                isError = true
            } finally {
                isLoading = false
            }
        }
    }

    Box(modifier = modifier.height(180.dp)) {
        when {
            isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(androidx.compose.ui.Alignment.Center))
            }

            isError || imageUrl.isNullOrBlank() -> {
                Image(
                    painter = errorImage,
                    contentDescription = "Error al cargar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .size(50.dp)
                        .align(alignment = Alignment.Center)
                    )
            }

            bitmap != null -> {
                Image(
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = "Último corte",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(8.dp))
                    )
            }

            else -> {
                Image(
                    painter = placeholder,
                    contentDescription = "Placeholder",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .size(50.dp)
                )
            }
        }
    }
}