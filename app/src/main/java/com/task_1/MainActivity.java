package com.task_1;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.AppDelegate;
import com.adapters.PagerAdapter;
import com.adapters.ProductRecyclerViewAdapter;
import com.constants.Tags;
import com.fragments.BannerFragment;
import com.interfaces.OnListItemClickListener;
import com.models.ProductModel;
import com.utils.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, OnListItemClickListener {

    private PagerAdapter bannerPagerAdapter;
    private List<Fragment> bannerFragment = new ArrayList();
    private LinearLayout pager_indicator;
    private ViewPager view_pager;
    private Handler mHandler;

    private RecyclerView recyclerView;
    private SpacesItemDecoration itemDecoration;
    private ProductRecyclerViewAdapter rcAdapter;
    public ArrayList<ProductModel> productArray = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setHandler();
        initView();
        setBanner();
        switchTab(0);
    }

    private void setHandler() {
        mHandler = new Handler() {
            @Override
            public void dispatchMessage(Message msg) {
                super.dispatchMessage(msg);
                switch (msg.what) {
                    case 10:
                        break;
                }
            }
        };
    }

    private void initView() {
        view_pager = (ViewPager) findViewById(R.id.view_pager);
        pager_indicator = (LinearLayout) findViewById(R.id.pager_indicator);

        findViewById(R.id.rl_video).setOnClickListener(this);
        findViewById(R.id.rl_image).setOnClickListener(this);
        findViewById(R.id.rl_milestone).setOnClickListener(this);

        itemDecoration = new SpacesItemDecoration(AppDelegate.dpToPix(this, 5));

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        fillArray();
        rcAdapter = new ProductRecyclerViewAdapter(this, productArray, this);
        recyclerView.setBackgroundColor(getResources().getColor(R.color.grey_font));
        recyclerView.setPadding(AppDelegate.dpToPix(this, 5), AppDelegate.dpToPix(this, 5), AppDelegate.dpToPix(this, 5), AppDelegate.dpToPix(this, 5));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(rcAdapter);

    }

    private void fillArray() {
        productArray.add(new ProductModel("1", "Title 1", "2 HOUR", "Lorem Ipsum is simply dummy text of the printing and typesetting industry."));
        productArray.add(new ProductModel("2", "Title 2", "2 HOUR", "Lorem Ipsum is simply dummy text of the printing and typesetting industry."));
        productArray.add(new ProductModel("3", "Title 3", "3 HOUR", "Lorem Ipsum is simply dummy text of the printing and typesetting industry."));
        productArray.add(new ProductModel("4", "Title 4", "4 HOUR", "Lorem Ipsum is simply dummy text of the printing and typesetting industry."));
        productArray.add(new ProductModel("5", "Title 5", "5 HOUR", "Lorem Ipsum is simply dummy text of the printing and typesetting industry."));
        productArray.add(new ProductModel("6", "Title 6", "6 HOUR", "Lorem Ipsum is simply dummy text of the printing and typesetting industry."));
        productArray.add(new ProductModel("7", "Title 7", "7 HOUR", "Lorem Ipsum is simply dummy text of the printing and typesetting industry."));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_video:
                switchTab(0);
                break;
            case R.id.rl_image:
                switchTab(1);
                break;
            case R.id.rl_milestone:
                switchTab(2);
                break;
        }
    }

    private void switchTab(int i) {
        findViewById(R.id.view_video).setVisibility(View.GONE);
        findViewById(R.id.view_image).setVisibility(View.GONE);
        findViewById(R.id.view_milestone).setVisibility(View.GONE);

        ((TextView) findViewById(R.id.txt_video)).setTextColor(Color.DKGRAY);
        ((TextView) findViewById(R.id.txt_image)).setTextColor(Color.DKGRAY);
        ((TextView) findViewById(R.id.txt_milestone)).setTextColor(Color.DKGRAY);

        findViewById(R.id.img_video).setSelected(false);
        findViewById(R.id.img_image).setSelected(false);
        findViewById(R.id.img_milestone).setSelected(false);

        switch (i) {
            case 0:
                findViewById(R.id.view_video).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.txt_video)).setTextColor(getColor(R.color.colorPrimary));
                findViewById(R.id.img_video).setSelected(true);
//                Update list here according to Videos

                break;
            case 1:
                findViewById(R.id.view_image).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.txt_image)).setTextColor(getColor(R.color.colorPrimary));
                findViewById(R.id.img_image).setSelected(true);
//                Update list here according to Videos

                break;
            case 2:
                findViewById(R.id.view_milestone).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.txt_milestone)).setTextColor(getColor(R.color.colorPrimary));
                findViewById(R.id.img_milestone).setSelected(true);
//                Update list here according to Videos

                break;
        }
    }

    private ImageView dotsArray[];
    private int dotsCount;

    private void switchBannerPage_Hero(int position) {
        if (dotsCount > 0) {
            for (int i = 0; i < dotsCount; i++) {
                dotsArray[i].setImageResource(R.drawable.bg_deselect_dot);
            }
            dotsArray[position].setImageResource(R.drawable.bg_select_dot);
        }
    }

    public void setBanner() {
        ArrayList<String> imageArray = new ArrayList<>();
        imageArray.add("Header 1");
        imageArray.add("Header 2");
        imageArray.add("Header 3");
        imageArray.add("Header 4");
        imageArray.add("Header 5");
        for (int i = 0; i < imageArray.size(); i++) {
            Fragment fragment = new BannerFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Tags.DATA, imageArray.get(i));
            fragment.setArguments(bundle);
            bannerFragment.add(fragment);
        }

        pager_indicator.removeAllViews();
        dotsCount = bannerFragment.size();
        if (dotsCount > 0) {
            dotsArray = new android.widget.ImageView[dotsCount];
            for (int i = 0; i < dotsCount; i++) {
                dotsArray[i] = new android.widget.ImageView(this);
                dotsArray[i].setImageDrawable(getResources().getDrawable(R.drawable.bg_deselect_dot));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(AppDelegate.getInstance(this).dpToPix(this, 7), AppDelegate.getInstance(this).dpToPix(this, 7));
                params.setMargins(AppDelegate.getInstance(this).dpToPix(this, 3), 0, AppDelegate.getInstance(this).dpToPix(this, 3), 0);
                pager_indicator.addView(dotsArray[i], params);
            }
            dotsArray[0].setImageDrawable(getResources().getDrawable(R.drawable.bg_select_dot));
        }

        bannerPagerAdapter = new PagerAdapter(getSupportFragmentManager(), bannerFragment);
        view_pager.setAdapter(bannerPagerAdapter);
        view_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switchBannerPage_Hero(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        switchBannerPage_Hero(0);
        timeOutLoop();
    }

    private Runnable mRunnable;

    private void timeOutLoop() {
        mHandler.removeCallbacks(mRunnable);
        if (mRunnable == null)
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        int value = view_pager.getCurrentItem();
                        if (value == bannerPagerAdapter.getCount()) {
                            value = 0;
                        } else {
                            value++;
                        }
                        if (value < bannerPagerAdapter.getCount())
                            view_pager.setCurrentItem(value, true);
                        if (!isDestroyed()) {
                            mHandler.postDelayed(this, 3000);
                        } else {
                            mHandler.removeCallbacks(this);
                        }
                    } catch (Exception e) {
                        AppDelegate.LogE(e);
                    }
                }
            };
        mHandler.postDelayed(mRunnable, 3000);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void setOnListItemClickListener(String name, int position) {
        // Handle the item click event

    }
}
