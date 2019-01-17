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

public class Reg_user_name extends AppCompatActivity {
    private TextView hasSelect;
    private Button btUserName;
    private EditText editName, editUserLastName;
    private ImageView backRegUserName;
    private ViewGroup transition;
    private TextWatcher setCheck = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String userEditTextNameCheck = editName.getText().toString().trim();
            String userEditTextSurNameCheck = editUserLastName.getText().toString().trim();

            if (!userEditTextNameCheck.isEmpty() && !userEditTextSurNameCheck.isEmpty()) {
                TransitionManager.beginDelayedTransition(transition);
                btUserName.setVisibility(View.VISIBLE);
                btUserName.setEnabled(true);
            } else {
                TransitionManager.beginDelayedTransition(transition);
                btUserName.setVisibility(View.INVISIBLE);
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
        setContentView(R.layout.activity_reg_user_name);

        //get element on activity
        transition = (ViewGroup) findViewById(R.id.transition_bt);
        hasSelect = (TextView) findViewById(R.id.hasSelect);
        btUserName = (Button) findViewById(R.id.btUserName);
        editName = (EditText) findViewById(R.id.editName);
        editUserLastName = (EditText) findViewById(R.id.editUserLastName);
        backRegUserName = (ImageView) findViewById(R.id.backRegUserName);

        editName.addTextChangedListener(setCheck);
        editUserLastName.addTextChangedListener(setCheck);

        //Already has account btn click
        hasSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Clear all data in FirebaseHelper
                FirebaseHelper.clearAllData();
                //Back to home
                Intent toLogin = new Intent(Reg_user_name.this, Login.class);
                startActivity(toLogin);
            }
        });

        //Continue btn click
        btUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseHelper.put("Fname", editName.getText().toString());
                FirebaseHelper.put("Lname", editUserLastName.getText().toString());
                Intent toRegUserPhone = new Intent(Reg_user_name.this, Reg_user_phone.class);
                startActivity(toRegUserPhone);
            }
        });

        //Back button click
        backRegUserName.setOnClickListener(new View.OnClickListener() {
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

