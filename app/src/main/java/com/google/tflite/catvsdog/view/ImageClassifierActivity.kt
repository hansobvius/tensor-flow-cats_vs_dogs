package com.google.tflite.catvsdog.view

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.tflite.catvsdog.R
import com.google.tflite.catvsdog.databinding.ActivityImageClassifierBinding
import com.google.tflite.catvsdog.tflite.Classifier

class ImageClassifierActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageClassifierBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this@ImageClassifierActivity, R.layout.activity_image_classifier
        )
    }
}
