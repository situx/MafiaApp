package com.example.LNDWA.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.LNDWA.R;
import com.example.LNDWA.cards.Ability;
import com.example.LNDWA.adapters.AbilityAdapter;
import com.example.LNDWA.cards.Event;
import com.example.LNDWA.cards.Karte;
import com.example.LNDWA.util.Utils;

import java.io.FileOutputStream;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: timo
 * Date: 21.11.13
 * Time: 16:53
 * To change this template use File | Settings | File Templates.
 */
public class Round extends ViewUtils{

     private ArrayList<Karte> cardlist;
     private List<Karte> position2list;
     private List<String> grouplist;
     private List<Event> events;
     private Map<String,Boolean> groupmap;
    private String icon;
    private Random random;

    private String winninggroup;

     private RoundHolder holder;

     private Integer counter=0,rounds=1;
     private boolean end=false,returntoChars=false, calleveryone =false;

    /**
     * Constructor for this class.
     */
     public Round(){
         this.random=new Random(System.currentTimeMillis());
         this.counter=0;
         this.cardlist=new ArrayList<>();
         this.position2list=new LinkedList<>();
         this.holder=new RoundHolder();
     }

    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        this.counter=0;
        Bundle bundle=intent.getExtras();
        this.rounds=bundle.getInt("rounds");
        this.cardlist= bundle.getParcelableArrayList("cards");
        this.grouplist=bundle.getStringArrayList("groups");
        this.groupmap=new TreeMap<String,Boolean>();
        this.winninggroup="";
        for(String group:this.grouplist){
           this.groupmap.put(group,false);
        }
        this.calleveryone =bundle.getBoolean("calleveryone");
        this.setTitle(this.getResources().getString(R.string.round)+" "+this.rounds);
        holder.textview.setText(Round.this.getResources().getString(R.string.intro) + "\n\n");
        holder.textview2.setText(Round.this.getResources().getString(R.string.introtext) + "\n\n");
        holder.imageview.setImageDrawable(Round.this.getResources().getDrawable(Round.this.getResources().getIdentifier(Round.this.icon,"drawable",Round.this.getPackageName())));

