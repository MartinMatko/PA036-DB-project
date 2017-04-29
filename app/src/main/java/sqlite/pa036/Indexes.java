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
        TestAuthorAndLength();
    }
    //authors = 6G50F 24095 2WKYO
    //songs = WEACQ25 0WXXD8182 OQ5TT599998

    private void TestAuthor(){
        long start = System.currentTimeMillis();
        ArrayList<Song> songs = this.getSongsByAuthor("2WKYO");
        long finish = System.currentTimeMillis();
        for (Song song: songs) {
            System.out.println(song);
        }
        System.out.println(finish-start);
    }

    private void TestAuthorWithSingleColumn(){
        myDataBase.execSQL("CREATE INDEX author_idx ON songs (author)");
        long start = System.currentTimeMillis();
        ArrayList<Song> songs = this.getSongsByAuthor("2WKYO");
        long finish = System.currentTimeMillis();
        for (Song song: songs) {
            System.out.println(song);
        }
        System.out.println(finish-start);
    }

    private void TestAuthorAndLength(){
        long start = System.currentTimeMillis();
        ArrayList<Song> songs = this.getSongsByAuthorAndLength("2WKYO", 5);
        long finish = System.currentTimeMillis();
        for (Song song: songs) {
            System.out.println(song);
        }
        System.out.println(finish-start);
    }

    private void TestAuthorAndDateWithComposite(){
        myDataBase.execSQL("CREATE INDEX author_idx ON songs (author)");
        long start = System.currentTimeMillis();
        ArrayList<Song> songs = this.getSongsByAuthorAndLength("2WKYO", 5);
        long finish = System.currentTimeMillis();
        for (Song song: songs) {
            System.out.println(song);
        }
        System.out.println(finish-start);
    }

    private void TestName(){
        long start = System.currentTimeMillis();
        Song song = this.getSongByName("OQ5TT599998");
        long finish = System.currentTimeMillis();
        System.out.println(song);
        System.out.println(finish-start);
    }

    private void TestNameWithUniqueColumn(){
        myDataBase.execSQL("CREATE UNIQUE INDEX name_idx ON songs (name)");
        long start = System.currentTimeMillis();
        Song song = this.getSongByName("OQ5TT599998");
        long finish = System.currentTimeMillis();
        System.out.println(song);
        System.out.println(finish-start);
    }

    private void TestInsert(){
        Song song = new Song("Waka waka", "Shakira", "waka waka oleeee", 5, new Date(System.currentTimeMillis()));
        long start = System.currentTimeMillis();
        insertSong(song);
        long finish = System.currentTimeMillis();
        System.out.println(finish-start);
    }

    private void TestInsertWithUniqueColumn(){
        Song song = new Song("Waka waka", "Shakira", "waka waka oleeee", 5, new Date(System.currentTimeMillis()));
        myDataBase.execSQL("CREATE UNIQUE INDEX name_idx ON songs (name)");
        long start = System.currentTimeMillis();
        insertSong(song);
        long finish = System.currentTimeMillis();
        System.out.println(finish-start);
    }
}
