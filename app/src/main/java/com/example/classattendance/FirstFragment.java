package com.example.classattendance;

import static android.content.ContentValues.TAG;

import android.content.Context;
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

public class FirstFragment extends Fragment {

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

        adapter = new ClassAdapter(data);
        recyclerView.setAdapter(adapter);

        joinClass = bottomSheetDialog.findViewById(R.id.join_class);
        createClass = bottomSheetDialog.findViewById(R.id.create_class);

        joinClass.setOnClickListener(v->{
            bottomSheetDialog.dismiss();
            NavHostFragment.findNavController(FirstFragment.this)
                    .navigate(R.id.action_FirstFragment_to_joinClassFragment2);
        });

        createClass.setOnClickListener(v->{
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
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                    Log.d(TAG, "onResponse: " + user.getName());

                    if (user != null) {
                        data = new ArrayList<>();

                        for (SimpleClass c : user.getCreatedClasses()) {
                            data.add(c);
                        }

                        adapter = new ClassAdapter(data);
                        recyclerView.setAdapter(adapter);
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