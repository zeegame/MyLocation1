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

public class Reg_sup_pass extends AppCompatActivity {
    private TextView hasRegSupPass;
    private Button btRegSupPass;
    private EditText editRegSupPass;
    private ImageView backRegSupPass;
    private ViewGroup reg_sup_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.LoginTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_sup_pass);

        reg_sup_pass = (ViewGroup) findViewById(R.id.reg_sup_pass);
        hasRegSupPass = (TextView) findViewById(R.id.hasRegSupPass);
        btRegSupPass = (Button) findViewById(R.id.btRegSupPass);
        backRegSupPass = (ImageView) findViewById(R.id.backRegSupPass);
        editRegSupPass = (EditText) findViewById(R.id.editRegSupPass);

        //onclick next button
        btRegSupPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseHelper.put2("secret", editRegSupPass.getText().toString());
                Intent toRegSupPhone = new Intent(Reg_sup_pass.this, Reg_sup_pic.class);
                startActivity(toRegSupPhone);
            }
        });

        //onclick already has account button
        hasRegSupPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseHelper.clearAllData();
                Intent toLogin = new Intent(Reg_sup_pass.this, Login.class);
                startActivity(toLogin);
            }
        });

        //onclick back button
        backRegSupPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        editRegSupPass.addTextChangedListener(setPassWord);
    }

    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }


    private TextWatcher setPassWord = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String userInputPassWord = editRegSupPass.getText().toString().trim();
            if (!userInputPassWord.isEmpty() && userInputPassWord.length() > 5 && txtVad(userInputPassWord) == true) {
                TransitionManager.beginDelayedTransition(reg_sup_pass);
                btRegSupPass.setVisibility(View.VISIBLE);
                btRegSupPass.setEnabled(true);
            } else {
                TransitionManager.beginDelayedTransition(reg_sup_pass);
                btRegSupPass.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

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
}
