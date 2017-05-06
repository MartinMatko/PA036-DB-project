package sqlite.pa036;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Michael on 29/03/2017.
 */

public final class Storage extends DBHelper {

    protected Storage(Context context, String dbName) {
        super(context, dbName);
    }

    // Testing
    @Override
    protected void Tests() {

        // TEST 1
        //long test1 = oneHundredInsertsTest("songs");
        //long test1sd = 0;
        //printTestResults(1, "100x Insert",test1, test1sd);

        // TEST 2
        //long test2 = tenSelectsTest("songs");
        //long test2sd = 0;
        //printTestResults(2, "10x Select",test2, test2sd);


        //TEST 3
        //long test4 = selectStringComparisonTest("songs");
        //long test4sd = 0;
        //printTestResults(4, "Select (String comparison)",test4, test4sd);


        //TEST 4
        //long test5 = updateTest("songs");
        //long test5sd = 0;
        //printTestResults(5, "Update",test5, test5sd);


        //TEST 5
        //long test6 = deleteTest("songs");
        //long test6sd = 0;
        //printTestResults(6, "Delete",test6, test6sd);
    }

    private long oneHundredInsertsTest(String table) {
        long startNow, endNow = 0;
        startNow = android.os.SystemClock.elapsedRealtime();

        for (int i = 0; i < 100; i++) {
            Song s = new Song(i + "song", "auth", "text", 123456789, new Date());
            insertSong(s, table);
        }

        endNow = android.os.SystemClock.elapsedRealtime();
        return endNow - startNow;
    }

    private long tenSelectsTest(String table) {
        Cursor cursor = null;
        long startNow, endNow = 0;
        startNow = android.os.SystemClock.elapsedRealtime();

        for (int i = 0; i < 10; i++) {
            double len = ((double)i/10);
            cursor = myDataBase.rawQuery("SELECT count(*) from " + table + " WHERE length>=" + len + " AND length<" + (3+len), new String[]{});
            cursor.moveToFirst();
            cursor.close();
        }

        endNow = android.os.SystemClock.elapsedRealtime();
        return endNow - startNow;
    }

    private long selectStringComparisonTest(String table) {
        Cursor cursor = null;
        long startNow, endNow = 0;
        startNow = android.os.SystemClock.elapsedRealtime();

        for (int i = 0; i < 10; i++) {
            char c = (char)(65 + i);
            cursor = myDataBase.rawQuery("SELECT count(*) from " + table + " WHERE name LIKE '%" + c + "%'", new String[]{});
            cursor.moveToFirst();
            cursor.close();
        }

        endNow = android.os.SystemClock.elapsedRealtime();
        return endNow - startNow;
    }

    private long tenUpdates(String table) {
        //UPDATE t1 SET b=b*2 WHERE a>=0 AND a<10;
        Cursor cursor = null;
        long startNow, endNow = 0;
        startNow = android.os.SystemClock.elapsedRealtime();

        for (int i = 0; i < 10; i++) {
            double len = ((double)i/10);
            cursor = myDataBase.rawQuery("UPDATE " + table + " SET length=length*2 WHERE length>=" + len + " AND length<" + (3+len), new String[]{});
            cursor.moveToFirst();
            cursor.close();
        }

        endNow = android.os.SystemClock.elapsedRealtime();
        return endNow - startNow;
    }

    private long updateTest(String table) {
        Cursor cursor = null;
        long startNow, endNow = 0;
        startNow = android.os.SystemClock.elapsedRealtime();

        myDataBase.execSQL("UPDATE " + table + " SET author='UPDATED' WHERE name LIKE 'AAA%'");

        endNow = android.os.SystemClock.elapsedRealtime();
        return endNow - startNow;
    }

    private long deleteTest(String table) {
        long startNow, endNow = 0;
        startNow = android.os.SystemClock.elapsedRealtime();

        myDataBase.execSQL("DELETE from " + table + " WHERE name LIKE 'AAA%'");

        endNow = android.os.SystemClock.elapsedRealtime();
        return endNow - startNow;
    }

    // Database methods
    public boolean insertSong(Song song, String table) {
        ContentValues contentValues = songToContentValues(song);
        myDataBase.insert(table, null, contentValues);
        return true;
    }

    // Helper methods
    public ContentValues songToContentValues(Song song){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", song.getNameOfSong());
        contentValues.put("author", song.getNameOfAuthor());
        contentValues.put("text_of_song", song.getTextOfSong());
        contentValues.put("date", song.getDateAdded().getTime());
        contentValues.put("length", song.getLength());
        return contentValues;
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
    private void printTestResults(int i, String testName, long testResult, long testResultSd) {
        System.out.println("TEST " + i + " - " + testName + ", " + myDbName + " - Execution time: " + testResult + " ms.");
    }

}
