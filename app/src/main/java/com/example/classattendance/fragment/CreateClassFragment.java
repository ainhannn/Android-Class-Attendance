package com.example.classattendance.fragment;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.classattendance.ClassActivity;
import com.example.classattendance.R;
import com.example.classattendance.api.ClassAPI;
import com.example.classattendance.api.NetworkUtil;
import com.example.classattendance.model.Class;
import com.example.classattendance.model.ClassDTO;
import com.example.classattendance.model.SimpleClass;
import com.example.classattendance.model.User;
import com.example.classattendance.utils.MyAuth;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateClassFragment extends Fragment {
    private TextInputEditText className, subject, section, room;

    private MaterialButton createClass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_class, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        className = view.findViewById(R.id.class_name_cre);
        subject = view.findViewById(R.id.subject_cre);
        section = view.findViewById(R.id.section_cre);
        room = view.findViewById(R.id.room_cre);

        createClass = view.findViewById(R.id.create_class_btn);
        createClass.setOnClickListener(v -> {
            ClassDTO dto = new ClassDTO();
            dto.setName(String.valueOf(className.getText()));
            dto.setSubject(String.valueOf(subject.getText()));
            dto.setSection(String.valueOf(section.getText()));
            dto.setRoom(String.valueOf(room.getText()));

            //goi api tao class
            ClassAPI classAPI = NetworkUtil.self().getRetrofit().create(ClassAPI.class);
            Call<Class> call = classAPI.createClass(MyAuth.getUid(), dto);
            call.enqueue(new Callback<Class>() {
                @Override
                public void onResponse(Call<Class> call, Response<Class> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(),"Tạo thành công", Toast.LENGTH_SHORT).show();

                        Class model = response.body();
                        User user = MyAuth.getModelUser();
                        if (user != null) {
                            List<SimpleClass> cClasses = user.getCreatedClasses();
                            cClasses.add(new SimpleClass(model));
                            user.setCreatedClasses(cClasses);
                            MyAuth.setModelUser(user);
                        }

                        // Đóng fragment hiện tại
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager(); // or getSupportFragmentManager() if in AppCompatActivity
                        fragmentManager.beginTransaction().remove(CreateClassFragment.this).commit();

                        // Mở FirstFragment
                        fragmentManager.beginTransaction()
                                .replace(R.id.nav_host_fragment_content_main, new FirstFragment())
                                .addToBackStack(null) // Để có thể quay lại fragment trước đó nếu cần
                                .commit();

                        // Chuyển đến lớp vừa tạo
                        Intent intent = new Intent(getContext(), ClassActivity.class);
                        intent.putExtra("role", "teacher");
                        intent.putExtra("class_id", model.getId());
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<Class> call, Throwable t) {
                    Toast.makeText(getContext(),"Đã có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onFailure: " + t.getMessage());
                }
            });
        });
    }
}