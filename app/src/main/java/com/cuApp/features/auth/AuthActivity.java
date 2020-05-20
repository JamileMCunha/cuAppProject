package com.cuApp.features.auth;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.cuApp.features.dashboard.DashboardActivity;
import com.cuApp.R;
import com.cuApp.core.Constants;
import com.cuApp.core.exception.Failure;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

public class AuthActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;

    private GoogleSignInClient mGoogleSignInClient;
    private AuthViewModel mAuthViewModel;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Login");
        }

        mAuthViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        mAuthViewModel.getUser().observe(this, this::goToMainActivity);
        mAuthViewModel.getFailure().observe(this, this::handleFailure);

        initSignInWithGoogle();
        initGoogleSignInClient();
        initSignInWithPassword();
        initRecoverPassword();
        initRegisterUser();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
                if (googleSignInAccount != null) {
                    getGoogleAuthCredential(googleSignInAccount);
                }
            } catch (ApiException e) {
                Log.e(Constants.TAG, e.getMessage(), e);
            }
        }
    }

    private void initSignInWithGoogle() {
        SignInButton btnSignIn = findViewById(R.id.googleLoginBtn);
        btnSignIn.setOnClickListener(v -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
    }

    private void initGoogleSignInClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void initSignInWithPassword() {
        EditText editEmail = findViewById(R.id.emailEt);
        EditText editPassword = findViewById(R.id.passwordEt);
        findViewById(R.id.login_btn).setOnClickListener(v -> {
            if (isInvalidEmail(editEmail)) return;
            String email = editEmail.getText().toString();
            String password = editPassword.getText().toString();
            mProgressDialog.setMessage("Logging In.....");
            mProgressDialog.show();
            mAuthViewModel.signIn(email, password);
        });
    }

    private void initRecoverPassword() {
        findViewById(R.id.recoverPass).setOnClickListener(v -> showRecoverPasswordDialog());
    }

    private void initRegisterUser() {
        findViewById(R.id.no_account).setOnClickListener(v -> goToRegisterActivity());
    }

    private void getGoogleAuthCredential(GoogleSignInAccount googleSignInAccount) {
        String googleTokenId = googleSignInAccount.getIdToken();
        AuthCredential googleAuthCredential = GoogleAuthProvider.getCredential(googleTokenId, null);
        mAuthViewModel.signIn(googleAuthCredential);
    }

    private void showRecoverPasswordDialog() {
        //Alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //layout
        LinearLayout linearLayout = new LinearLayout(this);
        //views
        final EditText emailEt = new EditText(this);
        emailEt.setHint("Email");
        emailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        emailEt.setMinEms(16);

        linearLayout.addView(emailEt);
        linearLayout.setPadding(10, 10, 10, 10);

        builder.setView(linearLayout);

        //button
        builder.setPositiveButton("Recover", (dialog, which) -> {
            if (isInvalidEmail(emailEt)) return;
            String email = emailEt.getText().toString().trim();
            beginRecovery(email);
        });
        //button to cancel
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        //show dialog
        builder.create().show();
    }

    private void beginRecovery(String email) {
        //show progress dialog
        mProgressDialog.setMessage("Sending email....");
        mProgressDialog.show();
        mAuthViewModel.recoverPassword(email);
    }

    private boolean isInvalidEmail(EditText editEmail) {
        if (!Patterns.EMAIL_ADDRESS.matcher(editEmail.getText().toString().trim()).matches()) {
            editEmail.setError("Invalid Email");
            editEmail.setFocusable(true);
            return true;
        }
        return false;
    }

    private void goToMainActivity(User authenticatedUser) {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        startActivity(new Intent(AuthActivity.this, DashboardActivity.class));
        finish();
    }

    private void goToRegisterActivity() {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        startActivity(new Intent(AuthActivity.this, RegisterActivity.class));
        finish();
    }

    private void handleFailure(Failure failure) {
        mProgressDialog.dismiss();
        Toast.makeText(AuthActivity.this, failure.getMessage(),
                Toast.LENGTH_LONG).show();
    }
}
