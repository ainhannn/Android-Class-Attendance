package com.example.classattendance.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.classattendance.activity.ClassActivity;
import com.example.classattendance.R;
import com.example.classattendance.api.ClassAPI;
import com.example.classattendance.api.NetworkUtil;
import com.example.classattendance.api.UserAPI;
import com.example.classattendance.databinding.FragmentFirstBinding;
import com.example.classattendance.model.SimpleClass;
import com.example.classattendance.model.User;
import com.example.classattendance.recycler.ClassAdapter;
import com.example.classattendance.utils.MyAuth;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class FirstFragment extends Fragment implements ClassAdapter.OnItemClickListener, ClassAdapter.OnLongClickListener {

    private FragmentFirstBinding binding;
    private RecyclerView recyclerView;
    private ClassAdapter adapter;
    private BottomSheetDialog bottomSheetDialog;
    private BottomSheetDialog bottomSheetClassItemDialog;
    private Context context;
    private TextView joinClass, createClass;
    private TextView btnActionSheetDialog;

    private List<SimpleClass> data = new ArrayList<>();
    private String FILTER;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = container.getContext();
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FILTER = getActivity().getIntent().getStringExtra("filter");

        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet);

        bottomSheetClassItemDialog = new BottomSheetDialog(context);
        bottomSheetClassItemDialog.setContentView(R.layout.bottom_sheet_class_item);

        recyclerView = view.findViewById(R.id.recycler_class);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ClassAdapter(data, this, this);
        recyclerView.setAdapter(adapter);

        joinClass = bottomSheetDialog.findViewById(R.id.join_class);
        createClass = bottomSheetDialog.findViewById(R.id.create_class);
        btnActionSheetDialog = bottomSheetClassItemDialog.findViewById(R.id.action_btn_sheet_dialog);

        joinClass.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
//            NavHostFragment.findNavController(FirstFragment.this)
//                    .navigate(R.id.action_FirstFragment_to_joinClassFragment);

            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment_content_main, new JoinClassFragment())
                    .addToBackStack(null)
                    .commit();
        });

        createClass.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
//            NavHostFragment.findNavController(FirstFragment.this)
//                    .navigate(R.id.action_FirstFragment_to_createClassFragment);

            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment_content_main, new CreateClassFragment())
                    .addToBackStack(null)
                    .commit();
        });

        binding.fab.setOnClickListener(v -> {
            bottomSheetDialog.show();
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        UserAPI userAPI = NetworkUtil.self().getRetrofit().create(UserAPI.class);
        Call<User> call = userAPI.login(MyAuth.getUid());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    MyAuth.setModelUser(response.body());
                    data.clear();

                    if (FILTER != null && FILTER.equalsIgnoreCase("created_only")) {
                        data.addAll(MyAuth.getModelUser().getCreatedClasses());
                    } else if (FILTER != null && FILTER.equalsIgnoreCase("joined_only")) {
                        data.addAll(MyAuth.getModelUser().getJoinedClasses());
                    } else {
                        data.addAll(MyAuth.getModelUser().getCreatedClasses());
                        data.addAll(MyAuth.getModelUser().getJoinedClasses());
                    }

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(SimpleClass classroom) {
        Intent intent = new Intent(getContext(), ClassActivity.class);
        intent.putExtra("class_id", classroom.getId());
        try {
            if (classroom.getTeacherId() == MyAuth.getModelUser().getId())
                intent.putExtra("role", "teacher");
            else
                intent.putExtra("role", "student");
        } catch (Exception e) {
            intent.putExtra("role", "teacher");
        }
        startActivity(intent);
    }

    @Override
    public void onLongClick(SimpleClass classroom) {
        boolean isTeacher = classroom.getTeacherId() == MyAuth.getModelUser().getId();
        if (!isTeacher)
            btnActionSheetDialog.setText("Out class");
        else
            btnActionSheetDialog.setText("Archive class");

        // Thiet lap event onclick
        btnActionSheetDialog.setOnClickListener(v -> {
            ClassAPI api = NetworkUtil.self().getRetrofit().create(ClassAPI.class);
            if (isTeacher) {
                // luu tru lop
                Call<Void> call = api.archiveClass(classroom.getId());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(getContext(), "Đã lưu trữ lớp", Toast.LENGTH_SHORT).show();
                        data.remove(classroom);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t.getMessage());
                    }
                });
            }
            else {
                // roi lop
                Call<Void> call = api.outClass(classroom.getId(), MyAuth.getModelUser().getId());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(getContext(), "Đã rời lớp", Toast.LENGTH_SHORT).show();
                        data.remove(classroom);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t.getMessage());
                    }
                });
            }

            // Tat dialog
            bottomSheetClassItemDialog.dismiss();
        });

        // Hien thi dialog
        bottomSheetClassItemDialog.show();
    }
}