        this.position2list.clear();
        for(Karte card:this.cardlist){
           if(card.getPosition2()!=-1){
               this.position2list.add(card);
           }
        }
        Collections.sort(this.position2list, new Comparator<Karte>() {
            public int compare(Karte s1, Karte s2) {
                return s1.getPosition2().compareTo(s2.getPosition2());
            }
        });

    }

    private class RoundHolder{
        TextView textview;
        TextView textview2;
        TextView textview3;
        ImageView imageview;
        ListView listView;
        View view;
    }

    private String checkIfWon(){
        Integer counter=0;
        String lasttrue="";
        for(String group:this.groupmap.keySet()){
            if(this.groupmap.get(group)){
                lasttrue=group;
                counter++;
            }
        }
        if(counter==1){
            return lasttrue;
        }
        return null;
    }

    public void onCreate(Bundle b){
         super.onCreate(b);
         this.setContentView(R.layout.round);
         this.counter=0;
         Bundle bundle=this.getIntent().getExtras();
         this.rounds=bundle.getInt("rounds");
        this.grouplist=bundle.getStringArrayList("groups");
        this.events=bundle.getParcelableArrayList("events");
        this.icon=bundle.getString("icon");
        if(this.getResources().getIdentifier(this.icon,"drawable",this.getPackageName())!=0){
            this.getSupportActionBar().setIcon(this.getResources().getIdentifier(this.icon,"drawable",this.getPackageName()));
        }else{
            this.getSupportActionBar().setIcon(R.drawable.title);
        }
        this.groupmap=new TreeMap<String,Boolean>();
        for(String group:this.grouplist){
            this.groupmap.put(group,false);
        }
         this.setTitle(this.getResources().getString(R.string.round)+" "+this.rounds);
         this.cardlist= bundle.getParcelableArrayList("cards");
        this.position2list.clear();
        for(Karte card:this.cardlist){
            if(card.getPosition2()!=-1){
                this.position2list.add(card);
            }
        }
        Collections.sort(this.position2list, new Comparator<Karte>() {
            public int compare(Karte s1, Karte s2) {
                return s1.getPosition2().compareTo(s2.getPosition2());
            }
        });
         holder.textview = (TextView) Round.this.findViewById(R.id.roundTextView);
         holder.textview.setText(this.getResources().getString(R.string.intro) + "\n\n");
         holder.textview2 = (TextView) Round.this.findViewById(R.id.roundDescriptionTextView);
         holder.textview2.setText(this.getResources().getString(R.string.introtext) + "\n\n");
         holder.textview3=(TextView)Round.this.findViewById(R.id.charViewRoundAbility);
         holder.imageview = (ImageView) Round.this.findViewById(R.id.roundImageView);
         holder.imageview.setImageDrawable(Round.this.getResources().getDrawable(Round.this.getResources().getIdentifier(Round.this.icon,"drawable",Round.this.getPackageName())));
         holder.listView=(ListView)Round.this.findViewById(R.id.abbListView);
         holder.view = this.findViewById(R.id.test);
         holder.view.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(final View view) {

                 if (Round.this.counter == cardlist.size() && Round.this.position2list.isEmpty()) {
                     int lovers = 0, flute = 0, alive = 0;
                     for (Karte card : Round.this.cardlist) {
                         if (end) {
                             final Intent intent = new Intent(Round.this,Results.class);
                             intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                             Bundle mBundle = new Bundle();
                             mBundle.putParcelableArrayList("cards", Round.this.cardlist);
                             mBundle.putStringArrayList("winners", new ArrayList<String>());
                             mBundle.putBoolean("end", true);
                             mBundle.putString("winninggroup",winninggroup);
                             mBundle.putString("icon",icon);
                             intent.putExtras(mBundle);
                             Round.this.startActivity(intent);
                             Round.this.finish();
                             return;
                         }
                         if (!card.isdead()) {
                             alive++;
                             switch (card.getGroup().getGroupIdentifier()) {
                                 case "U": break;
                                 default:if(Round.this.groupmap.containsKey(card.getGroup()))
                                     Round.this.groupmap.put(card.getGroup().getGroupIdentifier(),true);
                             }
                         }
                         if (!card.isverzaubert()) {
                             flute++;
                         }
                     }
                     String temp;
                     if (flute == (alive - 1)) {
                         holder.textview.setText(Round.this.getResources().getString(R.string.flute) + " " + Round.this.getResources().getString(R.string.won) + "\n\n");
                         holder.textview2.setText(Html.fromHtml("<b>"+Round.this.getResources().getString(R.string.congrattext) + "</b>")+"\n");
                         holder.textview3.setText(" ");
                         holder.imageview.setImageDrawable(Round.this.getResources().getDrawable(Round.this.getResources().getIdentifier(Round.this.icon,"drawable",Round.this.getPackageName())));
                         Round.this.end=true;
                         Round.this.winninggroup="F";
                     }else if (lovers == 2 && Round.this.cardlist.size() == 2) {
                         holder.textview.setText(Round.this.getResources().getString(R.string.lovers) + " " + Round.this.getResources().getString(R.string.won) + "\n\n");
                         holder.textview2.setText(Round.this.getResources().getString(R.string.congrattext) + "\n\n");
                         holder.textview3.setText(" ");
                         holder.imageview.setImageDrawable(Round.this.getResources().getDrawable(Round.this.getResources().getIdentifier(Round.this.icon,"drawable",Round.this.getPackageName())));
                         Round.this.end=true;
                         Round.this.winninggroup="";
                     }

                     else if((temp=checkIfWon())!=null){
                         holder.textview.setText(Round.this.getResources().getString(getResources().getIdentifier(temp, "string", Round.this.getPackageName())) + " " + Round.this.getResources().getString(R.string.won) + "\n\n");
                         holder.textview2.setText(Round.this.getResources().getString(R.string.congrattext) + "\n\n");
                         holder.textview3.setText(" ");
                         holder.imageview.setImageDrawable(Round.this.getResources().getDrawable(Round.this.getResources().getIdentifier(Round.this.icon,"drawable",Round.this.getPackageName())));
                         Round.this.end=true;
                         Round.this.winninggroup=temp;
                     }else {
                         holder.textview.setText(Round.this.getResources().getString(R.string.outro) + "\n\n");
                         holder.textview3.setText(Round.this.getResources().getString(R.string.outrotext) + "\n\n");
                         int eveIndex=Round.this.random.nextInt()%Round.this.events.size();
                         if(eveIndex<0){
                             eveIndex*=-1;
                         }
                         Event event=Round.this.events.get(eveIndex);
                         holder.textview2.setText(Html.fromHtml(Round.this.getResources().getString(R.string.event))+"\n"+event.getTitle()+"\n"+Html.fromHtml(event.getDescription()));
                         holder.listView.setAdapter(new AbilityAdapter(Round.this,new TreeSet<Ability>(),R.layout.ability));
                         holder.imageview.setImageDrawable(Round.this.getResources().getDrawable(Round.this.getResources().getIdentifier(Round.this.icon,"drawable",Round.this.getPackageName())));
                         if(Round.this.returntoChars){
                             Round.this.returntoChars=false;
                             Round.this.counter=0;
                             final Intent intent = new Intent(Round.this, ChooseChars.class);
                             intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                             Bundle mBundle = new Bundle();
                             mBundle.putParcelableArrayList("cardlist", Round.this.cardlist);
                             mBundle.putString("icon",icon);
                             mBundle.putBoolean("new",false);
                             mBundle.putBoolean("end", false);
                             intent.putExtras(mBundle);
                             Round.this.startActivity(intent);

                         }else{
                             Round.this.returntoChars=true;
                         }

                     }
                 } else {
                     Karte currentcard;
                     if(Round.this.counter==Round.this.cardlist.size()){
                         //Round.this.setTitle(Round.this.getResources().getString(R.string.round)+" "+Round.this.rounds+" "+Round.this.position2list.size());
                         currentcard=Round.this.position2list.get(0);
                         Round.this.position2list.remove(0);
                         Round.this.counter--;
                     }else{
                         currentcard = Round.this.cardlist.get(Round.this.counter);
                     }
                     Log.e("beep", currentcard.getName());
                     if(currentcard.getRound()!=0)
                     Log.e("beep", Round.this.rounds % (currentcard.getRound() * -1)+"");
                     if ((currentcard.isdead() && !Round.this.calleveryone)
                             || (currentcard.getRound() != 0
                                     && !currentcard.getRound().equals(Round.this.rounds))
                             || (currentcard.getRound()<-1 && (Round.this.rounds%(currentcard.getRound()*-1))!=0)) {
                         Round.this.counter++;
                         onClick(holder.view);
                         return;
                     }else if(currentcard.getFixeddeath().equals(Round.this.rounds)){
                         currentcard.dead();
                     }
                     holder.textview.setText(Html.fromHtml(currentcard.getName()) + " " + Round.this.getResources().getString(R.string.awakens) + "\n");
                     holder.textview2.setText(Html.fromHtml(currentcard.getDescription()) + "\n\n");
                     String result=Html.fromHtml("<b>"+Round.this.getResources().getString(R.string.group)+"</b>")+": "+Round.this.getResources().getString(Round.this.getResources().getIdentifier(currentcard.getGroup().getGroupIdentifier(),"string",Round.this.getPackageName()))+"\n"+
                             Round.this.getResources().getString(R.string.protection)+": "+(currentcard.isprotected()?Round.this.getResources().getString(R.string.yes)+"von \n":Round.this.getResources().getString(R.string.no)+"\n")
                             +Round.this.getResources().getString(R.string.enchanted)+": "+(currentcard.isverzaubert()?Round.this.getResources().getString(R.string.yes):Round.this.getResources().getString(R.string.no));
                     holder.textview3.setText(result);
                     holder.listView.setAdapter(new AbilityAdapter(Round.this,currentcard.getabblist(),R.layout.ability));

                     if(currentcard.imgexists()){
                         Drawable drawable = Round.this.getResources().getDrawable(Round.this.getResources()
                                 .getIdentifier(currentcard.getImg(), "drawable", Round.this.getPackageName()));
                         holder.imageview.setImageDrawable(drawable);
                     }else{
                         holder.imageview.setMaxWidth(20);
                         holder.imageview.setMaxHeight(20);
                         holder.imageview.setImageResource(R.drawable.title);
                     }

                     Round.this.counter++;
                 }

             }
         });

     }

    public void saveGame(final String filepath){
        FileOutputStream outputStream;
        //FileOutputStream outputStream=this.openFileOutput(filepath,);

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        menu.add(0,0,0,this.getResources().getString(R.string.about));
        menu.add(1,0,0,this.getResources().getString(R.string.statistics));
        menu.add(2,0,0,this.getResources().getString(R.string.savegame));
        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                Utils.dialogtext=Round.this.
                        getResources().getString(R.string.copyright);
                Round.this.chooseDialogs(DialogEnum.ABOUT);
                return true;
            }
        });
        menu.getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                final Intent intent = new Intent(Round.this,ViewStats.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                Round.this.startActivity(intent);
                return true;
            }
        });
        menu.getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                Round.this.saveGame("");
                Utils.dialogtext=Round.this.
                        getResources().getString(R.string.savegame);
                Round.this.showDialog(1);
                return true;
            }
        });
        return true;
    }


    @Override
    protected Dialog onCreateDialog(final int id) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch(id){
            case 0:         builder.setMessage(Utils.dialogtext)
                    .setCancelable(true)
                    .setIcon(this.getResources().getDrawable(R.drawable.failure))
                    .setTitle(this.getResources().getString(R.string.startfailed))
                    .setPositiveButton(Round.this.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int buttonid) {
                            dialog.cancel();

                        }
                    });break;
            case 1:          builder.setMessage(Utils.dialogtext)
                    .setCancelable(true)
                    .setIcon(this.getResources().getDrawable(R.drawable.title))
                    .setTitle(this.getResources().getString(R.string.about))
                    .setPositiveButton(Round.this.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int buttonid) {
                            dialog.cancel();

                        }
                    });break;
            default:
        }


        return builder.create();
    }

}
