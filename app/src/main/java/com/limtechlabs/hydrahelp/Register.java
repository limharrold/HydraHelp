package com.limtechlabs.hydrahelp;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import android.graphics.Typeface;
import android.widget.Button;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.graphics.Color;
import android.text.method.LinkMovementMethod;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;



import android.util.Log;

public class Register extends AppCompatActivity {

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

        Button signInButton = findViewById(R.id.signUpButton);

        signInButton.setOnClickListener(v -> {
            // Handle sign in click (e.g., start a new activity, validate credentials, etc.)
            Log.d("SignUpButton", "Sign up button clicked"); // Example
            // Replace this with your actual sign-in logic.
        });

        TextView termsAndConditionsTextView = findViewById(R.id.termsAndConditionsTextView);

        String fullText = "By clicking Sign Up, you agree to our Terms and Conditions & Privacy Policy";
        SpannableString spannableString = new SpannableString(fullText);

        // Style and make "Terms and Conditions" clickable
        int termsStart = fullText.indexOf("Terms and Conditions");
        int termsEnd = termsStart + "Terms and Conditions".length();

        spannableString.setSpan(new StyleSpan(Typeface.BOLD), termsStart, termsEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), termsStart, termsEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new UnderlineSpan(), termsStart, termsEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new CustomClickableSpan() { // Use CustomClickableSpan
            @Override
            public void onClick(@NonNull View widget) {
                widget.clearFocus();
                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                builder.setTitle("Terms and Conditions");
                builder.setMessage("Pogi si Harrold"); // Replace with your content
                builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }, termsStart, termsEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        // Style and make "Privacy Policy" clickable
        int privacyStart = fullText.indexOf("Privacy Policy");
        int privacyEnd = privacyStart + "Privacy Policy".length();

        spannableString.setSpan(new StyleSpan(Typeface.BOLD), privacyStart, privacyEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), privacyStart, privacyEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new UnderlineSpan(), privacyStart, privacyEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new CustomClickableSpan() { // Use CustomClickableSpan
            @Override
            public void onClick(@NonNull View widget) {
                widget.clearFocus();
                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                builder.setTitle("Privacy Policy");
                builder.setMessage("Sobrang Pogi ni Harrold"); // Replace with your content
                builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        }, privacyStart, privacyEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        termsAndConditionsTextView.setText(spannableString);
        termsAndConditionsTextView.setMovementMethod(LinkMovementMethod.getInstance());

        TextView loginLinkTextView = findViewById(R.id.loginLinkTextView);


        loginLinkTextView.setOnClickListener(v -> {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        });

    }
}