package com.google.tflite.catvsdog.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.tflite.catvsdog.databinding.FragmentImageClassifierBinding
import kotlinx.android.synthetic.main.fragment_image_classifier.view.*

class ImageClassifierFragment: Fragment() {

    private lateinit var binding: FragmentImageClassifierBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentImageClassifierBinding.inflate(inflater).apply{
            this.lifecycleOwner = this@ImageClassifierFragment
        }
        return binding.root
    }
}