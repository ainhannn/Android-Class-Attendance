package com.example.classattendance.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classattendance.R;
import com.example.classattendance.model.SimpleClass;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {
    private final List<SimpleClass> data;
    private final OnItemClickListener listener;

    // Define interface for item click
    public interface OnItemClickListener {
        void onItemClick(SimpleClass classroom);
    }

    public ClassAdapter(List<SimpleClass> data, OnItemClickListener listener) {
        this.data = data;
        this.listener = listener;
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
        classViewHolder.bind(data, listener);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder {
        public TextView className;
        public TextView classSub;

        public ClassViewHolder(View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.class_name);
            classSub = itemView.findViewById(R.id.class_sub);
        }

        public void bind(final SimpleClass classroom, final OnItemClickListener listener) {
            // Set data to views
            className.setText(classroom.getName());
            classSub.setText(classroom.getSubject());

            // Set click listener
            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    // Invoke onItemClick method of the listener
                    listener.onItemClick(classroom);
                }
            });
        }
    }

}
