package com.example.LNDWA.views.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import com.example.LNDWA.R;
import com.example.LNDWA.util.Utils;

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
                .setPositiveButton(this.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int buttonid) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }
}
