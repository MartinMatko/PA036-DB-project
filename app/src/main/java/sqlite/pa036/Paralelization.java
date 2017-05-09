package sqlite.pa036;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jakub on 29/03/2017.
 */

public final class Paralelization extends DBHelper {

    private TextView textOut;
    private List<String> selects = new ArrayList<String>();
    private List<String> updates = new ArrayList<String>();
    private int NUM_QUERIES = 500;
    private int COUNT = 10;

    private class first implements Runnable{

        @Override
        public void run() {
            int idx = 0;
            for(int i = 0; i< NUM_QUERIES; i++)
            {
                Cursor myCurs = myDataBase.rawQuery(selects.get(idx), null);
                myCurs.moveToFirst();
                myCurs.close();
                idx++;
                if(idx ==selects.size())
                    idx = 0;
            }
        }
    }

    private class second implements Runnable{

        @Override
        public void run() {
            int idx = 0;
            for(int i = 0; i< NUM_QUERIES; i++)
            {
                Cursor myCurs =myDataBase.rawQuery(updates.get(idx), null);
                myCurs.moveToFirst();
                myCurs.close();
                idx++;
                if(idx ==updates.size())
                    idx = 0;
            }
        }
    }


    protected Paralelization(Context context, String dbName, TextView out) {
        super(context, dbName);

            textOut = out;

    }

    @Override
    protected void Tests() {
        testMaxConnectionsCount();
        testSingleVsMultipleThreads();
    }

    private void testMaxConnectionsCount()
    {
        textOut.append("\n\nTesting maximum number of database connections: \n");

        for(int x=0;x<COUNT;x++) {
            int count = 0;
            List<SQLiteDatabase> connections = new ArrayList<SQLiteDatabase>();
            try {
                while (true) {
                    SQLiteDatabase newConnection = SQLiteDatabase.openDatabase("/data/data/sqlite.pa036/databases/db100.db", null, SQLiteDatabase.OPEN_READWRITE);
                    connections.add(newConnection);
                    count++;
                }
            } catch (Exception ex) {
                String str = "" + count;
                textOut.append(str + "\n");
                for (int i = 0; i < connections.size(); i++) {
                    if (connections.get(i).isOpen()) {
                        connections.get(i).close();
                    }
                }
            }
        }
        return;
    }

    private void testSingleVsMultipleThreads()
    {
        selects.add("SELECT * FROM songs");
        selects.add("SELECT _id, name, length FROM songs WHERE length > 2 AND date < 12000");
        selects.add("SELECT count(DISTINCT author) AS count FROM songs");
        selects.add("SELECT min(date) AS newest FROM songs");
        selects.add("SELECT author,count(length) FROM (SELECT author,length FROM songs WHERE length > 6.5) GROUP BY author");


        updates.add("UPDATE songs SET text_of_song = 'sample text' WHERE _id = 100");
        updates.add("UPDATE songs SET date = date+1 WHERE date = (SELECT min(date) FROM songs)");
        updates.add("UPDATE songs SET date = date+10");
        updates.add("UPDATE songs SET date = date-10");


        textOut.append("\n\nTesting single thread\n");
        long total=0;
        long startTime;
        long endTime;
        String time;
        for(int x=0;x<COUNT;x++ ) {
            startTime = System.currentTimeMillis();
            int idxSelect = 0;
            int idxUpdate = 0;
            for (int i = 0; i < NUM_QUERIES; i++) {
                int count;
                Cursor myCurs = myDataBase.rawQuery(selects.get(idxSelect), null);
                myCurs.moveToFirst();
                myCurs.close();
                myCurs = myDataBase.rawQuery(updates.get(idxUpdate), null);
                myCurs.moveToFirst();
                myCurs.close();
                idxSelect++;
                idxUpdate++;
                if (idxSelect == 5)
                    idxSelect = 0;
                if (idxUpdate == 4)
                    idxUpdate = 0;
            }
            endTime = System.currentTimeMillis();
            time = "" + (endTime - startTime);
            textOut.append("Time: " + time + "\n");
            total += endTime-startTime;

            reloadDatabase();
        }
        textOut.append("Average: " + total/COUNT + "\n");

        textOut.append("\n\nTesting two threads\n");
        total=0;
        for(int x=0;x<COUNT;x++ ) {

            Thread firstThread = new Thread(new first());
            Thread secondThread = new Thread(new second());
            startTime = System.currentTimeMillis();
            firstThread.start();
            secondThread.start();
            try {
                firstThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                secondThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            endTime = System.currentTimeMillis();
            time = "" + (endTime - startTime);
            textOut.append("Time: " + time + "\n");
            total += endTime-startTime;
            reloadDatabase();
        }
        textOut.append("Average: " + total/COUNT + "\n");

    }
}
