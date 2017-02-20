package tk.trandinhphuc.speedfingers;

import android.animation.Animator;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout mBgLayout;
    private SecondFragment mSecFragment;
    private MinuteFragment mMinFragment;
    private CustomFragment mCustomFragment;
    private View mColorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBgLayout = (RelativeLayout) findViewById(R.id.mainBg);
        mColorView = findViewById(R.id.colorView);

        AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

    // Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.second, R.drawable.one_s, R.color.colorBg1);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.minute, R.drawable.sixty_s, R.color.colorBg2);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.custom, R.drawable.x_s, R.color.colorBg3);

    // Add items
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);

//        bottomNavigation.setAccentColor(Color.parseColor("#F63D2B"));
        bottomNavigation.setInactiveColor(Color.parseColor("#747474"));

        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);
        bottomNavigation.setColored(true);
        bottomNavigation.setTranslucentNavigationEnabled(true);

        mSecFragment = new SecondFragment();
        mMinFragment = new MinuteFragment();
        mCustomFragment = new CustomFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFrame, mSecFragment);
        transaction.commit();

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if(position == 0) {
//                    mBgLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBg1));
                    setBackground(R.color.colorBg1, mColorView.getMeasuredWidth() / 6);
                    transaction.replace(R.id.mainFrame, mSecFragment);
                } else if(position == 1){
//                    mBgLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBg2));
                    setBackground(R.color.colorBg2, mColorView.getMeasuredWidth() /2);
                    transaction.replace(R.id.mainFrame, mMinFragment);
                } else if(position == 2){
//                    mBgLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBg3));
                    setBackground(R.color.colorBg3, mColorView.getMeasuredWidth() * 5 / 6);
                    transaction.replace(R.id.mainFrame, mCustomFragment);
                }
                transaction.commit();
                return true;
            }
        });

    }

    public void setBackground(final int color, int cx){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            //int cx = mColorView.getMeasuredWidth() / 2;
            int cy = mColorView.getMeasuredHeight();
            int finalRadius = Math.max(mColorView.getWidth(), mColorView.getHeight());
            Animator anim = ViewAnimationUtils.createCircularReveal(mColorView, cx, cy, 0, finalRadius);
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mColorView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), color));
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mBgLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), color));
                    mColorView.setBackgroundColor(Color.TRANSPARENT);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            anim.setStartDelay(120);
            anim.start();
        } else {
            mBgLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), color));
        }
    }
}
