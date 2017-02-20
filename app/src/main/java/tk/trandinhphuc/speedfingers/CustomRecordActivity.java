package tk.trandinhphuc.speedfingers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CustomRecordActivity extends AppCompatActivity {

    private SQLiteOpenHelper mDbHelper;
    private SQLiteDatabase mDb;

    private RecyclerView mRecordList;
    private CustomRecordAdapter mRecordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_record);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecordList = (RecyclerView) findViewById(R.id.recordList);
        mRecordList.setLayoutManager(new LinearLayoutManager(this));
        mRecordList.setHasFixedSize(true);

        mDbHelper = new DbHelper(this);
        mDb = mDbHelper.getReadableDatabase();
        mRecordAdapter = new CustomRecordAdapter(this);
        mRecordAdapter.setRecordList(getRecords());
        mRecordList.setAdapter(mRecordAdapter);
    }

    private List<CustomRecord> getRecords(){
        List<CustomRecord> records = new ArrayList<>();
        try{
            Cursor cursor = mDb.rawQuery("select * from " + DbHelper.TABLE_CUSTOMRECORD, null);
            if(cursor.moveToFirst()){
                do{
                    int time = cursor.getInt(cursor.getColumnIndex("time"));
                    int record = cursor.getInt(cursor.getColumnIndex("record"));
                    CustomRecord customRecord = new CustomRecord(time, record);
                    records.add(customRecord);
                } while (cursor.moveToNext());
            }
        } catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
        return records;
    }

    public class CustomRecord{
        int time;
        int record;

        public CustomRecord(int time, int record){
            this.time = time;
            this.record = record;
        }
    }
}
