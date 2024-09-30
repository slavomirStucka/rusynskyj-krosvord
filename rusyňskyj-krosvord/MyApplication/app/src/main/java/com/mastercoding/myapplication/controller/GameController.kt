package com.mastercoding.myapplication.controller

import android.content.Context
import android.media.MediaPlayer
import android.widget.Toast
import com.mastercoding.myapplication.R
import com.mastercoding.myapplication.crossword.Generator
import kotlin.random.Random

class GameController(context: Context) {


    final var context=context
    fun splitWordsIntoChars(wordList: List<String>): List<String> {
        return wordList.flatMap { word ->
            word.map { it.toString() } // Convert each character to a string
        }
    }
    fun from_grid_to_string(grid:MutableMap<Pair<Int, Int>, Char>): List<String>{
        val generator= Generator(context)
        val result = mutableListOf<String>()
        val stringBuilder = StringBuilder()
        val (xMin,yMin,xMax,yMax) = generator.cornersCoordinates(grid)
        for (col in yMin..yMax) {
            val rowStringBuilder = StringBuilder()
            for (row in xMin..xMax) {
                val char = grid[Pair(row, col)] ?: ' ' // Default to space if no character is found
                rowStringBuilder.append(char)
            }
            result.add(rowStringBuilder.toString())
        }

        return result
    }


    fun actual_game_grid(grid:MutableMap<Pair<Int, Int>, Char>):MutableMap<Pair<Int, Int>, Char>{
        val generator= Generator(context)
        val (xMin,yMin,xMax,yMax) = generator.cornersCoordinates(grid)
        var actual_grid: MutableMap<Pair<Int, Int>, Char> = mutableMapOf()
        for (y in yMin..yMax) {
            for (x in xMin..xMax) {
                if (Pair(x, y) in grid && grid[Pair(x, y)] != '#' && (actual_grid[Pair(x,y)]?.isLetter()==false||actual_grid[Pair(x,y)]==null)) {
                    actual_grid[Pair(x,y)]='*'
                }
            }
        }
        return actual_grid
    }

    fun check(grid:MutableMap<Pair<Int, Int>, Char>,actual_grid:MutableMap<Pair<Int, Int>, Char>,guess:String,context:Context,zvuk: Boolean):MutableMap<Pair<Int, Int>, Char>{
        val generator= Generator(context)
        val (xMin,yMin,xMax,yMax) = generator.cornersCoordinates(grid)
        for (y in yMin..yMax) {
            for (x in xMin..xMax) {
                if (grid[Pair(x,y)]==guess[0]){
                    for (h in listOf(true, false)) {
                        if (grid[generator.add(x, y, -1, h)] !='#') {
                            continue
                        }
                        var new_actual_grid: MutableMap<Pair<Int, Int>, Char> = mutableMapOf()
                        new_actual_grid[Pair(x, y)] = guess[0]
                        var index: Int = 1
                        while (index < guess.length) {
                            if (grid[generator.add(x, y, index, h)] == guess[index]) {
                                new_actual_grid[generator.add(x, y, index, h)] =
                                    guess[index]
                                index++
                                if (index == guess.length && grid[generator.add( x,y, index,h)] == '#') {
                                    new_actual_grid =
                                        (actual_grid + new_actual_grid) as MutableMap<Pair<Int, Int>, Char>
                                    if (zvuk){
                                        val mediaPlayerright=MediaPlayer.create(context, R.raw.correctanswersoundeffect)
                                        mediaPlayerright.start()
                                    }
                                    return new_actual_grid
                                }

                            } else {
                                break
                            }
                        }
                    }
                }
            }
        }
        Toast.makeText(context,"BAD GUESS!", Toast.LENGTH_SHORT).show()
        if(zvuk){
            val mediaPlayerbad=MediaPlayer.create(context, R.raw.clickerror)
            mediaPlayerbad.start()
        }
        return actual_grid
    }


    fun hint(grid:MutableMap<Pair<Int, Int>, Char>,unresolvedGrid:MutableMap<Pair<Int, Int>, Char>,context: Context,zvuk:Boolean): MutableMap<Pair<Int, Int>, Char>{
        val generator= Generator(context)
        val (xMin,yMin,xMax,yMax) = generator.cornersCoordinates(grid)

        var randomNumberX= Random.nextInt(xMin,xMax+1)
        var randomNumberY= Random.nextInt(yMin,yMax+1)

        if(isWon(unresolvedGrid,context)){
            return unresolvedGrid
        }

        while(unresolvedGrid[Pair(randomNumberX,randomNumberY)]!='*'){
            randomNumberX= Random.nextInt(xMin,xMax+1)
            randomNumberY= Random.nextInt(yMin,yMax+1)
        }

        val hintedLetter: Char? = grid[Pair(randomNumberX, randomNumberY)]
        if (zvuk){
            val mediaPlayer=MediaPlayer.create(context,R.raw.hinteffect)
            mediaPlayer.start()
        }
        if (hintedLetter != null) {
            unresolvedGrid[Pair(randomNumberX, randomNumberY)] = hintedLetter
        } else {
            return unresolvedGrid
        }
        return unresolvedGrid
    }


    fun isWon(grid: MutableMap<Pair<Int, Int>, Char>, context: Context): Boolean {
        val generator = Generator(context)
        val (xMin, yMin, xMax, yMax) = generator.cornersCoordinates(grid)
        for (y in yMin..yMax) {
            for (x in xMin..xMax) {
                if (grid[Pair(x, y)] == '*') {
                    return false
                }
            }
        }
        //Toast.makeText(context, "Congratulations, you won !!!!!", Toast.LENGTH_LONG).show()
        return true
    }
}