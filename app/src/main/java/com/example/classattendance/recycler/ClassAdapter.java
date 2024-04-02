package com.example.classattendance.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.classattendance.R;
import com.example.classattendance.model.Classroom;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {
    private List<Classroom> data;

    public ClassAdapter(List<Classroom> data) {
        this.data = data;
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
        Classroom data = this.data.get(i);
        classViewHolder.className.setText(data.getName());
        classViewHolder.classSub.setText(data.getSubject());

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
    }

}
