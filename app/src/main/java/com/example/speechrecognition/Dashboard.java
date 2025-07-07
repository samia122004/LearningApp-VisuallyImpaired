package com.example.speechrecognition;

import static android.Manifest.permission.RECORD_AUDIO;

import androidx.annotation.NonNull;
import android.Manifest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.os.Handler;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import java.util.Locale;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    private TextToSpeech tts;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private DrawerLayout rootLayout;
    private LinearLayout maths;
    private LinearLayout science;
    private LinearLayout english;
    private LinearLayout test;
    private TextView name, userNameTextView, userEmailTextView, email;
    private boolean isTTSInitialized = false;
    private ImageView menuIcon, backButton;
    ProgressDialog progressDialog;
    GoogleSignInClient gsc;
    private LinearLayout history,braille;
    private View mathsCard, scienceCard, englishCard;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        getWindow().setStatusBarColor(ContextCompat.getColor(Dashboard.this,R.color.ochre));
        rootLayout = findViewById(R.id.drawer_layout);
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "User");

        // Display the name
        //userName1.setText("Welcome, " + userName + "!");

        name=findViewById(R.id.name);
        View headerView = navigationView.getHeaderView(0); //For name ,email and backButton
        userNameTextView = headerView.findViewById(R.id.name1);
        userEmailTextView = headerView.findViewById(R.id.email);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account!=null){
            String personName=account.getDisplayName();
            String personemail=account.getEmail();
            name.setText("Welcome, " + personName);
            userNameTextView.setText(personName);
            userEmailTextView.setText(personemail);
        }

        maths = findViewById(R.id.l1);
        science = findViewById(R.id.l2);
        english = findViewById(R.id.l3);
        test = findViewById(R.id.l4);
        braille=findViewById(R.id.l6);
        history=findViewById(R.id.l7);

        mathsCard = findViewById(R.id.cv1);   // The ID of your Maths view
        scienceCard = findViewById(R.id.cv2); // Similarly for other subjects
        englishCard = findViewById(R.id.cv3);

        drawerLayout = findViewById(R.id.drawer_layout);
        //NavigationView navigationView = findViewById(R.id.navigation_view);
        menuIcon = findViewById(R.id.menu_icon);
        backButton = headerView.findViewById(R.id.back_button);
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = tts.setLanguage(Locale.US);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Language is not supported or missing data.");
                } else {
                    isTTSInitialized = true;
                    Log.d("TTS", "Text-to-Speech is initialized successfully.");
                }
            } else {
                Log.e("TTS", "TTS initialization failed.");
            }
        });


        maths.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // Announce "Maths" when finger enters the LinearLayout
                speakOut("Maths");
            }
            return false; // Return true to consume the hover event
        });
        // Set up OnClickListener
        maths.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, Maths.class);
            startActivity(intent);
        });


        history.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, HistoryActivity.class);
            startActivity(intent);
        });

        history.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // Announce "Maths" when finger enters the LinearLayout
                speakOut("Result History");
            }
            return false; // Return true to consume the hover event
        });

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    // Handle navigating back or other functionality
                    drawerLayout.openDrawer(GravityCompat.START); // or any other logic you want
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Close the drawer when back button is clicked
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        });
        science.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // Play TTS for "Science"
                speakOut("Science");
            }
            return false;
        });
        science.setOnClickListener(v -> {
            //openSubjectScreen("Science");
            Intent intent = new Intent(Dashboard.this, Science.class);
            startActivity(intent);
        });
        english.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // Play TTS for "Science"
                speakOut("English");
            }
            return false;
        });
        english.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, English.class);
            startActivity(intent);
        });
        test.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                speakOut("Test");
            }
            return false;
        });
        test.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, QuizActivity.class);
            startActivity(intent);
        });

        braille.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                speakOut("Braille");
            }
            return false;
        });
        braille.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, BrailleLessonsActivity.class);
            startActivity(intent);
        });


        //askWhichSubjectToStudy();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
        }
    }


    private void setUpClickListeners() {
        mathsCard.setOnClickListener(v -> {
            // Open Maths activity
            Intent intent = new Intent(Dashboard.this, Maths.class);
            startActivity(intent);
        });

        scienceCard.setOnClickListener(v -> {
            // Open Science activity
            Intent intent = new Intent(Dashboard.this, Science.class);
            startActivity(intent);
        });

        englishCard.setOnClickListener(v -> {
            // Open English activity
            Intent intent = new Intent(Dashboard.this, English.class);
            startActivity(intent);
        });
    }


    private void speakOut(String text) {
        if (tts != null && !tts.isSpeaking()) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }
    private void openSubjectScreen(String subjectName) {
        if ("Maths".equals(subjectName)) {
            Intent intent = new Intent(Dashboard.this, Maths.class);
            startActivity(intent);
        } else if ("Science".equals(subjectName)) {
            Intent intent = new Intent(Dashboard.this, Science.class);
            startActivity(intent);
        } else if ("English".equals(subjectName)) {
            Intent intent = new Intent(Dashboard.this, English.class);
            startActivity(intent);
        } else if ("Test".equals(subjectName)) {
            Intent intent = new Intent(Dashboard.this, QuizActivity.class);
            startActivity(intent);
        } else if ("Braille".equals(subjectName)) {
            Intent intent = new Intent(Dashboard.this, BrailleLessonsActivity.class);
            startActivity(intent);
        } else if ("Result History".equals(subjectName)) {
            Intent intent = new Intent(Dashboard.this, HistoryActivity.class);
            startActivity(intent);
        }
    }

   @Override
   protected void onDestroy() {
       if (tts != null) {
           tts.stop();
           tts.shutdown();
       }
       super.onDestroy();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId=item.getItemId();
        if (itemId==R.id.nav_signout){
            Toast.makeText(this,"Signed out successfully", Toast.LENGTH_SHORT).show();
            drawerLayout.closeDrawer(GravityCompat.START);
            signOut();
            vibrate();
        }else if(itemId==R.id.nav_home){
            startActivity(new Intent(this, Dashboard.class));
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }else if(itemId==R.id.nav_help){
            startActivity(new Intent(this, Help.class));
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }else if(itemId==R.id.nav_aboutus){
            startActivity(new Intent(this, AboutUs.class));
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        return true;
    }

    void signOut(){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>(){
            public void onComplete(Task<Void> task){
                if (task.isSuccessful()) {
                    // Wait for 2 seconds before navigating to Sign In
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Navigate to the Sign In page
                            finish();

                            startActivity(new Intent(Dashboard.this, SignIn.class));
                        }
                    }, 2000); // 2000 milliseconds delay
                }else {
                    // Handle sign-out failure
                    Toast.makeText(Dashboard.this, "Sign-out failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        //Log.d("VibrationTest", "Attempting to vibrate for " + duration + " ms");
        if (vibrator != null && vibrator.hasVibrator()) {
            long[] pattern = {0, 100, 250, 0}; // Wait 0 ms, vibrate for 500 ms, wait 250 ms, vibrate for 500 ms
            vibrator.vibrate(pattern, -1); // -1 to not repeat
            //vibrator.vibrate(duration);
            Log.d("VibrationTest", "Vibration triggered.");
        } else {
            Toast.makeText(this, "Vibration not supported on this device", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onBackPressed() {
        // Close the current activity and app if no other activity in the stack
        super.onBackPressed();
        finishAffinity(); // This will finish the current activity and all parent activities
        System.exit(0); // Optionally, you can call this to ensure the app closes completely
    }
    }
