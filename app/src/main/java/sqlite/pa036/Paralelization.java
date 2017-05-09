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
    private int NUM_QUERIES = 3600;
    private int COUNT = 10;

    private class first implements Runnable{
        private int numOps;
        public first(int num)
        {
            numOps=num;
            totalTime=0;
        }
        public long totalTime;
        @Override
        public void run() {
            int idx = 0;
            long startTime;
            long endTime;
            long time =0;
            for(int i = 0; i< numOps; i++)
            {
                startTime = System.currentTimeMillis();
                Cursor myCurs = myDataBase.rawQuery(selects.get(idx), null);
                myCurs.moveToFirst();
                myCurs.close();
                endTime = System.currentTimeMillis();
                time += (endTime-startTime);
                idx++;
                if(idx ==selects.size())
                    idx = 0;
            }
            totalTime=time;
        }
    }

    private class second implements Runnable{

        private int numOps;
        public second(int num)
        {
            numOps=num;
            totalTime=0;
        }
        public long totalTime;
        @Override
        public void run() {
            int idx = 0;
            long startTime;
            long endTime;
            long time = 0;
            for(int i = 0; i< numOps; i++)
            {
                startTime = System.currentTimeMillis();
                Cursor myCurs =myDataBase.rawQuery(updates.get(idx), null);
                myCurs.moveToFirst();
                myCurs.close();
                endTime = System.currentTimeMillis();
                time += (endTime-startTime);
                idx++;
                if(idx ==updates.size())
                    idx = 0;
            }
            totalTime=time;
        }
    }

    private class third implements Runnable{
        private int numOps;
        public third(int num)
        {
            numOps=num;
        }
        public long totalTime;
        @Override
        public void run() {
            int idx = 0;
            int idx2 = 0;
            long startTime;
            long endTime;
            long time=0;
            for(int i = 0; i< numOps; i++)
            {
                startTime = System.currentTimeMillis();
                Cursor myCurs = myDataBase.rawQuery(updates.get(idx), null);
                myCurs.moveToFirst();
                myCurs.close();
                endTime = System.currentTimeMillis();
                time += (endTime-startTime);
                idx++;
                if(idx ==updates.size())
                    idx = 0;

                startTime = System.currentTimeMillis();
                myCurs = myDataBase.rawQuery(selects.get(idx2), null);
                myCurs.moveToFirst();
                myCurs.close();
                endTime = System.currentTimeMillis();
                time += (endTime-startTime);
                idx2++;
                if(idx2 ==selects.size())
                    idx2 = 0;
            }
            totalTime = time;
        }
    }


    protected Paralelization(Context context, String dbName, TextView out) {
        super(context, dbName);

            textOut = out;

    }

    @Override
    protected void Tests() {
        //testMaxConnectionsCount();
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
        selects.add("SELECT author,count(length) FROM (SELECT author,length FROM songs WHERE length > 6.5) GROUP BY author"); //pocet skladieb dlhsich ako 6.5 min pre kazdeho autora


        updates.add("UPDATE songs SET date = date+10");
        updates.add("UPDATE songs SET date = date-10");

        long startTime;
        long endTime;
        long totalTime=0;
        long time=0;

        //
        // SINGLE
        //
        textOut.append("\n\nTesting single thread\n");
        for(int x=0;x<COUNT;x++ ) {

            time=0;
            int idxSelect = 0;
            int idxUpdate = 0;
            for (int i = 0; i < NUM_QUERIES; i++) {
                startTime = System.currentTimeMillis();
                Cursor myCurs = myDataBase.rawQuery(selects.get(idxSelect), null);
                myCurs.moveToFirst();
                myCurs.close();
                endTime = System.currentTimeMillis();
                time += (endTime-startTime);

                startTime = System.currentTimeMillis();
                myCurs = myDataBase.rawQuery(updates.get(idxUpdate), null);
                myCurs.moveToFirst();
                myCurs.close();
                endTime = System.currentTimeMillis();
                time += (endTime-startTime);

                idxSelect++;
                idxUpdate++;
                if (idxSelect == selects.size())
                    idxSelect = 0;
                if (idxUpdate == updates.size())
                    idxUpdate = 0;
            }


            textOut.append("Time: " + time  + "\n");

            totalTime+= time;
            reloadDatabase();
        }
        textOut.append("Average: " + totalTime/COUNT + "\n");

        //
        // TWO
        //

        textOut.append("\n\nTesting two threads\n");
        totalTime=0;
        for(int x=0;x<COUNT;x++ ) {
            first fst= new first(NUM_QUERIES);
            second snd = new second(NUM_QUERIES);
            Thread firstThread = new Thread(fst);
            Thread secondThread = new Thread(snd);
            firstThread.start();
            secondThread.start();
            try {
                firstThread.join();
                secondThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            firstThread.interrupt();
            secondThread.interrupt();

            time = fst.totalTime + snd.totalTime;
            textOut.append("Time: " + time + "\n");
            totalTime += time;
            reloadDatabase();
        }
        textOut.append("Average: " + totalTime/COUNT + "\n");

        //
        // THREE
        //

        textOut.append("\n\nTesting three threads\n");
        totalTime=0;
        for(int x=0;x<COUNT;x++ ) {

            first fst= new first(NUM_QUERIES*2/3);
            second snd = new second(NUM_QUERIES*2/3);
            third trd = new third(NUM_QUERIES/3);
            Thread firstThread = new Thread(fst);
            Thread secondThread = new Thread(snd);
            Thread thirdThread = new Thread(trd);
            time = 0;

            firstThread.start();
            secondThread.start();
            thirdThread.start();
            try {
                firstThread.join();
                secondThread.join();
                thirdThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            time = fst.totalTime + snd.totalTime;// + trd.totalTime;
            textOut.append("Time: " + time + "\n");
            totalTime += fst.totalTime + snd.totalTime + trd.totalTime;
            reloadDatabase();
        }
        textOut.append("Average: " + totalTime/COUNT + "\n");

    }
}
