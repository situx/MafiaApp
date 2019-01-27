package com.github.situx.mafiaapp.views.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import com.github.situx.mafiaapp.R;
import com.github.situx.mafiaapp.util.Utils;

/**
 * Created by timo on 30.04.14.
 */
public class AboutDialogFragment extends DialogFragment{

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this.getActivity());
        builder.setMessage(Utils.dialogtext)
                .setCancelable(true)
                .setIcon(this.getResources().getDrawable(R.drawable.title))
                .setTitle(this.getResources().getString(R.string.about))
                .setPositiveButton(this.getResources().getString(R.string.ok), (dialog, buttonid) -> dialog.cancel());
        return builder.create();
    }
}
