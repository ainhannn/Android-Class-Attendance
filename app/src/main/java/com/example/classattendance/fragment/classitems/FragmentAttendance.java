package com.example.classattendance.fragment.classitems;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classattendance.R;
import com.example.classattendance.model.AttendanceItem;
import com.example.classattendance.model.AttendanceStudent;
import com.example.classattendance.recycler.AttendanceAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FragmentAttendance extends Fragment {
    private RecyclerView recyclerView;
    private AttendanceAdapter adapter;
    private List<AttendanceItem> itemList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewAttendance);
        itemList = createItemList();
        adapter = new AttendanceAdapter(getContext(), itemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        return view;
    }

    private List<AttendanceItem> createItemList() {
        // Create your list of items here
        List<AttendanceItem> itemList = new ArrayList<>();
        // Add items to the list
        List<AttendanceStudent> studentList1 = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy", Locale.getDefault());
        studentList1.add(new AttendanceStudent("23:59:59 12/12/2024", "Ngô Lê Huệ Ngân", true));
        studentList1.add(new AttendanceStudent("23:59:59 12/12/2024", "Ngô Lê Huệ Ngân", false));
        studentList1.add(new AttendanceStudent("23:59:59 12/12/2024", "Ngô Lê Huệ Ngân", true));

        List<AttendanceStudent> studentList2 = new ArrayList<>();
        studentList2.add(new AttendanceStudent("23:59:59 12/12/2024", "Ngô Lê Huệ Ngân", true));
        studentList2.add(new AttendanceStudent("23:59:59 12/12/2024", "Ngô Lê Huệ Ngân", true));
        studentList2.add(new AttendanceStudent("23:59:59 12/12/2024", "Ngô Lê Huệ Ngân", true));

        itemList.add(new AttendanceItem("23:59:59 12/12/2024", 12, 12, studentList1));
        itemList.add(new AttendanceItem("23:59:59 12/12/2024", 12, 13, studentList2));
        return itemList;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getView() != null) {
            LinearLayout memberAttendanceLayout = getView().findViewById(R.id.memberAttendance);
            memberAttendanceLayout.setVisibility(View.GONE);
            LinearLayout attendanceLayout = getView().findViewById(R.id.attendanceLayout);
            attendanceLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }
}