package com.example.taskman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initialize();
    }

    private void initialize() {
        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);

        getSupportFragmentManager().beginTransaction().replace(R.id.prjContainer,new projectFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment = null;


                    switch (item.getItemId()){
                        case R.id.project:
                            fragment = new projectFragment();
                            break;
                        case R.id.issues:
                            fragment = new issuesFragment();
                            break;
                        case R.id.notifications:
                            fragment = new notificationFragment();
                            break;
                        case R.id.account:
                            fragment = new accountFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.prjContainer, fragment).commit();

                    return true;
                }
            };


}
