package com.example.pierdeloapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.github.muddz.styleabletoast.StyleableToast;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextSignupUsername , editTextSignupEmail, editTextSignupPassword , editTextSignupConfirmPassword;
    private ProgressBar progressBar;
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressBar = findViewById(R.id.progressBar);
        editTextSignupUsername = findViewById(R.id.editText_signup_username);
        editTextSignupEmail = findViewById(R.id.editText_signup_email);
        editTextSignupPassword = findViewById(R.id.editText_signup_password) ;
        editTextSignupConfirmPassword = findViewById(R.id.editText_signup_confirmPassword);
        Button buttonCreate = findViewById(R.id.button_create);

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textUsername = editTextSignupUsername.getText().toString();
                String textEmail = editTextSignupEmail.getText().toString();
                String textPassword = editTextSignupPassword.getText().toString();
                String textConfirmPassword = editTextSignupConfirmPassword.getText().toString();
                if (TextUtils.isEmpty(textUsername)) {
                    StyleableToast.makeText(RegisterActivity.this, getString(R.string.register_enter_name), Toast.LENGTH_SHORT, R.style.warningToast).show();
                    editTextSignupUsername.setError(getString(R.string.register_username_req));
                    editTextSignupUsername.requestFocus();
                } else if (TextUtils.isEmpty(textEmail)) {
                    StyleableToast.makeText(RegisterActivity.this, getString(R.string.register_enter_email), Toast.LENGTH_SHORT, R.style.warningToast).show();
                    editTextSignupEmail.setError(getString(R.string.register_email_req));
                    editTextSignupEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                    StyleableToast.makeText(RegisterActivity.this, getString(R.string.register_enter_re_mail), Toast.LENGTH_SHORT, R.style.warningToast).show();
                    editTextSignupEmail.setError(getString(R.string.register_email_valid));
                    editTextSignupEmail.requestFocus();
                } else if (TextUtils.isEmpty(textPassword)) {
                    StyleableToast.makeText(RegisterActivity.this, getString(R.string.register_enter_password), Toast.LENGTH_SHORT, R.style.warningToast).show();
                    editTextSignupPassword.setError(getString(R.string.register_password_req));
                    editTextSignupPassword.requestFocus();
                } else if (textPassword.length() < 6) {
                    StyleableToast.makeText(RegisterActivity.this, getString(R.string.register_password_digit), Toast.LENGTH_SHORT, R.style.warningToast).show();
                    editTextSignupPassword.setError(getString(R.string.register_password_weak));
                    editTextSignupPassword.requestFocus();
                } else if (TextUtils.isEmpty(textConfirmPassword)) {
                    StyleableToast.makeText(RegisterActivity.this, getString(R.string.register_password_confirm), Toast.LENGTH_SHORT, R.style.warningToast).show();
                    editTextSignupConfirmPassword.setError(getString(R.string.register_password_conf));
                    editTextSignupConfirmPassword.requestFocus();
                } else if (!textPassword.equals(textConfirmPassword)) {
                    StyleableToast.makeText(RegisterActivity.this, getString(R.string.register_password_same), Toast.LENGTH_SHORT, R.style.warningToast).show();
                    editTextSignupConfirmPassword.setError(getString(R.string.register_password_conf));
                    editTextSignupConfirmPassword.requestFocus();
                    //CLEAR
                    editTextSignupPassword.clearComposingText();
                    editTextSignupConfirmPassword.clearComposingText();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(textUsername, textEmail, textPassword);
                }
            }

            private void registerUser(String textUsername, String textEmail, String textPassword) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.createUserWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            FirebaseUser firebaseUser = auth.getCurrentUser();

                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(textUsername).build();
                            firebaseUser.updateProfile(profileChangeRequest);
                            ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(textEmail);
                            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
                            referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(task1 -> {
                                //send Email
                                if (task1.isSuccessful()) {
                                    firebaseUser.sendEmailVerification();
                                    StyleableToast.makeText(RegisterActivity.this, getString(R.string.register_password_toast_success), Toast.LENGTH_LONG, R.style.successToast).show();
                                    Intent intent = new Intent(RegisterActivity.this, UserProfileActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    StyleableToast.makeText(RegisterActivity.this, getString(R.string.register_password_toast_failed), Toast.LENGTH_LONG, R.style.errorToast).show();
                                }
                                progressBar.setVisibility(View.GONE);
                            });


                        } else {
                            Exception exception = task.getException();
                            if (exception != null) {
                                try {
                                    throw exception;
                                } catch (FirebaseAuthWeakPasswordException e) {
                                    editTextSignupPassword.setError(getString(R.string.register_to_weak_alphabet));
                                    editTextSignupPassword.requestFocus();
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    editTextSignupPassword.setError(getString(R.string.register_to_email));
                                    editTextSignupPassword.requestFocus();
                                } catch (FirebaseAuthUserCollisionException e) {
                                    editTextSignupPassword.setError(getString(R.string.register_to_email_with));
                                    editTextSignupPassword.requestFocus();
                                } catch (Exception e) {
                                    Log.e(TAG, " " + e.getMessage());
                                    StyleableToast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG, R.style.infoToast).show();
                                }
                            }
                            progressBar.setVisibility(View.GONE);

                        }
                    }
                });
            }
        });
        }

    public void goToLogin(View view) {
        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(intent);
    }
}