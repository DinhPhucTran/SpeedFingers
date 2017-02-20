package tk.trandinhphuc.speedfingers;


import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class MinuteFragment extends Fragment {

    public static final String MIN_RECORD = "minRecord";

    private TextView mLabelHigh;
    private TextView mLabelSpeed;
    private TextView mLabelRecord;
    private TextView mTvHigh;
    private TextView mTvSpeed;
    private TextView mTvRecord;
    private TextView mTvTimer;
    private ImageButton mBtReset;
    private Button mBtMain;
    private boolean mIsFirstClick = true;
    private boolean mIsFinished = false;
    private CountDownTimer mTimer;

    private int mHigh = 0;
    private int mSpeed = 0;
    private int mRecord = 0;

    public MinuteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_minute, container, false);

        mLabelHigh = (TextView) v.findViewById(R.id.labelHigh);
        mLabelSpeed = (TextView) v.findViewById(R.id.labelSpeed);
        mLabelRecord = (TextView) v.findViewById(R.id.labelRecord);
        mTvHigh = (TextView) v.findViewById(R.id.tvHighMin);
        mTvSpeed = (TextView) v.findViewById(R.id.tvSpeedMin);
        mTvRecord = (TextView) v.findViewById(R.id.tvRecordMin);
        mTvTimer = (TextView) v.findViewById(R.id.tvTimer);
        mBtReset = (ImageButton) v.findViewById(R.id.btReset);
        mBtMain = (Button) v.findViewById(R.id.btMin);

        Typeface type = Typeface.createFromAsset(getContext().getAssets(),"fonts/Play-Regular.ttf");
        mLabelHigh.setTypeface(type);
        mLabelSpeed.setTypeface(type);
        mLabelRecord.setTypeface(type);
        mTvHigh.setTypeface(type);
        mTvSpeed.setTypeface(type);
        mTvRecord.setTypeface(type);

        SharedPreferences sharedPref = getActivity().getPreferences(MODE_PRIVATE);
        mRecord = sharedPref.getInt(MIN_RECORD, 0);
        mTvRecord.setText(mRecord + "");
        mTvHigh.setText(mHigh + "");
        final SharedPreferences.Editor editor = sharedPref.edit();

        mBtMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mIsFirstClick){
                    mTimer = new CountDownTimer(60000, 10) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            mTvTimer.setText(millisUntilFinished / 1000 + "." + millisUntilFinished % 100);
                        }

                        @Override
                        public void onFinish() {
                            mIsFinished = true;
                            mTvTimer.setText("0.00");
                            if(mSpeed > mHigh){
                                mHigh = mSpeed;
                                mTvHigh.setText(mSpeed + "");
                                if(mHigh > mRecord){
                                    mRecord = mHigh;
                                    mTvRecord.setText(mRecord + "");
                                    editor.putInt(MIN_RECORD, mRecord);
                                    editor.commit();
                                }
                            }
                        }
                    }.start();
                    mIsFirstClick = false;
                } else {
                    if(!mIsFinished) {
                        mSpeed++;
                        mTvSpeed.setText(mSpeed + "");
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
                mTvTimer.setText("60.00");
                mTimer.cancel();
            }
        });

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

}
