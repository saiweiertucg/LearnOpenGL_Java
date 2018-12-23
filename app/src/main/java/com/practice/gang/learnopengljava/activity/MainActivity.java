package com.practice.gang.learnopengljava.activity;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.practice.gang.learnopengljava.R;
import com.practice.gang.learnopengljava.fragment.BaseFragment;
import com.practice.gang.learnopengljava.fragment.ItemFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements BaseFragment.OnListFragmentInteractionListener {

    private static final String TAG = "MainActivity";
    private Toolbar mToolbar;
    private Map<String, List<Class>> map = new HashMap<>();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigationview);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                String title = String.valueOf(item.getTitle());
                navFrgment(title);

                DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.setPackage(getPackageName());
        List<ResolveInfo> infos = getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : infos) {
            String name = info.activityInfo.name;
            String pkg = name.substring(name.lastIndexOf(".", name.lastIndexOf(".") - 1) + 1, name.lastIndexOf("."));
            try {
                if (map.containsKey(pkg)) {
                    map.get(pkg).add(Class.forName(name));
                } else {
                    List<Class> list = new ArrayList<>();
                    list.add(Class.forName(name));
                    map.put(pkg, list);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        navFrgment("gettingstart");
    }

    private void navFrgment(String title) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, BaseFragment.newInstance(title)).commit();
        mToolbar.setTitle(title.toUpperCase());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    @Override
    public void onListFragmentInteraction(ItemFactory.FragmentItem item) {
        startActivity(new Intent(this, item.getActivityClass()));
    }

    @Override
    public List<Class> getActivityClasses(String pkg) {
        return map.get(pkg);
    }
}
