package sqlite.pa036;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Jakub on 29/03/2017.
 */

public final class Paralelization extends DBHelper {

    protected Paralelization(Context context, String dbName) {
        super(context, dbName);
    }

    @Override
    protected void Tests() {

    }
}
