package project.istic.com.fetedelascience.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import project.istic.com.fetedelascience.R;
import project.istic.com.fetedelascience.global.PrefManager;
import project.istic.com.fetedelascience.helper.DBManager;
import project.istic.com.fetedelascience.task.DataAsyncTask;
import project.istic.com.fetedelascience.task.OnDataLoaded;


public class TutorialActivity extends AppCompatActivity implements OnDataLoaded {

    private static final String TAG = "TutorialActivity";

    private PrefManager prefManager;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;

    private TextView progress;
    private Button btnNext;

    private boolean readyToGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Checking for first time launch - before calling setContentView()
        prefManager = new PrefManager(this);
        if ( false && !prefManager.isFirstTimeLaunch()) {
            startMainActivity();

        } else {

            setContentView(R.layout.activity_tutorial);

            // Set to true, after loading data from AsyncTask!
            readyToGo = false;

            // Start load data
            DBManager.init(this);
            DBManager manager = DBManager.getInstance();
            DataAsyncTask task = new DataAsyncTask(getApplicationContext(), manager, this);
            task.execute();

            viewPager = (ViewPager) findViewById(R.id.view_pager);
            dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
            progress = (TextView) findViewById(R.id.progress);
            btnNext = (Button) findViewById(R.id.btn_next);

            // layouts of all tutorials sliders
            layouts = new int[]{
                    R.layout.slide_tutorial_1,
                    R.layout.slide_tutorial_2,
                    R.layout.slide_tutorial_3
            };

            // adding bottom dots
            addBottomDots(0);

            myViewPagerAdapter = new MyViewPagerAdapter();
            viewPager.setAdapter(myViewPagerAdapter);
            viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // checking for last page
                    // if last page home screen will be launched
                    int current = getItem(+1);
                    if (current < layouts.length) {
                        // move to next screen
                        viewPager.setCurrentItem(current);
                    } else {
                        if (readyToGo) {
                            startMainActivity();
                        } else {
                            Toast.makeText(getApplicationContext(), "Les données ne sont pas encore prêtes !", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onDataProgress(Double value) {
        progress.setText(value + "%");
    }

    /**
     * When AsyncTask is done
     */
    @Override
    public void onDataLoaded() {
        Log.d(TAG, "onDataLoaded done !");
        readyToGo = true;
        Toast.makeText(this, "GO !", Toast.LENGTH_LONG).show();
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            if (position == layouts.length - 1) {
                btnNext.setText(getResources().getString(R.string.tutorial_end));
                //PermissionHelper.checkFineLocationPermission(TutorialActivity.this, false);
            } else {
                btnNext.setText(getResources().getString(R.string.tutorial_next));
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) { }

        @Override
        public void onPageScrollStateChanged(int arg0) { }
    };

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int colorsActive = getResources().getColor(R.color.tutorial_dot_active);
        int colorsInactive = getResources().getColor(R.color.tutorial_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[currentPage].setTextColor(colorsActive);
        }
    }

    private void startMainActivity() {
        prefManager.setFirstTimeLaunchToFalse();
        startActivity(new Intent(TutorialActivity.this, MainActivity.class));
        finish();
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        private MyViewPagerAdapter() {}

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
