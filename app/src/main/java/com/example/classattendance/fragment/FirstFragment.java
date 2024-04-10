package com.example.classattendance.fragment;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classattendance.ClassActivity;
import com.example.classattendance.R;
import com.example.classattendance.api.UserAPI;
import com.example.classattendance.databinding.FragmentFirstBinding;
import com.example.classattendance.model.SimpleClass;
import com.example.classattendance.model.User;
import com.example.classattendance.recycler.ClassAdapter;
import com.example.classattendance.api.NetworkUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirstFragment extends Fragment implements ClassAdapter.OnItemClickListener {

    private FragmentFirstBinding binding;
    private RecyclerView recyclerView;
    private ClassAdapter adapter;
    private User user;
    private List<SimpleClass> data = new ArrayList<>();
    private BottomSheetDialog bottomSheetDialog;
    private Context context;
    private TextView joinClass, createClass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = container.getContext();
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet);

        recyclerView = view.findViewById(R.id.recycler_class);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Sample data
        data = new ArrayList<>();
        data.add(new SimpleClass("Class 1", "Subject 1"));
        data.add(new SimpleClass("Class 2", "Subject 2"));
        data.add(new SimpleClass("Class 3", "Subject 3"));
        data.add(new SimpleClass("Class 4", "Subject 4"));
        data.add(new SimpleClass("Class 5", "Subject 5"));
        data.add(new SimpleClass("Class 6", "Subject 6"));
        data.add(new SimpleClass("Class 7", "Subject 7"));
        data.add(new SimpleClass("Class 8", "Subject 8"));

        adapter = new ClassAdapter(data, this);
        recyclerView.setAdapter(adapter);

        joinClass = bottomSheetDialog.findViewById(R.id.join_class);
        createClass = bottomSheetDialog.findViewById(R.id.create_class);

        joinClass.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            NavHostFragment.findNavController(FirstFragment.this)
                    .navigate(R.id.action_FirstFragment_to_joinClassFragment2);
        });

        createClass.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            NavHostFragment.findNavController(FirstFragment.this)
                    .navigate(R.id.action_FirstFragment_to_createClassFragment);
        });

        binding.fab.setOnClickListener(v -> {
            bottomSheetDialog.show();
        });

        loadData();
    }

    @Override
    public void onItemClick(SimpleClass classroom) {
        Intent intent = new Intent(getContext(), ClassActivity.class);
        intent.putExtra("class_name", classroom.getName());
        intent.putExtra("class_subject", classroom.getSubject());
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void loadData() {
        // login or register
        getUserAfterLogin("hMnuzMB8SdMPHxaYMD8ooQHcrXB2");

    }

    private void getUserAfterLogin(String uid) {
        UserAPI userAPI = NetworkUtil.self().getRetrofit().create(UserAPI.class);
        Call<User> call = userAPI.login(uid);
        call.enqueue(new Callback<User>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                    Log.d(TAG, "onResponse: " + user.getName());

                    if (user != null) {
                        data.clear();
                        data.addAll(user.getCreatedClasses());
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    Log.e(TAG, "onResponse: " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}