package com.example.jjplayer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.jjplayer.databinding.ActivityMainWindowBinding;
import com.google.android.material.navigation.NavigationView;

public class MainWindow extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainWindowBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainWindowBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMainWindow.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Set up navigation menu
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_window);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        // Find the TextView in the navigation header by its ID
        View headerView = navigationView.getHeaderView(0);
        TextView textViewSubtitle = headerView.findViewById(R.id.textView);


        // Set onClickListener on the TextView
        textViewSubtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the URL to open
                String url = getString(R.string.nav_header_subtitle);

                // Create an Intent to open the URL in a browser
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });


        // Set the text color of the username TextView
        String username = getString(R.string.nav_header_username);
        TextView usernameTextView = headerView.findViewById(R.id.usernameTextView);

        try {
            if (!username.equals("Not registered")) {
                // If user is registered, set text color to white
                usernameTextView.setTextColor(getResources().getColor(R.color.registered_color));
            } else {
                // If user is not registered, set text color to red
                usernameTextView.setTextColor(getResources().getColor(R.color.not_registered_color));
            }
        }
        catch (Exception e) {
            Log.e("Set text color error", "Error: " + e.getMessage(), e);
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_window, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_window);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
