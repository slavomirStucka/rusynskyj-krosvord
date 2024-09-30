package com.mastercoding.myapplication.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mastercoding.myapplication.R
import com.mastercoding.myapplication.controller.MusicController


class MainActivity : AppCompatActivity() {

    lateinit var playButton: ImageButton
    lateinit var game_title:TextView
    lateinit var rusyn_symbol:ImageView
    lateinit var settingsButton: ImageButton


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playButton=findViewById(R.id.play_button)
        game_title=findViewById(R.id.game_title)
        rusyn_symbol=findViewById(R.id.rusyn_symbol)
        settingsButton=findViewById(R.id.settings)

        val zvukEnabled=intent.getBooleanExtra("zvukEnabled",true)
        val hudbaEnabled=intent.getBooleanExtra("hudbaEnabled",true)

        val musicController= MusicController.getInstance(this)
        if (!musicController.mediaPlayer.isPlaying&&hudbaEnabled) {
            musicController.startMusic()
        }


        val sharedPreferences=getSharedPreferences("Score", MODE_PRIVATE)
        val levelToGo=sharedPreferences.getInt("level",1)
        val coins=sharedPreferences.getInt("coins",120)

        playButton.setOnClickListener {
            var i=Intent(this, GameActivity::class.java)
            i.putExtra("nextLevel",levelToGo)  //levelToGo tu daj kamosko
            i.putExtra("coins",coins)    //coins tu daj
            i.putExtra("zvukEnabled",zvukEnabled)
            i.putExtra("hudbaEnabled",hudbaEnabled)
            startActivity(i)
        }

        settingsButton.setOnClickListener{
            var i= Intent(this, SettingsActivity::class.java)
            i.putExtra("activityNumber",1)
            i.putExtra("zvukEnabled",zvukEnabled)
            i.putExtra("hudbaEnabled",hudbaEnabled)
            startActivity(i)
        }

    }




}