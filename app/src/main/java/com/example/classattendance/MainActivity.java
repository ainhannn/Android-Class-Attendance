package com.example.classattendance;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.classattendance.databinding.ActivityMainBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    DrawerLayout drawLayout;
    ImageButton buttonDrawerToggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        drawLayout = findViewById(R.id.drawerLayout);
        buttonDrawerToggle = findViewById(R.id.buttonDrawerToggle);
        navigationView = findViewById(R.id.nav_view);

        buttonDrawerToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawLayout.open();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();

                if(itemId == R.id.nav_school){
                    Toast.makeText(MainActivity.this, "School Clicked", Toast.LENGTH_SHORT).show();
                }
                if(itemId == R.id.nav_calendar){
                    Toast.makeText(MainActivity.this, "Calendar Clicked", Toast.LENGTH_SHORT).show();
                }
                if(itemId == R.id.nav_Class1){
                    Toast.makeText(MainActivity.this, "Class1 Clicked", Toast.LENGTH_SHORT).show();
                }
                if(itemId == R.id.nav_Class2){
                    Toast.makeText(MainActivity.this, "Class2 Clicked", Toast.LENGTH_SHORT).show();
                }
                if(itemId == R.id.nav_Class3){
                    Toast.makeText(MainActivity.this, "Class3 Clicked", Toast.LENGTH_SHORT).show();
                }
                if(itemId == R.id.nav_class_passed){
                    Toast.makeText(MainActivity.this, "Class-passed Clicked", Toast.LENGTH_SHORT).show();
                }
                if(itemId == R.id.nav_folder){
                    Toast.makeText(MainActivity.this, "Folder Clicked", Toast.LENGTH_SHORT).show();
                }
                if(itemId == R.id.nav_setting){
                    Toast.makeText(MainActivity.this, "Setting Clicked", Toast.LENGTH_SHORT).show();
                }
                if(itemId == R.id.nav_help){
                    Toast.makeText(MainActivity.this, "Help Clicked", Toast.LENGTH_SHORT).show();
                }

                drawLayout.close();

                return false;
            }
        });

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}
