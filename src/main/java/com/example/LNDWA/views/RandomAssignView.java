package com.example.LNDWA.views;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.LNDWA.R;
import com.example.LNDWA.cards.Karte;
import com.example.LNDWA.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by timo on 19.05.14.
 */
public class RandomAssignView extends ViewUtils {

    private String gamesetid,language;

    private List<Karte> cardlist;

    private List<String> playerlist;

    private ImageView imageView;

    private Intent intent;

    private Button button;

    private TextView textView;

    private View.OnClickListener onclick;

    private View.OnLongClickListener onlongclick;

    private Integer index=-1;

    private Boolean shuffle;

    public RandomAssignView(){

    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        outState.putInt("index",this.index);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onNewIntent(final Intent intent) {
        this.index=-1;
        this.intent=intent;
        Collections.shuffle(this.cardlist,new Random(System.currentTimeMillis()));
        this.cardlist=intent.getExtras().getParcelableArrayList("cardlist");
        this.playerlist=intent.getExtras().getStringArrayList("playerlist");
        this.gamesetid=intent.getExtras().getString("gamesetid");
        this.language=intent.getExtras().getString("language");
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            this.index=savedInstanceState.getInt("index");
        }else{
            this.intent=this.getIntent();
        }
        this.setContentView(R.layout.randomassign);
        this.button=(Button)this.findViewById(R.id.nextRandomRole);
        this.cardlist=this.intent.getExtras().getParcelableArrayList("cardlist");
        this.playerlist=intent.getExtras().getStringArrayList("playerlist");
        this.gamesetid=this.intent.getExtras().getString("gamesetid");
        this.language=this.intent.getExtras().getString("language");
        this.shuffle=this.intent.getExtras().getBoolean("shuffle");
        this.setTitle(this.getResources().getString(R.string.randomassign));
        this.getSupportActionBar().setIcon(Utils.loadDrawable(this.getFilesDir().getAbsolutePath(),this.language+"_"+this.gamesetid,this.language+"_"+this.gamesetid+".png"));
        if(shuffle)
            Collections.shuffle(this.cardlist,new Random(System.currentTimeMillis()));
        this.textView=(TextView)this.findViewById(R.id.cardNameView);
        textView.setText(RandomAssignView.this.getResources().getString(R.string.passon));
        this.imageView=(ImageView)this.findViewById(R.id.randomimageView);
        this.onclick=new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if(RandomAssignView.this.imageView.isShown()) {
                    RandomAssignView.this.imageView.setVisibility(View.INVISIBLE);
                    RandomAssignView.this.textView.setText(RandomAssignView.this.getResources().getString(R.string.passon));
                    RandomAssignView.this.button.setOnClickListener(null);
                    RandomAssignView.this.button.setOnLongClickListener(RandomAssignView.this.onlongclick);
                }
                if(index>=RandomAssignView.this.cardlist.size()){
                    RandomAssignView.this.finish();
                }
            }
        };
        this.onlongclick=new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                if(index<RandomAssignView.this.cardlist.size()-1){
                    RandomAssignView.this.imageView.setImageDrawable(Utils.loadDrawable(RandomAssignView.this.getFilesDir().getAbsolutePath(),RandomAssignView.this.language+"_"+RandomAssignView.this.gamesetid,RandomAssignView.this.cardlist.get(++index).getCardid()+".png"));
                    textView.setText(RandomAssignView.this.cardlist.get(index).getName()+" ("+RandomAssignView.this.cardlist.get(index).getGroup().getGroupIdentifier()+")");
                    RandomAssignView.this.button.setText(RandomAssignView.this.getString(R.string.nextcard));
                    RandomAssignView.this.setTitle(RandomAssignView.this.playerlist.get(index));
                    RandomAssignView.this.imageView.setVisibility(View.VISIBLE);
                }else{
                    RandomAssignView.this.button.setText(RandomAssignView.this.getString(R.string.finish));
                    textView.setText(RandomAssignView.this.getResources().getString(R.string.selectingfinished));
                    index++;
                }
                RandomAssignView.this.button.setOnLongClickListener(null);
                RandomAssignView.this.button.setOnClickListener(RandomAssignView.this.onclick);
                return true;
            }
        };
        button.setText(this.getResources().getString(R.string.start));
        button.setOnLongClickListener(onlongclick);

    }

    @Override
    public void finish() {
        Bundle conData = new Bundle();
        conData.putParcelableArrayList("cardlist", new ArrayList<Karte>(this.cardlist));
        conData.putBoolean("shuffle", this.shuffle);
        Intent intent = this.getIntent();
        intent.putExtras(conData);
        setResult(RESULT_OK, intent);
        super.finish();
    }
}
