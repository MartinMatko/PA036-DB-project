package sqlite.pa036;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Jakub on 29/03/2017.
 */

public final class Indexes extends DBHelper {

    protected Indexes(Context context, String dbName) {
        super(context, dbName);
    }

    @Override
    protected void Tests() {

    }

    public boolean insertSong(Song song, String table) {
        ContentValues contentValues = songToContentValues(song);
        myDataBase.insert(table, null, contentValues);
        return true;
    }

    public Song getSongByName(String name, String table) {
        Cursor cursor = null;
        Song song = new Song();
        try {
            cursor = myDataBase.rawQuery("SELECT * FROM " + table +" WHERE name=?", new String[] {name});
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
        ContentValues contentValues = songToContentValues(song);
        myDataBase.update(table, contentValues, "name = ? ", new String[] { song.getNameOfSong() } );
        return true;
    }

    public Integer deleteSong(String name, String table) {
        return myDataBase.delete(table,
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
