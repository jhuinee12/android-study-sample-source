package com.zahi.scrollviewinsiderecyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zahi.scrollviewinsiderecyclerview.databinding.ItemSecondBinding

class SecondAdapter(private val list: ArrayList<MainData>) : RecyclerView.Adapter<SecondAdapter.MainHolder>(){

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MainHolder {
        val binding = ItemSecondBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: SecondAdapter.MainHolder, position: Int) {
        holder.bind(getItem(position))

    }

    override fun getItemCount(): Int = list.size

    fun getItem(position: Int) = list[position]

    inner class MainHolder(private val binding: ItemSecondBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ClickableViewAccessibility")
        fun bind(item: MainData) {
            binding.secondLayout.setOnTouchListener { view, motionEvent ->
                view.parent.requestDisallowInterceptTouchEvent(false)
                view.onTouchEvent(motionEvent)
            }
        }
    }
}