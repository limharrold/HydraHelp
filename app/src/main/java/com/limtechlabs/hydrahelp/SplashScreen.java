    package com.limtechlabs.hydrahelp;

    import android.content.pm.ActivityInfo;
    import android.os.Bundle;
    import android.os.Handler;

    import android.os.Looper;

    import androidx.activity.EdgeToEdge;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.core.graphics.Insets;
    import androidx.core.view.ViewCompat;
    import androidx.core.view.WindowInsetsCompat;
    import android.content.Intent;
    import android.util.Log;

    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;

    public class SplashScreen extends AppCompatActivity {

        private FirebaseAuth mAuth;

        @Override
        protected void onCreate(Bundle savedInstanceState) {

            mAuth = FirebaseAuth.getInstance();

            super.onCreate(savedInstanceState);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_splash_screen);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            mAuth = FirebaseAuth.getInstance();
        }

        //CHECK IF USER IS LOGGED IN
        @Override
        protected void onStart() {
            super.onStart();
            Log.d("SplashScreen", "onStart called and checking the user if logged in");

            new Handler(Looper.getMainLooper()).postDelayed(() -> { // Main looper to delay for 3 seconds
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashScreen.this, Login.class);
                    startActivity(intent);
                    finish();
            }
        },3000); // 3-second delay
    }
}