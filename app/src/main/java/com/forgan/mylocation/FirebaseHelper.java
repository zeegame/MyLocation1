package com.forgan.mylocation;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FirebaseHelper {

    //Local variable
    private static final String TAG = "FirebaseHelper";
    private static Map<String, Object> data = new HashMap<>();
    private static Map<String, Object> logData = new HashMap<>();
    private static boolean flag = true;
    private static String mSelectedImageUri1;
    private static int docID;
    private static String tmpString1, tmpString2, tmpString3, fetchedData;


    //put data into data
    public static void put(String key, @Nullable Object value) {
        data.put(key, value);
        Log.i(TAG, "Data saved");
    }

    //remove data in data
    public static void remove(String key) {
        data.remove(key);
        Log.i(TAG, "Data removed");
    }

    //put data into logData
    public static void put2(String key, String value) {
        logData.put(key, value);
        Log.i(TAG, "Data saved");
    }

    //upload database and clear local data
    public static void upload(String collectionID, String docID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(collectionID).document(docID)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Database successfully written!");
                        data = new HashMap<>();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document to database", e);
                    }
                });
        clearData();
    }

    //update into database
    public static void update(String collectionID, String docID, Map<String, Object> newData) {
        Log.i(TAG, "Start update database");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(collectionID).document(docID)
                .update(newData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Database successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document to database", e);
                    }
                });
        clearData();
    }

    //Get next order number from database
    public static String getNextOrderNum() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("etc").document("order");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        docID = (document.getLong("count").intValue()) + 1;
                        Log.i(TAG, "New docID: : " + docID);
                    }
                } else {
                    Log.w(TAG, "get failed with ", task.getException());
                }
            }
        });
        return convToString(docID);
    }

    //Convert number into 5 digit string
    private static String convToString(int number) {
        String str = "";
        if (number < 10) {
            str = "0000" + Integer.toString(number);
        } else if (number < 100) {
            str = "000" + Integer.toString(number);
        } else if (number < 1000) {
            str = "00" + Integer.toString(number);
        } else if (number < 10000) {
            str = "0" + Integer.toString(number);
        } else {
            str = Integer.toString(number);
        }
        Log.i(TAG, "Converted docID to: " + str);
        return str;
    }

    //Update order count in database
    public static void updateOrderNum() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("etc").document("order")
                .update("count", docID)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Updated order count in database");
                    }
                });
    }

    public static Object get(String key) {
        return data.get(key);
    }

    //return logData
    public static Map<String, Object> getLogData() {
        return logData;
    }

    //clear logData
    public static void clearLogData() {
        logData = new HashMap<>();
        Log.i(TAG, "logData have been cleared");
    }

    //clear data
    public static void clearData() {
        data = new HashMap<>();
        Log.i(TAG, "logData have been cleared");
    }

    //Clear all data
    public static void clearAllData() {
        clearLogData();
        clearData();
        mSelectedImageUri1 = null;
        Log.i(TAG, "All data have been cleared");
    }

    //Create user
    public static void createAccount() {
        //authentication and database initialize
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        //get login information from logData
        String email = logData.get("E-mail").toString();
        String password = logData.get("secret").toString();
        //Create user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //upload additional information into database
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(data.get("Fname").toString() + " " + data.get("Lname").toString())
                                    .build();
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.i(TAG, "user DisplayNameSet");
                                            }
                                        }
                                    });
                            upload("users", user.getEmail());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        }
                    }
                });
    }

    public static void createAccountSup() {
        //authentication and database initialize
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        //get login information from logData
        String email = logData.get("E-mail").toString();
        String password = logData.get("secret").toString();
        //Create user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //upload additional information into database
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(data.get("name").toString())
                                    .build();
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.i(TAG, "user DisplayNameSet");
                                            }
                                        }
                                    });
                            upload("users", user.getEmail());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        }
                    }
                });
    }

    public static void setFlag(boolean f) {
        flag = f;
    }


    public static void setPath(@Nullable String mSelectedImageUri) {
        mSelectedImageUri1 = mSelectedImageUri;
        Log.i(TAG, "picture path saved");
    }

    public static String getPath() {
        return mSelectedImageUri1;
    }

    public static void signIn() {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(logData.get("E-mail").toString(), logData.get("secret").toString());
    }

//    public static String getUserDataOnDatabase(String username, final String key) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        DocumentReference docRef = db.collection("users").document(username);
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        //if document of current user is valid get data
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                        fetchedData = document.getString(key);
//                    } else {
//                        Log.d(TAG, "No such document");
//                    }
//                } else {
//                    Log.d(TAG, "get failed with ", task.getException());
//                }
//            }
//        });
//        return fetchedData;
//    }

//    public static void getDataFromDB() {
//
//    }

    public static void setTmpString(int index, String value) {
        switch (index) {
            case 1:
                tmpString1 = value;
                break;
            case 2:
                tmpString2 = value;
                break;
            case 3:
                tmpString3 = value;
                break;
        }
    }

    public static String getTmpString(int index) {
        String myTmpString = null;
        switch (index) {
            case 1:
                myTmpString = tmpString1;
                break;
            case 2:
                myTmpString = tmpString2;
                break;
            case 3:
                myTmpString = tmpString3;
                break;
        }
        return myTmpString;
    }

    public static void resetTmpString() {
        tmpString1 = null;
        tmpString2 = null;
        tmpString3 = null;
    }

}
