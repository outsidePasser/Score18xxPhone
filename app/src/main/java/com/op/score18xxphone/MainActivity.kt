package com.op.score18xxphone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.op.score18xxphone.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(Setup())

        binding.bottomNavigationView.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.setup -> replaceFragment(Setup())
                R.id.companies -> replaceFragment(Companies())
                R.id.players -> replaceFragment(Players())
                R.id.spreadsheet -> replaceFragment(Spreadsheet())
                else -> {
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment : Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()


    }
}
