package com.c323FinalProject.colejmetzger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.c323FinalProject.colejmetzger.fragments.CalendarFragment;
import com.c323FinalProject.colejmetzger.fragments.HomeFragment;
import com.c323FinalProject.colejmetzger.fragments.RecentOrdersFragment;
import com.c323FinalProject.colejmetzger.utilities.DatabaseHelper;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHelper.createInstance(getApplicationContext()); // create db singleton

        // setup nav
        nav = (NavigationView) findViewById(R.id.nvView);
        setUpDrawer();
        setupDrawerContent(nav);

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.flContent, HomeFragment.class, null)
                .commit();

    }

    private void setUpDrawer() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.burger);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        selectDrawerItem(item);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;
        Class frag;
        switch(menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                frag = RecentOrdersFragment.class;
                break;
            case R.id.nav_second_fragment:
                frag = CalendarFragment.class;
                break;
            default:
                frag = HomeFragment.class;
        }

        try {
            fragment = (Fragment) frag.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        drawer.closeDrawers();
    }
}