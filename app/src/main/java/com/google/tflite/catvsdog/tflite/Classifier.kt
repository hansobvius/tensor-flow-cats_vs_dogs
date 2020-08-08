package com.google.tflite.catvsdog.tflite

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
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

    data class Recognition(
        var id: String = "",
        var title: String = "",
        var confidence: Float = 0F
    )  {
        override fun toString(): String {
            return "Title = $title, Confidence = $confidence)"
        }
    }

    init {
        // TODO 3 (Initialize the Interpreter) - setting up the interpreter to encapsulate the TF trained model.
        //  This options config the state of the interpreter to be used
        val options = Interpreter.Options()
        options.setNumThreads(5)
        options.setUseNNAPI(true)

        // TODO 4 (Initialize the Interpreter) - Initialize and Loading the model into the interpreter
        interpreter = Interpreter(loadModelFile(assetManager, modelPath), options)

        // TODO 5 (Initialize the Interpreter) - get model labels (.txt)
        labelList = loadLabelList(assetManager, labelPath)
    }

    private fun loadModelFile(assetManager: AssetManager, modelPath: String): MappedByteBuffer {
        // get the file descriptor of tflite model
        val fileDescriptor = assetManager.openFd(modelPath)
        // open the input stream
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        // read the file channels along its offset and length as follow
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        // finally, load the TFLite model
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun loadLabelList(assetManager: AssetManager, labelPath: String): List<String> {
        return assetManager.open(labelPath).bufferedReader().useLines { it.toList() }
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
        val byteBuffer = convertBitmapToByteBuffer(scaledBitmap) // input buffer
        val result = Array(1) { FloatArray(labelList.size) } // output buffer containing the probabilities of the two classes (cats and dogs)

        // TODO 7 (Perform inference) - Running inference and accumulating the results, passing the input and
        //  output buffers as arguments
        interpreter.run(byteBuffer, result)

        // TODO 8 (Obtain and map the results) - All of the results are gathered in a recognition of objetcs,
        //  wich contains information about specific recognition results including its title and confidence
        return getSortedResult(result)
    }


    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        // batch size is four because we need four bytes for each value since the dataset for inputs and outputs of the exported model is float
        val byteBuffer = ByteBuffer.allocateDirect(4 * inputSize * inputSize * pixelSize)
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(inputSize * inputSize)

        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        var pixel = 0
        // get R-G-B channels of the image
        for (i in 0 until inputSize) {
            for (j in 0 until inputSize) {
                val input = intValues[pixel++]

                byteBuffer.putFloat((((input.shr(16)  and 0xFF) - imageMean) / imageStd))
                byteBuffer.putFloat((((input.shr(8) and 0xFF) - imageMean) / imageStd))
                byteBuffer.putFloat((((input and 0xFF) - imageMean) / imageStd))
            }
        }
        return byteBuffer
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