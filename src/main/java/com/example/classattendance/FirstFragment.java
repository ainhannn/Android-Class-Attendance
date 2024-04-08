package com.example.classattendance;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.classattendance.databinding.FragmentFirstBinding;
import com.example.classattendance.model.Classroom;
import com.example.classattendance.recycler.ClassAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    private RecyclerView recyclerView;
    private ClassAdapter adapter;
    private List<Classroom> data;
    private BottomSheetDialog bottomSheetDialog;

    private Context context;

    private TextView joinClass, createClass;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
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
        data.add(new Classroom("Class 1", "Subject 1"));
        data.add(new Classroom("Class 2", "Subject 2"));
        data.add(new Classroom("Class 3", "Subject 3"));
        data.add(new Classroom("Class 4", "Subject 4"));
        data.add(new Classroom("Class 5", "Subject 5"));
        data.add(new Classroom("Class 6", "Subject 6"));
        data.add(new Classroom("Class 7", "Subject 7"));
        data.add(new Classroom("Class 8", "Subject 8"));


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

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}