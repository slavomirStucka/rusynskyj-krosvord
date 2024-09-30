package com.mastercoding.myapplication.crossword

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.MutableInt
import com.mastercoding.myapplication.crossword.database.DataBaseHelper


class Generator(context: Context) {
    var sol_g: MutableMap<Pair<Int, Int>, Char> = mutableMapOf()
    final var context=context
    //val dataBaseHelper=DataBaseHelper(context)
    val dataBaseHelper=DataBaseHelper.getInstance(context)
    var letterList=listOf("")
    var soundOn:Boolean=true




    fun createCharacterList(words: List<String>): List<String> {
        val characterSet = mutableSetOf<Char>()
        val characterRepeated= mutableSetOf<Char>()

        for (word in words) {
            val charsInWord = word.toCharArray().toList()
            characterSet.addAll(charsInWord)

            val repeatedChars = charsInWord.groupBy { it }.filter { it.value.size > 1 }.keys
            characterRepeated.addAll(repeatedChars)
        }

        val characterList = characterSet.map { it.toString() }
        val characterRepeatedList=characterRepeated.map { it.toString() }
        val mergedList = characterList + characterRepeatedList
        // Prevedieme množinu na zoznam reťazcov s jedným písmenom
        return mergedList
    }

    fun getWords(level: Int):List<String>{
        dataBaseHelper.initialize();
        val db: SQLiteDatabase = dataBaseHelper.getReadableDatabase()
        println(dataBaseHelper.getWordsFromSpecifiedLevel(1))

        return dataBaseHelper.getWordsFromSpecifiedLevel(level)
    }

    fun generate(level: Int):MutableMap<Pair<Int, Int>, Char> {

        //letterList=createCharacterList(slova)
        val slova =getWords(level)
        var mriezka: MutableMap<Pair<Int, Int>, Char> = mutableMapOf()
        val new_mriezka: MutableMap<Pair<Int, Int>, Char> = mutableMapOf()
        // maps letter to all locations on grid
        val suradnicePismen = mutableMapOf<Char,MutableSet<Pair<Int, Int>>>()
        val new_suradnicePismen = mutableMapOf<Char, MutableSet<Pair<Int, Int>>>()
        val intersects= MutableInt(0)
        // initialize g and letters by inserting the first word
        val prveSlovo = slova[0]
        for (i in prveSlovo.indices) {
            mriezka[Pair(i, 0)] = prveSlovo[i]
            suradnicePismen.computeIfAbsent(prveSlovo[i]) {mutableSetOf() }.add(Pair(i, 0))
        }
        mriezka[Pair(-1, 0)] = '#'
        mriezka[Pair(prveSlovo.length, 0)] = '#'

        println(mriezka)
        println(suradnicePismen)
        println(cornersCoordinates(mriezka))
        print_grid(mriezka)
        mriezka=place_all(mriezka,suradnicePismen,slova.subList(1,slova.size),intersects)
        return mriezka
    }

    fun cornersCoordinates(grid: Map<Pair<Int, Int>, Char>): List<Int> {
        var xMin=0
        var xMax=0
        var yMin=0
        var yMax=0
        for ((x,y)in grid.keys){        //zistenie minimalnych a maximalnych suradnici
            xMin= minOf(xMin,x)
            xMax= maxOf(xMax,x)
            yMin= minOf(yMin,y)
            yMax= maxOf(yMax,y)
        }
        return listOf(xMin,yMin,xMax,yMax)
    }

    //funkcia na printovanie aktualnej mriezky
    fun print_grid(grid: MutableMap<Pair<Int, Int>, Char>){
        val xMin = cornersCoordinates(grid)[0]
        val yMin = cornersCoordinates(grid)[1]
        val xMax = cornersCoordinates(grid)[2]
        val yMax = cornersCoordinates(grid)[3]
        for (y in yMin..yMax) {
            for (x in xMin..xMax) {
                if (Pair(x, y) in grid && grid[Pair(x, y)] != '#') {    //ked je # printuje mederu, inak printuje pismeno
                    print(grid[Pair(x,y)])
                    print(' ')
                } else{
                    print(' ')
                    print(' ')
                }
            }
            println()
        }
    }

    //funkcia na vyplnenie kriza v okoli prisecnika
    fun fill_cross(grid: MutableMap<Pair<Int, Int>, Char>, x:Int, y:Int){
        if(Pair(x+1,y) in grid && grid[Pair(x+1,y)]!='#'){
            if(Pair(x,y+1) in grid && grid[Pair(x,y+1)]!='#') {
                grid[Pair(x+1,y+1)]='#'
            }
            if(Pair(x,y-1) in grid && grid[Pair(x,y-1)]!='#') {
                grid[Pair(x+1,y-1)]='#'
            }
        }
        if(Pair(x-1,y) in grid && grid[Pair(x-1,y)]!='#'){
            if(Pair(x,y+1) in grid && grid[Pair(x,y+1)]!='#') {
                grid[Pair(x-1,y+1)]='#'
            }
            if(Pair(x,y-1) in grid && grid[Pair(x,y-1)]!='#') {
                grid[Pair(x-1,y-1)]='#'
            }
        }
    }

