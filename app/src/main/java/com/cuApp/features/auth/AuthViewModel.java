package com.cuApp.features.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cuApp.R;
import com.cuApp.core.exception.Failure;
import com.cuApp.core.platform.BaseViewModel;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

public class AuthViewModel extends BaseViewModel {

    private MutableLiveData<User> user;
    private MutableLiveData<Integer> recoverMessage;

    private AuthRepository repository;

    public AuthViewModel() {
        user = new MutableLiveData<>();
        repository = new AuthRepository();
    }

    void signIn(AuthCredential credential) {
        repository.signIn(credential)
                .thenAccept(user::setValue)
                .exceptionally(this::handleException);
    }

    void signIn(String email, String password) {
        repository.signIn(email, password)
                .thenAccept(user::setValue)
                .exceptionally(this::handleException);
    }

    void recoverPassword(String email) {
        repository.recoveryPassword(email)
                .thenAccept(v -> recoverMessage.setValue(R.string.send_email_success))
                .exceptionally(this::handleException);
    }

    LiveData<User> getUser() {
        return user;
    }

    private Void handleException(Throwable t) {
        Throwable cause = t.getCause();
        if (cause instanceof FirebaseAuthInvalidCredentialsException) {
            handleFailure(AuthFailure.INSTANCE);
            return null;
        }
        handleFailure(Failure.NETWORK);
        return null;
    }
}
