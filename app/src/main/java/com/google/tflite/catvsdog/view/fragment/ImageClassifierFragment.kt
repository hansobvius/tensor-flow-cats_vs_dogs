package com.google.tflite.catvsdog.view.fragment

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.tflite.catvsdog.databinding.FragmentImageClassifierBinding
import com.google.tflite.catvsdog.tflite.Classifier
import com.google.tflite.catvsdog.viewmodel.ImageViewModel

class ImageClassifierFragment: Fragment() {

    private lateinit var binding: FragmentImageClassifierBinding
    private lateinit var classifier: Classifier
    private lateinit var imageViewModel: ImageViewModel

    private val mInputSize = 224
    private val mModelPath = "converted_model.tflite"
    private val mLabelPath = "label.txt"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        imageViewModel = ViewModelProvider(this.requireActivity()).get(ImageViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentImageClassifierBinding.inflate(inflater).apply{
            this.lifecycleOwner = this@ImageClassifierFragment
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        classifier = Classifier(this.requireActivity().assets, mModelPath, mLabelPath, mInputSize)
    }

    override fun onResume(){
        super.onResume()
        imageViewModel.imageBitmap.observe(this, Observer {
            it?.let{
                initImageView(it)
                initImageClassifier(it)
            }
        })
    }

    override fun onDestroy(){
        super.onDestroy()
        imageViewModel.resetBitmap()
    }

    private fun initImageView(bitmap: Bitmap){
        binding.imageClassifier.setImageBitmap(bitmap)
    }

    private fun initImageClassifier(imageBitmap: Bitmap) {
        val result = classifier.recognizeImage(imageBitmap)
        this.requireActivity().runOnUiThread {
            Toast.makeText(this.requireContext(), result[0].title, Toast.LENGTH_SHORT).show()
        }
    }
}