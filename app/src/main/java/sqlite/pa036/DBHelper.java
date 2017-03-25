package sqlite.pa036;

/**
 * Created by Martin on 25-Mar-17.
 */
import java.util.Date;
import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "songsDB.db";

    public static final String SONGS1_TABLE_NAME = "songs1";
    public static final String SONGS2_TABLE_NAME = "songs2";
    public static final String SONGS3_TABLE_NAME = "songs3";

    public static final String SONGS_COLUMN_ID = "id";
    public static final String SONGS_COLUMN_NAME = "name";
    public static final String SONGS_COLUMN_AUTHOR = "author";
    public static final String SONGS_COLUMN_TEXT = "text_of_song";
    public static final String SONGS_COLUMN_DATE_ADDED = "date";
    public static final String SONGS_COLUMN_DATE_LENGTH = "length";

    public static final String COLUMNS_WITH_CONSTRAINTS =
            "(id INTEGER PRIMARY KEY, name TEXT NOT NULL UNIQUE, author TEXT NOT NULL, text_of_song TEXT NOT NULL, date INTEGER, length REAL)";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE songs1 " + COLUMNS_WITH_CONSTRAINTS);
        db.execSQL("CREATE TABLE songs2 " + COLUMNS_WITH_CONSTRAINTS);
        db.execSQL("CREATE TABLE songs3 " + COLUMNS_WITH_CONSTRAINTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS songs1");
        db.execSQL("DROP TABLE IF EXISTS songs2");
        db.execSQL("DROP TABLE IF EXISTS songs3");
        onCreate(db);
    }

    public boolean insertSong(Song song, String table) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = songToContentValues(song);
        db.insert(table, null, contentValues);
        return true;
    }

    public Song getSongByName(String name, String table) {
        Cursor cursor = null;
        Song song = new Song();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            cursor = db.rawQuery("SELECT * FROM " + table +" WHERE name=?", new String[] {name});
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                song = cursorToSong(cursor);
            }
            return song;
        }finally {
            cursor.close();
        }
    }

    public boolean updateSong(Song song, String table) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = songToContentValues(song);
        db.update(table, contentValues, "name = ? ", new String[] { song.getNameOfSong() } );
        return true;
    }

    public Integer deleteSong(String name, String table) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(table,
                "name = ? ",
                new String[] { name });
    }

    public ArrayList<Song> getAllSongs(String table) {
        ArrayList<Song> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        long diff = System.currentTimeMillis();
        Cursor cursor =  db.rawQuery( "select * from " + table, null );
        diff = System.currentTimeMillis() - diff;
        System.out.println(diff);
        cursor.moveToFirst();

        while(cursor.isAfterLast() == false){
            array_list.add(cursorToSong(cursor));
            cursor.moveToNext();
        }
        return array_list;
    }

    public Song cursorToSong(Cursor cursor){
        Song song = new Song();
        song.setDateAdded(new Date(cursor.getLong(cursor.getColumnIndex("date"))));
        song.setLength(cursor.getDouble(cursor.getColumnIndex("length")));
        song.setNameOfAuthor(cursor.getString(cursor.getColumnIndex("author")));
        song.setNameOfSong(cursor.getString(cursor.getColumnIndex("name")));
        song.setTextOfSong(cursor.getString(cursor.getColumnIndex("text_of_song")));
        return song;
    }

    public ContentValues songToContentValues(Song song){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", song.getNameOfSong());
        contentValues.put("author", song.getNameOfAuthor());
        contentValues.put("text_of_song", song.getTextOfSong());
        contentValues.put("date", song.getDateAdded().getTime());
        contentValues.put("length", song.getLength());
        return contentValues;
    }

    public void populateDB(){
        Song song = new Song();
        song.setNameOfSong("Iná žena");
        song.setNameOfAuthor("Conchita Wurst");
        song.setLength(5.1d);
        song.setDateAdded(new Date(System.currentTimeMillis()));
        song.setTextOfSong("Byť denne inou to ja viem\n" +
                "Chlapskému srdcu rozumiem\n" +
                "Zvečera krásna zrána tiež\n" +
                "Veď ty to vieš\n" +
                "\n" +
                "Mať krásku z televízií\n" +
                "Je túžbou tvojich vízií\n" +
                "Máš mňa tak čo viac chceš\n" +
                "Lepšiu už nenájdeš\n" +
                "\n" +
                "Refr:\n" +
                "Každý deň je zo mňa iná žena\n" +
                "Večer hravá ráno opustená\n" +
                "Každý deň mi dávaj to čo treba\n" +
                "Stačí mi aj malý kúsok z teba.\n" +
                "\n" +
                "Každý deň je zo mňa iná žena\n" +
                "Večer DIVA ráno unavená\n" +
                "Každý deň mi dávaj kúsok z teba\n" +
                "Každý deň mi hovor chcem len teba.\n" +
                "\n" +
                "Každá je štíhla okatá\n" +
                "Kto tvoje túžby poráta\n" +
                "Ako ich získať to ty vieš\n" +
                "Rád šancu chceš\n" +
                "\n" +
                "Ja som však inou každý deň\n" +
                "Preskočím lacný ženský tieň\n" +
                "Nájsť takú lásku teraz smieš\n" +
                "Som inou ženou tiež\n" +
                "\n");
        insertSong(song, "songs1");
    }
}
