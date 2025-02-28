package com.limtechlabs.hydrahelp;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    private EditText signUpEmail, signUpPassword, confirmPasswordText, fullNameText, usernameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        signUpEmail = findViewById(R.id.signUpEmail);
        signUpPassword = findViewById(R.id.signUpPassword);
        confirmPasswordText = findViewById(R.id.confirmPasswordText);
        fullNameText = findViewById(R.id.fullNameText);
        usernameText = findViewById(R.id.usernameText);

        setupTextWatchers();

        Button signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(v -> handleSignUp());

        TextView loginLinkTextView = findViewById(R.id.loginLinkTextView);
        loginLinkTextView.setOnClickListener(view -> {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        });

        TextView termsAndConditionsTextView = findViewById(R.id.termsAndConditionsTextView);
        setUpTermsAndConditions(termsAndConditionsTextView);

        // PASSWORD VISIBILITY TOGGLE

        signUpPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (signUpPassword.getRight() - ((TextView) signUpPassword).getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        togglePasswordVisibility(signUpPassword);
                        return true;
                    }
                }
                return false;
            }
        });

        confirmPasswordText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (confirmPasswordText.getRight() - ((TextView) confirmPasswordText).getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        togglePasswordVisibility(confirmPasswordText);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    // TEXT WATCHERS TO VALIDATE FIELDS

    private void setupTextWatchers() {
        signUpEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) { validateEmail(); }
        });

        signUpPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) { validatePassword(); }
        });

        confirmPasswordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) { validateConfirmPassword(); }
        });

        fullNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) { validateFullName(); }
        });

        usernameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) { validateUsername(); }
        });
    }

    // HANDLE SIGN UP BUTTON CLICK
    private void handleSignUp() {
        String email = signUpEmail.getText().toString();
        String password = signUpPassword.getText().toString();
        String confirmPassword = confirmPasswordText.getText().toString();
        String fullName = fullNameText.getText().toString();
        String username = usernameText.getText().toString();

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || fullName.isEmpty() || username.isEmpty()) {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        String userId = user.getUid();

                        Map<String, Object> userData = new HashMap<>();
                        userData.put("fullName", fullName);
                        userData.put("username", username);
                        userData.put("email", email);

                        db.collection("users").document(userId)
                                .set(userData)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Sign up successful.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(this, Login.class);
                                    startActivity(intent);
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Error storing user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.e("RegisterActivity", "Error storing user data", e);
                                    user.delete().addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Log.d("RegisterActivity", "User deleted from Authentication due to data storage failure");
                                        } else {
                                            Log.e("RegisterActivity", "Failed to delete user from Authentication", task1.getException());
                                        }
                                    });
                                });
                    } else {
                        Toast.makeText(this, "Sign up failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void validateEmail() {
        String email = signUpEmail.getText().toString().trim();
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            setErrorState(signUpEmail);
        } else {
            resetErrorState(signUpEmail);
        }
    }

    private void validatePassword() {
        String password = signUpPassword.getText().toString();
        if (password.length() < 6) {
            setErrorState(signUpPassword);
        } else {
            resetErrorState(signUpPassword);
        }
    }

    private void validateConfirmPassword() {
        String password = signUpPassword.getText().toString();
        String confirmPassword = confirmPasswordText.getText().toString();
        if (!password.equals(confirmPassword)) {
            setErrorState(confirmPasswordText);
        } else {
            resetErrorState(confirmPasswordText);
        }
    }

    private void validateFullName() {
        String fullName = fullNameText.getText().toString().trim();
        if (fullName.isEmpty()) {
            setErrorState(fullNameText);
        } else {
            resetErrorState(fullNameText);
        }
    }

    private void validateUsername() {
        String username = usernameText.getText().toString().trim();
        if (username.isEmpty() || username.length() < 3) {
            setErrorState(usernameText);
        } else {
            resetErrorState(usernameText);
        }
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

    private void setErrorState(EditText editText) {
        editText.setBackgroundResource(R.drawable.error_rounded_edit_text);
    }

    private void resetErrorState(EditText editText) {
        editText.setBackgroundResource(R.drawable.rounded_edit_text);
    }

    private void setUpTermsAndConditions(TextView termsAndConditionsTextView) {
        String fullText = "By clicking Sign Up, you agree to our Terms and Conditions & Privacy Policy";
        SpannableString spannableString = new SpannableString(fullText);

        int termsStart = fullText.indexOf("Terms and Conditions");
        int termsEnd = termsStart + "Terms and Conditions".length();

        spannableString.setSpan(new StyleSpan(Typeface.BOLD), termsStart, termsEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), termsStart, termsEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new UnderlineSpan(), termsStart, termsEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new CustomClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                widget.clearFocus();
                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                builder.setTitle("Terms and Conditions");
                builder.setMessage("Pogi si Harrold"); // Terms and Conditions content
                builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }, termsStart, termsEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        int privacyStart = fullText.indexOf("Privacy Policy");
        int privacyEnd = privacyStart + "Privacy Policy".length();

        spannableString.setSpan(new StyleSpan(Typeface.BOLD), privacyStart, privacyEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), privacyStart, privacyEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new UnderlineSpan(), privacyStart, privacyEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new CustomClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                widget.clearFocus();
                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                builder.setTitle("Privacy Policy");
                builder.setMessage("Sobrang Pogi ni Harrold"); // Privacy Policy content
                builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }, privacyStart, privacyEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        termsAndConditionsTextView.setText(spannableString);
        termsAndConditionsTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}