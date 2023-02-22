package com.example.stopwatch

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class AdapterFragment(var context: FragmentActivity): FragmentStateAdapter(context) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment = when(position){
        0->ClockFragment()
        1->StopFragment()
        else->TimerFragment()
        }
}
