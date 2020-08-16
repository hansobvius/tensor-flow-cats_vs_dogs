package com.google.tflite.catvsdog.model

data class Recognition(
    var id: String = "",
    var title: String = "",
    var confidence: Float = 0F): ModelContract  {

    override fun toString(): String {
        return "Title = $title, Confidence = $confidence)"
    }
}