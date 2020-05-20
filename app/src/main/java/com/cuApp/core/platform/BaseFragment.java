package com.cuApp.core.platform;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cuApp.MainActivity;
import com.cuApp.R;
import com.cuApp.features.auth.AuthContext;
import com.google.firebase.auth.FirebaseAuth;

public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(layoutId(), container, false);
    }

    protected abstract int layoutId();

    protected void onBackPressed() {}

    protected void startFragment(BaseFragment fragment) {
        if (!(getActivity() instanceof BaseActivity)) {
            return;
        }
        BaseActivity activity = (BaseActivity) getActivity();
        activity.changeFragment(fragment);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout){
            FirebaseAuth.getInstance().signOut();
            AuthContext.uninstall();
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
