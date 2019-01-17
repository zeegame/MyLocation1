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

public class Reg_user_profile_pic extends AppCompatActivity {
    private static final String TAG = "reg_user_profile_pic";
    private TextView textLater;
    private TextView hasSelect;
    private ImageView backRegUserPic;
    private ImageView selectpic_;
    private String mSelectedImageUri1;
    private AppCompatActivity frthis_1 = this;
    private Button next;
    private ImageView checkPic_;
    private ViewGroup reg_user_pro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.LoginTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_user_profile_pic);

        //get element on activity
        reg_user_pro = (ViewGroup) findViewById(R.id.reg_user_pic);
        textLater = (TextView) findViewById(R.id.tvLater);
        hasSelect = (TextView) findViewById(R.id.hasAccountInProfilePic);
        checkPic_ = (ImageView) findViewById(R.id.imageView3);
        backRegUserPic = (ImageView) findViewById(R.id.backRegUserPic);
        next = (Button) findViewById(R.id.btRegUserPic);
        selectpic_ = (ImageView) findViewById(R.id.imageView3);

        //on click button
        backRegUserPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //on click ad photo later
        textLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseHelper.setPath(null);
                Intent textLater = new Intent(Reg_user_profile_pic.this, Reg_user_profile_complete.class);
                startActivity(textLater);
            }
        });

        //on click already has account button
        hasSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseHelper.clearAllData();
                Intent backToLogIn = new Intent(Reg_user_profile_pic.this, Login.class);
                startActivity(backToLogIn);
            }
        });

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

        //NEXT on continued
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseHelper.setPath(mSelectedImageUri1);
                Intent i = new Intent(Reg_user_profile_pic.this, Reg_user_profile_complete.class);
                startActivity(i);
            }
        });


        //BACK Touch IMG or BackPress

//        view.setFocusableInTouchMode(true);
//        view.requestFocus();
//        view.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
//                    getFragmentManager().popBackStack();
//                    return true;
//                }
//                return false;
//            }
//        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Image image = ImagePicker.getFirstImageOrNull(data);

        if (image != null) {
            mSelectedImageUri1 = image.getPath();
            Glide.with(this)
                    .load(mSelectedImageUri1)
                    .apply(RequestOptions.circleCropTransform())
                    .into(selectpic_);
            TransitionManager.beginDelayedTransition(reg_user_pro);
            next.setVisibility(View.VISIBLE);
            next.setEnabled(true);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
