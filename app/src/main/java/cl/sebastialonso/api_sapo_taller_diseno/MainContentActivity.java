package cl.sebastialonso.api_sapo_taller_diseno;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainContentActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_content);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new DifferentFragmentsAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        //Tabs use all space, and are all shown
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        tabLayout.setupWithViewPager(viewPager);

    }

    private class DifferentFragmentsAdapter extends FragmentPagerAdapter {

        private String mTabTitles[] = new String[]{ "Sapear", "Historial", "Predecir"};

        public DifferentFragmentsAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int pos){
            Log.w("TAG", "\n\n\nposicion: " + String.valueOf(pos));
            switch (pos){
                case 0: return SapearFragment.newInstance("holi");
                case 1: return HistoryFragment.newInstance("holo");
                case 2: return PredictFragment.newInstance("hulu");
                default: return new Fragment();
            }
        }

        @Override
        public int getCount(){
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position){
            return mTabTitles[position];
        }
    }

}
