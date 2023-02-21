package com.example.signin.base

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.dylanc.viewbinding.BindingViewHolder
import com.dylanc.viewbinding.base.ViewBindingUtil

abstract class BaseBindingAdapter<T, VB : ViewBinding> : BaseQuickAdapter<T, BindingViewHolder<VB>>() {
    class BindingViewHolder<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int) =  com.dylanc.viewbinding.BindingViewHolder(ViewBindingUtil.inflateWithGeneric<VB>(this, parent))

    override fun onBindViewHolder(holder: com.dylanc.viewbinding.BindingViewHolder<VB>, position: Int, item: T?) {
        item?.let {
            onBindViewHolder(holder.binding, it, holder.bindingAdapterPosition)
        }

    }


    abstract fun onBindViewHolder(holder: VB, item: T, position: Int)



}