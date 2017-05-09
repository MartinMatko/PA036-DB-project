package sqlite.pa036;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void Run(View view){

        // Tu odkomentujte testy ktore chcete spustit a potom v jednotlivych triedach implementujte metodu Tests(). Jednotlive triedy su podtriedami dbHelper a lisia sa akurat implementaciou metody Tests().
        // V konstruktore sa predava kontext a nazov databazy ktoru chcete pouzit cize db100.db, db10000.db alebo db1000000.db.
        // Konstruktor vasej triedy zavola konstruktor dbHelper ktory otvori databazu z assets a nacitaju ju do systemoveho uloziska.
        // RunTests() potom spusti vasu implemntaciu metody Tests() a po jej dokonceni uzavrie spojenie s databazou a zmaze databazu zo systemoveho uloziska takze pri opakovanych testoch sa vam vzdy nahra rovnaka nezmenena databaza.
        // Databaza sa otvara ako readwrite cize sa da aj citat aj zapisovat, na pristup mozete pouzit myDataBase, meno databazy je pristupne cez myDbName a kontext cez myContext.

        /*
        db = new Indexes(this, "db100.db");
        db.runTests();
        db = new Indexes(this, "db10000.db");
        db.runTests();
        db = new Indexes(this, "db1000000.db");
        db.runTests();
*/
        //db = new Storage(this, "db100.db");
        //db = new Storage(this, "db100000.db");
        //db.runTests();
        //db = new Storage(this, "db10000.db");
        //db.runTests();
        //db = new Storage(this, "db1000000.db");
        //db.runTests();
/*
        db = new ProsCons(this, "db100.db");
        db.runTests();
        db = new ProsCons(this, "db10000.db");
        db.runTests();
        db = new ProsCons(this, "db1000000.db");
        db.runTests();
*/
        db = new Paralelization(this, "db100.db", (TextView)findViewById(R.id.numberView));
        db.runTests();
        /*db = new Paralelization(this, "db10000.db", (TextView)findViewById(R.id.numberView));
        db.runTests();
        db = new Paralelization(this, "db1000000.db", (TextView)findViewById(R.id.numberView));
        db.runTests();
        */
    }
}
