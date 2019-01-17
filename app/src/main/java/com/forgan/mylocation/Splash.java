package com.forgan.mylocation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class Splash extends AppCompatActivity {
    private static final String TAG = "Splash";
    private TextView tv;
    private ImageView iv;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_splash);

        tv = (TextView) findViewById(R.id.tv);
        iv = (ImageView) findViewById(R.id.ivsplash);
        Animation transit = AnimationUtils.loadAnimation(this,R.anim.transition);
        tv.startAnimation(transit);
        iv.startAnimation(transit);
        final Intent i = new Intent(this,UserMain.class);
        Thread timer = new Thread(){
            public void run(){
                try {
                    sleep(2000);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    //check if user is already logged in
                    //or not, go to login activity
                    if(currentUser!=null) {
                        checkAndLogin(currentUser);
                    } else {
                        Intent toLogin = new Intent(Splash.this, Login.class);
                        startActivity(toLogin);
                        finish();
                    }
                }
            }
        };
        timer.start();
    }

    //This method will check account type (user or supplier)
    //If account is valid and have account then go to main page of each account
    private void checkAndLogin(@Nullable FirebaseUser currentUser) {
        //Initialize firestore database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //check account type from db
        if(currentUser!= null) {
            DocumentReference docRef = db.collection("users").document(currentUser.getEmail());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            //if document of current user is valid get data
                            Log.i(TAG, "DocumentSnapshot data: " + document.getData());
                            if(document.getString("type").equalsIgnoreCase("user")) {
                                Toast.makeText(Splash.this, getString(R.string.signInComplete),
                                        Toast.LENGTH_SHORT).show();
                                Log.i(TAG, "Detected: user");
                                startActivity(new Intent(Splash.this, UserMain.class));
                            }
                            else if (document.getString("type").equalsIgnoreCase("sup")) {
                                Toast.makeText(Splash.this, getString(R.string.signInComplete),
                                        Toast.LENGTH_SHORT).show();
                                Log.i(TAG, "Detected: supplier");
                                startActivity(new Intent(Splash.this, SupMain.class));
                            }
                            else {
                                Log.w(TAG, "No account type in database");
                                FirebaseAuth.getInstance().signOut();
                                Intent toLogin = new Intent(Splash.this, Login.class);
                                startActivity(toLogin);
                            }
                        } else {
                            Log.w(TAG, "No user's document in database");
                            FirebaseAuth.getInstance().signOut();
                            Intent toLogin = new Intent(Splash.this, Login.class);
                            startActivity(toLogin);
                        }
                    } else {
                        Log.w(TAG, "get failed with ", task.getException());
                        FirebaseAuth.getInstance().signOut();
                        Intent toLogin = new Intent(Splash.this, Login.class);
                        startActivity(toLogin);
                    }
                }
            });
        }
    }
}

