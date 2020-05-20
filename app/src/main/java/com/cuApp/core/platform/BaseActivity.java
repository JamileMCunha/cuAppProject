package com.cuApp.core.platform;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cuApp.R;

import java.util.Deque;
import java.util.LinkedList;

public abstract class BaseActivity extends AppCompatActivity {

    private Deque<BaseFragment> deque = new LinkedList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);
    }

    @Override
    public void onBackPressed() {
        BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.content);
        if (fragment != null) {
            fragment.onBackPressed();
        }
        BaseFragment last = deque.pollLast();
        if (last != null) {
            replaceFragment(last);
        }
    }

    protected void changeFragment(BaseFragment fragment) {
        BaseFragment current = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.content);
        deque.offer(current);
        replaceFragment(fragment);
    }

    private void replaceFragment(BaseFragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }
}
