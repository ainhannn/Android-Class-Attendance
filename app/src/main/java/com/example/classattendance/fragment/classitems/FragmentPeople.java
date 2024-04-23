package com.example.classattendance.fragment.classitems;

import static android.content.ContentValues.TAG;
import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classattendance.R;
import com.example.classattendance.api.ClassAPI;
import com.example.classattendance.api.NetworkUtil;
import com.example.classattendance.model.Class;
import com.example.classattendance.model.SimpleUser;
import com.example.classattendance.recycler.MemberAdapter;
import com.example.classattendance.utils.MyAuth;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentPeople extends Fragment {
    private TextView classNameTextView;
    private TextView classSubTextView;
    private TextView classStatusTextView;
    private MemberAdapter memberAdapter;
    private List<SimpleUser> memberList;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_people, container, false);

        // Include
        View includeView = view.findViewById(R.id.include);
        classNameTextView = includeView.findViewById(R.id.class_name);
        classSubTextView = includeView.findViewById(R.id.class_sub);
        classStatusTextView = includeView.findViewById(R.id.status);

        // Member List
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewPeople);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        memberList = new ArrayList<>();
        memberAdapter = new MemberAdapter(memberList, getContext());
        recyclerView.setAdapter(memberAdapter);

        // Load data
        ClassAPI api = NetworkUtil.self().getRetrofit().create(ClassAPI.class);
        Call<Class> call = api.getClassById(getActivity().getIntent().getIntExtra("class_id", 0));
        call.enqueue(new Callback<Class>() {
            @Override
            public void onResponse(Call<Class> call, Response<Class> response) {
                if (response.isSuccessful()) {
                    Class classItem = response.body();

                    // Pass data to include
                    classNameTextView.setText(classItem.getName());
                    classSubTextView.setText(classItem.getTeacher().getName());
                    if (getActivity().getIntent().getStringExtra("role").contains("teacher"))
                        classStatusTextView.setText("Created");
                    else
                        classStatusTextView.setText("Joined");

                    // Pass data to people list
                    memberList.clear();
                    memberList.addAll(classItem.getMembers());
                    memberAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<Class> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}