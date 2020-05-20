package com.cuApp.features.auth;

import com.cuApp.R;
import com.cuApp.core.exception.Failure;

class AuthFailure extends Failure {

    public static final AuthFailure INSTANCE = new AuthFailure();

    private AuthFailure() {
        super(R.string.auth_failure);
    }
}
