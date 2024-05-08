package com.example.classattendance.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.classattendance.R;
import com.example.classattendance.model.User;
import com.example.classattendance.model.UserDTO;
import com.example.classattendance.utils.MyAuth;
import com.example.classattendance.viewmodel.UserVM;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Configure Google Sign In
        mGoogleSignInClient = GoogleSignIn.getClient(this,
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build());

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        Button signInButton = findViewById(R.id.btnBatdau);
        signInButton.setOnClickListener(view -> signIn());
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (mAuth.getCurrentUser() != null) {
            // User is signed in
            navigateToMain();
        }
    }

    private void navigateToMain() {
        setModelUserAfterLogin();

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // [END on_start_check_user]

    // [START signin]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w("LoginActivity", "Google sign in failed", e);
                updateUI(null);
            }
        }
    }
    // [END onActivityResult]

    // [START firebaseAuthWithGoogle]
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                // Đăng nhập thành công, hiển thị thông tin người dùng hoặc chuyển hướng đến activity chính
                Log.d("LoginActivity", "signInWithCredential:success");
                FirebaseUser user = mAuth.getCurrentUser();
                updateUI(user);
            } else {
                // Nếu đăng nhập thất bại, hiển thị thông báo cho người dùng.
                Log.w("LoginActivity", "signInWithCredential:failure", task.getException());
                Toast.makeText(LoginActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                updateUI(null);
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            navigateToMain();
        } else {
            findViewById(R.id.btnBatdau).setVisibility(View.VISIBLE);
        }
    }

//    private void setModelUserAfterLogin() {
//        UserVM userVM = new ViewModelProvider(this).get(UserVM.class);
//        User data = userVM.login(MyAuth.getUid()).getValue();
//        if (data != null) {
//            MyAuth.setModelUser(data);
//        } else {
//            UserDTO dto = new UserDTO(mAuth.getCurrentUser().getDisplayName(), mAuth.getUid());
//            userVM.register(dto).observe(this, d -> {
//                if (d != null) {
//                    MyAuth.setModelUser(d);
//                }
//            });
//        }
//    }

    private void setModelUserAfterLogin() {
        if (mAuth.getCurrentUser() != null) {
            UserDTO dto = new UserDTO(mAuth.getCurrentUser().getDisplayName(), mAuth.getUid());

            UserVM userVM = new ViewModelProvider(this).get(UserVM.class);
            userVM.login(dto).observe(LoginActivity.this, registeredUser -> {
                if (registeredUser != null) {
                    MyAuth.setModelUser(registeredUser);
                }
            });
        }
    }
}
