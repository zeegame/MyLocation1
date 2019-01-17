package com.forgan.mylocation;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.TransitionManager;

public class Reg_user_Email extends AppCompatActivity {
    private static final String TAG = "reg/usr/email";
    private Button btUserMail;
    private TextView tvBackToLogin;
    private EditText editUserMail;
    private String email;
    private ImageView backRegUserEmail;
    private ViewGroup reg_user_email;
    private ProgressDialog progressDialog;
    private TextWatcher setEmail = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String test = editUserMail.getText().toString().trim();

            if (!test.isEmpty() && emailValid(test) == true) {
                TransitionManager.beginDelayedTransition(reg_user_email);
                btUserMail.setVisibility(View.VISIBLE);
                btUserMail.setEnabled(true);
            } else {
                TransitionManager.beginDelayedTransition(reg_user_email);
                btUserMail.setVisibility(View.INVISIBLE);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.LoginTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_user__email);

        //Get element on activity
        reg_user_email = (ViewGroup) findViewById(R.id.reg_user_email);
        btUserMail = (Button) findViewById(R.id.btUserMail);
        tvBackToLogin = (TextView) findViewById(R.id.hasSupPhone);
        editUserMail = (EditText) findViewById(R.id.editUserMail);
        backRegUserEmail = (ImageView) findViewById(R.id.backRegUserEmail);

        progressDialog = new ProgressDialog(Reg_user_Email.this);
        progressDialog.setTitle(getString(R.string.checkingData));
        progressDialog.setMessage(getString(R.string.plsWait));
        progressDialog.dismiss();

        //Next Button Click
        btUserMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                //Check user's email if it have been regis
                //Database initialize
                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                //set database reference to 'users'
                DocumentReference docRef = db.collection("users").document(editUserMail.getText().toString());
                Task<DocumentSnapshot> documentSnapshotTask = docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                //if document of user is valid get data
                                Log.w(TAG, "This email have been sign up.");
                                editUserMail.setError(getString(R.string.usedEmail));
                                progressDialog.dismiss();
                            } else {
                                FirebaseHelper.put2("E-mail", editUserMail.getText().toString());
                                Intent toSSo = new Intent(Reg_user_Email.this, Reg_user_sso.class);
                                progressDialog.dismiss();
                                startActivity(toSSo);
                            }
                        }
                    }
                });
            }
        });

        //onclick already hac account button
        tvBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseHelper.clearAllData();
                Intent backToLogin = new Intent(Reg_user_Email.this, Login.class);
                startActivity(backToLogin);
            }
        });

        //onclick back button
        backRegUserEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        editUserMail.addTextChangedListener(setEmail);

    }

    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    public boolean emailValid(String test) {
        Pattern emailPattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");

        if (!emailPattern.matcher(test).find()) {
            return false;
        }
        return true;
    }

}
