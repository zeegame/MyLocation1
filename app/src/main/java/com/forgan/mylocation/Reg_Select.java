package com.forgan.mylocation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Reg_Select extends AppCompatActivity {
    private static final String TAG = "reg/select";
    private TextView hasSelect;
    private Button selectSup, selectUser;
    private ImageView backRegSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.LoginTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_select);

        //Get element on activity
        hasSelect = (TextView) findViewById(R.id.hasSelect);
        selectSup = (Button) findViewById(R.id.selectSup);
        selectUser = (Button) findViewById(R.id.selectUser);

        //Set address data to null as default
        FirebaseHelper.put("address", null);
        FirebaseHelper.put("address_ext", null);

        //on click Already has account button
        hasSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toLogin = new Intent(Reg_Select.this, Login.class);
                startActivity(toLogin);
            }
        });

        //onclick supplier
        selectSup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Selected: sup");
                FirebaseHelper.put("type", "sup");
                Intent toRegSupName = new Intent(Reg_Select.this, Reg_sup_name.class);
                startActivity(toRegSupName);
            }
        });

        //onclick user
        selectUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseHelper.put("type", "user");
                FirebaseHelper.put("address_ext", null);
                Intent toRegUserName = new Intent(Reg_Select.this, Reg_user_name.class);
                startActivity(toRegUserName);
            }
        });

        //onclick back
        backRegSelect = (ImageView) findViewById(R.id.backRegSelect);
        backRegSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}

