package com.selflearning.json_and_recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Button
import android.widget.EditText

import android.widget.Toast
import com.github.kittinunf.fuel.core.Parameters
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result

import android.content.Intent
import android.view.MenuItem
import android.view.Menu
import android.view.MenuInflater

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Creating the adapter object here
        val songListView = findViewById<RecyclerView>(R.id.webSongDataDisplay)
        songListView.layoutManager = LinearLayoutManager(this)

        var songsList = listOf<Song>()
        /*
        When a lambda function is the last parameter for an object, it is recommended
        to move it outside of the parentheses. This is done so that your code looks
        much cleaner and easier to read.
        */
        val myAdapter = MySongAdapter(songsList) {Toast.makeText(this, "${songsList[it].title}, ${songsList[it].artist}, ${songsList[it].year}", Toast.LENGTH_LONG).show()}
        songListView.adapter = myAdapter

        val nameEntry = findViewById<EditText>(R.id.songArtistSearchName)
        val songSearchBtn = findViewById<Button>(R.id.searchSongArtistBtn)
        //val resultView = findViewById<TextView>(R.id.webTextDisplay)
        //resultView.movementMethod = ScrollingMovementMethod()
        // Button listener that will send requests to the server
        songSearchBtn.setOnClickListener {

            // First attempt to access the value entered in the EditText
            if(nameEntry?.text.toString().trim() == ""){
                // Right now, we want the user to enter some text into this filed before submitting
                Toast.makeText(this, "No name entered in the text field above.", Toast.LENGTH_SHORT).show()
                nameEntry.error = "Artist name field shouldn't be empty."
            } else {
                // The EditText isn't empty, so get the value and then send the request to the server
                val artistName = nameEntry.text.toString()

                // Use 10.0.2.2:3000 when running the server on the same machine as the app
                val url = "http://10.0.2.2:3000/artist/${artistName}"
                url.httpGet().responseObject<List<Song>> { request, response, result ->

                    when(result) {
                        is Result.Success -> {
                            // Parsing not done right now, we'll just be displaying raw data
                            //resultView.text = result.get().decodeToString()

                            // Setting the variable 'songs' to the list obtained from the web search
                            myAdapter.songs = result.get()

                            // We also need to tell the adapter to update itself with new data
                            // when you perform a new song search so that the RecyclerView gets redrawn
                            myAdapter.notifyDataSetChanged()

                        }

                        is Result.Failure -> {
                            // Failure is when there is an HTTP error
                            AlertDialog.Builder(this)
                                .setNeutralButton("OK", null)
                                .setMessage("A network error occurred: ${result.error.message}")
                                .show()
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.addsongActivity -> {
                val intent = Intent(this, AddNewSongActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return false
    }
}

data class Song (
    val title: String,
    val artist: String,
    val day: Int,
    val month: String,
    val year: Int,
    val chart: Int,
    val likes: Int,
    val downloads: Int,
    val ID: Int,
    val review: String,
    val quantity: Int
)