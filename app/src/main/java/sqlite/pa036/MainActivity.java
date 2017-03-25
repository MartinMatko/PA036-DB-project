package sqlite.pa036;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    int number = 1;
    DBHelper dbHelper = new DBHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper.populateDB();
    }

    public void selectAllSongs1(View view){
        TextView numberView = (TextView) findViewById(R.id.numberView);
        try {
            ArrayList<Song> allSongs = dbHelper.getAllSongs("songs1");
            numberView.setText(allSongs.toString());
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
