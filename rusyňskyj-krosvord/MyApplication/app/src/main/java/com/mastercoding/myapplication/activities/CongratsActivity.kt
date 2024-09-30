package com.mastercoding.myapplication.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.LinearGradient
import android.graphics.Shader
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.mastercoding.myapplication.R
import com.mastercoding.myapplication.crossword.database.DataBaseHelper
import com.mastercoding.myapplication.crossword.TranslationsAdapter

class CongratsActivity : AppCompatActivity() {

    lateinit var nextlevel_button: ImageButton
    lateinit var textik: TextView
    lateinit var ellipseImage: ImageView
    lateinit var gratulacija: TextView
    lateinit var nextlevel_value:TextView
    lateinit var addedCoins: ImageView
    lateinit var translations: GridView
    var zvukEnabled:Boolean=true
    var hudbaEnabled:Boolean=true


    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_congrats)

        addedCoins=findViewById(R.id.addedCoins)
        nextlevel_button=findViewById(R.id.nextlevel_button)
        ellipseImage=findViewById(R.id.successed_level)
        textik=findViewById(R.id.level)
        nextlevel_value=findViewById(R.id.nextlevel_value)
        gratulacija=findViewById(R.id.gratulacija)
        translations=findViewById(R.id.gridView_Translations)

        setGradient(gratulacija)

        val dataBaseHelper=DataBaseHelper.getInstance(this)
        textik.text=intent.getIntExtra("level",0).toString()
        zvukEnabled=intent.getBooleanExtra("zvukEnabled",true)
        hudbaEnabled=intent.getBooleanExtra("hudbaEnabled",true)

        val currentLevel=intent.getIntExtra("level",0)

        val nextLevel=intent.getIntExtra("level",0)+1

        val nextLevelCoins=intent.getIntExtra("coins",0)+120
        nextlevel_value.text="LEVEL " + (nextLevel.toString())
        var gridItems= listOf("RUSYN","SLOVAK")
        gridItems=gridItems+dataBaseHelper.getTranslations(currentLevel)
        translations.adapter= TranslationsAdapter(this,gridItems)


        saveLevel(nextLevelCoins,nextLevel)

        nextlevel_button.setOnClickListener{
            if (nextLevel==22){
                var i2= Intent(this, MainActivity::class.java)
                startActivity(i2)
            }
            var i= Intent(this, GameActivity::class.java)
            i.putExtra("nextLevel",nextLevel)
            i.putExtra("coins",nextLevelCoins)
            i.putExtra("zvukEnabled",zvukEnabled)
            i.putExtra("hudbaEnabled",hudbaEnabled)
            startActivity(i)
        }

    }


    fun saveLevel(coins:Int,level:Int){
        val sharedPreferences=getSharedPreferences("Score", MODE_PRIVATE)
        val myEdit = sharedPreferences.edit()
        myEdit.putInt("level",level)
        myEdit.putInt("coins",coins)
        myEdit.apply()
    }

    fun setGradient(gratulacija:TextView){
        val paint=gratulacija.paint
        val width= paint.measureText(gratulacija.text.toString())
        gratulacija.paint.shader=LinearGradient(
            0f,0f,width,gratulacija.textSize,
            intArrayOf(android.graphics.Color.parseColor("#49FF00"),
                android.graphics.Color.parseColor("#FF000000"),
                android.graphics.Color.parseColor("#49FF00")
            ),null,Shader.TileMode.REPEAT
        )
    }
}