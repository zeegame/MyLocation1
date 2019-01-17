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

public class Reg_sup_email extends AppCompatActivity {
    private static final String TAG = "reg/sup/email";
    private TextView hasRegSupEmail;
    private Button btRegSupEmail;
    private EditText editRegSupEmail;
    private String email;
    private ImageView backRegSupEmail;
    private ViewGroup reg_sup_email;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.LoginTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_sup_email);

        //Get element on actiity
        reg_sup_email = (ViewGroup) findViewById(R.id.reg_sup_email);
        hasRegSupEmail = (TextView) findViewById(R.id.hasRegSupEmail);
        btRegSupEmail = (Button) findViewById(R.id.btRegSupEmail);
        editRegSupEmail = (EditText) findViewById(R.id.editRegSupEmail);
        backRegSupEmail = (ImageView) findViewById(R.id.backRegSupEmail);

        //Progress dialog
        progressDialog = new ProgressDialog(Reg_sup_email.this);
        progressDialog.setTitle(getString(R.string.checkingData));
        progressDialog.setMessage(getString(R.string.plsWait));
        progressDialog.dismiss();

        //onclick already has account button
        hasRegSupEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseHelper.clearAllData();
                Intent toLogin = new Intent(Reg_sup_email.this, Login.class);
                startActivity(toLogin);
            }
        });

        //onclick next button
        btRegSupEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String s = editRegSupEmail.getText().toString().trim();
//                if(TextUtils.isEmpty(editRegSupEmail.getText().toString()))
//                    editRegSupEmail.setError(getString(R.string.plsEntEmail));
//                else  if (s.matches(emailPattern)&&(s.length()>0)){
                progressDialog.show();
                //Check user's email if it have been regis
                //initialize database
                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                //set database reference to 'users'
                DocumentReference docRef = db.collection("users").document(editRegSupEmail.getText().toString());
                Task<DocumentSnapshot> documentSnapshotTask = docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                //if document of user is valid get data
                                Log.w(TAG, "This email have been sign up.");
                                editRegSupEmail.setError(getString(R.string.usedEmail));
                                progressDialog.dismiss();
                            } else {
                                FirebaseHelper.put2("E-mail", editRegSupEmail.getText().toString());
                                Intent toRegSupPhone = new Intent(Reg_sup_email.this, Reg_sup_phone.class);
                                progressDialog.dismiss();
                                startActivity(toRegSupPhone);
                            }
                        }
                    }
                });
            }
//                else {
//                    Toast.makeText(getApplicationContext(), "invalid email address", Toast.LENGTH_SHORT).show();
//                }
//            }

        });

        //onclick back button
        backRegSupEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Check email syntax
        editRegSupEmail.addTextChangedListener(setSupEMail);
    }

    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    private TextWatcher setSupEMail = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String email = editRegSupEmail.getText().toString().trim();

            if (!email.isEmpty() && emailValid(email) == true) {
                TransitionManager.beginDelayedTransition(reg_sup_email);
                btRegSupEmail.setVisibility(View.VISIBLE);
                btRegSupEmail.setEnabled(true);
            } else {
                TransitionManager.beginDelayedTransition(reg_sup_email);
                btRegSupEmail.setVisibility(View.INVISIBLE);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public boolean emailValid(String email) {
        Pattern emailPattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");

        if (!emailPattern.matcher(email).find()) {
            return false;
        }
        return true;

    }
}