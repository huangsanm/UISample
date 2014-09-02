package com.huashengmi.ui.android.ui;

import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.huashengmi.ui.android.R;
import com.huashengmi.ui.android.utils.Globals;

public class MainActivity extends BaseActivity {

    private final static long EXIT_TIME = 2500;

    private long mTime;
    private DrawerLayout mDrawerLayout;
    private ListView mListView;
    private ActionBarDrawerToggle mDrawerToggle;

    //标题
    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    private String[] mArray;
    private String[] mValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buildComponent();

    }

    private void buildComponent() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mListView = (ListView) findViewById(R.id.drawer_navigation);
        mArray = getResources().getStringArray(R.array.ui_array);
        mValues = getResources().getStringArray(R.array.ui_value);

        mTitle = mDrawerTitle = getTitle();
        mListView.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mArray));
        mListView.setOnItemClickListener(mOnItemClickListener);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return false;
    }

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    };

    private void selectItem(int position){
        MainFragment fragment = new MainFragment();
        Bundle b = new Bundle();
        b.putString(MainFragment.PARAM_ACTION, mValues[position]);
        fragment.setArguments(b);
        FragmentManager fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.content_layout, fragment).commit();

        mListView.setItemChecked(position, true);
        setTitle(mArray[position]);
        mDrawerLayout.closeDrawer(mListView);
    }

    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() < mTime + EXIT_TIME){
            super.onBackPressed();
        }else{
            Toast.makeText(mContext, "再按一次推出UiSample", Toast.LENGTH_SHORT).show();
            mTime = System.currentTimeMillis();
        }
    }
}
