package com.example.classattendance.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.classattendance.R;
import com.example.classattendance.model.AttendanceItem;

import java.util.ArrayList;

public class AttendanceListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<AttendanceItem> attendanceList;

    public AttendanceListAdapter(Context context, ArrayList<AttendanceItem> attendanceList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.attendance_list_item, parent, false);
        }

        // Get the current item
        AttendanceItem currentItem = (AttendanceItem) getItem(position);

        // Find views from the layout
        TextView indexTextView = convertView.findViewById(R.id.listIndex);
        TextView timeTextView = convertView.findViewById(R.id.listTime);
        ImageView presentIcon = convertView.findViewById(R.id.iconPresent);
        TextView presentTextView = convertView.findViewById(R.id.present);
        ImageView lateIcon = convertView.findViewById(R.id.iconLate);
        TextView lateTextView = convertView.findViewById(R.id.late);
        ImageView qrIcon = convertView.findViewById(R.id.iconQR);

        // Set data to views
        indexTextView.setText(String.valueOf(position + 1)); // Adding 1 to position to start from 1
        timeTextView.setText(currentItem.getTime());
        presentTextView.setText(String.valueOf(currentItem.getPresentCount()));
        lateTextView.setText(String.valueOf(currentItem.getLateCount()));

        // You can set icons for qrIcon and others similarly if needed

        return convertView;
    }
}
