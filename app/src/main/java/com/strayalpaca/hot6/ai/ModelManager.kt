package com.strayalpaca.hot6.ai

import android.annotation.SuppressLint
import android.content.Context
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader

@SuppressLint("StaticFieldLeak")
class ModelManager {
    private var context : Context ?= null
    var classes = arrayOf<String>()
        private set
    var absFilePath = ""
        private set

    fun attachContext(context : Context) {
        this.context = context
    }

    fun detachContext() {
        this.context = null
    }

    fun loadModel(fileName : String) {
        context?.run {
            val assetManager = assets
            val outputFile = File("$filesDir/$fileName")
            assetManager.open(fileName).use { inputStream ->
                FileOutputStream(outputFile).use { outputStream ->
                    val buffer = ByteArray(1024)
                    var read : Int
                    while (inputStream.read(buffer).also { read = it } != -1) {
                        outputStream.write(buffer, 0, read)
                    }
                }
            }
            absFilePath = outputFile.absolutePath
        }
    }

    fun loadLabel(fileName : String) {
        context?.run {
            BufferedReader(InputStreamReader(assets.open(fileName))).use { reader ->
                var line : String?
                val classList = ArrayList<String>()
                while(reader.readLine().also { line = it } != null) {
                    classList.add(line!!)
                }
                classes = classList.toTypedArray()
            }
        }
    }

}