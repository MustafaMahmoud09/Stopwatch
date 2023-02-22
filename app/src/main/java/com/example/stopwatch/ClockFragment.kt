package com.example.stopwatch

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_clock.*
import kotlinx.android.synthetic.main.fragment_clock.view.*
import java.text.SimpleDateFormat
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ClockFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
   private var inflaterClock:View?= null
   private var objCounter:CountDownTimer?=null
   private val map=HashMap<String,String>()
   private var check=true;
   private var count=0;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inflaterClock=inflater.inflate(R.layout.fragment_clock, container, false)
        insertInMap()
        timerCounter(container)
        return inflaterClock
    }

    private fun timerCounter(container: ViewGroup?) {
        objCounter=object : CountDownTimer(24 * 60 * 60 * 1000, 100) {
            override fun onTick(p0: Long) {
                mainClock()
                secondaryClock(container)
            }
            override fun onFinish() {
                timerCounter(container)
            }
        }
        objCounter!!.start()
    }
private fun secondaryClock(container: ViewGroup?){
    if(check||Calendar.getInstance().get(Calendar.MINUTE)!=count) {
        val list = LinkedList<Information>()
        for (count in map.keys) {
            val tz=TimeZone.getTimeZone(map[count])
            val calender=Calendar.getInstance(tz)
            var hour=calender.get(Calendar.HOUR).toString()
            var minute=calender.get(Calendar.MINUTE).toString()
            if(hour.length==1){
                hour="0$hour"
            }
            if(minute.length==1){
                minute="0$minute"
            }
            list.add(Information(count,"$hour:$minute"))
         }
        inflaterClock!!.recyclerView.layoutManager =
            LinearLayoutManager(container?.context, RecyclerView.VERTICAL, false)
        inflaterClock!!.recyclerView.adapter = RecyclerAdapter(list)
        count = Calendar.getInstance().get(Calendar.MINUTE);
        check = false
    }
}
private fun mainClock(){
    val c = Calendar.getInstance()
    var hour = c.get(Calendar.HOUR_OF_DAY).toString()
    var minute = c.get(Calendar.MINUTE).toString()
    var second = c.get(Calendar.SECOND).toString()
    val day=c.get(Calendar.DAY_OF_MONTH)
    val month=c.get(Calendar.MONTH)
    val year=c.get(Calendar.YEAR)
    val mil = c.get(Calendar.MILLISECOND)
    if (hour.length == 1) {
        hour = "0$hour"
    }
    if (minute.length == 1) {
        minute = "0$minute"
    }
    if (second.length == 1) {
        second = "0$second"
    }
    textTimerClock.text = "$hour:$minute:$second.${mil / 100}"
    textDay.text="$day-${month+1}-$year"
}
private fun insertInMap(){
    map[resources.getString(R.string.Paris)] = "Europe/Paris"
    map[resources.getString(R.string.Berlin)] = "Europe/Berlin"
    map[resources.getString(R.string.NewYork)] = "America/New_York"
    map[resources.getString(R.string.Madrid)] = "Europe/Madrid"
    map[resources.getString(R.string.Roma)] = "Europe/Rome"
    map[resources.getString(R.string.Tokyo)] = "Asia/Tokyo"
    map[resources.getString(R.string.Algiers)] = "Africa/Algiers"
    map[resources.getString(R.string.Cairo)] = "Africa/Cairo"
    map[resources.getString(R.string.Riyadh)] = "Asia/Riyadh"
    map[resources.getString(R.string.Casablanca)] = "Africa/Casablanca"
    map[resources.getString(R.string.London)] = "Europe/London"
    map[resources.getString(R.string.Mauritius)] = "Indian/Mauritius"
    map[resources.getString(R.string.Tripoli)]="Africa/Tripoli"
    map[resources.getString(R.string.Khartoum)]="Africa/Khartoum"
    }
    companion object {
        fun newInstance(param1: String, param2: String) =
            ClockFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}