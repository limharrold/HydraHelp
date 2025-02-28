package com.limtechlabs.hydrahelp;


import android.graphics.Color; // Import for Color
import android.os.Bundle;
import android.text.Spannable; // Import for Spannable
import android.text.SpannableString; // Import for SpannableString
import android.text.style.ForegroundColorSpan; // Import for ForegroundColorSpan
import android.util.Log; // Import for Log
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView; // Import for TextView
import android.text.style.UnderlineSpan;
import android.content.Intent;

import android.widget.CheckBox;
import android.content.SharedPreferences;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.pm.ActivityInfo;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    FirebaseAuth mAuth;
    private static final String PREF_NAME = "MyAppPrefs";
    private static final String KEY_REMEMBER_ME = "remember_me";
    private static final String KEY_EMAIL = "email"; // Key for storing email
    private SharedPreferences sharedPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // HYDRA HELP TEXT COLOR CONFIGURATION YAN

        TextView hydraTextView = findViewById(R.id.hydraTextView);
        if (hydraTextView != null) {
            String text = "Hydra Help";
            SpannableString spannableString = new SpannableString(text);

            ForegroundColorSpan colorHyd = new ForegroundColorSpan(Color.WHITE);
            spannableString.setSpan(colorHyd, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            ForegroundColorSpan colorRa = new ForegroundColorSpan(Color.parseColor("#8fd8ff"));
            spannableString.setSpan(colorRa, 3, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            hydraTextView.setText(spannableString);

        }

        // FORGOT PASSWORD CLICK LINK CONFIGURATION

        TextView forgotPasswordTextView = findViewById(R.id.forgotPasswordButton);
        if (forgotPasswordTextView != null) {
            String text = "Forgot Password?";
            SpannableString spannableString = new SpannableString(text);
            spannableString.setSpan(new UnderlineSpan(), 0, text.length(), 0);
            forgotPasswordTextView.setText(spannableString);

            forgotPasswordTextView.setOnClickListener(v -> {
                Intent intent = new Intent(this, ResetPassword.class);
                startActivity(intent);
            });

        }

        // SIGN IN BUTTON LOGIC/CONFIGS/FIREBASE AUTHENTICATION

        mAuth = FirebaseAuth.getInstance();

        // REMEMBER CHECKBOX  ME LOGIC
        sharedPrefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        Button signInButton = findViewById(R.id.signInButton);
        CheckBox rememberMeCheckBox = findViewById(R.id.rememberMeCheckBox);
        boolean isRemembered = sharedPrefs.getBoolean(KEY_REMEMBER_ME, false);
        rememberMeCheckBox.setChecked(isRemembered);

        if (isRemembered) {
            String storedEmail = sharedPrefs.getString(KEY_EMAIL, ""); // Get stored email
            EditText loginEmail = findViewById(R.id.loginEmail); // Find EditText
            loginEmail.setText(storedEmail);
        }

        signInButton.setOnClickListener(v -> {
            Log.d("SignInButton", "Sign in button clicked");

            EditText loginEmail = findViewById(R.id.loginEmail);
            EditText loginPassword = findViewById(R.id.loginPassword);

            String email = loginEmail.getText().toString();
            String password = loginPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(Login.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Login success
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Login.this, "Login successful.", Toast.LENGTH_SHORT).show();

                            // Save email when remember me is checked
                            boolean rememberMe = rememberMeCheckBox.isChecked();
                            if (rememberMe) {
                                sharedPrefs.edit().putString(KEY_EMAIL, email).apply(); // Store email
                                sharedPrefs.edit().putBoolean(KEY_REMEMBER_ME, rememberMe).apply(); // Store remember me
                            } else {
                                // Clear the email if "Remember Me" is not checked.
                                sharedPrefs.edit().remove(KEY_EMAIL).apply();
                                sharedPrefs.edit().remove(KEY_REMEMBER_ME).apply();
                            }

                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Login.this, "Login failed. Please Check your Email or Password ",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // REGISTER CLICK LINK CONFIGURATION

        TextView registerLinkTextView = findViewById(R.id.registerLinkTextView);
        String signUpText = "SIGN UP NOW";
        SpannableString spannableString = new SpannableString(signUpText);
        spannableString.setSpan(new UnderlineSpan(), 0, signUpText.length(), 0);
        registerLinkTextView.setText(spannableString);


        registerLinkTextView.setOnClickListener(v -> {
            Intent intent = new Intent(this, Register.class);
            startActivity(intent);
        });

        //
        EditText loginPassword = findViewById(R.id.loginPassword);

        loginPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (loginPassword.getRight() - loginPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        togglePasswordVisibility(loginPassword);
                        return true;
                    }
                }
                return false;
            }
        });
    }

        private void togglePasswordVisibility(EditText passwordEditText) {
            if (passwordEditText.getTag().equals("passwordHidden")) {
                passwordEditText.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_on, 0);
                passwordEditText.setTag("passwordVisible");
            } else {
                passwordEditText.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off, 0);
                passwordEditText.setTag("passwordHidden");
            }
            passwordEditText.setSelection(passwordEditText.getText().length());
        }
    }