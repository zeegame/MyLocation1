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

public class Reg_user_phone extends AppCompatActivity {
    private Button btUserPhonePhone;
    private TextView hasSelect;
    private EditText editUserPhone;
    private ImageView backRegUserPhone;
    private ViewGroup reg_user_phone;
    private TextWatcher setNumPhone = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String i = editUserPhone.getText().toString().trim();

            if (!i.isEmpty() && (i.length() >= 9)) {
                TransitionManager.beginDelayedTransition(reg_user_phone);
                btUserPhonePhone.setVisibility(View.VISIBLE);
                btUserPhonePhone.setEnabled(true);
            } else {
                TransitionManager.beginDelayedTransition(reg_user_phone);
                btUserPhonePhone.setVisibility(View.INVISIBLE);
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
        setContentView(R.layout.activity_reg_user_phone);

        //get element onn activity
        reg_user_phone = (ViewGroup) findViewById(R.id.reg_user_phone);
        btUserPhonePhone = (Button) findViewById(R.id.btUserPhonePhone);
        hasSelect = (TextView) findViewById(R.id.hasSelect);
        editUserPhone = (EditText) findViewById(R.id.editUserPhone);
        backRegUserPhone = (ImageView) findViewById(R.id.backRegUserPhone);

        editUserPhone.addTextChangedListener(setNumPhone);

        //Next btn click
        btUserPhonePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseHelper.put("phone", editUserPhone.getText().toString());
                Intent toEmail = new Intent(Reg_user_phone.this, Reg_user_Email.class);
                startActivity(toEmail);
            }
        });

        //Already has account btn click
        hasSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseHelper.clearAllData();
                Intent toLogin = new Intent(Reg_user_phone.this, Login.class);
                startActivity(toLogin);
            }
        });

        //Back button click
        backRegUserPhone.setOnClickListener(new View.OnClickListener() {
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
