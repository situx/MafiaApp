package com.example.LNDWA.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.example.LNDWA.R;
import com.example.LNDWA.util.Utils;
import com.example.LNDWA.views.fragment.AboutDialogFragment;
import com.example.LNDWA.views.fragment.ChoosePlayerFragment;

/**
 * Created by timo on 10.03.14.
 * A class supporting View specific default implementations.
 */
public abstract class ViewUtils extends ActionBarActivity {
    /**Boolean indicating if the screen was turned.*/
    protected Boolean turn=false;

    /**
     * Constructor for this class.
     */
    public ViewUtils(){

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        menu.add(0,0,0,this.getResources().getString(R.string.about));
        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                Utils.dialogtext=ViewUtils.this.
                        getResources().getString(R.string.copyright);
                ViewUtils.this.chooseDialogs(DialogEnum.ABOUT);
                return true;
            }
        });
        return true;
    }

    public void chooseDialogs(DialogEnum denum){
        DialogFragment dialog;
        FragmentManager fm = getSupportFragmentManager();
        switch(denum){
            case ABOUT:dialog=new AboutDialogFragment();break;
          /*case CHOOSECHARS:break;
          case ABOUT:break;*/
            default: dialog=new ChoosePlayerFragment();
        }
        dialog.show(fm, denum.getName());
    }

    @Override
    protected Dialog onCreateDialog(final int id) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch(id){
            case 0:         builder.setMessage(Utils.dialogtext)
                    .setCancelable(true)
                    .setIcon(this.getResources().getDrawable(R.drawable.title))
                    .setTitle(this.getResources().getString(R.string.about))
                    .setPositiveButton(this.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int buttonid) {
                            dialog.cancel();

                        }
                    });break;
            default:
        }


        return builder.create();
    }

    @Override
    protected void onPrepareDialog(final int id, final Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        removeDialog(id);
    }

    @Override
    public void onBackPressed() {
        super.finish();
    }
}
