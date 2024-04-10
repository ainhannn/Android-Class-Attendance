package com.example.classattendance.fragment.classitems;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.classattendance.R;
import com.example.classattendance.model.AttendanceItem;
import com.example.classattendance.recycler.AttendanceListAdapter;

import java.util.ArrayList;

public class FragmentAttendance extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_attendance, container, false);

        // Sample data for demonstration, replace it with your actual data
        ArrayList<AttendanceItem> attendanceItems = new ArrayList<>();
        attendanceItems.add(new AttendanceItem("23:59:59 - 31/12/2024", 12, 12));
        attendanceItems.add(new AttendanceItem("23:59:59 - 31/12/2024", 12, 12));
        attendanceItems.add(new AttendanceItem("23:59:59 - 31/12/2024", 12, 12));
        attendanceItems.add(new AttendanceItem("23:59:59 - 31/12/2024", 12, 12));
        attendanceItems.add(new AttendanceItem("23:59:59 - 31/12/2024", 12, 12));
        attendanceItems.add(new AttendanceItem("23:59:59 - 31/12/2024", 12, 12));
        attendanceItems.add(new AttendanceItem("23:59:59 - 31/12/2024", 12, 12));
        attendanceItems.add(new AttendanceItem("23:59:59 - 31/12/2024", 12, 12));
        attendanceItems.add(new AttendanceItem("23:59:59 - 31/12/2024", 12, 12));
        attendanceItems.add(new AttendanceItem("23:59:59 - 31/12/2024", 12, 12));
        attendanceItems.add(new AttendanceItem("23:59:59 - 31/12/2024", 12, 12));
        attendanceItems.add(new AttendanceItem("23:59:59 - 31/12/2024", 12, 12));
        attendanceItems.add(new AttendanceItem("23:59:59 - 31/12/2024", 12, 12));

        // Add more attendance items as needed

        // Create adapter and set it to ListView
        AttendanceListAdapter adapter = new AttendanceListAdapter(getContext(), attendanceItems);
        ListView listView = rootView.findViewById(R.id.listAttendanceView);
        listView.setAdapter(adapter);

        return rootView;
    }
}