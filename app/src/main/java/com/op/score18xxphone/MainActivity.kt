package com.op.score18XXphone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.op.score18XXphone.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val tabIds = listOf(
        R.id.setupFragment,
        R.id.companiesFragment,
        R.id.albumsFragment,
        R.id.summaryFragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, systemBars.top, 0, 0)
            insets
        }

        binding.viewPager.adapter = MainPagerAdapter(this)
        binding.viewPager.offscreenPageLimit = 3

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.bottomNavView.selectedItemId = tabIds[position]
            }
        })

        binding.bottomNavView.setOnItemSelectedListener { item ->
            val index = tabIds.indexOf(item.itemId)
            if (index >= 0) binding.viewPager.currentItem = index
            true
        }
    }

    override fun onPause() {
        super.onPause()
        Persistence.save()
    }
}
