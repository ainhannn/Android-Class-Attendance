package com.example.classattendance.recycler;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

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

        if (attendance.getCode() == null || attendance.getCode().isEmpty()) {
            holder.iconQR.setVisibility(View.GONE);
        } else {
            holder.iconQR.setVisibility(View.VISIBLE);
            holder.iconQR.setTooltipText(attendance.getCode());
        }

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

            if (!String.valueOf(iconQR.getTooltipText()).isEmpty()) {
                iconQR.setOnClickListener(v -> {
                    View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_custom, null);
                    String qrCodeStr = String.valueOf(iconQR.getTooltipText());

                    // Generate QR code and set it to ImageView
                    ImageView dialogIcon = dialogView.findViewById(R.id.dialog_icon);
                    try {
                        Bitmap bitmap = encodeAsBitmap(qrCodeStr);
                        dialogIcon.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        Log.e("QR Code Error", e.getMessage());
                    }

                    // Set QR code to TextView
                    TextView qrCodeTxt = dialogView.findViewById(R.id.qrCode);
                    qrCodeTxt.setText(qrCodeStr);

                    // Create Dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("QUÉT MÃ").setView(dialogView).create().show();
                });
            }

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

        private Bitmap encodeAsBitmap(String str) throws WriterException {
            BitMatrix result;
            try {
                result = new MultiFormatWriter().encode(str,
                        BarcodeFormat.QR_CODE, 500, 500, null);
            } catch (IllegalArgumentException iae) {
                // Unsupported format
                return null;
            }
            int w = result.getWidth();
            int h = result.getHeight();
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                int offset = y * w;
                for (int x = 0; x < w; x++) {
                    pixels[offset + x] = result.get(x, y) ? 0xFF000000 : 0xFFFFFFFF;
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, 500, 0, 0, w, h);
            return bitmap;
        }
    }
}
