package com.google.tflite.catvsdog.tflite

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.util.Log
import com.google.tflite.catvsdog.model.Recognition
import com.google.tflite.catvsdog.tool.ByteBufferUtil
import com.google.tflite.catvsdog.tool.AssetsUtil
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*
import kotlin.math.min


class Classifier(assetManager: AssetManager, modelPath: String, labelPath: String, private val inputSize: Int) {

    private var interpreter: Interpreter
    private var labelList: List<String>
    private val pixelSize: Int = 3
    private val imageMean = 0
    private val imageStd = 255.0f
    private val maxResult = 3
    private val threshHold = 0.4f

    init {
        val options = Interpreter.Options()
        options.setNumThreads(5)
        options.setUseNNAPI(true)
        interpreter = Interpreter(ByteBufferUtil.loadModelFile(assetManager, modelPath), options)
        labelList = AssetsUtil.load(assetManager, labelPath)
    }

    fun recognizeImage(bitmap: Bitmap): List<Recognition> {
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, false)
        val byteBuffer = ByteBufferUtil.convertBitmapToByteBuffer(scaledBitmap, inputSize, pixelSize, imageMean, imageStd) // input buffer
        val result = Array(1) { FloatArray(labelList.size) } // output buffer containing the probabilities of the two classes (cats and dogs)
        interpreter.run(byteBuffer, result)
        return getSortedResult(result)
    }

    private fun getSortedResult(labelProbArray: Array<FloatArray>): List<Recognition> {
        Log.d("Classifier", "List Size:(%d, %d, %d)".format(labelProbArray.size,labelProbArray[0].size,labelList.size))

        val pq = PriorityQueue(
            maxResult,
            Comparator<Recognition> {
                    (_, _, confidence1), (_, _, confidence2)
                -> confidence1.compareTo(confidence2) * -1
            })

        for (i in labelList.indices) {
            val confidence = labelProbArray[0][i]
            if (confidence >= threshHold) {
                pq.add(Recognition("" + i,
                    if (labelList.size > i) labelList[i] else "Unknown", confidence)
                )
            }
        }
        Log.d("Classifier", "pqsize:(%d)".format(pq.size))

        val recognitions = ArrayList<Recognition>()
        val recognitionsSize = min(pq.size, maxResult)
        for (i in 0 until recognitionsSize) {
            recognitions.add(pq.poll())
        }
        return recognitions
    }
}