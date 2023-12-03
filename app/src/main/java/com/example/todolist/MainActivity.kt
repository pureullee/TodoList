package com.example.todolist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.todolist.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity(){

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentList = listOf(MainFragment(), CheckFragment(), CalendarFragment())
        val adapter = ViewPagerAdapter(fragmentList, supportFragmentManager, lifecycle)

        binding.viewPager.adapter = adapter

        // 뷰 페이저를 이용한 프래그먼트 전환
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "할 일"
                1 -> tab.text = "관리"
                2 -> tab.text = "달력"
            }
        }.attach()

    }

}
