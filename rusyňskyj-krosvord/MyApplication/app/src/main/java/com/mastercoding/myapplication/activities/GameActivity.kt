package com.mastercoding.myapplication.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.GridView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.mastercoding.myapplication.controller.GameController
import com.mastercoding.myapplication.crossword.Generator
import com.mastercoding.myapplication.crossword.LetterAdapter
import com.mastercoding.myapplication.R


class GameActivity : AppCompatActivity() {

    val generator= Generator(this)
    val gameController= GameController(this)

    lateinit var guess_button: ImageButton
    lateinit var hint_button: ImageButton
    lateinit var shuffle_button: ImageButton
    lateinit var delete_button: ImageButton
    lateinit var available_coins_view: TextView
    lateinit var linearLayoutLetters: LinearLayout
    lateinit var linearLayoutGuess: LinearLayout
    lateinit var editText: EditText
    lateinit var coinsUpperImage: ImageView
    lateinit var settingsButton: ImageButton
    lateinit var levelSquare: ImageView
    lateinit var levelValue: TextView
    var level: Int = 1
    var coins: Int= 120
    var zvukEnabled:Boolean = true
    var hudbaEnabled:Boolean = true
    lateinit var customAdapter:LetterAdapter


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)


        //INICIALIZACIA VIEWOV
        var gridView: GridView= findViewById(R.id.gridView_Crossword)
        guess_button=findViewById(R.id.guess_button)
        hint_button=findViewById(R.id.napoveda_button)
        shuffle_button=findViewById(R.id.shuffle_button)
        delete_button=findViewById(R.id.delete_character_button)
        available_coins_view=findViewById(R.id.available_coins)
        coinsUpperImage=findViewById(R.id.coins_upper)
        settingsButton=findViewById(R.id.settingsButton)
        levelSquare=findViewById(R.id.currentLevel)
        levelValue=findViewById(R.id.currentLevelValue)
        level=intent.getIntExtra("nextLevel",1)
        levelValue.text=level.toString()
        coins=intent.getIntExtra("coins",120)
        zvukEnabled=intent.getBooleanExtra("zvukEnabled",true)
        hudbaEnabled=intent.getBooleanExtra("hudbaEnabled",true)
        available_coins_view.text=coins.toString()

///////////////////////////////////////////////////////////////////////////////////////////////
        //PRIPRAVA HERNEJ MRIEZKY
        var game_grid_gen: MutableMap<Pair<Int, Int>, Char> = mutableMapOf()
        game_grid_gen=generator.generate(level)
        var unresolved_game_grid: MutableMap<Pair<Int, Int>, Char> = gameController.actual_game_grid(game_grid_gen)
        var griditemsBefore= gameController.from_grid_to_string(unresolved_game_grid)
        var gridItems=gameController.splitWordsIntoChars(griditemsBefore)
        setWidthOfGrid(gridView,generator,unresolved_game_grid,gridItems)

//////////////////////////////////////////////////////////////////////////////////////
        //PRIPRAVA DOSTUPNYCH PISMEN
        linearLayoutLetters=findViewById(R.id.guessableLetters)
        linearLayoutGuess=findViewById(R.id.guessedWord)

        setWidthOfGuessableLetters(linearLayoutLetters, unresolved_game_grid)


        editText=findViewById(R.id.guess_word)
        editText.visibility=View.INVISIBLE

        var slovo=""
        initializeGuessLetters(generator, linearLayoutLetters, editText)
        initializeGuessWordLetters(linearLayoutGuess,slovo)