    //funkcia na novu suradnicu podla too ci je to horizontalne alebo vertikalne
    fun add(x:Int,y: Int,c:Int,horizontal:Boolean): Pair<Int,Int>{
        if (horizontal){
            return Pair(x+c,y)
        }
        return Pair(x,y+c)
    }

    //funkcia na najdenie vhodneho priesecnika v krizovke
    fun find_place_for_word(position:Int, word:String, grid: MutableMap<Pair<Int, Int>, Char>, grid_new: MutableMap<Pair<Int, Int>, Char>,
                            letters: MutableMap<Char, MutableSet<Pair<Int, Int>>>, letters_new: MutableMap<Char, MutableSet<Pair<Int, Int>>>, gridX:Int, gridY:Int, horizontal: Boolean, intersections:  MutableInt) : Boolean{

        var bocne_intersections:Int=0
        for (let in 0 until position){
            bocne_intersections=intersections.value
            grid_new[add(gridX,gridY,let-position,horizontal)] = word[let]
            if(add(gridX,gridY,let-position,horizontal) in grid){
                if(grid[add(gridX,gridY,let-position,horizontal)] !=word[let])
                    return false
                bocne_intersections++

            }
            if (word[let] !in letters_new) {
                letters_new[word[let]] = mutableSetOf()
            }
            letters_new[word[let]]?.add(add(gridX,gridY,let-position,horizontal))
        }
        for (let in 0 until word.length-position){
            grid_new[add(gridX,gridY,let,horizontal)] = word[let+position]
            if(add(gridX,gridY,let,horizontal) in grid){
                if (grid[add(gridX,gridY,let,horizontal)] != word[let+position]){
                    return false
                }
                bocne_intersections++
            }
            if (word[let+position] !in letters_new){
                letters_new[word[let+position]]= mutableSetOf()
            }
            letters_new[word[let+position]]?.add(add(gridX,gridY,let,horizontal))
        }
        if (add(gridX,gridY,-position-1,horizontal)in grid && grid[add(gridX,gridY,-position-1,horizontal)]!='#'){
            return false
        }
        grid_new[add(gridX,gridY,-position-1,horizontal)]='#'
        if (add(gridX,gridY,word.length-position,horizontal)in grid && grid[add(gridX,gridY,word.length-position,horizontal)]!='#'){
            return false
        }
        grid_new[add(gridX,gridY,word.length-position,horizontal)]='#'
        intersections.value=bocne_intersections
        return true
    }
    fun place_one_word(grid: MutableMap<Pair<Int, Int>, Char>,letters: MutableMap<Char, MutableSet<Pair<Int, Int>>>,word:String,intersections: MutableInt): MutableList<Triple<MutableMap<Pair<Int, Int>, Char>, MutableMap<Char, MutableSet<Pair<Int, Int>>>, MutableInt>>
    {
        var sol: MutableList<Triple<MutableMap<Pair<Int, Int>, Char>,
                MutableMap<Char, MutableSet<Pair<Int, Int>>>,
                MutableInt>> = mutableListOf()

        for (j in 0 until word.length){
            if(word[j] in letters){
                val pairSet: MutableSet<Pair<Int, Int>>? = letters[word[j]]
                val (gridX, gridY) = pairSet!!.first()
                //for ((gridX, gridY) in (letters[word[j]] ?: emptySet<Pair<Int, Int>>())) {
                for (horizontal in listOf(true,false)){
                    var newGrid = mutableMapOf<Pair<Int, Int>, Char>()
                    var newLetters = mutableMapOf<Char, MutableSet<Pair<Int, Int>>>()
                    var newIntersections =intersections
                    if(find_place_for_word(j,word,grid,newGrid,letters,newLetters,gridX,gridY,horizontal,newIntersections)){
                        //newGrid.putAll(grid)
                        newGrid= (grid+newGrid) as MutableMap<Pair<Int, Int>, Char>
                        //newLetters.putAll(letters)
                        ((letters+newLetters) as MutableMap<Char, MutableSet<Pair<Int, Int>>>).also { newLetters = it }
                        for (jj: Int in 0 until j){
                            val result = add(gridX, gridY, jj - j, horizontal)
                            fill_cross(newGrid, result.first,result.second )
                        }
                        for (jj in 0 until word.length-j){
                            val result2 = add(gridX, gridY, jj, horizontal)
                            fill_cross(newGrid, result2.first,result2.second )

                        }
                        sol.add(Triple(newGrid, newLetters, newIntersections))
                    }
                }
            }
        }

        return sol
    }
    var max_int: Int = 0  // This is a top-level variable in a file
    fun place_all(grid: MutableMap<Pair<Int, Int>, Char>,letters: MutableMap<Char, MutableSet<Pair<Int, Int>>>,words: List<String>,intersections: MutableInt) :MutableMap<Pair<Int, Int>, Char>{
        if(words.isNotEmpty()){
            val word=words[0]
            for ((solution_grid,solution_letters,n_intersections)in place_one_word(grid,letters,word,intersections)){
                place_all(solution_grid,solution_letters,words.subList(1, words.size),n_intersections)
                //print_grid(solution_grid)
                if(n_intersections.value>max_int){
                    max_int=n_intersections.value
                    if(sol_g.size<solution_grid.size){
                        sol_g=solution_grid
                    }
                    print_grid(sol_g)
                }
            }
        }
        return sol_g
    }


}