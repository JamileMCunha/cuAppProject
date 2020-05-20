package com.cuApp.features.auth;

import android.util.Log;

import com.cuApp.core.Constants;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.CompletableFuture;

class AuthRepository {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase rootRef = FirebaseDatabase.getInstance();
    private DatabaseReference usersRef = rootRef.getReference("Users");

    CompletableFuture<User> signIn(AuthCredential credential) {
        CompletableFuture<User> completableFuture = new CompletableFuture<>();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(authResult -> handle(completableFuture, authResult));
        return completableFuture;
    }

    CompletableFuture<User> signIn(String email, String password) {
        CompletableFuture<User> completableFuture = new CompletableFuture<>();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(authResult -> handle(completableFuture, authResult));
        return completableFuture;
    }

    CompletableFuture<Void> recoveryPassword(String email) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                completableFuture.complete(null);
                return;
            }
            completableFuture.completeExceptionally(task.getException());
        });
        return completableFuture;
    }

    private void handle(CompletableFuture<User> completableFuture, Task<AuthResult> authResult) {
        if (authResult.isSuccessful()) {
            boolean isNewUser = authResult.getResult().getAdditionalUserInfo().isNewUser();
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser != null) {
                String uid = firebaseUser.getUid();
                String name = firebaseUser.getDisplayName();
                String email = firebaseUser.getEmail();
                User user = User.of(uid, name, email);
                if (isNewUser) {
                    usersRef.child(user.getUid()).setValue(user);
                }
                AuthContext.install(user);
                completableFuture.complete(user);
            }
            return;
        }
        Exception ex = authResult.getException();
        if (ex != null) {
            Log.e(Constants.TAG, ex.getMessage(), ex);
        }
        completableFuture.completeExceptionally(ex);
    }
}
