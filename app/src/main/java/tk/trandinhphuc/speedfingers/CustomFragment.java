package tk.trandinhphuc.speedfingers;


import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class CustomFragment extends Fragment {

    private TextView mLabelSpeed;
    private TextView mTvSpeed;
    private TextView mTvTimer;
    private ImageButton mBtReset;
    private Button mBtMain;
    private EditText mEtInterval;
    private ImageButton mBtApply;
    private Button mBtViewRecords;

    private boolean mIsFirstClick = true;
    private boolean mIsFinished = false;
    private CountDownTimer mTimer;

    private int mSpeed = 0;
    private long mIntervalMillis = 0;
    private int mRecord = 0;

    private SQLiteOpenHelper mDbHelper;
    private SQLiteDatabase mDb;

    public CustomFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_custom, container, false);

        mLabelSpeed = (TextView) v.findViewById(R.id.labelSpeed);
        mTvSpeed = (TextView) v.findViewById(R.id.tvSpeed);
        mTvTimer = (TextView) v.findViewById(R.id.tvTimer);
        mBtReset = (ImageButton) v.findViewById(R.id.btReset);
        mBtMain = (Button) v.findViewById(R.id.btMain);
        mEtInterval = (EditText) v.findViewById(R.id.etInterval);
        mBtApply = (ImageButton) v.findViewById(R.id.btApply);
        mBtViewRecords = (Button) v.findViewById(R.id.btViewRecords);

        Typeface type = Typeface.createFromAsset(getContext().getAssets(),"fonts/Play-Regular.ttf");
        mLabelSpeed.setTypeface(type);
        mTvSpeed.setTypeface(type);

        mBtApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mEtInterval.getText().toString().equals("")){
                    try{
                        int sec = Integer.parseInt(mEtInterval.getText().toString());
                        mIntervalMillis = sec * 1000;
                        mTvTimer.setText(sec + ".00");
                        mIsFirstClick = true;
                        mIsFinished = false;
                        mSpeed = 0;
                        mTvSpeed.setText("0");

                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        int t = (int)mIntervalMillis/1000;
                        getRecord(t);
                    } catch (Exception e){
                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        mBtMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mIntervalMillis > 0) {
                    if (mIsFirstClick) {
                        mTimer = new CountDownTimer(mIntervalMillis, 10) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                mTvTimer.setText(millisUntilFinished / 1000 + "." + millisUntilFinished % 100);
                            }

                            @Override
                            public void onFinish() {
                                mIsFinished = true;
                                mTvTimer.setText("0.00");
                                if(mSpeed > mRecord){
                                    saveRecord((int)mIntervalMillis/1000, mSpeed);
                                }
                            }
                        }.start();
                        mIsFirstClick = false;
                    } else {
                        if (!mIsFinished) {
                            mSpeed++;
                            mTvSpeed.setText(mSpeed + "");
                        }
                    }
                }
            }
        });

        mBtReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsFirstClick = true;
                mIsFinished = false;
                mSpeed = 0;
                mTvSpeed.setText("0");
                mTvTimer.setText(mIntervalMillis / 1000 + ".00");
                mTimer.cancel();
            }
        });

        mBtViewRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CustomRecordActivity.class);
                startActivity(intent);
            }
        });

        mDbHelper = new DbHelper(getContext());
        mDb = mDbHelper.getReadableDatabase();
        return v;
    }

    @Override
    public void onPause(){
        if(mTimer != null)
            mTimer.cancel();
        mIsFirstClick = true;
        mIsFinished = false;
        mSpeed = 0;
        super.onPause();
    }

    private void saveRecord(int time, int record){
        ContentValues cv = new ContentValues();
        cv.put("time", time);
        cv.put("record", record);
        if(mDb != null){
            Cursor cursor = mDb.query(DbHelper.TABLE_CUSTOMRECORD, null, "time = ?", new String[]{time + ""}, null, null, null);
            if(cursor.moveToFirst()){
                mDb.update(DbHelper.TABLE_CUSTOMRECORD, cv, "time = ?", new String[]{time + ""});
            } else {
                mDb.insert(DbHelper.TABLE_CUSTOMRECORD, null, cv);
            }
            cursor.close();
        }
    }

    private void getRecord(int time){
        Cursor cursor = mDb.query(DbHelper.TABLE_CUSTOMRECORD, null, "time = ?", new String[]{time + ""}, null, null, null);
        if(cursor.moveToFirst()){
            mRecord = cursor.getInt(cursor.getColumnIndex("record"));
        }
    }

}
