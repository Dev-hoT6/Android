package com.strayalpaca.hot6.ai.classifier

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import android.content.Context
import com.strayalpaca.hot6.ai.ImageHandler
import com.strayalpaca.hot6.ai.ModelManager
import java.util.Collections

class Yolov8Classifier(private val context : Context) : ImageCategoryClassifier {
    private lateinit var ortEnvironment: OrtEnvironment
    private lateinit var session : OrtSession
    private lateinit var imageHandler: ImageHandler
    private lateinit var modelManager: ModelManager
    private var loaded = false

    override fun isLoaded(): Boolean = loaded

    override fun load() {
        if (isLoaded()) return

        modelManager = ModelManager()

        modelManager.attachContext(context)
        modelManager.loadLabel("classes.txt")
        modelManager.loadModel("best.onnx")

        ortEnvironment = OrtEnvironment.getEnvironment()
        session = ortEnvironment.createSession(
            modelManager.absFilePath,
            OrtSession.SessionOptions()
        )
        imageHandler = ImageHandler(context)
        loaded = true
    }

    override fun close() {
        modelManager.detachContext()
        ortEnvironment.close()
        session.close()
    }

    @Suppress("UNCHECKED_CAST")
    override fun preferenceByUrl(url: String, categoryIds: List<String>): Boolean {
        val floatBuffer = imageHandler.urlToImageFloatBuffer(url)
        val inputName = session.inputNames.iterator().next()
        val inputShape = longArrayOf(1, 3, 256, 256)
        val inputTensor = OnnxTensor.createTensor(ortEnvironment, floatBuffer, inputShape)
        val resultTensor = session.run(Collections.singletonMap(inputName, inputTensor))
        val outputs = resultTensor.get(0).value as Array<Array<FloatArray>> // [1 9 1344]

        val detectedCategoryIds = mutableSetOf<String>()
        for (i in 0 until modelManager.classes.size) {
            val currentCategoryIndex = 4 + i
            val contain = outputs[0][currentCategoryIndex].filter { it > 0.5f }
            if (contain.isNotEmpty()) {
                detectedCategoryIds.add(i.toString())
            }
        }

        return detectedCategoryIds.containsAll(categoryIds)
    }
}