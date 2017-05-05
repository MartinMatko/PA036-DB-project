package sqlite.pa036;

/**
 * Created by Jakub on 29-Mar-17.
 */
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public abstract class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_PATH = "/data/data/sqlite.pa036/databases/";
    protected final String myDbName;
    protected SQLiteDatabase myDataBase;
    protected final Context myContext;

    protected DBHelper(Context context, String dbName) {

        super(context, dbName, null, 1);
        this.myContext = context;
        this.myDbName = dbName;
        this.myDataBase = null;

        try{
            createDataBase();
        }
        catch (IOException ioe){
            throw new Error("Unable to create database");
        }

        try{
            openDataBase();
        }
        catch(SQLException sqle){
            throw sqle;
        }
    }

    private void createDataBase() throws IOException {

        this.getReadableDatabase();
        try{
            copyDataBase();
        }
        catch (IOException e){
            throw new Error("Error copying database");
        }
    }

    private void copyDataBase() throws IOException{

        InputStream myInput = myContext.getAssets().open(myDbName);
        String outFileName = DATABASE_PATH + myDbName;
        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    private void openDataBase() throws SQLException {

        String myPath = DATABASE_PATH + myDbName;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

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
        cleanUp();
    }

    protected abstract void Tests();

}
