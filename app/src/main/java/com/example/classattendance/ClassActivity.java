package com.example.classattendance;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.classattendance.fragment.classitems.FragmentAttendance;
import com.example.classattendance.fragment.classitems.FragmentNotification;
import com.example.classattendance.fragment.classitems.FragmentPeople;
import com.example.classattendance.recycler.VPAdapter;
import com.google.android.material.tabs.TabLayout;

public class ClassActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        // Get data from intent extras
        String className = getIntent().getStringExtra("class_name");
        String classSubject = getIntent().getStringExtra("class_subject");

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        tabLayout.setupWithViewPager(viewPager);

        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new FragmentNotification(), getString(R.string.notification));
        vpAdapter.addFragment(new FragmentAttendance(), getString(R.string.attendance));
        vpAdapter.addFragment(new FragmentPeople(), getString(R.string.people));
        viewPager.setAdapter(vpAdapter);
    }
}