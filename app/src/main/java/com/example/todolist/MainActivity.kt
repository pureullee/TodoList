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

        val fragmentList = listOf(MainFragment(), CheckFragment())
        val adapter = ViewPagerAdapter(fragmentList, supportFragmentManager, lifecycle)

        binding.viewPager.adapter = adapter

        // 뷰 페이저를 이용한 프래그먼트 전환
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "할 일"
                1 -> tab.text = "관리"
            }
        }.attach()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // 탭이 선택되었을 때 해당 프래그먼트로 전환
                val selectedFragment = fragmentList[tab?.position ?: 0]
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, selectedFragment)
                    .commit()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                //미구현
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {
                //미구현
            }
        })


    }

}
