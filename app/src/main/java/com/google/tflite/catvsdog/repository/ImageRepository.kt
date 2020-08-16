package com.google.tflite.catvsdog.repository

import com.google.tflite.catvsdog.R
import com.google.tflite.catvsdog.model.Images
import com.google.tflite.catvsdog.model.Recognition

object ImageRepository {

    val images = listOf(
        R.drawable.img0,
        R.drawable.img1,
        R.drawable.img17,
        R.drawable.img3,
        R.drawable.img6,
        R.drawable.img5)

    fun getImagesObject(): MutableList<Images>{
        val obj = mutableListOf<Images>()
        images.forEachIndexed{ position, value ->
            obj.add(Images((value)))
        }
        return obj
    }
}