package com.example.pierdeloapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import io.github.muddz.styleabletoast.StyleableToast;

public class ForgotPasswordActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView textOne, textTwo;
    private EditText editTextForgotEmail;
    Button buttonForgotPassword;
    FirebaseAuth authProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        editTextForgotEmail = findViewById(R.id.editText_Forgot_Email);
        buttonForgotPassword = findViewById(R.id.button_forgot_email);
        textOne = findViewById(R.id.textViewOneDesc);
        textTwo = findViewById(R.id.textViewTwoDesc);

        progressBar = findViewById(R.id.progressBar);
        buttonForgotPassword.setOnClickListener(v -> {
            String email = editTextForgotEmail.getText().toString();
            if (TextUtils.isEmpty(email)) {
                StyleableToast.makeText(ForgotPasswordActivity.this, getString(R.string.forgot_activity_enter_email), Toast.LENGTH_SHORT, R.style.warningToast).show();
                editTextForgotEmail.setError(getString(R.string.forgot_activity_email_req));
                editTextForgotEmail.requestFocus();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                StyleableToast.makeText(ForgotPasswordActivity.this, getString(R.string.forgot_activity_enter_email_valid), Toast.LENGTH_SHORT, R.style.warningToast).show();
                editTextForgotEmail.setError(getString(R.string.forgot_activity_email_req));
                editTextForgotEmail.requestFocus();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                resetPassword(email);
            }
        });
    }

    private void resetPassword(String email) {
        authProfile = FirebaseAuth.getInstance();
        authProfile.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                textOne.setVisibility(View.VISIBLE);
                textTwo.setVisibility(View.VISIBLE);
                StyleableToast.makeText(ForgotPasswordActivity.this, getString(R.string.forgot_activity_email_box), Toast.LENGTH_SHORT, R.style.infoToast).show();
                StyleableToast.makeText(ForgotPasswordActivity.this, getString(R.string.forgot_activity_home_page), Toast.LENGTH_LONG, R.style.warningToast).show();
                new Handler().postDelayed(() -> {
                    Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }, 5000);
            } else {
                try {
                    throw task.getException();
                } catch (FirebaseAuthInvalidUserException e) {
                    editTextForgotEmail.setError(getString(R.string.forgot_activity_user_error));
                } catch (Exception e) {
                    Log.e("ForgotPasswordActivity", e.getMessage());
                    StyleableToast.makeText(ForgotPasswordActivity.this, getString(R.string.forgot_activity_something), Toast.LENGTH_LONG, R.style.errorToast).show();
                }
            }
            progressBar.setVisibility(View.GONE);
        });
    }
}
