package com.c323FinalProject.colejmetzger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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

    private Bitmap getBitmapProfile() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String previouslyEncodedImage = sharedPref.getString("IMAGEDATA", "image not found");

        Bitmap bitmap = null;
        if( !previouslyEncodedImage.equalsIgnoreCase("") ){
            byte[] b = Base64.decode(previouslyEncodedImage, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        }
        return bitmap;
    }

    private void setupDrawerContent(NavigationView navigationView) {
        //Set up profile with image and info
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        Menu menu = navigationView.getMenu();
        MenuItem profileItem = (MenuItem) menu.getItem(0);
        Menu profileMenu = profileItem.getSubMenu();
        MenuItem menu_image = profileMenu.findItem(R.id.menu_image);
        MenuItem menu_name = profileMenu.findItem(R.id.menu_name);
        MenuItem menu_email = profileMenu.findItem(R.id.menu_email);

        //Drawable drawable = new BitmapDrawable(getResources(), getBitmapProfile());
        //menu_image.setIcon(drawable);
        menu_name.setTitle(sharedPref.getString("NAME", "name not found"));
        menu_email.setTitle(sharedPref.getString("EMAIL", "email not found"));

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