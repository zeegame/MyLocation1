package com.forgan.mylocation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Reg_user_profile_complete extends AppCompatActivity {
    private Button start;
    private FirebaseUser currentUser;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_user_profile_complete);

        progressDialog = new ProgressDialog(Reg_user_profile_complete.this);
        progressDialog.setTitle(getString(R.string.checkingData));
        progressDialog.setMessage(getString(R.string.plsWait));
        progressDialog.setCancelable(false);
        progressDialog.show();
        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                progressDialog.cancel();
            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 2000);

        start = (Button) findViewById(R.id.letStart);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toUserMain = new Intent(Reg_user_profile_complete.this, Splash.class);
                startActivity(toUserMain);
                FirebaseHelper.clearAllData();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseHelper.createAccount();
//        progressDialog.dismiss();
        FirebaseHelper.signIn();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        uploadPic();

    }

    public void uploadPic() {
        if (TextUtils.isEmpty(FirebaseHelper.getPath()))
            return;

        //initial database
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageReference = storage.getReference();
        final StorageReference ref = storageReference.child("users/prof/" + FirebaseHelper.getLogData().get("E-mail").toString() + ".jpg");
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(R.string.plsWait);
        progressDialog.show();
        //upload file
        ref.putFile(Uri.fromFile(new File(FirebaseHelper.getPath())))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //get download URL
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(final Uri uri) {
                                ref.putFile(Uri.fromFile(new File(FirebaseHelper.getPath())))
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                //Update user's photo URL
                                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                        .setPhotoUri(uri)
                                                        .build();
                                                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                                currentUser.updateProfile(profileUpdates)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    progressDialog.dismiss();
                                                                }
                                                            }
                                                        });
                                            }
                                        });
                            }
                        });
                    }
                });

    }
}
