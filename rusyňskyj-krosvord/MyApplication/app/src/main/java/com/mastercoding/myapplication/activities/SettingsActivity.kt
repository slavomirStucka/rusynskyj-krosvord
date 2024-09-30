package com.mastercoding.myapplication.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import com.mastercoding.myapplication.R
import com.mastercoding.myapplication.controller.MusicController

class SettingsActivity : AppCompatActivity() {
    lateinit var x_button: ImageButton
    lateinit var hudbaImageView: ImageView
    lateinit var zvukImageView: ImageView
    lateinit var hudbaCheckBox: CheckBox
    lateinit var zvukCheckBox: CheckBox
    private var zvukEnabled=true
    private var hudbaEnabled=true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        x_button=findViewById(R.id.X_button)
        hudbaImageView=findViewById(R.id.hudba_settings)
        zvukImageView=findViewById(R.id.zvuk_settings)
        hudbaCheckBox=findViewById(R.id.checkbox_hudba)
        zvukCheckBox=findViewById(R.id.checkbox_zvuk)


        val activityNumber=intent.getIntExtra("activityNumber",1)
        val level=intent.getIntExtra("currentLevel",1)
        val coins=intent.getIntExtra("coins",120)
        hudbaEnabled=intent.getBooleanExtra("hudbaEnabled",true)
        zvukEnabled=intent.getBooleanExtra("zvukEnabled",true)
        hudbaCheckBox.isChecked=hudbaEnabled
        zvukCheckBox.isChecked=zvukEnabled
        val musicController=MusicController.getInstance(this)
        if (!musicController.mediaPlayer.isPlaying&&hudbaEnabled) {
            musicController.startMusic()
        }

        hudbaCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                musicController.startMusic()
            }
            else{
                musicController.pauseMusic()
            }
            hudbaEnabled=isChecked
        }

        zvukCheckBox.setOnCheckedChangeListener { _, isChecked ->
            zvukEnabled=isChecked
        }

        x_button.setOnClickListener{

            if (activityNumber==1){
                var i= Intent(this, MainActivity::class.java)
                i.putExtra("zvukEnabled",zvukEnabled)
                i.putExtra("hudbaEnabled",hudbaEnabled)
                startActivity(i)
            }
            else{
                var i= Intent(this, GameActivity::class.java)
                i.putExtra("zvukEnabled",zvukEnabled)
                i.putExtra("hudbaEnabled",hudbaEnabled)
                i.putExtra("nextLevel",level)
                i.putExtra("coins",coins)
                startActivity(i)
            }


        }


    }


}