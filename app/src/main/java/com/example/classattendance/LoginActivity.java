package com.example.classattendance.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.classattendance.R;
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
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

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
            // Tạo ý định (Intent) mới để bắt đầu MainActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            // Bắt đầu activity mới và kết thúc LoginActivity hiện tại
            // để người dùng không trở lại được màn hình đăng nhập khi nhấn "Back"
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
                // người dùng đã đăng nhập, bạn có thể lấy thông tin từ đối tượng user.
                String name = user.getDisplayName();
                String email = user.getEmail();

                // Sau đó chuyển đến activity chính của ứng dụng
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                // người dùng chưa đăng nhập, hiển thị form đăng nhập
                findViewById(R.id.btnBatdau).setVisibility(View.VISIBLE);
            }
        }
        // Phương thức đăng xuất












    }

