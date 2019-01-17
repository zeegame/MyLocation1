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

public class Reg_sup_phone extends AppCompatActivity {
    private TextView hasRegSupPhone;
    private Button btRegSupPhone;
    private EditText editRegSupPhone;
    private ImageView backRegSupPhone;
    private ViewGroup reg_sup_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.LoginTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_sup_phone);

        //get element on activity
        reg_sup_phone = (ViewGroup) findViewById(R.id.reg_sup_phone);
        hasRegSupPhone = (TextView) findViewById(R.id.hasRegSupPhone);
        btRegSupPhone = (Button) findViewById(R.id.btRegSupPhone);
        editRegSupPhone = (EditText) findViewById(R.id.editRegSupPhone);
        backRegSupPhone = (ImageView) findViewById(R.id.backRegSupPhone);

        //onclick already has account button
        hasRegSupPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseHelper.clearAllData();
                Intent toLogin = new Intent(Reg_sup_phone.this, Login.class);
                startActivity(toLogin);
            }
        });

        //onclick next button
        btRegSupPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    FirebaseHelper.put("phone", editRegSupPhone.getText().toString());
                    Intent toRegSupPass = new Intent(Reg_sup_phone.this, Reg_sup_pass.class);
                    startActivity(toRegSupPass);
            }
        });

        //onclick back button
        backRegSupPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //check editText
        editRegSupPhone.addTextChangedListener(setSupPhone);
    }

    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    private TextWatcher setSupPhone = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String supPhoneCheck = editRegSupPhone.getText().toString().trim();
            if (!supPhoneCheck.isEmpty() && (supPhoneCheck.length()  >= 9)) {
                TransitionManager.beginDelayedTransition(reg_sup_phone);
                btRegSupPhone.setVisibility(View.VISIBLE);
                btRegSupPhone.setEnabled(true);
            } else {
                TransitionManager.beginDelayedTransition(reg_sup_phone);
                btRegSupPhone.setVisibility(View.INVISIBLE);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


}
