package com.google.tflite.catvsdog.view

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.tflite.catvsdog.databinding.FragmentContainerBinding
import com.google.tflite.catvsdog.tflite.Classifier

class ContainerFragment: Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentContainerBinding

    private lateinit var classifier: Classifier

    private val mInputSize = 224
    private val mModelPath = "converted_model.tflite"
    private val mLabelPath = "label.txt"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentContainerBinding.inflate(inflater).apply{
            this.lifecycleOwner = this@ContainerFragment
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        this@ContainerFragment.apply{
            this.initClassifier()
            this.initViews()
        }
    }

    private fun initClassifier() {
        classifier = Classifier(this.requireActivity().assets, mModelPath, mLabelPath, mInputSize)
    }

    private fun initViews() {
        binding.apply{
            this.iv1.setOnClickListener(this@ContainerFragment)
            this.iv2.setOnClickListener(this@ContainerFragment)
            this.iv3.setOnClickListener(this@ContainerFragment)
            this.iv4.setOnClickListener(this@ContainerFragment)
            this.iv5.setOnClickListener(this@ContainerFragment)
            this.iv6.setOnClickListener(this@ContainerFragment)
        }
    }

    override fun onClick(view: View?) {
        val bitmap = ((view as ImageView).drawable as BitmapDrawable).bitmap

        val result = classifier.recognizeImage(bitmap)

        this.requireActivity().runOnUiThread {
            Toast.makeText(this.requireContext(), result[0].title, Toast.LENGTH_SHORT).show()
        }
    }
}