///////////////////////////////////////////////////////////////////////////////////////////////

        guess_button.setOnClickListener{
            val guess=editText.text.toString()
            if(!guess.equals("")){
                unresolved_game_grid=(gameController.check(game_grid_gen,unresolved_game_grid,guess,this,zvukEnabled))
                gridItems=gameController.splitWordsIntoChars(gameController.from_grid_to_string(unresolved_game_grid))
                customAdapter= LetterAdapter(this,gridItems)
                gridView.adapter=customAdapter
                editText.setText("")
                slovo=""
                if(gameController.isWon(unresolved_game_grid,this)){
                    var i= Intent(this, CongratsActivity::class.java)
                    i.putExtra("level", level)
                    i.putExtra("coins", coins)// Passing level as extra data
                    i.putExtra("zvukEnabled",zvukEnabled)
                    i.putExtra("hudbaEnabled",hudbaEnabled)
                    startActivity(i)
                }
                initializeGuessLetters(generator, linearLayoutLetters, editText)
                initializeGuessWordLetters(linearLayoutGuess,slovo)
            }else{
                Toast.makeText(this,"You need to create word!",Toast.LENGTH_SHORT).show()
            }

        }


        hint_button.setOnClickListener{
            if (coins>=60){
                unresolved_game_grid=gameController.hint(game_grid_gen,unresolved_game_grid,this,zvukEnabled)
                gridItems=gameController.splitWordsIntoChars(gameController.from_grid_to_string(unresolved_game_grid))
                customAdapter= LetterAdapter(this,gridItems)
                gridView.adapter=customAdapter
                coins -= 60
                animationForCoins()
                available_coins_view.text=coins.toString()


                if(gameController.isWon(unresolved_game_grid,this)){
                    var i= Intent(this, CongratsActivity::class.java)
                    i.putExtra("level", level)
                    i.putExtra("coins", coins)// Passing level as extra data
                    i.putExtra("zvukEnabled",zvukEnabled)
                    i.putExtra("hudbaEnabled",hudbaEnabled)
                    startActivity(i)
                }
            }else{
                Toast.makeText(this,"You don't have enough money!",Toast.LENGTH_SHORT).show()
            }
        }

        shuffle_button.setOnClickListener{
            initializeShuffledLetters(generator, linearLayoutLetters, editText)
            shuffle_button.animate().rotationBy(360F).setDuration(300).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    shuffle_button.rotation = 0F
                }
            }).start()
        }

        delete_button.setOnClickListener{
            editText.setText("")
            slovo=""
            initializeGuessLetters(generator, linearLayoutLetters, editText)
            initializeGuessWordLetters(linearLayoutGuess,slovo)
        }

        settingsButton.setOnClickListener{
            var i= Intent(this, SettingsActivity::class.java)
            i.putExtra("activityNumber",2)
            i.putExtra("currentLevel",level)
            i.putExtra("coins",coins)
            i.putExtra("zvukEnabled",zvukEnabled)
            i.putExtra("hudbaEnabled",hudbaEnabled)
            startActivity(i)
        }



    }


    fun addViewGuessedWord(viewParent: LinearLayout,text: String){
        var linearLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        var textView = TextView(this)
        textView.layoutParams=linearLayoutParams
        textView.background= this.resources.getDrawable(R.drawable.rectanglefilled)
        //textView.setTextColor()=this.resources.getColor(R.color.black)
        textView.gravity=Gravity.CENTER
        val pismeno = StringBuilder()
        pismeno.append(" ").append(text).append(" ")
        textView.text = pismeno.toString()
        textView.isClickable=true
        textView.isFocusable=true

        val screenWidthPixels = resources.displayMetrics.widthPixels
        val textSize = screenWidthPixels * 0.078f
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)

        //textView.textSize= 30F

        textView.setTypeface(null, Typeface.BOLD)
        viewParent.addView(textView)
    }


    //NASTAVENIE DOSTUPNYCH PISMEN
    fun addViewGuessableLetters(viewParent: LinearLayout,text: String, editText: EditText){
        var linearLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        linearLayoutParams.rightMargin=15
        linearLayoutParams.leftMargin=15

        var textView = TextView(this)
        textView.layoutParams=linearLayoutParams
        textView.background= this.resources.getDrawable(R.drawable.rectangleempty)
        //textView.setTextColor()=this.resources.getColor(R.color.black)
        textView.gravity=Gravity.CENTER
        val pismeno = StringBuilder()
        pismeno.append(" ").append(text).append(" ")
        textView.text = pismeno.toString()
        textView.isClickable=true
        textView.isFocusable=true

        val screenWidthPixels = resources.displayMetrics.widthPixels
        val textSize = screenWidthPixels * 0.075f
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)


        //val screenWidthPixels = resources.displayMetrics.widthPixels
        //val desiredTextSize = 30F
        //textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, desiredTextSize)

        //textView.textSize= 30F


        textView.setTypeface(null, Typeface.BOLD)


        textView.setOnClickListener{
            editText.setText(editText.getText().toString() + text);
            initializeGuessWordLetters(linearLayoutGuess,editText.text.toString())
            textView.animate().alpha(0F).setDuration(300)
            textView.isEnabled=false
        }

        viewParent.addView(textView)


    }

    fun initializeGuessWordLetters(linearLayout: LinearLayout, actual_guess_word: String){
        linearLayout.removeAllViews()
        val wordToChars = actual_guess_word.toCharArray().toList()
        val letters = wordToChars.map { it.toString() }
        val layoutParams = linearLayout.layoutParams as ConstraintLayout.LayoutParams
        //layoutParams.width =  letters.size*100
        linearLayout.layoutParams = layoutParams
        for (letter in letters){
            addViewGuessedWord(linearLayout,letter)
        }
        linearLayout.gravity=Gravity.CENTER
    }

    //FUNKCIA ZOBRRAZI PISMENA
    fun initializeGuessLetters(generator: Generator,linearLayout: LinearLayout,editText: EditText){
        linearLayout.removeAllViews()
        val letters = generator.createCharacterList(generator.getWords(level))
        for (letter in letters){
            addViewGuessableLetters(linearLayout,letter,editText)
        }
        linearLayout.gravity=Gravity.CENTER
    }

    //FUNKCIA POMIESA A ZOBRAZI PISMENA
    fun initializeShuffledLetters(generator: Generator,linearLayout: LinearLayout,editText: EditText){
        linearLayout.removeAllViews()
        val letters = generator.createCharacterList(generator.getWords(level))
        val shuffledLetters=letters.shuffled()
        for (letter in shuffledLetters){
            addViewGuessableLetters(linearLayout,letter,editText)
        }
    }


    //FUNKCIA NASTAVI SIRKU JEDNOTLIVYCH POLICOK
    fun setWidthOfGrid(gridView: GridView,generator: Generator,unresolved_game_grid:MutableMap<Pair<Int, Int>, Char>,gridItems :List<String> ){
        val (xMin,yMin,xMax,yMax) = generator.cornersCoordinates(unresolved_game_grid)

        val gridViewForWidth = findViewById<GridView>(R.id.gridView_Crossword)
        val layoutParams = gridViewForWidth.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.width =  (xMax-xMin+1)*100

        var customAdapter = LetterAdapter(this, gridItems)
        gridView.numColumns=xMax-xMin+1
        gridView.adapter=customAdapter
        gridView.layoutParams = layoutParams
    }

    fun setWidthOfGuessableLetters(linearLayout: LinearLayout,unresolved_game_grid:MutableMap<Pair<Int, Int>, Char>){
        linearLayout.removeAllViews()
        val letters = generator.createCharacterList(generator.getWords(level))
        val count =letters.size
        val (xMin,yMin,xMax,yMax) = generator.cornersCoordinates(unresolved_game_grid)
        val layoutParams = linearLayout.layoutParams as ConstraintLayout.LayoutParams
      //  layoutParams.width = count*140-30
        linearLayout.layoutParams = layoutParams
    }

    fun animationForCoins(){
        coinsUpperImage.animate()
            .scaleX(1.5f)
            .scaleY(1.5f)
            .setDuration(1000)
            .withEndAction {

                coinsUpperImage.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(0)
                    .start()
            }
            .start()

        available_coins_view.animate()
            .scaleX(1.5f)
            .scaleY(1.5f)
            .setDuration(1000)
            .withEndAction {
                available_coins_view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(0)  //immediate effect
                    .start()
            }
            .start()
    }


}