package com.forgan.mylocation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;

import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.TransitionManager;

public class Reg_sup_pic extends AppCompatActivity {
    private TextView hasAccountInProfilePic, tvLater;
    private ImageView backRegSupPic, selectpic_;
    private String mSelectedImageUri1;
    private AppCompatActivity frthis_1 = this;
    private Button next;
    private ViewGroup reg_sup_pic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.LoginTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_sup_pic);

        //Get element on activity
        reg_sup_pic = (ViewGroup) findViewById(R.id.reg_sup_pic);
        hasAccountInProfilePic = (TextView) findViewById(R.id.hasAccountProfilePic);
        hasAccountInProfilePic = (TextView) findViewById(R.id.hasAccountProfilePic);
        tvLater = (TextView) findViewById(R.id.tvLater);
        backRegSupPic = (ImageView) findViewById(R.id.backRegSupPic);
        selectpic_ = (ImageView) findViewById(R.id.imageView3);
        next = (Button) findViewById(R.id.btRegUserPic);

        //onclick already has account button
        hasAccountInProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseHelper.clearAllData();
                Intent toLogin = new Intent(Reg_sup_pic.this, Login.class);
                startActivity(toLogin);
            }
        });

        //onclick upload profile picture later
        tvLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseHelper.setPath(null);
                Intent later = new Intent(Reg_sup_pic.this, Reg_sup_profile_complete.class);
                startActivity(later);
            }
        });

        //onclick back button
        backRegSupPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Load picture
        if (mSelectedImageUri1 != null) {
            next.setEnabled(true);
            Glide.with(this)
                    .load(mSelectedImageUri1)
                    .apply(RequestOptions.circleCropTransform())
                    .into(selectpic_);
        } else {
            selectpic_.setImageResource(R.drawable.ic_person_black_24dp);
        }

        //Select Pictures
        selectpic_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.create(frthis_1)
                        .single()
                        .theme(R.style.AppTheme)
                        .start();
            }
        });

        //onclick next button
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseHelper.setPath(mSelectedImageUri1);
                Intent i = new Intent(Reg_sup_pic.this, Reg_sup_profile_complete.class);
                startActivity(i);
            }
        });

    }

    //Get image Uri after selected
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Image image = ImagePicker.getFirstImageOrNull(data);

        if (image != null) {
            mSelectedImageUri1 = image.getPath();
            Glide.with(this)
                    .load(mSelectedImageUri1)
                    .apply(RequestOptions.circleCropTransform())
                    .into(selectpic_);
            TransitionManager.beginDelayedTransition(reg_sup_pic);
            next.setVisibility(View.VISIBLE);
            next.setEnabled(true);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}