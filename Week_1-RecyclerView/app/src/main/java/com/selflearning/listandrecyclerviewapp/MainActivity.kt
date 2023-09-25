package com.selflearning.listandrecyclerviewapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create your adapter object here
        val modListView = findViewById<RecyclerView>(R.id.moduleListView)
        modListView.layoutManager = LinearLayoutManager(this)

        val moduleTitlesList = listOf("Immersive Technologies", "Mobile Application Development", "Web Application Development")
        val moduleDescriptionList = listOf("Learn about developing augmented reality applications for the web, making use of three.js and A-Frame.",
                                            "Learn about Android development using Kotlin.",
                                            "Learn about web application development, including web APIs, JSON and AJAX")

        modListView.adapter = MyAdapter(moduleTitlesList, moduleDescriptionList)
    }
}