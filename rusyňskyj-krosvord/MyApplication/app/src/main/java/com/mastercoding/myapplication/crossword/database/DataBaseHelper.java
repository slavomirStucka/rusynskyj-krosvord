package com.mastercoding.myapplication.crossword.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static DataBaseHelper instance;
    public static final String WORDS_TABLE = "WORDS_TABLE";
    public static final String COLUMN_WORD_RUSYN = "RUSYN_WORD";
    public static final String COLUMN_LEVEL = "LEVEL";
    public static final String COLUMN_WORD_SLOVAK = "SLOVAK_WORD";
    public static final String COLUMN_ID = "ID";

    public static synchronized DataBaseHelper getInstance(Context context) {
        // If instance does not exist, we will create it
        if (instance == null) {
            instance = new DataBaseHelper(context);
        }
        return instance;
    }
    private DataBaseHelper(@Nullable Context context) {
        super(context, "word.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement= "CREATE TABLE " + WORDS_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_WORD_RUSYN + " TEXT, "+ COLUMN_WORD_SLOVAK + " TEXT, " + COLUMN_LEVEL + " INT)";
        db.execSQL(createTableStatement);
        inserting(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addOne(WordModel wordModel,SQLiteDatabase db){


        ContentValues cv = new ContentValues();
        cv.put(COLUMN_WORD_RUSYN, wordModel.getRusyn_word());
        cv.put(COLUMN_WORD_SLOVAK, wordModel.getSlovak_word());
        cv.put(COLUMN_LEVEL, wordModel.getLevel());

        long insert = db.insert(WORDS_TABLE, null, cv);
        return insert != -1; // Return true if insertion is successful, false otherwise


    }

    public List<String> getTranslations(int level){
        List<String> returnList=new ArrayList<>();
        String queryString ="SELECT " + COLUMN_WORD_RUSYN + "," + COLUMN_WORD_SLOVAK + " FROM " + WORDS_TABLE + " WHERE " + COLUMN_LEVEL + " = ?";

        SQLiteDatabase db=this.getReadableDatabase();
        String[] selectionArgs = { String.valueOf(level) };

        Cursor cursor=db.rawQuery(queryString,selectionArgs);

        if(cursor.moveToFirst()){
            do{
                String rusynword = cursor.getString(0);
                String slovakword = cursor.getString(1);
                returnList.add(rusynword);
                returnList.add(slovakword);
            }while(cursor.moveToNext());
        }else{
            System.out.println("GettingWords Error.");
        }
        cursor.close();
        db.close();
        return returnList;
    }
    public List<String> getWordsFromSpecifiedLevel(int level){
        List<String> returnList=new ArrayList<>();
        String queryString ="SELECT " + COLUMN_WORD_RUSYN + " FROM " + WORDS_TABLE + " WHERE " + COLUMN_LEVEL + " = ?";

        SQLiteDatabase db=this.getReadableDatabase();
        String[] selectionArgs = { String.valueOf(level) };

        Cursor cursor=db.rawQuery(queryString,selectionArgs);

        if(cursor.moveToFirst()){
            do{
                String word = cursor.getString(0);
                returnList.add(word);
            }while(cursor.moveToNext());
        }else{
            System.out.println("GettingWords Error.");
        }
        cursor.close();
        db.close();
        return returnList;

    }

    void inserting(SQLiteDatabase db){
        for (WordModel word : wordsList) {
            boolean success= this.addOne(word,db);
        }
    }
    public void initialize(){
        System.out.println("Database initialised.");
    }
    private static final List<WordModel> wordsList = new ArrayList<WordModel>() {{
        add(new WordModel(-1, "OKO", "OKO",1));
        add(new WordModel(-1, "ONO","ONO", 1));

        add(new WordModel(-1, "MENO","MENO", 2));
        add(new WordModel(-1, "NAM","NÁM", 2));

        add(new WordModel(-1, "POLE","POLE", 3));
        add(new WordModel(-1, "ALE","ALE", 3));
        add(new WordModel(-1, "APO","OTEC", 3));

        add(new WordModel(-1, "LUPA","LUPA", 4));
        add(new WordModel(-1, "UPAL","ÚPAL", 4));
        add(new WordModel(-1, "POL","PÓL", 4));

        add(new WordModel(-1, "MAMA","MAMA", 5));
        add(new WordModel(-1, "TAM","TAM", 5));
        add(new WordModel(-1, "CMA","TMA", 5));

        add(new WordModel(-1, "SIRA","SÍRA", 6));
        add(new WordModel(-1, "ROSA","ROSA", 6));
        add(new WordModel(-1, "ASI","ASI", 6));
        add(new WordModel(-1, "OSA","OSA", 6));

        add(new WordModel(-1, "KOMA","ČIARKA", 7));
        add(new WordModel(-1, "KUMA", "KMOTRA",7));
        add(new WordModel(-1, "AMOK","ANOK", 7));
        add(new WordModel(-1, "KUM","KMOTOR", 7));

        add(new WordModel(-1, "UJKO","UJEC", 8));
        add(new WordModel(-1, "LUKA","LÚKA", 8));
        add(new WordModel(-1, "OKAL","ODKVAP", 8));
        add(new WordModel(-1, "JUL","JÚL", 8));
        add(new WordModel(-1, "JAK","AK", 8));

        add(new WordModel(-1, "POPID","POPOD", 9));
        add(new WordModel(-1, "ODA","ÓDA", 9));
        add(new WordModel(-1, "PIP","FARÁR", 9));
        add(new WordModel(-1, "PAD","PÁD", 9));
        add(new WordModel(-1, "PID","POD", 9));

        add(new WordModel(-1, "OBOJE","OBIDVAJA", 10));
        add(new WordModel(-1, "AHOJ", "AHOJ",10));
        add(new WordModel(-1, "JOHO","JEHO", 10));
        add(new WordModel(-1, "HEJ","ÁNO", 10));

        add(new WordModel(-1, "MAMKA","MAMKA", 11));
        add(new WordModel(-1, "DALE","ĎALEJ", 11));
        add(new WordModel(-1, "MAK","MAK", 11));
        add(new WordModel(-1, "DEKA","DEKA", 11));
        add(new WordModel(-1, "KEL","KEL", 11));

        add(new WordModel(-1, "SVOHO","SVOJHO", 12));
        add(new WordModel(-1, "SOBOV","SEBOU", 12));
        add(new WordModel(-1, "OSOBA","OSOBA", 12));
        add(new WordModel(-1, "SOVA","SOVA", 12));
        add(new WordModel(-1, "OSA","OSA", 12));

        add(new WordModel(-1, "SELEP","VENTIL", 13));
        add(new WordModel(-1, "SKLEP","OBCHOD", 13));
        add(new WordModel(-1, "PEKLO","PEKLO", 13));
        add(new WordModel(-1, "SELO","DEDINA", 13));
        add(new WordModel(-1, "PES","PES", 13));

        add(new WordModel(-1, "KLUBOK","KĹBIK", 14));
        add(new WordModel(-1, "KLUBKO","KĹBKO", 14));
        add(new WordModel(-1, "OBLAK","OKNO", 14));
        add(new WordModel(-1, "KLUB","KĹB", 14));
        add(new WordModel(-1, "OKAL","ODKVAP", 14));
        add(new WordModel(-1, "ABO","ALEBO", 14));

        add(new WordModel(-1, "OSADA","OSADA", 15));
        add(new WordModel(-1, "DUDA","CUMEĽ", 15));
        add(new WordModel(-1, "FOSA","KANÁL", 15));
        add(new WordModel(-1, "DAS","CIRKA", 15));
        add(new WordModel(-1, "SAD","SAD", 15));

        add(new WordModel(-1, "OPALKA","OPÁLKA", 16));
        add(new WordModel(-1, "KALAP","KLOBÚK", 16));
        add(new WordModel(-1, "KLASA","STUPEŇ KVALITY", 16));
        add(new WordModel(-1, "LASKA","LÁSKA", 16));
        add(new WordModel(-1, "SALO","BRUŠNÝ TUK", 16));

        add(new WordModel(-1, "MANIAK","MANIAK", 17));
        add(new WordModel(-1, "KANAL","KANÁL", 17));
        add(new WordModel(-1, "MLAKA","KALUŽ", 17));
        add(new WordModel(-1, "KLAN","KLAN", 17));
        add(new WordModel(-1, "MAK","MAK", 17));

        add(new WordModel(-1, "LAMATY","LÁMAŤ", 18));
        add(new WordModel(-1, "BALTA","SEKERA(VEĽKÁ)", 18));
        add(new WordModel(-1, "MALTA","MALTA", 18));
        add(new WordModel(-1, "MATY","MAŤ", 18));
        add(new WordModel(-1, "BAL","BÁL", 18));

        add(new WordModel(-1, "SKOSOM","ŠIKMO", 19));
        add(new WordModel(-1, "KOSA","KOSA", 19));
        add(new WordModel(-1, "SMAK","CHUŤ", 19));
        add(new WordModel(-1, "SAMO","SAMO", 19));
        add(new WordModel(-1, "SAKO","SAKO", 19));
        add(new WordModel(-1, "KOSO","NAKRIVO", 19));


        add(new WordModel(-1, "METATY","HÁDZAŤ", 20));
        add(new WordModel(-1, "KRATY","KRÁJAŤ", 20));
        add(new WordModel(-1, "TERKA","TŔNKA", 20));
        add(new WordModel(-1, "KRETA","KRÉTA", 20));
        add(new WordModel(-1, "TETA","TETA", 20));
        add(new WordModel(-1, "META","CIEĽ", 20));

        add(new WordModel(-1, "ANYKUS","ANI TROCHU", 21));
        add(new WordModel(-1, "OSADNYK","OSADNÍK", 21));
        add(new WordModel(-1, "SUKNO","SÚKNO", 21));
        add(new WordModel(-1, "DAKUS","TROCHA", 21));
        add(new WordModel(-1, "KUS","KUS", 21));
        add(new WordModel(-1, "SON","SEN", 21));







    }};
}
