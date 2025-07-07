package com.example.speechrecognition;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1000;
    FirebaseAuth auth;
    SignInButton signInButton;
    private FirebaseAuth.AuthStateListener authStateListener;
    private GoogleSignInClient gsc;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        FirebaseApp.initializeApp(this);
        progressBar = findViewById(R.id.progressBar);
        signInButton = findViewById(R.id.google_sign_in_button);

        auth = FirebaseAuth.getInstance();
        getWindow().setStatusBarColor(ContextCompat.getColor(SignIn.this,R.color.ochre));


        // Configure Google Sign-In options
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // ensure this is set in your strings.xml
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso
        gsc = GoogleSignIn.getClient(this, gso);

        // Set the click listener for the Google Sign-In button
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }

    private void signIn() {
        progressBar.setVisibility(View.VISIBLE);
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...)
        if (requestCode == RC_SIGN_IN) {
            progressBar.setVisibility(View.GONE);
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign-In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if(account!=null) {
                    firebaseAuthWithGoogle(account);
                    //storeUserInFirebase(name, email);
                    //Intent intent = new Intent(this, Dashboard.class);
                    //startActivity(intent);
                }
            } catch (ApiException e) {
                // Google Sign-In failed
                Log.w("Google Sign-In", "Google sign in failed", e);
                Toast.makeText(this, "Sign in failed Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                // Sign-in successful, get the current Firebase user
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {
                    // User is authenticated, store the user data in Firebase
                    storeUserInFirebase(user.getDisplayName(), user.getEmail());
                }
            } else {
                Log.w("Firebase Auth", "signInWithCredential:failure", task.getException());
                Toast.makeText(SignIn.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void storeUserInFirebase(String name, String email) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("student");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();

            databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Toast.makeText(SignIn.this, "User already exists", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignIn.this, Dashboard.class);
                        startActivity(intent);
                    } else {
                        User user = new User(name, email);
                        databaseReference.child(userId).setValue(user).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignIn.this, "User details stored successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignIn.this, Dashboard.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(SignIn.this, "Failed to store user details", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(SignIn.this, "Error checking user existence", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No authenticated user found", Toast.LENGTH_SHORT).show();
        }
    }


    public class User {
        public String name;
        public String email;

        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User(String name, String email) {
            this.name = name;
            this.email = email;
        }
    }
}
