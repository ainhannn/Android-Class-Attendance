package com.example.classattendance.recycler;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.classattendance.R;
import com.example.classattendance.model.AttendanceItem;

import java.util.ArrayList;

public class AttendanceListAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<AttendanceItem> attendanceList;

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
        TextView presentTextView = convertView.findViewById(R.id.present);
        TextView lateTextView = convertView.findViewById(R.id.late);
        ImageView iconQR = convertView.findViewById(R.id.iconQR);

        // Set data to views
        indexTextView.setText(String.valueOf(position + 1)); // Adding 1 to position to start from 1
        timeTextView.setText(currentItem.getTime());
        presentTextView.setText(String.valueOf(currentItem.getPresentCount()));
        lateTextView.setText(String.valueOf(currentItem.getLateCount()));

        // Find the views for clicking
        LinearLayout attendanceLayout = convertView.findViewById(R.id.attendanceLayout);
        RelativeLayout attendanceClickedLayout = convertView.findViewById(R.id.attendanceClicked);
        LinearLayout memberAttendanceLayout = convertView.findViewById(R.id.memberAttendance);

        // Set OnClickListener to attendanceClickedLayout
        attendanceClickedLayout.setOnClickListener(v -> {
            // Toggle visibility of memberAttendanceLayout
            if (memberAttendanceLayout.getVisibility() == View.GONE) {
                TransitionManager.beginDelayedTransition(attendanceLayout, new AutoTransition());
                memberAttendanceLayout.setVisibility(View.VISIBLE);
                attendanceLayout.setBackgroundColor(Color.parseColor("#724CAF50"));
            } else {
                TransitionManager.beginDelayedTransition(attendanceLayout, new AutoTransition());
                memberAttendanceLayout.setVisibility(View.GONE);
                attendanceLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        });

        iconQR.setOnClickListener(v -> {
            View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_custom, null);
            ImageView dialogIcon = dialogView.findViewById(R.id.dialog_icon);
            dialogIcon.setImageResource(R.drawable.qrcode);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("QUÉT MÃ").setView(dialogView).create().show();
        });

        return convertView;
    }
}
