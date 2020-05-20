package com.cuApp.core.exception;

import androidx.annotation.StringRes;

import com.cuApp.R;

import java.io.Serializable;

public abstract class Failure implements Serializable {

    private int message;

    public Failure(@StringRes int message) {
        this.message = message;
    }

    public int getMessage() {
        return message;
    }

    public boolean is(Class<? extends Failure> failureClass) {
        return getClass().equals(failureClass);
    }

    private static class Network extends Failure {
        private Network() {
            super(R.string.network_failure);
        }
    }

    public static final Failure NETWORK = new Network();
}
