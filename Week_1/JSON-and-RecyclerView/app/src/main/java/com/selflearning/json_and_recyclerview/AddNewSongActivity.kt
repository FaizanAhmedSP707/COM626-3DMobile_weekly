package com.selflearning.json_and_recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Button

import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import android.widget.Toast

class AddNewSongActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addsong)

        val songTitleField = findViewById<EditText>(R.id.songTitle_field)
        val songArtistField = findViewById<EditText>(R.id.songArtist_field)
        val songYearField = findViewById<EditText>(R.id.songYear_field)
        val addNewSongBtn = findViewById<Button>(R.id.addSongBtn)

        addNewSongBtn.setOnClickListener {
            // Check for non-null values first and display error messages as appropriate
            if(songTitleField?.text.toString().trim() == "") {
                songTitleField.error = "The title field cannot be empty!"
            } else if(songArtistField?.text.toString().trim() == "") {
                songArtistField.error = "The artist field cannot be empty!"
            } else if(songYearField?.text.toString().trim() == "") {
                songYearField.error = "The year field cannot be empty!"
            } else {

                // Obtain the values entered in each of the EditTexts to use them for sending a POST request to the server
                val songtitle = songTitleField.text.toString()
                val songartist = songArtistField.text.toString()
                val songyear = songYearField.text.toString().toInt()

                // Do the web stuff here: setting the URL and sending the POST request
                val url = "http://192.168.98.222:3000/song/create"
                val httppostData = listOf("title" to songtitle, "artist" to songartist, "year" to songyear)
                url.httpPost(httppostData).response {request, response, result ->
                    when(result) {
                        is Result.Success -> {
                            // If the POST request is successful
                            Toast.makeText(this, result.get().decodeToString(), Toast.LENGTH_LONG).show()
                        }

                        is Result.Failure -> {
                            // If the POST request failed
                            Toast.makeText(this, result.error.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
}