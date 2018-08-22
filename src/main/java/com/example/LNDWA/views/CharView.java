package com.example.LNDWA.views;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.*;
import com.example.LNDWA.R;
import com.example.LNDWA.adapters.AbilityAdapter;
import com.example.LNDWA.cards.Karte;
import com.example.LNDWA.util.Utils;
import com.example.LNDWA.views.edit.EditChar;

import java.io.File;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: timo
 * Date: 21.11.13
 * Time: 15:01
 * To change this template use File | Settings | File Templates.
 */
public class CharView extends ViewUtils {

    private static final int EDIT_CHAR = 0;
    /**The card to display.*/
    private Karte card;
    /**The sourcefile of the gameset and its icon.*/
    private String sourcefile,icon;

    private ArrayList<Karte> cardlist;

    private TextView descriptionView,detailTextView,roundAbilityTextView;
    /**The image view to use in this View.*/
    private ImageView charViewImg;

    private ListView abblistView;

    private Button editCharButton;

    private static CharView instance;

    /**
     * Constructor of this class.
     */
    public CharView(){
        if(CharView.instance==null){
            CharView.instance=this;
            this.turn=false;
        }else{

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.viewchar);
        if(!turn){
            this.card=this.getIntent().getExtras().getParcelable("card");
            this.sourcefile=this.getIntent().getExtras().getString("sourcefile");
            this.icon=this.getIntent().getExtras().getString("icon");
            this.cardlist=this.getIntent().getExtras().getParcelableArrayList("cardlist");
            File file=new File(this.getFilesDir().getAbsolutePath()+sourcefile+"/"+card.getCardid() + ".png");
            if (file.exists()) {
                this.getSupportActionBar().setIcon(Utils.loadDrawable(this.getFilesDir().getAbsolutePath(),sourcefile, card.getCardid() + ".png"));
            } else {
                this.getSupportActionBar().setIcon(R.drawable.title);
            }
        }
            this.charViewImg=(ImageView)this.findViewById(R.id.charViewImg);
            this.descriptionView=(TextView)this.findViewById(R.id.charViewDescription);
            this.detailTextView=(TextView)this.findViewById(R.id.charViewDetailText);
            this.abblistView=(ListView)this.findViewById(R.id.charViewAbblist);
            this.roundAbilityTextView=(TextView)this.findViewById(R.id.charViewRoundAbility);
            this.editCharButton=(Button)this.findViewById(R.id.charViewEditCharBTN);
            this.setTitle(this.card.getName());
            if(!turn)
                this.setViewData();

    }

    /**
     * Sets the viewing data of this class.
     */
    void setViewData(){
        this.descriptionView.setText(Html.fromHtml(this.card.getDescription()));
        int round=this.card.getRound();
        String detailstring=this.getResources().getString(R.string.charactername)+": "+this.card.getName()+"\n"+
                this.getResources().getString(R.string.group)+": "
                +this.card.getGroup()+"\n"+this.getResources().getString(R.string.extracards)
                +this.card.getExtra()+"\n"+this.getResources().getString(R.string.minamount)+": "
                +this.card.getMinAmount()+"\n"+this.getResources().getString(R.string.maxamount)+": ";
        if(this.card.getMaxAmount()!=-1){
            detailstring+=this.card.getMaxAmount()+"\n"+this.getResources().getString(R.string.call)+" ";
        }else{
            detailstring+=this.getResources().getString(R.string.unbounded)+"\n"+this.getResources().getString(R.string.call)+" ";
        }
        if(round==0){
            detailstring+=this.getResources().getString(R.string.everyRound);
        }else if(round==-1){
            detailstring+=this.getResources().getString(R.string.noround);
        }else if(round>0){
            detailstring+=this.getResources().getString(R.string.xroundview1)+" "+round+this.getResources().getString(R.string.xroundview2);
        }else if(round<-1){
            detailstring+=this.getResources().getString(R.string.xroundview3)+" "+(round*-1)+this.getResources().getString(R.string.xroundview4);
        }
        this.detailTextView.setText(detailstring);

        if(this.card.imgexists()){

            Drawable drawable = Utils.loadDrawable(this.getFilesDir().getAbsolutePath(),sourcefile, card.getCardid() + ".png");
            this.charViewImg.setImageDrawable(drawable);
        }
        if(!this.card.getabblist().isEmpty()){

            this.roundAbilityTextView.setText(R.string.abilities);

            this.abblistView.setAdapter(new AbilityAdapter(this,this.card.getabblist(),R.layout.abilitycharview));
        }
        if(this.getIntent().getExtras().getBoolean("edit")){
            this.editCharButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    final Intent intent = new Intent(CharView.this,EditChar.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    Bundle mBundle = new Bundle();
                    mBundle.putParcelable("card", CharView.this.card);
                    mBundle.putString("sourcefile", CharView.this.sourcefile);
                    mBundle.putString("icon", CharView.this.icon);
                    mBundle.putParcelableArrayList("cardlist",CharView.this.cardlist);
                    intent.putExtras(mBundle);
                    CharView.this.startActivityForResult(intent, EDIT_CHAR);
                }
            });
        }else{
            this.editCharButton.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==EDIT_CHAR && resultCode==RESULT_OK){
             this.card=data.getExtras().getParcelable("card");
             this.setViewData();
        }
    }

    @Override
    public void finish()
    {
        Bundle conData = new Bundle();
        conData.putParcelable("card", this.card);
        Intent intent = new Intent();
        intent.putExtras(conData);
        setResult(RESULT_OK, intent);
        super.finish();
    }
}
