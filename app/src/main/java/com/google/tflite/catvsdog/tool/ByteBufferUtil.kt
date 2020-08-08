package com.google.tflite.catvsdog.tool

import android.content.res.AssetManager
import android.graphics.Bitmap
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

object ByteBufferUtil {

    fun loadModelFile(assetManager: AssetManager, modelPath: String): MappedByteBuffer {
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

    fun convertBitmapToByteBuffer(bitmap: Bitmap, inputSize: Int, pixelSize: Int, imageMean: Int, imageStd: Float): ByteBuffer {
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
}