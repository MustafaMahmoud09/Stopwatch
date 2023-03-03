package com.example.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.system.Os.close
import android.view.Gravity
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buildFragmentAndSetInViewPagerAndConnectToTabs()
        supportToolbarAsActionBar()
        createNavInTool()
        nav.setNavigationItemSelectedListener {
            codeSelectItemNav(it)
            true
        }
    }

    private fun codeSelectItemNav(it: MenuItem) {
        when (it.itemId) {
            R.id.itemClock -> {
                createFragment(0)
            }
            R.id.itemStopwatch -> {
                createFragment(1)
            }
            R.id.itemTimer -> {
                createFragment(2)
            }
        }
    }

    private fun createFragment(num:Int) {
        viewPagerFragment.currentItem = num
        draw.closeDrawer(Gravity.LEFT)
    }

    private fun supportToolbarAsActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
    }

    private fun createNavInTool() {
        val showNavInToolbar = ActionBarDrawerToggle(this, draw, toolbar, R.string.open, R.string.close)
        draw.addDrawerListener(showNavInToolbar)
        showNavInToolbar.syncState()
    }

    override fun onBackPressed() {
        if(draw.isDrawerOpen(Gravity.LEFT)){
            draw.closeDrawer(Gravity.LEFT)
        }else {
            super.onBackPressed()
        }
        }
    private fun buildFragmentAndSetInViewPagerAndConnectToTabs() {
        val adapter = AdapterFragment(this)
        viewPagerFragment.adapter = adapter
        TabLayoutMediator(
            tabs,
            viewPagerFragment,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                when (position) {
                    0 -> {
                        tab.text = resources.getText(R.string.clock)
                    }
                    1 -> {
                        tab.text = resources.getText(R.string.stopwatch)
                    }
                    else -> {
                        tab.text = resources.getText(R.string.timer)
                    }
                }
            }).attach()
    }
}
