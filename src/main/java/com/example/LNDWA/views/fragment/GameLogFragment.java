package com.example.LNDWA.views.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import com.example.LNDWA.R;

import java.util.List;

/**
 * Created by timo on 01.05.14.
 */
public class GameLogFragment extends DialogFragment {

    private String[] gameloglist;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
    }

    public GameLogFragment(String[] gameloglist) {
        this.gameloglist = gameloglist;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setItems(this.gameloglist, null)
                .setCancelable(true)
                .setTitle(this.getResources().getString(R.string.showgamelog))
                .setPositiveButton(this.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int buttonid) {
                        dialog.cancel();
                    }
                });
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();

    }
}
