package com.limtechlabs.hydrahelp;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    FirebaseAuth mAuth;

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

        TextView termsAndConditionsTextView = findViewById(R.id.termsAndConditionsTextView);
        setUpTermsAndConditions(termsAndConditionsTextView);

        // Firebase Authentication Logic

        Button signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(v -> {
            Log.d("SignUpButton", "Sign up button clicked");
            EditText signUpEmail = findViewById(R.id.signUpEmail);
            EditText signUpPassword = findViewById(R.id.signUpPassword);
            String email = signUpEmail.getText().toString();
            String password = signUpPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(Register.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Register.this, "Sign up successful.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Register.this, Login.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(Register.this, "Sign up failed.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // LOGIN CLICK LINK CONFIGURATION

        TextView loginLinkTextView = findViewById(R.id.loginLinkTextView);
        loginLinkTextView.setOnClickListener(view -> {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        });
    }

    // Terms and Conditions and Privacy Policy Logic
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