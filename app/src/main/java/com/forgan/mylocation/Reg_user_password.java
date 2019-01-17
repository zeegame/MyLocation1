package com.forgan.mylocation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.TransitionManager;

public class Reg_user_password extends AppCompatActivity {

    private Button userPassW, btUserPass;
    private TextView hasSupPhone;
    private EditText editUserPass;
    private ImageView backRegUserPass;
    private ViewGroup reg_user_pass;
    private TextWatcher setPassWord = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String userInputPassWord = editUserPass.getText().toString().trim();
            if (!userInputPassWord.isEmpty() && userInputPassWord.length() > 5 && txtVad(userInputPassWord) == true) {
                TransitionManager.beginDelayedTransition(reg_user_pass);
                userPassW.setVisibility(View.VISIBLE);
                userPassW.setEnabled(true);
            } else {
                TransitionManager.beginDelayedTransition(reg_user_pass);
                userPassW.setVisibility(View.INVISIBLE);
            } // show button
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

        public boolean onTouchEvent(MotionEvent event) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                    INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.LoginTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_user_password);

        //get element on activity
        reg_user_pass = (ViewGroup) findViewById(R.id.reg_user_pass);
        userPassW = (Button) findViewById(R.id.btUserPass);
        hasSupPhone = (TextView) findViewById(R.id.hasSupPhone);
        editUserPass = (EditText) findViewById(R.id.editUserPass);
        backRegUserPass = (ImageView) findViewById(R.id.backRegUserPassword);

        editUserPass.addTextChangedListener(setPassWord);

        // next button click
        userPassW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseHelper.put2("secret", editUserPass.getText().toString());
                Intent toProfile = new Intent(Reg_user_password.this, Reg_user_profile_pic.class);
                startActivity(toProfile);

            }
        });

        //onclick already has account button
        hasSupPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseHelper.clearAllData();
                Intent toLogin = new Intent(Reg_user_password.this, Login.class);
                startActivity(toLogin);
            }
        });

        backRegUserPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public boolean txtVad(String userInputPassWord) {

        Pattern upperCase = Pattern.compile("[A-Z]");
        Pattern lowerCase = Pattern.compile("[a-z]");
        Pattern digitCase = Pattern.compile("[0-9]");

        if (!upperCase.matcher(userInputPassWord).find()) {
            return false;
        }
        if (!lowerCase.matcher(userInputPassWord).find()) {
            return false;
        }
        if (!digitCase.matcher(userInputPassWord).find()) {
            return false;
        }
        return true;
    }

    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }


}
