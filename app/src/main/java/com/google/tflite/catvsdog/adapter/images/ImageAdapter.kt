package com.google.tflite.catvsdog.adapter.images

import com.google.tflite.catvsdog.R
import com.google.tflite.catvsdog.adapter.BaseAdapter
import com.google.tflite.catvsdog.databinding.ImageAdapterContentBinding
import com.google.tflite.catvsdog.model.Images

class ImageAdapter: BaseAdapter<Images, ImageAdapterContentBinding>(){

    override val adapterCallback: ((view: ImageAdapterContentBinding, position: Int, list: MutableList<Images>?) -> Unit)? = null

    override fun viewContainer(): Int? = R.layout.image_adapter_content

    override fun viewBinding(binding: ImageAdapterContentBinding, position: Int, list: MutableList<Images>?) {
        adapterCallback?.invoke(binding, position, list)
    }
}