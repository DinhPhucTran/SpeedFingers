package tk.trandinhphuc.speedfingers;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

public class SecondFragment extends Fragment {

    public static final String SEC_RECORD = "secRecord";

//    private OnButtonClickListener mListener;
    private int mSpeed = 0;
    private int mHigh = 0;
    private int mRecord = 0;

    private TextView mTvSpeed;
    private TextView mTvHigh;
    private TextView mTvRecord;
    private Button mBtMain;

    private Handler mHandler;
    private Runnable mRunnable;

    public SecondFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_second, container, false);
        mTvSpeed = (TextView) v.findViewById(R.id.tvSpeed);
        mTvHigh = (TextView) v.findViewById(R.id.tvHigh);
        mTvRecord = (TextView) v.findViewById(R.id.tvRecord);
        mBtMain = (Button) v.findViewById(R.id.btMain);
        TextView labelHigh = (TextView) v.findViewById(R.id.labelHigh);
        TextView labelSpeed = (TextView) v.findViewById(R.id.labelSpeed);
        TextView labelRecord = (TextView) v.findViewById(R.id.labelRecord);

        Typeface type = Typeface.createFromAsset(getContext().getAssets(),"fonts/Play-Regular.ttf");
        mTvSpeed.setTypeface(type);
        mTvHigh.setTypeface(type);
        labelHigh.setTypeface(type);
        labelSpeed.setTypeface(type);
        labelRecord.setTypeface(type);

        SharedPreferences sharedPref = getActivity().getPreferences(MODE_PRIVATE);
        mRecord = sharedPref.getInt(SEC_RECORD, 0);
        mTvRecord.setText(mRecord + "");
        mTvHigh.setText(mHigh + "");

        final SharedPreferences.Editor editor = sharedPref.edit();

        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                mTvSpeed.setText(mSpeed + "");
                if(mSpeed > mHigh)
                {
                    mHigh = mSpeed;
                    mTvHigh.setText(mHigh + "");
                    if(mHigh > mRecord){
                        mRecord = mHigh;
                        mTvRecord.setText(mRecord + "");
                        editor.putInt(SEC_RECORD, mRecord);
                        editor.commit();
                    }
                }
                mSpeed = 0;
                mHandler.postDelayed(this, 1000);
            }
        };

        mHandler.postDelayed(mRunnable, 1000);

        mBtMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSpeed++;
            }
        });

        return v;
    }

    @Override
    public void onPause(){
        mHandler.removeCallbacks(mRunnable);
        super.onPause();
    }

//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnButtonClickListener) {
//            mListener = (OnButtonClickListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }
}
