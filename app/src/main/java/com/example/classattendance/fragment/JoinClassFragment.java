package com.example.classattendance.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.classattendance.activity.ClassActivity;
import com.example.classattendance.R;
import com.example.classattendance.model.Class;
import com.example.classattendance.model.SimpleClass;
import com.example.classattendance.model.User;
import com.example.classattendance.utils.MyAuth;
import com.example.classattendance.viewmodel.ClassVM;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class JoinClassFragment extends Fragment {
    private TextInputEditText classCode;
    private MaterialButton joinClass;
    private ClassVM classVM;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_join_class, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        classVM = new ViewModelProvider(this).get(ClassVM.class);

        classCode = view.findViewById(R.id.class_code);

        joinClass = view.findViewById(R.id.join_class_button);
        joinClass.setOnClickListener(v->{
            // goi api join class
            classVM.joinClass(String.valueOf(classCode.getText())).observe(getActivity(), rs -> {
                if (rs != null) {
                    Toast.makeText(getContext(),"Tham gia thành công", Toast.LENGTH_SHORT).show();

                    Class model = rs;
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
                            .commit();

//                    NavHostFragment.findNavController(JoinClassFragment.this)
//                            .navigate(R.id.action_joinClassFragment_to_FirstFragment);

                    // Chuyển đến lớp vừa tạo
                    Intent intent = new Intent(getContext(), ClassActivity.class);
                    intent.putExtra("class_id", model.getId());
                    intent.putExtra("role", "student");
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(),"Đã có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}