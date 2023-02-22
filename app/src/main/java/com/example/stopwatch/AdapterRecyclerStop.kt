package com.example.stopwatch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.ui_recycler_stop_watch.view.*
import java.util.*

data class InformationRecycler(var number:String,var timeCount:String,var allTime:String)
class AdapterRecyclerStop(private val list: LinkedList<InformationRecycler>)
    :RecyclerView.Adapter<AdapterRecyclerStop.Inner>() {
    class Inner(view: View):RecyclerView.ViewHolder(view){
      var textNumber:TextView?=null
      var textTimeCount:TextView?=null
      var textAllTime:TextView?=null
        init {
            textNumber=view.textNumber
            textAllTime=view.textAllTime
            textTimeCount=view.textCountTimeId
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
    = Inner(LayoutInflater.from(parent.context).inflate(R.layout.ui_recycler_stop_watch,parent,false))

    override fun onBindViewHolder(holder: Inner, position: Int) {
        val item=list[position]
        holder.textNumber?.text=item.number
        holder.textAllTime?.text=item.allTime
        holder.textTimeCount?.text=item.timeCount
    }
    override fun getItemCount()=list.size
}