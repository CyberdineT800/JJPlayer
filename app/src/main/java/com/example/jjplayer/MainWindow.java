package com.example.jjplayer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.jjplayer.databinding.ActivityMainWindowBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

public class MainWindow extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainWindowBinding binding;
    NavigationView navigationView;
    View headerView;

    EditText username, password, reg_username, reg_password,
            reg_firstName, reg_lastName, reg_email;
    Button login, signUp, reg_register;
    TextInputLayout txtInLayoutUsername, txtInLayoutPassword, txtInLayoutRegPassword;
    CheckBox rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainWindowBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMainWindow.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        navigationView = binding.navView;

        // Set up navigation menu
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_window);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        // Find the TextView in the navigation header by its ID
        headerView = navigationView.getHeaderView(0);
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


        File userDataFile = new File(getFilesDir(), "user_data.json");
        if (userDataFile.exists()) {
            // User is registered, so load the user data and update the TextView with the full name
            try {
                // Read the JSON data from the file
                FileInputStream fis = new FileInputStream(userDataFile);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                StringBuilder userDataJson = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    userDataJson.append(line);
                }
                reader.close();

                // Parse the JSON data
                JSONObject userData = new JSONObject(userDataJson.toString());
                String fullName = userData.getString("firstName") + " " + userData.getString("lastName");

                // Update the TextView with the full name
                TextView usernameTextView = headerView.findViewById(R.id.usernameTextView);
                usernameTextView.setText(fullName);
                // Set text color to white for registered user
                usernameTextView.setTextColor(getResources().getColor(R.color.registered_color));

            } catch (IOException | JSONException e) {
                e.printStackTrace();
                // Handle any errors that may occur during file reading or JSON parsing
            }
        } else {
            // User is not registered, so set the TextView text to "Not registered" in red
            TextView usernameTextView = headerView.findViewById(R.id.usernameTextView);
            usernameTextView.setText(getString(R.string.nav_header_username));
            usernameTextView.setTextColor(getResources().getColor(R.color.not_registered_color));
        }


        // Set the text color of the username TextView
        String username = getString(R.string.nav_header_username);
        TextView usernameTextView = headerView.findViewById(R.id.usernameTextView);

//        try {
//            if (!username.toLowerCase().contains("not")) {
//                // If user is registered, set text color to white
//                usernameTextView.setTextColor(getResources().getColor(R.color.registered_color));
//            } else {
//                // If user is not registered, set text color to red
//                usernameTextView.setTextColor(getResources().getColor(R.color.not_registered_color));
//            }
//        }
//        catch (Exception e) {
//            Log.e("Set text color error", "Error: " + e.getMessage(), e);
//            //e.printStackTrace();
//        }

        usernameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the user is registered by checking the existence of the user_data.json file

                // String username = getString(R.string.nav_header_username);

                if (usernameTextView.getText().toString().toLowerCase().contains("not")) {
//                    Intent intent = new Intent(MainWindow.this, RegisterActivity.class);
//                    startActivity(intent);
                    ClickSignUp();
                } else {
                    // Create and show a Toast
                    Toast.makeText(getApplicationContext(), getString(R.string.already_registered), Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    //The method for opening the registration page and another processes or checks for registering
    private void ClickSignUp() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.register, null);
        dialogBuilder.setView(dialogView);

        reg_username = dialogView.findViewById(R.id.reg_username);
        reg_password = dialogView.findViewById(R.id.reg_password);
        reg_firstName = dialogView.findViewById(R.id.reg_firstName);
        reg_lastName = dialogView.findViewById(R.id.reg_lastName);
        reg_email = dialogView.findViewById(R.id.reg_email);
        reg_register = dialogView.findViewById(R.id.reg_register);
        txtInLayoutRegPassword = dialogView.findViewById(R.id.txtInLayoutRegPassword);

        AlertDialog dialog = dialogBuilder.create();

        reg_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check username
                if (reg_username.getText().toString().trim().isEmpty()) {
                    reg_username.setError("Please fill out this field");
                    return; // Exit the method if username is empty
                }
                // Check password
                if (reg_password.getText().toString().trim().isEmpty()) {
                    txtInLayoutRegPassword.setPasswordVisibilityToggleEnabled(false);
                    reg_password.setError("Please fill out this field");
                    return; // Exit the method if password is empty
                }
                txtInLayoutRegPassword.setPasswordVisibilityToggleEnabled(true);
                // Check first name
                if (reg_firstName.getText().toString().trim().isEmpty()) {
                    reg_firstName.setError("Please fill out this field");
                    return; // Exit the method if first name is empty
                }
                // Check last name
                if (reg_lastName.getText().toString().trim().isEmpty()) {
                    reg_lastName.setError("Please fill out this field");
                    return; // Exit the method if last name is empty
                }
                // Check email
                if (reg_email.getText().toString().trim().isEmpty()) {
                    reg_email.setError("Please fill out this field");
                    return; // Exit the method if email is empty
                }


                // If all fields are filled, you can proceed with registration logic here
                // Example: Store user information in a JSON file
                try {
                    JSONObject userData = new JSONObject();
                    userData.put("username", reg_username.getText().toString().trim());
                    userData.put("password", reg_password.getText().toString().trim());
                    userData.put("email", reg_email.getText().toString().trim());
                    userData.put("firstName", reg_firstName.getText().toString().trim());
                    userData.put("lastName", reg_lastName.getText().toString().trim());

                    // Write the JSON object to a file
                    String fileName = "user_data.json";
                    FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
                    fos.write(userData.toString().getBytes());
                    fos.close();

                    String fullName = reg_firstName.getText().toString().trim() + " " + reg_lastName.getText().toString().trim();
                    TextView usernameTextView = headerView.findViewById(R.id.usernameTextView);
                    usernameTextView.setText(fullName);

                    // Inform the user that registration is successful
                    Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_SHORT).show();

                    dialog.dismiss();

                    // Update the username TextView in the previous activity
                    Intent intent = new Intent(MainWindow.this, MainWindow.class);
                    intent.putExtra(String.valueOf(R.id.usernameTextView), fullName); // Pass the username as an extra
                    startActivity(intent);

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                    // Handle any errors that may occur during file writing or JSON object creation
                    Toast.makeText(getApplicationContext(), "Error saving user data", Toast.LENGTH_SHORT).show();
                }

            }
        });

        dialog.show();
    }

    // Method to update the value of a string resource programmatically
    private void updateStringResource(int resourceId, String newValue) {
        try {
            // Get the application package's resource ID for the given resource name
            Field field = R.string.class.getDeclaredField(getString(resourceId));
            field.setAccessible(true);

            // Set the new value for the string resource
            field.set(null, newValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
