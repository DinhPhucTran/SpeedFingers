package tk.trandinhphuc.speedfingers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by DinhPhuc on 20/02/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "speed";
    private static final int DB_VERSION = 1;
    public static final String TABLE_CUSTOMRECORD = "CustomRecord";

    public DbHelper (Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_CUSTOMRECORD +" (_id integer primary key autoincrement, time integer, record integer);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
