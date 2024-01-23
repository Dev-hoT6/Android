package com.strayalpaca.hot6.ai.review

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import android.content.Context
import com.strayalpaca.hot6.ai.ModelManager
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.util.Collections

class FullyConnectedReviewClassifier(private val context : Context) : ReviewCategoryClassifier {
    private lateinit var ortEnvironment: OrtEnvironment
    private lateinit var session : OrtSession
    private lateinit var modelManager: ModelManager
    private var loaded = false

    override fun isLoaded(): Boolean = loaded

    override fun load() {
        if (isLoaded()) return

        modelManager = ModelManager()

        modelManager.attachContext(context)
        modelManager.loadModel("review_classifier_01.onnx")

        ortEnvironment = OrtEnvironment.getEnvironment()
        session = ortEnvironment.createSession(
            modelManager.absFilePath,
            OrtSession.SessionOptions()
        )
        loaded = true
    }

    override fun close() {
        if (::modelManager.isInitialized) { modelManager.detachContext() }
        if (::ortEnvironment.isInitialized) { ortEnvironment.close() }
        if (::session.isInitialized) { session.close() }
    }

    override fun preferenceRelated(vector: List<List<Double>>): Boolean {
        val floatBuffer = convertDoubleListToFloatBuffer(vector)
        val inputName = session.inputNames.iterator().next()
        val inputShape = longArrayOf(1536)
        val inputTensor = OnnxTensor.createTensor(ortEnvironment, floatBuffer, inputShape)
        val resultTensor = session.run(Collections.singletonMap(inputName, inputTensor))
        val outputs = resultTensor.get(0).value as FloatArray

        resultTensor.close()
        inputTensor.close()

        return outputs[0] >= 0.9995
    }

    private fun convertDoubleListToFloatBuffer(doubleList: List<List<Double>>): FloatBuffer {
        val floatList: List<Float> = doubleList.flatten().map { it.toFloat() }
        val floatArray: FloatArray = floatList.toFloatArray()
        val FLOAT_BYTE_SIZE = 4

        // FloatBuffer를 생성하고 데이터를 복사합니다.
        val floatBuffer: FloatBuffer = ByteBuffer.allocateDirect(floatArray.size * FLOAT_BYTE_SIZE)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(floatArray)
        floatBuffer.rewind()

        return floatBuffer
    }

}