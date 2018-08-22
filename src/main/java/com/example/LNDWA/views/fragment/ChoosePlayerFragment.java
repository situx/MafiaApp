package com.example.LNDWA.views.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.example.LNDWA.R;
import com.example.LNDWA.adapters.PlayerListAdapter;
import com.example.LNDWA.cards.Player;
import com.example.LNDWA.views.Round2;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by timo on 30.04.14.
 */
public class ChoosePlayerFragment extends DialogFragment {

    Dialog dialog;

    List<Player> playerarray;

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        outState.putParcelableArrayList("playerarray", new ArrayList<Player>(this.playerarray));
    }


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if(savedInstanceState!=null)
    }

    public void pickChoice(Integer choice){
        ((Round2) ChoosePlayerFragment.this.getActivity()).currentfragment.setPlayerDialog(choice);
        ChoosePlayerFragment.this.playerarray=null;
        this.dialog.dismiss();
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        if(savedInstanceState!=null && savedInstanceState.containsKey("playerarray") && savedInstanceState.get("playerarray")!=null){
            this.dialog=new Dialog(this.getActivity());
            dialog.setTitle(R.string.pick_player);
            dialog.setContentView(R.layout.dialogchooseplayer);
            this.playerarray=savedInstanceState.getParcelableArrayList("playerarray");
            final ListView listview=(ListView)dialog.findViewById(R.id.personList);
            final PlayerListAdapter adapter=new PlayerListAdapter(this.getActivity(),this,this.playerarray , 2);
            listview.setAdapter(adapter);
            listview.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(final AdapterView<?> adapterView, final View view, final int i, final long l) {

                }

                @Override
                public void onNothingSelected(final AdapterView<?> adapterView) {

                }
            });
            final EditText edit=(EditText)dialog.findViewById(R.id.searchPerson);
            // Capture Text in EditText
            edit.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1,
                                              int arg2, int arg3) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                    String text = edit.getText().toString().toLowerCase(Locale.getDefault());
                    adapter.filter(text);
                    listview.setAdapter(adapter);
                }
            });
            Button cancel=(Button) dialog.findViewById(R.id.confirmplayer);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }else{
            this.dialog=new Dialog(this.getActivity());
            dialog.setContentView(R.layout.dialogchooseplayer);
            this.playerarray=((Round2) ChoosePlayerFragment.this.getActivity()).playerarray;
            ListView listview=(ListView)dialog.findViewById(R.id.personList);
            final PlayerListAdapter adapter=new PlayerListAdapter(this.getActivity(),this,this.playerarray , 2);
            listview.setAdapter(adapter);
            final EditText edit=(EditText)dialog.findViewById(R.id.searchPerson);
            // Capture Text in EditText
            edit.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub
                    String text = edit.getText().toString().toLowerCase(Locale.getDefault());
                    adapter.filter(text);
                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1,
                                              int arg2, int arg3) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                    // TODO Auto-generated method stub
                }
            });
            Button cancel=(Button) dialog.findViewById(R.id.confirmplayer);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        return dialog;


    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();
    }


}
