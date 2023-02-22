package com.example.stopwatch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_ui.view.*
import java.util.*

data class Information(val nameCity:String,val timeInCity:String)
class RecyclerAdapter(private val list: LinkedList<Information>):
    RecyclerView.Adapter<RecyclerAdapter.Holder>() {
    class Holder(view:View):RecyclerView.ViewHolder(view) {
     var textName:TextView?=null
     var textTime:TextView?=null
    init {
       textName=view.textName
       textTime=view.textTime
    }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
       val inflater=LayoutInflater.from(parent.context).inflate(R.layout.item_ui,parent,false)
       return Holder(inflater)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
     val item=list[position]
     holder.textName!!.text=item.nameCity
     holder.textTime!!.text=item.timeInCity
    }

    override fun getItemCount(): Int =list.size
}