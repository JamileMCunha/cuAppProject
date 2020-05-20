package com.cuApp.features.dashboard.chat;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cuApp.R;
import com.cuApp.core.platform.BaseFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

public class DialogsFragment extends BaseFragment implements
        DialogsListAdapter.OnDialogClickListener<DialogModel>,
        DialogsListAdapter.OnDialogLongClickListener<DialogModel> {

    private DialogsListAdapter<DialogModel> mDialogsListAdapter;
    private DialogViewModel dialogViewModel;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAdapter(view);
        initNewButton(view);

        dialogViewModel = new ViewModelProvider(this).get(DialogViewModel.class);
        dialogViewModel.observe(mDialogsListAdapter);
    }

    private void initAdapter(View view) {
        mDialogsListAdapter = new DialogsListAdapter<>((imageView, url, payload) -> Picasso.get().load(url).into(imageView));
        mDialogsListAdapter.setOnDialogClickListener(this);
        mDialogsListAdapter.setOnDialogLongClickListener(this);

        DialogsList dialogsList = view.findViewById(R.id.dialogsList);
        dialogsList.setAdapter(mDialogsListAdapter);
    }

    @SuppressWarnings("ConstantConditions")
    private void initNewButton(View view) {
        FloatingActionButton fb = view.findViewById(R.id.newDialog);
        fb.setOnClickListener(v -> {
            Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.dialog_list_user);

            ListUserAdapter adapter = new ListUserAdapter(getContext());
            RecyclerView recyclerView = dialog.findViewById(R.id.list_users);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(false);

            adapter.setOnItemClickListener(user -> {
                dialogViewModel.createDialog(user, this::onDialogClick);
                dialog.dismiss();
            });

            dialog.show();
        });
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_dialogs;
    }

    @Override
    public void onDialogClick(DialogModel dialog) {
        MessagesFragment fragment = new MessagesFragment(dialog);
        startFragment(fragment);
    }

    @Override
    public void onDialogLongClick(DialogModel dialog) {
        Toast.makeText(getContext(), dialog.getDialogName(), Toast.LENGTH_SHORT).show();
    }
}
