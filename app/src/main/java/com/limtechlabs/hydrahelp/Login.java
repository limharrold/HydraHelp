package com.limtechlabs.hydrahelp;

import android.graphics.Color; // Import for Color
import android.os.Bundle;
import android.text.Spannable; // Import for Spannable
import android.text.SpannableString; // Import for SpannableString
import android.text.style.ForegroundColorSpan; // Import for ForegroundColorSpan
import android.util.Log; // Import for Log
import android.widget.Button;
import android.widget.TextView; // Import for TextView
import android.text.style.UnderlineSpan;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;


        });
        TextView hydraTextView = findViewById(R.id.hydraTextView); // Make sure the ID matches!

        if (hydraTextView != null) { // Always check for null!
            String text = "Hydra Help";
            SpannableString spannableString = new SpannableString(text);

            ForegroundColorSpan colorHyd = new ForegroundColorSpan(Color.WHITE);
            spannableString.setSpan(colorHyd, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            ForegroundColorSpan colorRa = new ForegroundColorSpan(Color.parseColor("#8fd8ff"));
            spannableString.setSpan(colorRa, 3, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            hydraTextView.setText(spannableString);

        }
        TextView myTextView = findViewById(R.id.myTextView); // Assuming you have a TextView with this ID in your layout
        if (myTextView != null) {
            String text = "Forgot Password?";
            SpannableString spannableString = new SpannableString(text);
            spannableString.setSpan(new UnderlineSpan(), 0, text.length(), 0);
            myTextView.setText(spannableString);
        }

        Button signInButton = findViewById(R.id.signInButton);

        signInButton.setOnClickListener(v -> {
            // Handle sign in click (e.g., start a new activity, validate credentials, etc.)
            Log.d("SignInButton", "Sign in button clicked"); // Example
            // Replace this with your actual sign-in logic.
        });

        TextView registerLinkTextView = findViewById(R.id.registerLinkTextView);
        String signUpText = "SIGN UP NOW";
        SpannableString spannableString = new SpannableString(signUpText);
        spannableString.setSpan(new UnderlineSpan(), 0, signUpText.length(), 0);
        registerLinkTextView.setText(spannableString);


        registerLinkTextView.setOnClickListener(v -> {
            Intent intent = new Intent(this, Register.class);
            startActivity(intent);
        });

    }
}