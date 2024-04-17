package com.example.classattendance.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classattendance.R;
import com.example.classattendance.model.AttendanceStudent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AttendanceStudentAdapter extends RecyclerView.Adapter<AttendanceStudentAdapter.AttendanceStudentViewHolder> {

    private List<AttendanceStudent> attendanceStudentList;
    private Context context;

    public AttendanceStudentAdapter(Context context, List<AttendanceStudent> attendanceStudentList) {
        this.context = context;
        this.attendanceStudentList = attendanceStudentList;
    }

    @NonNull
    @Override
    public AttendanceStudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_list_item_student, parent, false);
        return new AttendanceStudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceStudentViewHolder holder, int position) {
        AttendanceStudent attendanceStudent = attendanceStudentList.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy", Locale.getDefault());
        Date date;
        try {
            date = sdf.parse(attendanceStudent.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            date = new Date(); // Đối tượng Date mặc định nếu không thể chuyển đổi
        }

        // Định dạng lại ngày thành chuỗi và hiển thị
        holder.listTimeTextView.setText(sdf.format(date));
        holder.nameTextView.setText(attendanceStudent.getName());
        if (attendanceStudent.isPresent()) {
            holder.iconPresentImageView.setImageResource(R.drawable.present);
        } else {
            holder.iconPresentImageView.setImageResource(R.drawable.late);
        }
    }

    @Override
    public int getItemCount() {
        return attendanceStudentList.size();
    }

    public class AttendanceStudentViewHolder extends RecyclerView.ViewHolder {
        TextView listTimeTextView;
        ImageView iconPresentImageView;
        TextView nameTextView;

        public AttendanceStudentViewHolder(@NonNull View itemView) {
            super(itemView);
            listTimeTextView = itemView.findViewById(R.id.listTime);
            iconPresentImageView = itemView.findViewById(R.id.iconPresent);
            nameTextView = itemView.findViewById(R.id.name);
        }
    }
}
