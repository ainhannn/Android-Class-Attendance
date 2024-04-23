package com.example.classattendance.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classattendance.R;
import com.example.classattendance.model.SimpleUser;

import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ClassMemberViewHolder> {

    private final List<SimpleUser> ClassMemberItemList;
    private Context context;

    public MemberAdapter(List<SimpleUser> ClassMemberItemList, Context context) {
        this.ClassMemberItemList = ClassMemberItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ClassMemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_list_item, parent, false);
        return new ClassMemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassMemberViewHolder holder, int position) {
        SimpleUser SimpleUser = ClassMemberItemList.get(position);
        holder.bind(SimpleUser);
    }

    @Override
    public int getItemCount() {
        return ClassMemberItemList.size();
    }

    public static class ClassMemberViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewUsername;

        public ClassMemberViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUsername = itemView.findViewById(R.id.name);
        }

        public void bind(SimpleUser SimpleUser) {
            textViewUsername.setText(SimpleUser.getName());
        }
    }
}
