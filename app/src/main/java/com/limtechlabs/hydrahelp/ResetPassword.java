package com.limtechlabs.hydrahelp;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        Button resetPasswordButton = findViewById(R.id.resetPasswordButton);

        resetPasswordButton.setOnClickListener(v -> {
            Log.d("ResetPasswordButton", "Reset Password Button clicked");
            EditText emailEditText = findViewById(R.id.emailResetPassText); // Get email EditText
            String email = emailEditText.getText().toString();

            if (email.isEmpty()) {
                Toast.makeText(ResetPassword.this, "Please enter your email address.", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPassword.this, "Password reset email sent. Check your inbox.", Toast.LENGTH_SHORT).show();
                            finish(); // Close the ResetPassword activity
                        } else {
                            Toast.makeText(ResetPassword.this, "Error sending reset email: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("ResetPassword", "Error: " + task.getException().getMessage());
                        }
                    });
        });
    }
}