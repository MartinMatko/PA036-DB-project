package sqlite.pa036;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int number = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void incrementClick(View view){
        TextView numberView = (TextView) findViewById(R.id.numberView);
        number++;
        numberView.setText(Integer.toString(number));
    }
}
