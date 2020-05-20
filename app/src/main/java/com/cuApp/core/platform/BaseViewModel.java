package com.cuApp.core.platform;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cuApp.core.exception.Failure;

public abstract class BaseViewModel extends ViewModel {

    private MutableLiveData<Failure> failure = new MutableLiveData<>();

    protected void handleFailure(Failure failure) {
        this.failure.setValue(failure);
    }

    public LiveData<Failure> getFailure() {
        return failure;
    }
}
