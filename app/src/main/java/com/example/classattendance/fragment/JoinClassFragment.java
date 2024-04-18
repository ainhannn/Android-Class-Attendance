package com.example.classattendance.fragment;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.classattendance.ClassActivity;
import com.example.classattendance.R;
import com.example.classattendance.api.ClassAPI;
import com.example.classattendance.api.NetworkUtil;
import com.example.classattendance.model.Class;
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

public class JoinClassFragment extends Fragment {
    private TextInputEditText classCode;
    private MaterialButton joinClass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_join_class, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        classCode = view.findViewById(R.id.class_code);

        joinClass = view.findViewById(R.id.join_class_button);
        joinClass.setOnClickListener(v->{
            // goi api join class
            ClassAPI classAPI = NetworkUtil.self().getRetrofit().create(ClassAPI.class);
            Call<Class> call = classAPI.joinClass(MyAuth.getUid(), String.valueOf(classCode.getText()));
            call.enqueue(new Callback<Class>() {
                @Override
                public void onResponse(Call<Class> call, Response<Class> response) {
                    Toast.makeText(getContext(),"Tham gia thành công", Toast.LENGTH_SHORT).show();

                    Class model = response.body();
                    User user = MyAuth.getModelUser();
                    if (user != null) {
                        List<SimpleClass> jClasses = user.getJoinedClasses();
                        jClasses.add(new SimpleClass(model));
                        user.setJoinedClasses(jClasses);
                        MyAuth.setModelUser(user);
                    }

                    // Đóng fragment hiện tại
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager(); // or getSupportFragmentManager() if in AppCompatActivity
                    fragmentManager.beginTransaction().remove(JoinClassFragment.this).commit();

                    // Mở FirstFragment
                    fragmentManager.beginTransaction()
                            .replace(R.id.nav_host_fragment_content_main, new FirstFragment())
                            .addToBackStack(null) // Để có thể quay lại fragment trước đó nếu cần
                            .commit();

                    // Chuyển đến lớp vừa tạo
                    Intent intent = new Intent(getContext(), ClassActivity.class);
                    intent.putExtra("class_id", model.getId());
                    intent.putExtra("role", "student");
                    startActivity(intent);
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