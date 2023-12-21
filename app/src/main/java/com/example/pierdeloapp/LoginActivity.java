package com.example.pierdeloapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import io.github.muddz.styleabletoast.StyleableToast;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextLoginEmail , editTextLoginPassword;
    private ProgressBar progressBar;
    private FirebaseAuth authProfile;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextLoginEmail = findViewById(R.id.editText_login_email);
        editTextLoginPassword = findViewById(R.id.editText_login_password);
        progressBar = findViewById(R.id.progressBar);
        authProfile = FirebaseAuth.getInstance();
        TextView textForgotPassword = findViewById(R.id.forgotPassword);

        textForgotPassword.setOnClickListener(v -> {
            StyleableToast.makeText(LoginActivity.this,getString(R.string.login_activity_reset_password),Toast.LENGTH_SHORT,R.style.infoToast).show();
            startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
        });

        ImageView imageViewShowHidePassword = findViewById(R.id.editText_show_hide_password);
        imageViewShowHidePassword.setImageResource(R.drawable.password_hide);
        imageViewShowHidePassword.setOnClickListener(v -> {
            if(editTextLoginPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                editTextLoginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                imageViewShowHidePassword.setImageResource(R.drawable.password_hide);
            }else {
                editTextLoginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                imageViewShowHidePassword.setImageResource(R.drawable.password_show);
            }
        });

        Button buttonLogin = findViewById(R.id.button_login);

        buttonLogin.setOnClickListener(v -> {
            String textEmail = editTextLoginEmail.getText().toString();
            String textPassword = editTextLoginPassword.getText().toString();
            if(TextUtils.isEmpty(textEmail)){
                StyleableToast.makeText(LoginActivity.this,getString(R.string.login_activity_enter_email), Toast.LENGTH_SHORT,R.style.warningToast).show();
                editTextLoginEmail.setError(getString(R.string.login_activity_req_email));
                editTextLoginEmail.requestFocus();
            }else if(!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                StyleableToast.makeText(LoginActivity.this,getString(R.string.login_activity_rEnter_email), Toast.LENGTH_SHORT,R.style.warningToast).show();
                editTextLoginEmail.setError(getString(R.string.login_activity_req_email_valid));
                editTextLoginEmail.requestFocus();
            }else if(TextUtils.isEmpty(textPassword)){
                StyleableToast.makeText(LoginActivity.this,getString(R.string.login_activity_enter_password), Toast.LENGTH_SHORT,R.style.warningToast).show();
                editTextLoginPassword.setError(getString(R.string.login_activity_req_password));
                editTextLoginPassword.requestFocus();
            }else{
                progressBar.setVisibility(View.VISIBLE);
                loginUser(textEmail,textPassword);
            }
        });

    }

    private void loginUser(String email, String password) {
        authProfile.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = authProfile.getCurrentUser();
                if(firebaseUser.isEmailVerified()){
                    StyleableToast.makeText(LoginActivity.this,getString(R.string.login_activity_now_log),Toast.LENGTH_SHORT,R.style.successToast).show();
                    Intent intent = new Intent(LoginActivity.this,UserProfileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK) ;
                    startActivity(intent);
                    finish();
                }else {
                    firebaseUser.sendEmailVerification();
                    authProfile.signOut();
                    showAlertDialog();
                }
            }else{
                try {
                    throw task.getException();
                }catch (FirebaseAuthInvalidUserException e){
                    editTextLoginEmail.setError(getString(R.string.login_activity_not_user));
                    editTextLoginEmail.requestFocus();
                }catch (FirebaseAuthInvalidCredentialsException e){
                    editTextLoginEmail.setError(getString(R.string.login_activity_invalid_user));
                    editTextLoginEmail.requestFocus();
                }catch (Exception e){
                    Log.e(TAG,e.getMessage());
                    StyleableToast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT,R.style.warningToast).show();
                }
            }
            progressBar.setVisibility(View.GONE);
        });
    }

    private void showAlertDialog() {
        View alertCustomDialog = LayoutInflater.from(LoginActivity.this).inflate(R.layout.email_dialog, null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
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

    public void goToSignUp(View view) {
        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(authProfile.getCurrentUser() != null){
            StyleableToast.makeText(LoginActivity.this,getString(R.string.login_activity_logged_in) , Toast.LENGTH_SHORT , R.style.infoToast).show();
            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
            finish();
        }else {
            StyleableToast.makeText(LoginActivity.this,getString(R.string.login_activity_logged_now) , Toast.LENGTH_SHORT , R.style.infoToast).show();
        }
    }

}