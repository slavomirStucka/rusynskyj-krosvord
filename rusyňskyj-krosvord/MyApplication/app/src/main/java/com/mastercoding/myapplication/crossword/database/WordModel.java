package com.mastercoding.myapplication.crossword.database;

public class WordModel {
    private int id;
    private String rusyn_word;
    private String slovak_word;
    private int level;

    public WordModel(int id,String rusyn_word, String slovak_word, int level){
        this.id=id;
        this.rusyn_word=rusyn_word;
        this.slovak_word=slovak_word;
        this.level=level;
    }
    public WordModel(){
    }



    @Override
    public String toString() {
        return "WordModel{" +
                "id=" + id +
                ", word='" + rusyn_word + '\'' +
                ", word='" + slovak_word + '\'' +
                ", level=" + level +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRusyn_word() {
        return rusyn_word;
    }
    public String getSlovak_word() {
        return slovak_word;
    }


    public void setRusyn_word(String word) {
        this.rusyn_word = word;
    }
    public void setSlovak_word(String word) {
        this.slovak_word = word;
    }


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
