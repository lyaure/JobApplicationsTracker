package com.lyaurese.jobsorganizer.Activities;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.lyaurese.jobsorganizer.Fragments.BoardFragment;
import com.lyaurese.jobsorganizer.Fragments.CompaniesFragment;
import com.lyaurese.jobsorganizer.R;

public class MainBoardActivity extends FragmentActivity {
    private BottomNavigationView bottomNavigation;
    private Fragment fragment;
    private int fragmentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_board);

        // bottom nav
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnItemSelectedListener(navigationItemSelectedListener);

        fragment = new BoardFragment();
        loadFragment(fragment);
        fragmentID = R.layout.fragment_board;
    }

    // load fragment
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_ID, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private NavigationBarView.OnItemSelectedListener navigationItemSelectedListener =
            new NavigationBarView.OnItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_board:
                            fragment = new BoardFragment();
                            loadFragment(fragment);
                            fragmentID = R.layout.fragment_board;
                            return true;
                        case R.id.nav_jobs:
                            fragment = new CompaniesFragment();
                            loadFragment(fragment);
                            fragmentID = R.layout.fragment_companies;
                            return true;
                    }
                    return false;
                }
            };

    public void setFragmentID(int id){
        this.fragmentID = id;
    }

    @Override
    public void onBackPressed(){
        // doesn't close the app
        if(fragmentID == R.layout.fragment_add_application || fragmentID == R.layout.fragment_application_pager){
            fragment = new CompaniesFragment();
            loadFragment(fragment);
        }
        else{ // close the app and go home
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
}