package com.example.classattendance.recycler;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classattendance.R;
import com.example.classattendance.model.Attendance;

import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {

    private List<Attendance> attendanceList;
    private Context context;

    public AttendanceAdapter(Context context, List<Attendance> attendanceList) {
        this.context = context;
        this.attendanceList = attendanceList;
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_list_item, parent, false);
        return new AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {
        Attendance attendance = attendanceList.get(position);
        holder.timeTextView.setText(String.valueOf(attendance.getTime()));
        holder.presentCountTextView.setText(String.valueOf(attendance.getPresentCount()));
        holder.lateCountTextView.setText(String.valueOf(attendance.getLateCount()));

        // Set up the RecyclerView for attendance students
        AttendanceStudentAdapter adapter = new AttendanceStudentAdapter(context, attendance.getAttendanceRecords());
        holder.recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    public class AttendanceViewHolder extends RecyclerView.ViewHolder {
        TextView timeTextView;
        TextView presentCountTextView;
        TextView lateCountTextView;
        RecyclerView recyclerView;
        ImageView iconQR;
        RelativeLayout attendanceLayout;
        LinearLayout memberAttendanceLayout;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.listTime);
            presentCountTextView = itemView.findViewById(R.id.present);
            lateCountTextView = itemView.findViewById(R.id.late);
            recyclerView = itemView.findViewById(R.id.recyclerViewStudents);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            iconQR = itemView.findViewById(R.id.iconQR);
            attendanceLayout = itemView.findViewById(R.id.attendanceClicked);
            memberAttendanceLayout = itemView.findViewById(R.id.memberAttendance);

            iconQR.setOnClickListener(v -> {
                View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_custom, null);
                ImageView dialogIcon = dialogView.findViewById(R.id.dialog_icon);
                dialogIcon.setImageResource(R.drawable.qrcode);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("QUÉT MÃ").setView(dialogView).create().show();
            });

            attendanceLayout.setOnClickListener(v -> {
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
        }
    }
}
