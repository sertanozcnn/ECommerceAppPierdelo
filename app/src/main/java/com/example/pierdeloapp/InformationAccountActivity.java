package com.example.pierdeloapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import io.github.muddz.styleabletoast.StyleableToast;

public class InformationAccountActivity extends AppCompatActivity {
    private String userName, email;
    FirebaseAuth authProfile;
    private EditText textEmail, textUserName;
    private ProgressBar progressBar;
    Button buttonGoToChangePassword , buttonGoToUpdateEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_account);

        textUserName = findViewById(R.id.edittextName);
        textEmail = findViewById(R.id.edittextEmail);
        progressBar = findViewById(R.id.progressBar);
        authProfile = FirebaseAuth.getInstance();
        buttonGoToChangePassword = findViewById(R.id.button_password_change);
        buttonGoToUpdateEmail = findViewById(R.id.button_update_email_information);

        ImageView backActivity = findViewById(R.id.toolbar_activityBack);
        TextView activityHeader = findViewById(R.id.toolbar_activityHeader);
        buttonGoToChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(InformationAccountActivity.this,ChangePasswordActivity.class);
            startActivity(intent);
        });
        buttonGoToUpdateEmail.setOnClickListener(v -> {
            Intent intent = new Intent(InformationAccountActivity.this,UpdateEmailActivity.class);
            startActivity(intent);
        });

        backActivity.setOnClickListener(v -> finish());
        activityHeader.setText(getString(R.string.information_title));

        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        if (firebaseUser == null) {
            StyleableToast.makeText(InformationAccountActivity.this, getString(R.string.information_activity_error), R.style.errorToast).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }
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
                }else {
                    StyleableToast.makeText(InformationAccountActivity.this, getString(R.string.information_activity_something), Toast.LENGTH_LONG, R.style.errorToast).show();
                }
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                StyleableToast.makeText(InformationAccountActivity.this, getString(R.string.information_activity_something), Toast.LENGTH_LONG, R.style.errorToast).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}