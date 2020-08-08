package com.google.tflite.catvsdog.view

import android.databinding.DataBindingUtil
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.google.tflite.catvsdog.R
import com.google.tflite.catvsdog.databinding.ActivityImageClassifierBinding
import com.google.tflite.catvsdog.tflite.Classifier

class ImageClassifierActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding: ActivityImageClassifierBinding
    private lateinit var classifier: Classifier

    private val mInputSize = 224
    private val mModelPath = "converted_model.tflite"
    private val mLabelPath = "label.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this@ImageClassifierActivity, R.layout.activity_image_classifier
        )
    }

    override fun onStart() {
        super.onStart()
        this@ImageClassifierActivity.apply{
            this.initClassifier()
            this.initViews()
        }
    }

    private fun initClassifier() {
        classifier = Classifier(assets, mModelPath, mLabelPath, mInputSize)
    }

    private fun initViews() {
        binding.apply{
            this.iv1.setOnClickListener(this@ImageClassifierActivity)
            this.iv2.setOnClickListener(this@ImageClassifierActivity)
            this.iv3.setOnClickListener(this@ImageClassifierActivity)
            this.iv4.setOnClickListener(this@ImageClassifierActivity)
            this.iv5.setOnClickListener(this@ImageClassifierActivity)
            this.iv6.setOnClickListener(this@ImageClassifierActivity)
        }
    }

    override fun onClick(view: View?) {
        val bitmap = ((view as ImageView).drawable as BitmapDrawable).bitmap

        val result = classifier.recognizeImage(bitmap)

        runOnUiThread { Toast.makeText(this, result[0].title, Toast.LENGTH_SHORT).show() }
    }
}
