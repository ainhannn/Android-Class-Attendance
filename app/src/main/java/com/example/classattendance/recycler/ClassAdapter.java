package com.example.classattendance.recycler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.classattendance.R;
import com.example.classattendance.model.SimpleClass;
import com.example.classattendance.utils.MyAuth;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {
    private final List<SimpleClass> data;
    private final OnItemClickListener listener;
    private final OnLongClickListener longClickListener;

    // Define interface for item click
    public interface OnItemClickListener {
        void onItemClick(SimpleClass classroom);
    }

    public interface OnLongClickListener {
        void onLongClick(SimpleClass classroom);
    }

    public ClassAdapter(List<SimpleClass> data, OnItemClickListener listener, OnLongClickListener longClickListener) {
        this.data = data;
        this.listener = listener;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @NotNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.class_item, viewGroup, false);
        return new ClassViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ClassViewHolder classViewHolder, int i) {
        final SimpleClass data = this.data.get(i);
        classViewHolder.bind(data, listener, longClickListener);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder {
        public TextView className;
        public TextView classSub;
        public TextView classStatus;

        public ClassViewHolder(View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.class_name);
            classSub = itemView.findViewById(R.id.class_sub);
            classStatus = itemView.findViewById(R.id.status);
        }

        public void bind(final SimpleClass classroom, final OnItemClickListener listener, final OnLongClickListener longClickListener) {
            // Set data to views
            Log.e("err", "bind: class"+ classroom.getName() + " " + classroom.getSubject());
            className.setText(classroom.getName());
            classSub.setText(classroom.getSubject());
            try {
                if (classroom.getTeacherId() == MyAuth.getModelUser().getId())
                    classStatus.setText("Created");
                else
                    classStatus.setText("Joined");
            } catch (Exception ex) {
                classStatus.setText("Joined");
            }
            // Set click listener
            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onItemClick(classroom);
                }
            });
            itemView.setOnLongClickListener(v -> {
                if (longClickListener != null) {
                    longClickListener.onLongClick(classroom);
                }
                return true;
            });
        }
    }

}
