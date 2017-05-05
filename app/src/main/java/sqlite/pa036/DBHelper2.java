package sqlite.pa036;

/**
 * Created by Michael on 29-Mar-17.
 */
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public abstract class DBHelper2 extends SQLiteOpenHelper {

    //samsung
    //private static final String DATABASE_PATH = "/storage/extSdCard/Android/data/sqlite.pa036/";
    //xperia
    private static final String DATABASE_PATH = "/mnt/ext_card/";
    protected final String myDbName;
    protected SQLiteDatabase myDataBase;
    protected final Context myContext;

    protected DBHelper2(Context context, String dbName) {

        super(context, dbName, null, 1);
        this.myContext = context;
        this.myDbName = dbName;
        this.myDataBase = null;

        try{
            openDataBase();
        }
        catch(SQLException sqle){
            throw sqle;
        }

    }

    private void openDataBase() throws SQLException {
        String myPath = DATABASE_PATH + myDbName;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE | SQLiteDatabase.NO_LOCALIZED_COLLATORS);
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
        //cleanUp();
    }

    protected abstract void Tests();

}
