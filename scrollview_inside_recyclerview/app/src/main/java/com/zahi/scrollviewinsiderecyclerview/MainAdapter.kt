package com.zahi.scrollviewinsiderecyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zahi.scrollviewinsiderecyclerview.databinding.ItemMainBinding

class MainAdapter(private val list: ArrayList<MainData>) : RecyclerView.Adapter<MainAdapter.MainHolder>(){

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MainHolder {34
        val binding = ItemMainBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return MainHolder(binding)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: MainAdapter.MainHolder, position: Int) {
        val item = getItem(position)

        holder.binding.secondLayout.setOnTouchListener { view, motionEvent ->

            if (motionEvent.action == MotionEvent.ACTION_DOWN || motionEvent.action == MotionEvent.ACTION_MOVE ) {
                view.parent.parent.parent.requestDisallowInterceptTouchEvent(true)
                view.parent.parent.requestDisallowInterceptTouchEvent(true)
                view.onTouchEvent(motionEvent)
                return@setOnTouchListener true
            }

            return@setOnTouchListener false
        }
    }

    override fun getItemCount(): Int = list.size

    private fun getItem(position: Int) = list[position]

    inner class MainHolder(var binding: ItemMainBinding): RecyclerView.ViewHolder(binding.root)
}