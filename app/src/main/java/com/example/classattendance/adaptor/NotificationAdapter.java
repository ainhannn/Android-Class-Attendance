package com.example.classattendance.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classattendance.R;
import com.example.classattendance.model.Notification;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private final List<Notification> notificationItemList;
    private Context context;

    public NotificationAdapter(List<Notification> notificationItemList, Context context) {
        this.notificationItemList = notificationItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationAdapter.NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_list_item, parent, false);
        return new NotificationAdapter.NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.NotificationViewHolder holder, int position) {
        Notification notification = notificationItemList.get(position);
        holder.bind(notification);
    }

    @Override
    public int getItemCount() {
        return notificationItemList.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewUsername;
        private final TextView textViewTime;
        private final TextView textViewContent;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUsername = itemView.findViewById(R.id.name);
            textViewTime = itemView.findViewById(R.id.time);
            textViewContent = itemView.findViewById(R.id.content);
        }

        public void bind(Notification notification) {
            textViewUsername.setText(notification.getUserName());
            textViewTime.setText(formatTime(notification.getTime()));
            textViewContent.setText(notification.getContent());
        }

        private String formatTime(Date time) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy", Locale.getDefault());
            return dateFormat.format(time);
        }
    }
}
