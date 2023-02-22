package com.example.stopwatch

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_clock.*
import kotlinx.android.synthetic.main.fragment_stop.*
import kotlinx.android.synthetic.main.fragment_stop.view.*
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class StopFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    private var inflaterStop:View?=null
    private var countObject:CountDownTimer?=null
    private var list=LinkedList<InformationRecycler>()
    private var check=true
    private var startTime=60*60*1000L
    private var timeRunning=0L
    private var mil=""
    private var second=""
    private var minute=""
    private var timeRunningNewCount=0L
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inflaterStop = inflater.inflate(R.layout.fragment_stop, container, false)
        inflaterStop?.btnStartAndStopStopWatch?.setOnClickListener {
            codeBtnStartAndStop()
        }
        inflaterStop?.btnResetStopWatch?.setOnClickListener {
            codeBtnReset(container)
        }
        inflaterStop?.btnStartCounter?.setOnClickListener {
            codeBtnCounter(container)
        }
        return inflaterStop
    }

    private fun codeBtnCounter(container: ViewGroup?) {
        if (!check) {
            list.add(
                InformationRecycler(
                    "${list.size + 1}",
                    textCounter.text.toString(),
                    textTimer.text.toString()
               )
            )
            timeRunningNewCount = 0L
            textCounter.text = resources.getText(R.string._00_00_000)
            adapterRecycler(container)
        } else {
            Toast.makeText(
                context?.applicationContext,
                resources.getString(R.string.Stopwatch_is_not_running),
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun adapterRecycler(container: ViewGroup?) {
     recyclerStopWatch.layoutManager=LinearLayoutManager(container?.context,RecyclerView.VERTICAL,false)
     recyclerStopWatch.adapter=AdapterRecyclerStop(list)
     }
    private fun codeBtnReset(container: ViewGroup?) {
        countObject?.cancel()
        progressTimer.progress = 0
        btnStartAndStopStopWatch.setImageResource(R.drawable.ic_baseline_crop_square_24)
        textTimer.text = resources.getText(R.string._00_00_000)
        textCounter.text = resources.getText(R.string._00_00_000)
        check = true
        timeRunning = 0
        timeRunningNewCount = 0
        list.removeAll(list)
        adapterRecycler(container)
    }
    private fun codeBtnStartAndStop() {
        if (check) {
            startRunningWatch(startTime)
        } else {
            inflaterStop?.btnStartAndStopStopWatch?.setImageResource(R.drawable.ic_baseline_crop_square_24)
            countObject?.cancel()
            check = true
        }
    }
    private fun startRunningWatch(startTime:Long) {
       var lastB0=startTime
        countObject=object :CountDownTimer(startTime,1){
           override fun onTick(p0: Long) {
               timeRunning+=(lastB0-p0)
               timeRunningNewCount+=(lastB0-p0)
               getPartTime(timeRunning)
               formatPartTime(textTimer)
               progressTimer.progress=((second.toDouble()/60)*100).toInt()
               if(second.toInt()==0&&minute.toInt()>0){
                 progressTimer.progress=100
               }
               getPartTime(timeRunningNewCount)
               formatPartTime(textCounter)
               lastB0=p0
           }
           override fun onFinish() {
             startRunningWatch(60*60*1000)
           }
           }
          btnStartAndStopStopWatch.setImageResource(R.drawable.ic_baseline_pause_24)
          countObject?.start()
          check=false
      }

    private fun formatPartTime(text:TextView) {
        if(minute.length==1){
            minute="0$minute"
        }
        if(second.length==1){
            second="0$second"
        }
        if(mil.length<3){
            mil = if(mil.length==1){
                "00$mil"
            }else{
                "0$mil"
            }
        }
        text.text="$minute:$second.$mil"
    }

    private fun getPartTime(time:Long) {
        minute = (time / 60 / 1000).toString()
        second = ((time - (minute.toLong() * 60 * 1000)) / 1000).toString()
        mil = (time - ((minute.toLong() *60 * 1000) + (second.toLong() * 1000))).toString()
     }

    companion object {

        fun newInstance(param1: String, param2: String) =
            StopFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}