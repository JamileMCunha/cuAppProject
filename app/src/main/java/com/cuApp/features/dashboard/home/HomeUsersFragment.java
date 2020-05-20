package com.cuApp.features.dashboard.home;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cuApp.R;
import com.cuApp.core.platform.BaseFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeUsersFragment extends BaseFragment {

    private HomeUserViewModel homeUserViewModel;

    @Override
    protected int layoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeUserViewModel = new ViewModelProvider(this).get(HomeUserViewModel.class);
        initList(view);
    }

    private void initList(View view) {
        UserAdapter userAdapter = new UserAdapter(getContext());
        RecyclerView recyclerView = view.findViewById(R.id.users_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(userAdapter);

        homeUserViewModel.observe(userAdapter);
    }

}

