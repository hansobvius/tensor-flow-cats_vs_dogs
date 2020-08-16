package com.google.tflite.catvsdog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.google.tflite.catvsdog.model.ModelContract

@Suppress("UNCHECKED_CAST")
abstract class BaseAdapter<O, D>:
    RecyclerView.Adapter<BaseViewHolder<D>>() where O : ModelContract, D: ViewDataBinding {

    private var objectList: MutableList<O>? = null

    init{ _BaseAdapter() }

    private fun _BaseAdapter(){ objectList = mutableListOf() }

    abstract var adapterCallback: ((view: D, position: Int, list: MutableList<O>?) -> Unit)?

    abstract fun viewContainer(): Int?

    abstract fun viewBinding(binding: D, position: Int, list: MutableList<O>?)

    fun initializeAdapterData(list: List<O>){
        this.objectList?.addAll(list)
        this.notifyDataSetChanged()
    }

    override fun getItemCount(): Int = objectList?.count() ?: OPTIONAL_SIZE_VALUE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<D> =
        BaseViewHolder(LayoutInflater.from(parent.context).inflate(viewContainer()!!, parent, false))

    override fun onBindViewHolder(holder: BaseViewHolder<D>, position: Int) {
        holder.initBindView { view -> view.apply{ viewBinding(this as D, position, objectList) } }
    }

    companion object{
        const val OPTIONAL_SIZE_VALUE = 0
    }
}