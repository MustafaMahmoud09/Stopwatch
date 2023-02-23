package com.example.stopwatch

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.dialog_ui.view.*
import kotlinx.android.synthetic.main.finish_dialog.view.*
import kotlinx.android.synthetic.main.fragment_timer.*
import kotlinx.android.synthetic.main.fragment_timer.view.*
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TimerFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    private var countDownTimer:CountDownTimer?=null
    private var inflaterTimer:View?=null
    private var sharedPreferences:SharedPreferences?=null
    private var allTimeRunning=0L
    private var minute=""
    private var second=""
    private var mil=""
    private var checkBtn=true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inflaterTimer= inflater.inflate(R.layout.fragment_timer, container, false)
        getTimeStart()
        inflaterTimer!!.btnStartAndStop.setOnClickListener {
            codeStartOrStop(container)
        }
        inflaterTimer!!.btnAddNewTime.setOnClickListener{
            codeBtnAddTime(container)
        }
        inflaterTimer!!.btnReset.setOnClickListener{
            codeBtnReset()
        }
        return inflaterTimer
    }

    private fun codeBtnReset() {
        checkBtn = true
        btnStartAndStop.setImageResource(R.drawable.ic_baseline_crop_square_24)
        try {
            countDownTimer?.cancel()
            codeFinish()
        } catch (ex: Exception) {
            ifSharedNotContainTime()
        }
    }

    private fun codeBtnAddTime(container: ViewGroup?) {
        if (!checkBtn) {
            codeStop()
        }
        val builder = AlertDialog.Builder(context)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_ui, container, false)
        adapterDialog(view)
        builder.setView(view)
        val create = builder.create()
        create.show()
        view.btnCancel.setOnClickListener {
            create.dismiss()
        }
        view.btnAdd.setOnClickListener {
            addTimeToSharedPreference(view)
            create.dismiss()
        }
    }

    private fun addTimeToSharedPreference(view: View) {
        allTimeRunning = view.inputHour.text.toString()
            .toLong() * 60 * 60 * 1000 + view.inputMinute.text.toString().toLong() * 60 * 1000+ view.inputSecond.text.toString().toLong() * 1000
        sharedPreferences =
            this.requireActivity().getSharedPreferences("TimerStopWatch", Context.MODE_PRIVATE)
        val edit = sharedPreferences!!.edit()
        edit.putString("Time", allTimeRunning.toString())
        edit.commit()
        calcTimerAndFormatPartNumber(allTimeRunning)
        codeSetTwoTextAndCalcProgress(true)
    }

    private fun codeSetTwoTextAndCalcProgress(check:Boolean) {
        inflaterTimer!!.textTimerStop.text = "$minute:$second.$mil"
        if(check) {
            inflaterTimer!!.textStartStopWatch.text = "$minute:$second.$mil"
        }
        if(((second.toDouble() / 60) * 100).toInt()==0&&(mil.toDouble()==0.0)){
            inflaterTimer!!.progressStop.progress =100
        }else {
            inflaterTimer!!.progressStop.progress = ((second.toDouble() / 60) * 100).toInt()
        }
    }

    private fun adapterDialog(view: View) {
        val listHour = LinkedList<String>()
        val listMinute = LinkedList<String>()
        val listSecond = LinkedList<String>()
        inputInListReadyToAdapter(listHour, 24)
        inputInListReadyToAdapter(listMinute, 59)
        inputInListReadyToAdapter(listSecond, 59)
        view.inputSecond.setAdapter(
            ArrayAdapter(
                view.context,
                R.layout.simple_adapter,
                listSecond
            )
        )
        view.inputHour.setAdapter(
            ArrayAdapter(
                view.context,
                R.layout.simple_adapter,
                listHour
            )
        )
        view.inputMinute.setAdapter(
            ArrayAdapter(
                view.context,
                R.layout.simple_adapter,
                listMinute
            )
        )
    }

    private fun inputInListReadyToAdapter(listHour: LinkedList<String>, i: Int) {
        for (count in 0..i){
            listHour.add("$count")
        }
    }

    private fun codeStartOrStop(container: ViewGroup?) {
        if (checkBtn) {
            counterRunning(container)
        } else {
            codeStop()
        }
    }

    private fun codeStop() {
        countDownTimer!!.cancel()
        btnStartAndStop.setImageResource(R.drawable.ic_baseline_crop_square_24)
        allTimeRunning = minute.toLong() * 60 * 1000 + second.toLong() * 1000 + mil.toLong()
        checkBtn = true
    }

    private fun getTimeStart() {
        sharedPreferences =
            this.requireActivity().getSharedPreferences("TimerStopWatch", Context.MODE_PRIVATE)
        if (sharedPreferences!!.getString("Time", "")!!.isEmpty()) {
            ifSharedNotContainTime()
        } else {
            getTimeFromSharedAndAnalysis()
        }
    }

    private fun ifSharedNotContainTime() {
        allTimeRunning = 1000
        inflaterTimer!!.textStartStopWatch.text = "00:01.000"
        inflaterTimer!!.textTimerStop.text = "00:01:000"
        inflaterTimer!!.progressStop.progress = ((1.0 / 60) * 100).toInt()
    }
    private fun getTimeFromSharedAndAnalysis() {
        allTimeRunning = sharedPreferences!!.getString("Time", "0")!!.toString().toLong()
        calcTimerAndFormatPartNumber(allTimeRunning)
        codeSetTwoTextAndCalcProgress(true)
    }
    private fun counterRunning(container: ViewGroup?) {
        if (allTimeRunning>1) {
            countDownTimer = object : CountDownTimer(allTimeRunning, 1) {
                override fun onTick(p0: Long) {
                    calcTimerAndFormatPartNumber(p0)
                    codeSetTwoTextAndCalcProgress(false)
                }
                override fun onFinish() {
                    btnStartAndStop.setImageResource(R.drawable.ic_baseline_crop_square_24)
                    try {
                        codeFinish()
                     } catch (ex: Exception) {
                        ifSharedNotContainTime()
                     }
                    dialogFinishTime(container)
                }
            }
            checkBtn = false
            countDownTimer!!.start()
            btnStartAndStop.setImageResource(R.drawable.ic_baseline_pause_24)
        }
    }

    private fun dialogFinishTime(container: ViewGroup?) {
        val dialogFinish = AlertDialog.Builder(context)
        val layoutDialog =
            LayoutInflater.from(context).inflate(R.layout.finish_dialog, container, false)
        dialogFinish.setView(layoutDialog)
        val create = dialogFinish.create()
        create.show()
        layoutDialog.btnCancelFinishMessage.setOnClickListener {
            create.dismiss()
        }
    }

    private fun codeFinish() {
        getTimeFromSharedAndAnalysis()
        checkBtn = true
    }

    private fun calcTimerAndFormatPartNumber(p0: Long) {
        minute = (p0 / (60 * 1000)).toString()
        second = ((p0 - (minute.toInt() * 60 * 1000)) / 1000).toString()
        mil = ((p0 - ((minute.toInt() * 60 * 1000) + second.toInt() * 1000))).toString()
        if (minute.length == 1) {
            minute = "0$minute"
        }
        if (second.length == 1) {
            second = "0$second"
        }
        if (mil.length == 1) {
            mil = "00$mil"
        } else if (mil.length == 2) {
            mil = "0$mil"
        }
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