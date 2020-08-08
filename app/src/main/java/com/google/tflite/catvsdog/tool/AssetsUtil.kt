package com.google.tflite.catvsdog.tool

import android.content.res.AssetManager

object AssetsUtil {

    fun load(assetManager: AssetManager, labelPath: String): List<String> {
        return assetManager.open(labelPath).bufferedReader().useLines { it.toList() }
    }
}