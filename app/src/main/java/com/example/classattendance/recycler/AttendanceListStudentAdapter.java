package com.example.classattendance.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.classattendance.R;
import com.example.classattendance.model.AttendanceStudent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AttendanceListStudentAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<AttendanceStudent> attendanceList;

    public AttendanceListStudentAdapter(Context context, ArrayList<AttendanceStudent> attendanceList) {
        this.context = context;
        this.attendanceList = attendanceList;
    }

    @Override
    public int getCount() {
        return attendanceList.size();
    }

    @Override
    public Object getItem(int position) {
        return attendanceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.attendance_list_item_student, parent, false);
        }

        // Get the current item
        AttendanceStudent currentItem = (AttendanceStudent) getItem(position);

        // Find views from the layout
        TextView timeTextView = convertView.findViewById(R.id.listTime);
        TextView lateTextView = convertView.findViewById(R.id.name);
        TextView presentIcon = convertView.findViewById(R.id.present);

        // Set data to views
        timeTextView.setText(formatDate(currentItem.getTime()));
        lateTextView.setText(currentItem.getName());

        // Check if presentIcon is null before setting background resource
        if (presentIcon != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy", Locale.getDefault());
            Date currentTime = new Date();
            try {
                Date itemTime = currentItem.getTime();
                if (itemTime != null && itemTime.after(currentTime)) {
                    presentIcon.setBackgroundResource(R.drawable.late);
                } else {
                    presentIcon.setBackgroundResource(R.drawable.present);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return convertView;
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy", Locale.getDefault());
        return sdf.format(date);
    }

}
