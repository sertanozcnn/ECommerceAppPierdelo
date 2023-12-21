package com.example.pierdeloapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.github.muddz.styleabletoast.StyleableToast;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText editTextPasswordCurrent , editTextPasswordNew , editTextPasswordNewConfirm;
    private Button buttonChangePassword , buttonReAuthenticate;
    private ProgressBar progressBar;
    private String userPasswordCurrent;
    FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        editTextPasswordCurrent = findViewById(R.id.editText_password_current);
        editTextPasswordNew = findViewById(R.id.editText_password_new);
        editTextPasswordNewConfirm = findViewById(R.id.editText_password_new_confirm);
        progressBar = findViewById(R.id.progressBar);
        buttonChangePassword = findViewById(R.id.button_change_password);
        buttonReAuthenticate = findViewById(R.id.button_authenticate);
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        if(firebaseUser.equals("")){
            StyleableToast.makeText(ChangePasswordActivity.this,getString(R.string.toast_change_password_error), Toast.LENGTH_SHORT,R.style.errorToast).show();
            Intent intent = new Intent(ChangePasswordActivity.this,UserProfileActivity.class);
            startActivity(intent);
            finish();
        }else {
            reAuthenticateUser(firebaseUser);
        }
    }

    private void reAuthenticateUser(FirebaseUser firebaseUser) {
        buttonReAuthenticate.setOnClickListener(v -> {
            userPasswordCurrent = editTextPasswordCurrent.getText().toString();
            if(TextUtils.isEmpty(userPasswordCurrent)){
                StyleableToast.makeText(ChangePasswordActivity.this,getString(R.string.toast_change_password_need), Toast.LENGTH_SHORT,R.style.warningToast).show();
                editTextPasswordCurrent.setError(getString(R.string.set_new_password_authenticate));
                editTextPasswordCurrent.requestFocus();
            }else {
                progressBar.setVisibility(View.VISIBLE);
            }

            AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(),userPasswordCurrent);
            firebaseUser.reauthenticate(credential).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    editTextPasswordCurrent.setEnabled(false);
                    editTextPasswordNew.setEnabled(true);
                    editTextPasswordNewConfirm.setEnabled(true);
                    buttonReAuthenticate.setEnabled(false);
                    buttonChangePassword.setEnabled(true);
                    buttonReAuthenticate.setBackgroundTintList(ContextCompat.getColorStateList(ChangePasswordActivity.this,R.color.update_button_auth));
                    buttonChangePassword.setBackgroundTintList(ContextCompat.getColorStateList(ChangePasswordActivity.this,R.color.update_button));
                    StyleableToast.makeText(ChangePasswordActivity.this,getString(R.string.toast_change_password_verified) + getString(R.string.toast_change_password_now), Toast.LENGTH_SHORT,R.style.infoToast).show();
                    buttonChangePassword.setOnClickListener(v1 -> changePassword(firebaseUser));
                }else {
                    try {
                        throw task.getException();
                    }catch (Exception e){
                        StyleableToast.makeText(ChangePasswordActivity.this,e.getMessage(), Toast.LENGTH_SHORT,R.style.errorToast).show();
                    }
                }
                progressBar.setVisibility(View.GONE);
            });
        });

    }

    private void changePassword(FirebaseUser firebaseUser) {
        String userPasswordNew = editTextPasswordNew.getText().toString();
        String userPasswordNewConfirm = editTextPasswordNewConfirm.getText().toString();
        if(TextUtils.isEmpty(userPasswordNew)){
            StyleableToast.makeText(ChangePasswordActivity.this,getString(R.string.toast_new_password), Toast.LENGTH_SHORT,R.style.errorToast).show();
            editTextPasswordNew.setError(getString(R.string.set_new_password));
            editTextPasswordNew.requestFocus();
        }else if(TextUtils.isEmpty(userPasswordNewConfirm)){
            StyleableToast.makeText(ChangePasswordActivity.this,getString(R.string.toast_new_password_confirm), Toast.LENGTH_SHORT,R.style.errorToast).show();
            editTextPasswordNewConfirm.setError(getString(R.string.set_new_password_again));
            editTextPasswordNewConfirm.requestFocus();
        }else if(!userPasswordNew.matches(userPasswordNewConfirm)){
            StyleableToast.makeText(ChangePasswordActivity.this,getString(R.string.toast_password_match), Toast.LENGTH_SHORT,R.style.errorToast).show();
            editTextPasswordNewConfirm.setError(getString(R.string.set_new_password_renter));
            editTextPasswordNewConfirm.requestFocus();
        }else if(userPasswordCurrent.matches(userPasswordNew)){
            StyleableToast.makeText(ChangePasswordActivity.this,getString(R.string.toast_password_old_new), Toast.LENGTH_SHORT,R.style.errorToast).show();
            editTextPasswordNewConfirm.setError(getString(R.string.set_new_password));
            editTextPasswordNewConfirm.requestFocus();
        }else {
            progressBar.setVisibility(View.VISIBLE);
            firebaseUser.updatePassword(userPasswordNew).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    StyleableToast.makeText(ChangePasswordActivity.this,getString(R.string.toast_password_has_been_changed), Toast.LENGTH_SHORT,R.style.infoToast).show();
                    Intent intent = new Intent(ChangePasswordActivity.this,UserProfileActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    try {
                        throw task.getException();
                    }catch (Exception e){
                        StyleableToast.makeText(ChangePasswordActivity.this,e.getMessage(), Toast.LENGTH_SHORT,R.style.errorToast).show();
                    }
                }
                progressBar.setVisibility(View.GONE);
            });
        }

    }
}