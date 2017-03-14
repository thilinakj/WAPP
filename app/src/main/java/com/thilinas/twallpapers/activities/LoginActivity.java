package com.thilinas.twallpapers.activities;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.thilinas.twallpapers.R;
import com.thilinas.twallpapers.fragments.LoginFragment;
import com.thilinas.twallpapers.fragments.RegisterFragment;

public class LoginActivity extends NetworkChangeActivity {

    public FragmentPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        adapterViewPager = new LoginPagerAdapter(getSupportFragmentManager());
        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        vpPager.setAdapter(adapterViewPager);
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if(position==2){
                    // button.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public static class LoginPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 2;

        public LoginPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() { return NUM_ITEMS; }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:    return  LoginFragment.newInstance(1, "Page # 1");
                case 1:    return  RegisterFragment.newInstance(2, "Page # 2");
                default:   return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }
    }
}
