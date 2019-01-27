package com.github.situx.mafiaapp.views.edit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.github.situx.mafiaapp.R;
import com.github.situx.mafiaapp.cards.Player;
import com.github.situx.mafiaapp.views.ViewUtils;

import static android.app.Activity.RESULT_OK;

/**
 * Created by timo on 03.02.14.
 */
public class EditPlayer extends ViewUtils {

    private Player player;

    public EditPlayer(){

    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.editplayer);
        if(this.getIntent().hasExtra("playerid")) {
            final Integer playerid = this.getIntent().getExtras().getInt("playerid");
            this.setTitle(R.string.addplayer);
        }
        else{
            this.setTitle(R.string.editplayer);
        }
        if(this.getIntent().hasExtra("player"))
            this.player=this.getIntent().getExtras().getParcelable("player");
        else
            this.player=new Player();
        EditText playerName=(EditText) this.findViewById(R.id.editPlayerName);
        playerName.setText(this.player.getName());
        playerName.setOnFocusChangeListener((view, b) -> {
            if(!b){
                EditPlayer.this.player.setName(((EditText)view).getText().toString());
            }
        });
        EditText playerFirstName=(EditText) this.findViewById(R.id.editPlayerFirstName);
        playerFirstName.setText(this.player.getFirstname());
        playerFirstName.setOnFocusChangeListener((view, b) -> {
            if(!b){
                EditPlayer.this.player.setFirstname(((EditText)view).getText().toString());
            }
        });
        Button savePlayerButton=(Button) this.findViewById(R.id.savePlayerButton);
        savePlayerButton.setOnClickListener(view -> EditPlayer.this.finish());
    }

    @Override
    public void onBackPressed() {
        super.finish();
    }

    @Override
    public void finish()
    {
        Bundle conData = new Bundle();
        conData.putParcelable("player", this.player);
        Intent intent = new Intent();
        intent.putExtras(conData);
        setResult(RESULT_OK, intent);
        super.finish();
    }
}
