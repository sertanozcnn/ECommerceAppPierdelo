package com.example.pierdeloapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.muddz.styleabletoast.StyleableToast;

public class UserProfileActivity extends AppCompatActivity {

    private TextView textEmail, textUserName;
    private ProgressBar progressBar;
    private String userName, email;
    private FirebaseAuth authProfile;
    private CircleImageView profileImageView;
    ImageView imageViewLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        textUserName = findViewById(R.id.profile_username);
        textEmail = findViewById(R.id.profile_email);
        progressBar = findViewById(R.id.progressBar);
        profileImageView = findViewById(R.id.imageView_Profile);
        imageViewLogOut = findViewById(R.id.logOut);
        authProfile = FirebaseAuth.getInstance();
        LinearLayout buttonInformationAccount = findViewById(R.id.information_account);
        LinearLayout buttonSettings = findViewById(R.id.user_settings);
        LinearLayout buttonAddress = findViewById(R.id.address);
        LinearLayout buttonOrder = findViewById(R.id.my_order);
        LinearLayout buttonPayment = findViewById(R.id.payment);

        buttonInformationAccount.setOnClickListener(v -> {
            Intent intent = new Intent(UserProfileActivity.this,InformationAccountActivity.class);
            startActivity(intent);
        });
        buttonSettings.setOnClickListener(v -> {
            Intent intent = new Intent(UserProfileActivity.this,SettingsActivity.class);
            startActivity(intent);
        });

        buttonAddress.setOnClickListener(v -> {
            Intent intent = new Intent(UserProfileActivity.this,AddressActivity.class);
            startActivity(intent);
        });
        buttonOrder.setOnClickListener(v -> {
            Intent intent = new Intent(UserProfileActivity.this, OrderActivity.class);
            startActivity(intent);
        });

        buttonPayment.setOnClickListener(v -> {
            Intent intent = new Intent(UserProfileActivity.this, PaymentMethodActivity.class);
            startActivity(intent);
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_profile);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_profile) {
                return true;
            } else if (item.getItemId() == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                overridePendingTransition(0,0);
                return true;
            }else if(item.getItemId() == R.id.bottom_discover){
                startActivity(new Intent(getApplicationContext(), ItemsActivity.class));
                overridePendingTransition(0,0);
                return true;
            }
            return false;
        });

        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        if (firebaseUser == null) {
            StyleableToast.makeText(UserProfileActivity.this, getString(R.string.user_profile_activity_error), R.style.errorToast).show();

        } else {
            checkifEmailVerified(firebaseUser);
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }

        imageViewLogOut.setOnClickListener(v -> {
            authProfile.signOut();
            StyleableToast.makeText(UserProfileActivity.this, getString(R.string.user_profile_activity_log_out), Toast.LENGTH_SHORT, R.style.successToast).show();
            Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });


    }

    private void checkifEmailVerified(FirebaseUser firebaseUser) {
        if (!firebaseUser.isEmailVerified()) {
            showAlertDialog();
        }
    }

    private void showAlertDialog() {
        View alertCustomDialog = LayoutInflater.from(UserProfileActivity.this).inflate(R.layout.email_dialog, null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserProfileActivity.this);

        alertDialog.setView(alertCustomDialog);
        Button buttonContinue = alertCustomDialog.findViewById(R.id.buttonContinue);

        final AlertDialog dialog = alertDialog.create();


        dialog.show();

        buttonContinue.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_APP_EMAIL);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readWriteUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readWriteUserDetails != null) {
                    userName = firebaseUser.getDisplayName();
                    email = firebaseUser.getEmail();
                    textUserName.setText(userName);
                    textEmail.setText(email);
                    Uri userPhotoUri = firebaseUser.getPhotoUrl();

                    if (userPhotoUri == null) {
                        profileImageView.setImageResource(R.drawable.profile_circle);
                    } else {
                        userPhotoUri = firebaseUser.getPhotoUrl();
                        Picasso.get().load(userPhotoUri).into(profileImageView);
                    }


                }else {
                    StyleableToast.makeText(UserProfileActivity.this, getString(R.string.user_profile_activity_something), Toast.LENGTH_LONG, R.style.errorToast).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                StyleableToast.makeText(UserProfileActivity.this, getString(R.string.user_profile_activity_something), Toast.LENGTH_LONG, R.style.errorToast).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }


}