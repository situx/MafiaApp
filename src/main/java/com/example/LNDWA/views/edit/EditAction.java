package com.example.LNDWA.views.edit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import com.example.LNDWA.R;
import com.example.LNDWA.cards.Action;
import com.example.LNDWA.views.ViewUtils;

/**
 * Created by timo on 26.04.14.
 */
public class EditAction extends ViewUtils {

    private Action action;

    public EditAction(){

    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.editaction);
        this.action=this.getIntent().getExtras().getParcelable("action");
        EditText editGameMaster=(EditText)this.findViewById(R.id.editActionGameMaster);
        EditText editPlayer=(EditText)this.findViewById(R.id.editActionPlayer);
        EditText editRound=(EditText)this.findViewById(R.id.editActionRound);
        EditText editPosition=(EditText)this.findViewById(R.id.editActionPosition);
        EditText editTitle=(EditText)this.findViewById(R.id.editActionTitle);
        final CheckBox ondead=(CheckBox)this.findViewById(R.id.editActionOnDeadCheckBox);
        editPosition.setText(action.getPosition().toString());
        editPosition.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View view, final boolean b) {
                if (!b) {
                    action.setPosition(Integer.valueOf(((EditText) view).getText().toString()));
                }
            }
        });
        editTitle.setText(action.getTitle());
        editTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View view, final boolean b) {
                if (!b) {
                    action.setTitle(((EditText) view).getText().toString());
                }
            }
        });
        editGameMaster.setText(action.getGamemaster());
        editGameMaster.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View view, final boolean b) {
                if (!b) {
                    action.setGamemaster(((EditText) view).getText().toString());
                }
            }
        });
        editPlayer.setText(action.getPlayer());
        editPlayer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View view, final boolean b) {
                if (!b) {
                    action.setPlayer(((EditText) view).getText().toString());
                }
            }
        });
        editRound.setText(action.getRound().toString());
        editRound.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View view, final boolean b) {
                if (!b) {
                    action.setRound(Integer.valueOf(((EditText) view).getText().toString()));
                }
            }
        });
        ondead.setChecked(action.getOndead());
        ondead.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {
                ondead.setChecked(b);
                action.setOndead(b);
            }
        });

    }
    @Override
    public void finish()
    {
        Bundle conData = new Bundle();
        conData.putParcelable("action", this.action);
        Intent intent = this.getIntent();
        intent.putExtras(conData);
        setResult(RESULT_OK, intent);
        super.finish();
    }


}
