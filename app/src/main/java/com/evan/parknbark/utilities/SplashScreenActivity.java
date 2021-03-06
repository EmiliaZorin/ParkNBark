package com.evan.parknbark.utilities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.evan.parknbark.R;
import com.evan.parknbark.emailpassword.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreenActivity extends BaseActivity {

    private Bundle bundle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        loadLocale(this);
        super.onCreate(savedInstanceState);

        //Create a bundle to pass to the next activity
        bundle = new Bundle();

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            DocumentReference docRef = db.collection("users").document(firebaseUser.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        User user = task.getResult().toObject(User.class);
                        //if user is logged in, put the permission into bundle
                        //else, do nothing
                        startMain(user);
                    }
                }
            });
        } else startMain(null);

    }

    private void startMain(User user) {
        bundle.putSerializable("current_user", user);
        EasySplashScreen config = new EasySplashScreen(SplashScreenActivity.this)
                .withFullScreen()
                .withTargetActivity(LoginActivity.class) //go to main activity
                .withBundleExtras(bundle) //send bundle, either user logged in or nah
                .withBackgroundColor(Color.parseColor("#e1f5fe"))
                .withLogo(R.mipmap.app_logo1_foreground); //TODO: adjust logo dimensions
        View easySplashScreen = config.create();
        setContentView(easySplashScreen);
    }
}
