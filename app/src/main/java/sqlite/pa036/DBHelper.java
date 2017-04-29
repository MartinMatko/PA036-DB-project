package sqlite.pa036;

/**
 * Created by Jakub on 29-Mar-17.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;

public abstract class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_PATH = "/data/data/sqlite.pa036/databases/";
    protected final String myDbName;
    protected SQLiteDatabase myDataBase;
    protected final Context myContext;

    protected DBHelper(Context context, String dbName) {

        super(context, dbName, null, 1);
        this.myContext = context;
        this.myDbName = dbName;
        this.myDataBase = null;

        try{
            createDataBase();
        }
        catch (IOException ioe){
            throw new Error("Unable to create database");
        }

        try{
            openDataBase();
        }
        catch(SQLException sqle){
            throw sqle;
        }
    }

    private void createDataBase() throws IOException {

        this.getReadableDatabase();
        try{
            copyDataBase();
        }
        catch (IOException e){
            throw new Error("Error copying database");
        }
    }

    private void copyDataBase() throws IOException{

        InputStream myInput = myContext.getAssets().open(myDbName);
        String outFileName = DATABASE_PATH + myDbName;
        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    private void openDataBase() throws SQLException {

        String myPath = DATABASE_PATH + myDbName;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private void cleanUp(){

        if(myDataBase != null) {
            myContext.deleteDatabase(myDbName);
            myDataBase = null;
        }

    }

    protected void runTests(){
        Tests();
        close();
//        cleanUp();
    }

    protected abstract void Tests();

    public boolean insertSong(Song song) {
        ContentValues contentValues = songToContentValues(song);
        myDataBase.insert("songs", null, contentValues);
        return true;
    }

    public Song getSongByName(String name) {
        Cursor cursor = null;
        Song song = new Song();
        try {
            cursor = myDataBase.rawQuery("SELECT * FROM songs WHERE name=?", new String[] {name});
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                song = cursorToSong(cursor);
            }
            return song;
        }finally {
            cursor.close();
        }
    }

    public ArrayList<Song> getSongsByAuthor(String author) {
        ArrayList<Song> array_list = new ArrayList<>();
        Cursor cursor =  myDataBase.rawQuery( "select * from songs WHERE author=?", new String[] {author});
        cursor.moveToFirst();

        while(cursor.isAfterLast() == false){
            array_list.add(cursorToSong(cursor));
            cursor.moveToNext();
        }
        return array_list;
    }

    public ArrayList<Song> getSongsByAuthorAndLength(String author, double length) {
        ArrayList<Song> array_list = new ArrayList<>();
        Cursor cursor =  myDataBase.rawQuery( "select * from songs WHERE author=? AND length<?",
                new String[] {author, String.valueOf(length)});
        cursor.moveToFirst();

        while(cursor.isAfterLast() == false){
            array_list.add(cursorToSong(cursor));
            cursor.moveToNext();
        }
        return array_list;
    }

    public boolean updateSong(Song song, String table) {
        ContentValues contentValues = songToContentValues(song);
        myDataBase.update(table, contentValues, "name = ? ", new String[] { song.getNameOfSong() } );
        return true;
    }

    public Integer deleteSong(String name) {
        return myDataBase.delete("songs",
                "name = ? ",
                new String[] { name });
    }

    public ArrayList<Song> getAllSongs(String table) {
        ArrayList<Song> array_list = new ArrayList<>();
        long diff = System.currentTimeMillis();
        Cursor cursor =  myDataBase.rawQuery( "select * from " + table, null );
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
        long tmp = cursor.getLong(cursor.getColumnIndex("date"));
        song.setId(cursor.getInt(cursor.getColumnIndex("_id")));
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

}
