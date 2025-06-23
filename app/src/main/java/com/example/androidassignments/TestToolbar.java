package com.example.androidassignments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.androidassignments.databinding.ActivityTestToolbarBinding;

public class TestToolbar extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityTestToolbarBinding binding;
    private String snackMess= "You selected item 1 Hamburger";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTestToolbarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_test_toolbar);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, getString(R.string.snack_message), Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show();
            }
        });

        Log.d("RTest", "ID de menu_pizza: " + R.id.menu_pizza);

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_test_toolbar);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        View view = findViewById(R.id.fab);
        int id = menuItem.getItemId();

        if (id == R.id.menu_hamburger) {
            Log.d("Toolbar", "Option 1 Hamburger Selected");
            Snackbar.make(view, snackMess, Snackbar.LENGTH_LONG)
                    .setAnchorView(R.id.fab)
                    .setAction("Action", null).show();
            return true;
        } else if (id == R.id.menu_hotdog) {
            Log.d("Toolbar", "Option 2 Hot Dog Selected");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.go_back);
                        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        });
                        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
            // Create the AlertDialog
                        AlertDialog dialog = builder.create();
                        dialog.show();
            return true;
        } else if (id == R.id.menu_pizza) {
            Log.d("Toolbar", "Option 3 Pizza Selected");
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_custom_design, null);
            EditText input = dialogView.findViewById(R.id.editText_new_message);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialogView)
                    .setTitle("Custom Message")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            snackMess = input.getText().toString();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        } else if(id == R.id.menu_about) {
            Toast.makeText(this,"Version 1.0, by Cristian Javier Da Camara Sousa", Toast.LENGTH_LONG).show();
            return true;
        } else {
            return super.onOptionsItemSelected(menuItem);
        }
    }



}