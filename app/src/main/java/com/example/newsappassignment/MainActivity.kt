package com.example.newsappassignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.newsappassignment.fragments.FactsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, FactsFragment.newInstance())
                .commitNow()
        }
    }
}
