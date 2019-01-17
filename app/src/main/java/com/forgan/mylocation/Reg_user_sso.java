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

import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.TransitionManager;

public class Reg_user_sso extends AppCompatActivity {
    private Button btUserID;
    private TextView hasSupPhone;
    private EditText editUserID, checkID;
    private ImageView backRegUserSSO;
    private ViewGroup reg_user_sso;
    private TextWatcher setSSO = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String userSSO = checkID.getText().toString().trim();

            if (!userSSO.isEmpty() && !(userSSO.length() != 13)) {
                TransitionManager.beginDelayedTransition(reg_user_sso);
                btUserID.setVisibility(View.VISIBLE);
                btUserID.setEnabled(true);
            } else {
                TransitionManager.beginDelayedTransition(reg_user_sso);
                btUserID.setVisibility(View.INVISIBLE);
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
        setContentView(R.layout.activity_reg_user_sso);

        //get element on activity
        reg_user_sso = (ViewGroup) findViewById(R.id.reg_user_sso);
        btUserID = (Button) findViewById(R.id.btUserID);
        hasSupPhone = (TextView) findViewById(R.id.hasSupPhone);
        editUserID = (EditText) findViewById(R.id.editUserID);
        checkID = (EditText) findViewById(R.id.editUserID);

        //onclick next button
        btUserID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseHelper.put("SSO", editUserID.getText().toString());
                Intent toPassword = new Intent(Reg_user_sso.this, Reg_user_password.class);
                startActivity(toPassword);
            }
        });

        checkID.addTextChangedListener(setSSO);

        //onclick already has account button
        hasSupPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseHelper.clearAllData();
                Intent toLogin = new Intent(Reg_user_sso.this, Login.class);
                startActivity(toLogin);
            }
        });
        backRegUserSSO = (ImageView) findViewById(R.id.backRegUserSSO);
        backRegUserSSO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }
}
