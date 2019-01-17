package com.forgan.mylocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.TransitionManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class Forgetpass extends AppCompatActivity {
    private TextView backToLoginPage;
    private ImageView backToPrevious;
    private EditText inputemail;
    private ViewGroup fgpass;
    private Button bt;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpass);

        fgpass = (ViewGroup) findViewById(R.id.forgetpass);
        bt = (Button) findViewById(R.id.btEmail);
        inputemail = (EditText) findViewById(R.id.editEmail);
        backToLoginPage = (TextView) findViewById(R.id.hasEmail);
        backToPrevious = (ImageView) findViewById(R.id.backfgEmail);
        auth = FirebaseAuth.getInstance();
        backToLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toLoginPage = new Intent(Forgetpass.this, Login.class);
                startActivity(toLoginPage);
            }
        });
        //TODO onclick back button
        backToPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //TODO Check email syntax
        inputemail.addTextChangedListener(setSupEMail);
//TODO reset password
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputemail.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), "ปรดป้อนอีเมลของคุณเพื่อตั้งค่ารหัสผ่านใหม่", Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.sendPasswordResetEmail(email)

                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Forgetpass.this, "เราได้ส่งอีเมลเพื่อตั้งค่ารหัสผ่านใหม่ของคุณแล้ว! โปรดตรวจสอบอีเมลของคุณ", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Forgetpass.this, "ไม่สามารถส่งอีเมลเพื่อตั้งค่ารหัสผ่านใหม่ของคุณได้!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }
        });
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
            String email = inputemail.getText().toString().trim();

            if (!email.isEmpty() && emailValid(email) == true) {
                TransitionManager.beginDelayedTransition(fgpass);
                bt.setVisibility(View.VISIBLE);
                bt.setEnabled(true);
            } else {
                TransitionManager.beginDelayedTransition(fgpass);
                bt.setVisibility(View.INVISIBLE);
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