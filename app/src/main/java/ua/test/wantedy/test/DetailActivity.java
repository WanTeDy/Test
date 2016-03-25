package ua.test.wantedy.test;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import java.lang.reflect.Field;

/**
 * Created by user on 20.03.2016.
 */
public class DetailActivity extends FragmentActivity{

    FilmList mFilmList;
    int PAGES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mFilmList = (FilmList)getIntent().getParcelableExtra("FilmList");
        PAGES = mFilmList.getList().size();
        int position = getIntent().getIntExtra("position", 0);

        ViewPager vpager = (ViewPager) findViewById(R.id.vpager);
        PagerAdapter pagerAdapter = new FilmPagerAdapter(getSupportFragmentManager());
        try {
            Field field = ViewPager.class.getDeclaredField("mRestoredCurItem");
            field.setAccessible(true);
            field.set(vpager, position);
        } catch (Exception e) {
            e.printStackTrace();
        }
         vpager.setAdapter(pagerAdapter);
    }

    private class FilmPagerAdapter extends FragmentPagerAdapter {

        public FilmPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Parcelable obj = mFilmList;
            return ViewFragment.newInstance(position, obj);
        }

        @Override
        public int getCount() {
            return PAGES;
        }
    }
}