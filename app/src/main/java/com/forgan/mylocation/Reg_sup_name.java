package com.forgan.mylocation;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.TransitionManager;

public class Reg_sup_name extends AppCompatActivity {
    private TextView hasRegSupName;
    private Button btRegSupName;
    private EditText editRegSupPass, editRegSupName;
    private ImageView backRegSupName;
    private ViewGroup reg_sup_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.LoginTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_sup_name);

        //Get element on Activity
        reg_sup_name = (ViewGroup) findViewById(R.id.reg_sup_name);
        hasRegSupName = (TextView) findViewById(R.id.hasRegSupName);
        btRegSupName = (Button) findViewById(R.id.btRegSupName);
        editRegSupPass = (EditText) findViewById(R.id.editRegSupPass);
        editRegSupName = (EditText) findViewById(R.id.editRegSupName);
        backRegSupName = (ImageView) findViewById(R.id.backRegSupName);
        editRegSupPass.addTextChangedListener(setSupp);
        editRegSupName.addTextChangedListener(setSupp);

        //onclick already has account button
        hasRegSupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseHelper.clearAllData();
                Intent toLogin = new Intent(Reg_sup_name.this, Login.class);
                startActivity(toLogin);
            }
        });

        //onclick next
        btRegSupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check supplier's ID empty
                if (TextUtils.isEmpty(editRegSupPass.getText().toString()))
                    editRegSupPass.setError(getString(R.string.plsEntSupID));
                //Check supplier's name empty
                if (TextUtils.isEmpty(editRegSupName.getText().toString()))
                    editRegSupName.setError(getString(R.string.plsEntSupName));
                if (!TextUtils.isEmpty(editRegSupName.getText().toString()) && !TextUtils.isEmpty(editRegSupName.getText().toString())) {
                    FirebaseHelper.put("code", editRegSupPass.getText().toString());
                    FirebaseHelper.put("name", editRegSupName.getText().toString());
                    Intent toRegSupEmail = new Intent(Reg_sup_name.this, Reg_sup_email.class);
                    startActivity(toRegSupEmail);
                }
            }
        });
        backRegSupName.setOnClickListener(new View.OnClickListener() {
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

    private TextWatcher setSupp = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String supp = editRegSupPass.getText().toString().trim();
            String supp2 = editRegSupName.getText().toString().trim();

            if (!supp.isEmpty() && !supp2.isEmpty()) {
                TransitionManager.beginDelayedTransition(reg_sup_name);
                btRegSupName.setVisibility(View.VISIBLE);
                btRegSupName.setEnabled(true);
            } else {
                TransitionManager.beginDelayedTransition(reg_sup_name);
                btRegSupName.setVisibility(View.INVISIBLE);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
