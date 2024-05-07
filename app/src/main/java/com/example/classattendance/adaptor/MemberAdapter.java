package com.example.classattendance.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classattendance.R;
import com.example.classattendance.model.SimpleUser;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ClassMemberViewHolder> {

    private static List<Integer> avtList;
    private final List<SimpleUser> ClassMemberItemList;
    private Context context;

    public MemberAdapter(List<SimpleUser> ClassMemberItemList, Context context) {
        this.ClassMemberItemList = ClassMemberItemList;
        this.context = context;
        if (avtList == null) {
            avtList = new ArrayList<>();
            avtList.add(R.drawable.ic_avt_1);
            avtList.add(R.drawable.ic_avt_2);
            avtList.add(R.drawable.ic_avt_3);
        }
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
        private final ShapeableImageView avt;

        public ClassMemberViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUsername = itemView.findViewById(R.id.name);
            avt = itemView.findViewById(R.id.user);
        }

        public void bind(SimpleUser SimpleUser) {
            textViewUsername.setText(SimpleUser.getName());
            avt.setImageResource(avtList.get(SimpleUser.getId()%3));
        }
    }
}
