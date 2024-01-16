package com.strayalpaca.hot6.ai

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.core.graphics.scale
import androidx.core.net.toUri
import java.nio.FloatBuffer

class ImageHandler(private val context : Context) {
    private val INPUT_SIZE = 256

    fun urlToImageFloatBuffer(url : String) : FloatBuffer {
        val bitmap = urlToBitmap(url)
        val softwareBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true).scale(INPUT_SIZE, INPUT_SIZE)
        return bitmapToFloatBuffer(softwareBitmap)
    }

    private fun urlToBitmap(url : String) : Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, url.toUri()))
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, url.toUri())
        }
    }

    private fun bitmapToFloatBuffer(bitmap: Bitmap) : FloatBuffer {
        val imageSTD = 255f
        val buffer = FloatBuffer.allocate(3 * INPUT_SIZE * INPUT_SIZE)
        buffer.rewind()

        val area = INPUT_SIZE * INPUT_SIZE
        val bitmapData = IntArray(area)
        bitmap.getPixels(
            bitmapData,
            0,
            bitmap.width,
            0,
            0,
            bitmap.width,
            bitmap.height
        )

        for (i in 0 until INPUT_SIZE) {
            for (j in 0 until INPUT_SIZE) {
                val idx = INPUT_SIZE * i + j
                val pixelValue = bitmapData[idx]
                buffer.put(idx, ((pixelValue shr 16 and 0xff) / imageSTD))
                buffer.put(idx + area, ((pixelValue shr 8 and 0xff) / imageSTD))
                buffer.put(idx + area * 2 , ((pixelValue and 0xff)/ imageSTD))
            }
        }
        buffer.rewind()
        return buffer
    }
}