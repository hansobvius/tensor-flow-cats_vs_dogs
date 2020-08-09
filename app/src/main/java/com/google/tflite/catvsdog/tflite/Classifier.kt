package com.google.tflite.catvsdog.tflite

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.util.Log
import com.google.tflite.catvsdog.model.Recognition
import com.google.tflite.catvsdog.tool.AssetsUtil
import com.google.tflite.catvsdog.tool.ByteBufferUtil
import org.tensorflow.lite.Interpreter
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
        // TODO 3 (Initialize the Interpreter) - setting up the interpreter to encapsulate the TF trained model.
        //  This options config the state of the interpreter to be used
        val options = Interpreter.Options()
        options.setNumThreads(5)
        options.setUseNNAPI(true)

        // TODO 4 (Initialize the Interpreter) - Initialize and Loading the model into the interpreter
        interpreter = Interpreter(ByteBufferUtil.loadModelFile(assetManager, modelPath), options)

        // TODO 5 (Initialize the Interpreter) - get model labels (.txt)
        labelList = AssetsUtil.load(assetManager, labelPath)
    }

    /**
     * Returns the result after running the recognition with the help of
     * interpreter on the passed bitmap
     */
    // TODO 6 (preparing the image output) - Rescaling and allocating a buffer.
    fun recognizeImage(bitmap: Bitmap): List<Recognition> {
        // resize the bitmap to 224 x 224
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, false)

        // convert the bitmap to bytebuffer
        val byteBuffer = ByteBufferUtil.convertBitmapToByteBuffer(scaledBitmap, inputSize, pixelSize, imageMean, imageStd) // input buffer
        val result = Array(1) { FloatArray(labelList.size) } // output buffer containing the probabilities of the two classes (cats and dogs)

        // TODO 7 (Perform inference) - Running inference and accumulating the results, passing the input and
        //  output buffers as arguments
        interpreter.run(byteBuffer, result)

        // TODO 8 (Obtain and map the results) - All of the results are gathered in a recognition of objects,
        //  which contains information about specific recognition results including its title and confidence
        return getSortedResult(result)
    }

    private fun getSortedResult(labelProbArray: Array<FloatArray>): List<Recognition> {
        Log.d("Classifier", "List Size:(%d, %d, %d)".format(labelProbArray.size,labelProbArray[0].size,labelList.size))

        // The size of de queue indicates the maximum number of results to be shown
        val pq = PriorityQueue(
            maxResult,
            Comparator<Recognition> {
                    (_, _, confidence1), (_, _, confidence2)
                -> confidence1.compareTo(confidence2) * -1
            })

        // only consider results that have a confidence of 0.5 or greater
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