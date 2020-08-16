package com.google.tflite.catvsdog.view.fragment

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.tflite.catvsdog.AppApplication
import com.google.tflite.catvsdog.R
import com.google.tflite.catvsdog.databinding.FragmentImagesBinding
import com.google.tflite.catvsdog.tflite.Classifier
import com.google.tflite.catvsdog.viewmodel.ImageViewModel

class ImagesFragment: Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentImagesBinding
    private lateinit var imageViewModel: ImageViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        imageViewModel = ViewModelProvider(this.requireActivity()).get(ImageViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentImagesBinding.inflate(inflater).apply{
            this.lifecycleOwner = this@ImagesFragment
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        this@ImagesFragment.apply{
            this.initViews()
        }
    }

    private fun initViews() {
        binding.apply{
            this.iv1.setOnClickListener(this@ImagesFragment)
            this.iv2.setOnClickListener(this@ImagesFragment)
            this.iv3.setOnClickListener(this@ImagesFragment)
            this.iv4.setOnClickListener(this@ImagesFragment)
            this.iv5.setOnClickListener(this@ImagesFragment)
            this.iv6.setOnClickListener(this@ImagesFragment)
        }
    }

    override fun onClick(view: View?) {
        val bitmap = ((view as ImageView).drawable as BitmapDrawable).bitmap
        imageViewModel.setBitmap(bitmap).also{
            navigateToFragment(R.id.action_imagesFragment_to_imageClassifierFragment)
        }
    }

    private fun navigateToFragment(navigationId: Int){
        this.findNavController().navigate(navigationId)
    }
}