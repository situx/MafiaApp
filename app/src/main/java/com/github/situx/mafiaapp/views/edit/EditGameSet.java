package com.github.situx.mafiaapp.views.edit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.github.situx.mafiaapp.R;
import com.github.situx.mafiaapp.cards.GameSet;
import com.github.situx.mafiaapp.cards.Karte;
import com.github.situx.mafiaapp.views.ViewUtils;

import java.util.List;

/**
 * Created by timo on 12.02.14.
 */
public class EditGameSet extends ViewUtils {
    private static final int REARRANGE_CARDS = 1;
    private static EditGameSet instance;
    private static final int FILE_CHOOSER = 0;
    private GameSet gameset;
    private Integer gamesetid;
    private boolean turn;

    public EditGameSet(){
        if(EditGameSet.instance==null){
            this.turn=false;
            EditGameSet.instance=this;
        }else{
            this.gameset=EditGameSet.instance.gameset;
            this.turn=true;
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.editgameset);
        this.gameset=this.getIntent().getExtras().getParcelable("gameset");
        this.gamesetid=this.getIntent().getExtras().getInt("gamesetid");
        EditText editName=(EditText)this.findViewById(R.id.editGameSetName);
        EditText editFromPlayers=(EditText) this.findViewById(R.id.editGameSetMinPlayers);
        EditText editToPlayers=(EditText) this.findViewById(R.id.editGameSetMaxPlayers);
        EditText editIntroTitle=(EditText)this.findViewById(R.id.editGameSetIntroTitle);
        EditText editIntroText=(EditText)this.findViewById(R.id.editGameSetIntroText);
        EditText editOutroTitle=(EditText)this.findViewById(R.id.editGameSetOutroTitle);
        EditText editOutroText=(EditText)this.findViewById(R.id.editGameSetOutroText);
        ImageView gamesetImg=(ImageView)this.findViewById(R.id.editGameSetImgView);
        ImageView backImg=(ImageView)this.findViewById(R.id.editGameSetBackImgView);
        CheckBox checkBox=(CheckBox)this.findViewById(R.id.editGameSetHasBalance);

        int test = this.getResources().getIdentifier(this.gameset.getGamesetImg().toLowerCase().replace(' ','_'), "drawable", this.getPackageName());
        if (test != 0){
            gamesetImg.setImageResource(test);
        }else{
            gamesetImg.setImageResource(R.drawable.title);
        }
        test = this.getResources().getIdentifier(this.gameset.getBackImg().toLowerCase().replace(' ','_'), "drawable", this.getPackageName());
        if (test != 0){
            backImg.setImageResource(test);
        }else{
            backImg.setImageResource(R.drawable.back);
        }
        editName.setText(gameset.getTitle());
        editName.setOnFocusChangeListener((view, b) -> {
            if(!b)
                EditGameSet.this.gameset.setTitle(((EditText)view).getText().toString());
        });

        editFromPlayers.setText(gameset.getFromPlayers().toString());
        editFromPlayers.setOnFocusChangeListener((view, b) -> {
            if(!b)
                EditGameSet.this.gameset.setFromPlayers(Integer.valueOf(((EditText)view).getText().toString()));
        });

        editToPlayers.setText(gameset.getToPlayers().toString());
        editToPlayers.setOnFocusChangeListener((view, b) -> {
            if(!b)
                EditGameSet.this.gameset.setToPlayers(Integer.valueOf(((EditText)view).getText().toString()));
        });

        editIntroTitle.setText(gameset.getIntrotitle());
        editIntroTitle.setOnFocusChangeListener((view, b) -> {
            if(!b)
                EditGameSet.this.gameset.setIntroTitle(((EditText)view).getText().toString());
        });

        editIntroText.setText(gameset.getIntrotext());
        editIntroText.setOnFocusChangeListener((view, b) -> {
            if(!b)
                EditGameSet.this.gameset.setIntroText(((EditText)view).getText().toString());
        });

        editOutroTitle.setText(gameset.getOutrotitle());
        editOutroTitle.setOnFocusChangeListener((view, b) -> {
            if(!b)
                EditGameSet.this.gameset.setOutroTitle(((EditText)view).getText().toString());
        });

        editOutroText.setText(gameset.getOutrotext());
        editOutroText.setOnFocusChangeListener((view, b) -> {
            if(!b)
                EditGameSet.this.gameset.setOutroText(((EditText)view).getText().toString());
        });
        checkBox.setChecked(this.gameset.getHasBalance());
        checkBox.setOnCheckedChangeListener((compoundButton, b) -> EditGameSet.this.gameset.setHasBalance(b));

        gamesetImg.setOnClickListener(view -> {
            Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
            fileIntent.setType("file/*"); // intent type to filter application based on your requirement
            startActivityForResult(fileIntent, FILE_CHOOSER);
        });

        backImg.setOnClickListener(view -> {
            Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
            fileIntent.setType("file/*"); // intent type to filter application based on your requirement
            startActivityForResult(fileIntent, FILE_CHOOSER);
        });

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if(resultCode==RESULT_OK) {
            switch (requestCode) {
                case REARRANGE_CARDS:
                    List<Karte> newcardlist = data.getExtras().getParcelableArrayList("cardlist");
                    int i = 0;
                    Log.e("Save Positions", newcardlist.toString());
                    for (Karte card : newcardlist) {
                        Log.e(card.getName(), "is now Position " + i);
                        card.setPosition(i++);
                    }
                    this.gameset.setCards(newcardlist);
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        Bundle conData = new Bundle();
        conData.putParcelable("gameset", this.gameset);
        conData.putInt("gamesetid",this.gamesetid);
        Intent intent = new Intent();
        intent.putExtras(conData);
        setResult(RESULT_OK, intent);
        this.finish();
    }

}
