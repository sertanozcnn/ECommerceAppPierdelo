package com.example.pierdeloapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.github.muddz.styleabletoast.StyleableToast;

public class UpdateEmailActivity extends AppCompatActivity {

    private FirebaseAuth authProfile;
    private FirebaseUser firebaseUser;
    private ProgressBar progressBar;
    private EditText editTextAuthEmail , editTextAuthPassword, editTextNewEmail ;
    private String userOldEmail , userNewEmail , userPassword;
    private Button buttonUpdateEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);

        progressBar = findViewById(R.id.progressBar);
        editTextAuthEmail = findViewById(R.id.editText_Update_Old_Email);
        editTextAuthPassword = findViewById(R.id.editText_Update_Email_Password);
        editTextNewEmail = findViewById(R.id.editText_Update_New_Email);
        buttonUpdateEmail = findViewById(R.id.button_update_email);

        buttonUpdateEmail.setEnabled(false);
        editTextNewEmail.setEnabled(false);
        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();
        userOldEmail = firebaseUser.getEmail();
        editTextAuthEmail.setText(userOldEmail);

        if(firebaseUser.equals("")){
            StyleableToast.makeText(UpdateEmailActivity.this,getString(R.string.update_email_activity_something), Toast.LENGTH_SHORT,R.style.errorToast).show();
        }else{
            reAuthenticate(firebaseUser);
        }
    }

    private void reAuthenticate(FirebaseUser firebaseUser) {
        Button buttonVerify = findViewById(R.id.button_authenticate);
        buttonVerify.setOnClickListener(v -> {
            userPassword = editTextAuthPassword.getText().toString();
            if(TextUtils.isEmpty(userPassword)){
                StyleableToast.makeText(UpdateEmailActivity.this,getString(R.string.update_email_activity_need_pass),Toast.LENGTH_SHORT,R.style.errorToast).show();
                editTextAuthPassword.setError(getString(R.string.update_email_activity_need_auth));
                editTextAuthPassword.requestFocus();
            }else{
                progressBar.setVisibility(View.VISIBLE);
                AuthCredential credential = EmailAuthProvider.getCredential(userOldEmail , userPassword);
                firebaseUser.reauthenticate(credential).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        progressBar.setVisibility(View.GONE);
                        StyleableToast.makeText(UpdateEmailActivity.this,getString(R.string.update_email_activity_pass_auth) + getString(R.string.update_email_activity_email_update),Toast.LENGTH_LONG,R.style.infoToast).show();
                        editTextNewEmail.setEnabled(true);
                        buttonUpdateEmail.setEnabled(true);
                        editTextAuthPassword.setEnabled(false);
                        editTextAuthEmail.setEnabled(false);
                        buttonVerify.setEnabled(false);
                        buttonVerify.setBackgroundTintList(ContextCompat.getColorStateList(UpdateEmailActivity.this,R.color.update_button_auth));
                        buttonUpdateEmail.setBackgroundTintList(ContextCompat.getColorStateList(UpdateEmailActivity.this,R.color.update_button));
                        buttonUpdateEmail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v1) {
                                userNewEmail = editTextNewEmail.getText().toString();
                                if(TextUtils.isEmpty(userNewEmail)){
                                    StyleableToast.makeText(UpdateEmailActivity.this,getString(R.string.update_email_activity_new_email),Toast.LENGTH_SHORT , R.style.errorToast).show();
                                    editTextNewEmail.setError(getString(R.string.update_email_activity_req_email));
                                    editTextNewEmail.requestFocus();
                                }else if(!Patterns.EMAIL_ADDRESS.matcher(userNewEmail).matches()){
                                    StyleableToast.makeText(UpdateEmailActivity.this,getString(R.string.update_email_activity_valid_email),Toast.LENGTH_SHORT , R.style.errorToast).show();
                                    editTextNewEmail.setError(getString(R.string.update_email_activity_valid_email));
                                    editTextNewEmail.requestFocus();
                                }else if(userOldEmail.matches(userNewEmail)){
                                    StyleableToast.makeText(UpdateEmailActivity.this,getString(R.string.update_email_activity_new_old_email),Toast.LENGTH_SHORT ,R.style.errorToast).show();
                                    editTextNewEmail.setError(getString(R.string.update_email_activity_enter_new_email));
                                    editTextNewEmail.requestFocus();
                                }else{
                                    progressBar.setVisibility(View.VISIBLE);
                                    updateEmail(firebaseUser);
                                }
                            }
                        });
                    }else {
                        try {
                            throw task.getException();
                        }catch (Exception e){
                            StyleableToast.makeText(UpdateEmailActivity.this,e.getMessage(),Toast.LENGTH_SHORT,R.style.errorToast).show();
                        }
                        finally {
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });
            }
        });
    }

    private void updateEmail(FirebaseUser firebaseUser) {
        firebaseUser.updateEmail(userNewEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()){
                    firebaseUser.sendEmailVerification();
                    StyleableToast.makeText(UpdateEmailActivity.this,getString(R.string.update_email_activity_new_old_update_email),Toast.LENGTH_SHORT,R.style.infoToast).show();
                    Intent intent = new Intent(UpdateEmailActivity.this,UserProfileActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    try {
                        throw task.getException();
                    }catch (Exception e){
                        StyleableToast.makeText(UpdateEmailActivity.this,e.getMessage(),Toast.LENGTH_SHORT,R.style.infoToast).show();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }
        });

    }
}