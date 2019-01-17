package com.forgan.mylocation;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class Login extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Login";
    private Button btRegis, btnLogin;
    private TextView btForget;
    private EditText editUser, editPass;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    boolean doubleBackToExitPressedOnce = false;
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton googleSign;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.LoginTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //[START] Firebase Auth initialize
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //[END] Firebase Auth initialize

        //Get element on activity
        btRegis = (Button) findViewById(R.id.btRegis);
        btnLogin = (Button) findViewById(R.id.btLogin);
        btForget = (TextView) findViewById(R.id.btForget);
        editUser = (EditText) findViewById(R.id.editUser);
        editPass = (EditText) findViewById(R.id.editPass);

        googleSign = findViewById(R.id.googleSign);

        googleSign.setSize(SignInButton.SIZE_WIDE);
        googleSign.setOnClickListener(this);

        //[START] google Signin
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        // [END initialize_auth]
        //[END] google Signin

        //Progress Dialog
        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setTitle(getString(R.string.checkingData));
        progressDialog.setMessage(getString(R.string.plsWait));
        progressDialog.dismiss();

        //Check internet
        ConnectionDetector cd = new ConnectionDetector(this);
        if (cd.isConnected()){
        } else {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(Login.this);
            builder.setCancelable(false);
            builder.setMessage("โปรดเชื่อมต่ออินเตอร์เน็ต");
            builder.setPositiveButton("ปิด", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    moveTaskToBack(true);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                }
            });
            builder.show();
        }

        //On click login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get text from editText
                String txtUser = editUser.getText().toString();
                String txtPass = editPass.getText().toString();
                //Check empty editText
                if (TextUtils.isEmpty(txtUser) && TextUtils.isEmpty(txtPass)) {
                    Toast.makeText(Login.this, getString(R.string.invalidData),
                            Toast.LENGTH_SHORT).show();
                } else {
                    //Start Login
                    progressDialog.show();
                    checkAndLogin(txtUser, txtPass);
                }
            }
        });

        //On Click regis button
        btRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toLogin = new Intent(Login.this, Reg_Start.class);
                startActivity(toLogin);
            }
        });

        //On click forget password button
        btForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toLogin1 = new Intent(Login.this, Forgetpass.class);
                startActivity(toLogin1);
            }
        });
    }

    //Hide keyboard when touch background
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    //Check user's type
    private void checkAndLogin(FirebaseUser currentUser) {
        //database initialize
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (currentUser != null) {
            DocumentReference docRef = db.collection("users").document(currentUser.getEmail());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            //if document of current user is valid get data
                            if (document.getString("type").equalsIgnoreCase("user")) {
                                Log.i(TAG, "detected: user");
                                Toast.makeText(Login.this, getString(R.string.signInComplete),
                                        Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Login.this, UserMain.class));
                            } else if (document.getString("type").equalsIgnoreCase("sup")) {
                                Log.i(TAG, "detected: supplier");
                                Toast.makeText(Login.this, getString(R.string.signInComplete),
                                        Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Login.this, SupMain.class));
                            } else {
                                Log.w(TAG, "No account type in database");
                                FirebaseAuth.getInstance().signOut();
                            }
                        } else {
                            Log.w(TAG, "No user's document in database");
                            FirebaseAuth.getInstance().signOut();
                        }
                    } else {
                        Log.w(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
    }

    //Login using email
    private void checkAndLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i(TAG, "signInWithEmail:success");
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            checkAndLogin(currentUser);

                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, getString(R.string.signInFailed),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
    //back to exit
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            moveTaskToBack(true);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.press2exit, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            handleSignInResult(task);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(Login.this);
                builder.setCancelable(false);
                builder.setMessage(R.string.googleLoginErr);
                builder.setPositiveButton("ปิด", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        moveTaskToBack(true);
                    }
                });
                builder.show();
                // [START_EXCLUDE]
                // [END_EXCLUDE]
            }
        }
//        else {
//            // Pass the activity result back to the Facebook SDK
//            mCallbackManager.onActivityResult(requestCode, resultCode, data);
//        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.googleSign:
                signIn();
                break;
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.i(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i(TAG, "signInWithCredential:success");
                            final FirebaseUser user = mAuth.getCurrentUser();
                            final FirebaseFirestore db = FirebaseFirestore.getInstance();
                            DocumentReference docRef = db.collection("users").document(user.getEmail());
                            com.google.android.gms.tasks.Task<DocumentSnapshot> documentSnapshotTask = docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (!document.exists()) {
                                            Map<String, Object> userDB = new HashMap<>();
                                            userDB.put("Fname", user.getDisplayName().substring(0, user.getDisplayName().indexOf(" ")));
                                            userDB.put("Lname", user.getDisplayName().substring(user.getDisplayName().indexOf(" ")+1, user.getDisplayName().length()));
                                            userDB.put("type", "user");
                                            userDB.put("SSO", null);
                                            userDB.put("address", null);
                                            userDB.put("address_ext", null);
                                            userDB.put("phone", null);
                                            //TODO change user's profile image size to 500x500
//                                            GoogleSignInAccount abc = GoogleSignIn.getLastSignedInAccount();
                                            db.collection("users").document(user.getEmail())
                                                    .set(userDB)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.i(TAG, "DocumentSnapshot successfully written!");
                                                            startActivity(new Intent(Login.this, Splash.class));
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.w(TAG, "Error adding document", e);
                                                        }
                                                    });
                                        }

                                    }
                                }
                            });
                            startActivity(new Intent(Login.this, Splash.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }

                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}

