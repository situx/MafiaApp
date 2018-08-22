package com.example.LNDWA.adapters;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.LNDWA.cards.Player;
import com.example.LNDWA.R;
import com.example.LNDWA.cards.Karte;
import com.example.LNDWA.cards.Game;
import com.example.LNDWA.util.Utils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: timo
 * Date: 25.11.13
 * Time: 18:20
 * To change this template use File | Settings | File Templates.
 */
public class ResultListAdapter extends BaseAdapter {
    /**The context of this adapter.*/
    private final Context context;
    private final String gamesetid;
    private final String language;
    private final List<Player> players;
    /**The uuid of this game.*/
    private final String gameUUID;
    /**The cards winning this game.*/
    private List<Karte> winningcards;
    private final List<StatHolder> listviews;
    private ListView listv;
    private View.OnClickListener onButtonClick;
    private View.OnClickListener onButtonClick2;
    private AdapterView.OnItemSelectedListener itemselect;
    /**The values to put into the spinner.*/
    private List<String> spinnervalues;
    /**The players_config who won this game.*/
    private List<Player> winners;

    /**
     * Constructor for this class.
     * @param context the context of this adapter
     * @param players the playerlist to choose from
     * @param winningcards the cards winning the game
     * @param winninggroup the winning group
     * @param lview
     */
    public ResultListAdapter(final Context context, final List<Player> players, final List<Karte> winningcards,String winninggroup,ListView lview,List<Player> winners,String language,String gamesetid) {
        this.players=players;
        this.gameUUID = UUID.randomUUID().toString();
        this.context=context;
        this.listv=lview;
        this.winners=winners;
        this.winningcards=winningcards;
        this.language=language;
        this.gamesetid=gamesetid;
        this.listviews=new LinkedList<>();
        this.spinnervalues=new LinkedList<>();
        for(Karte card:this.winningcards){
                this.listviews.add(new StatHolder());
                this.spinnervalues.add("");
        }
        this.onButtonClick=new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                int points;
                if(winningcards.get(view.getId()).isdead()){
                    points=winningcards.get(view.getId()).getWinningDead();
                }else{
                    points=winningcards.get(view.getId()).getWinningAlive();
                }
                ResultListAdapter.this.players.get(view.getId()).addGame(new Game(ResultListAdapter.this.listviews.get(view.getId()).textView.getText().toString(),points,gameUUID));
                if(winningcards.get(view.getId()).getCurrentamount()>1){
                    winningcards.get(view.getId()).setCurrentamount(winningcards.get(view.getId()).getCurrentamount()-1);
                    ResultListAdapter.this.listviews.get(view.getId()).textView.setText(winningcards.get(view.getId()).getName()+
                            "\n"+ResultListAdapter.this.context.getResources().getString(R.string.amount)+" "+winningcards.get(view.getId()).getCurrentamount()
                            +"\n"+ResultListAdapter.this.context.getResources().getString(R.string.points)+" "+points);
                }else{
                    ResultListAdapter.this.listviews.get(view.getId()).view.setVisibility(View.GONE);
                    ResultListAdapter.this.listv.invalidateViews();
                }
            }
        };
        this.onButtonClick2=new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                int points;
                if(winningcards.get(view.getId()).isdead()){
                    points=winningcards.get(view.getId()).getWinningDead();
                }else{
                    points=winningcards.get(view.getId()).getWinningAlive();
                }
                ResultListAdapter.this.players.get(view.getId()).addGame(new Game(ResultListAdapter.this.listviews.get(view.getId()).textView.getText().toString(),points,gameUUID));
                if(winningcards.get(view.getId()).getCurrentamount()>1){
                    winningcards.get(view.getId()).setCurrentamount(winningcards.get(view.getId()).getCurrentamount()-1);
                    ResultListAdapter.this.listviews.get(view.getId()).textView.setText(winningcards.get(view.getId()).getName()+
                            "\n"+ResultListAdapter.this.context.getResources().getString(R.string.amount)+" "+winningcards.get(view.getId()).getCurrentamount()
                            +"\n"+ResultListAdapter.this.context.getResources().getString(R.string.points)+" "+points);
                }else{
                    ResultListAdapter.this.listviews.get(view.getId()).view.setVisibility(View.GONE);
                    ResultListAdapter.this.listv.invalidateViews();
                }
            }
        };
        this.itemselect=new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int pos, final long l) {
                ResultListAdapter.this.spinnervalues.set(pos,parent.getItemAtPosition(pos).toString());
            }

            @Override
            public void onNothingSelected(final AdapterView<?> adapterView) {

            }
        };

    }

    @Override
    public int getCount() {
        return this.winningcards.size();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object getItem(final int i) {
        return this.winningcards.get(i);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public long getItemId(final int i) {
        return i;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Holder class.
     */
    private class StatHolder{
        TextView textView;
        ImageView imageView;
        Button button;
        Button button2;
        View view;
        Spinner spinner;
        Integer position;
    }
    
    @Override
    public View getView(final int position, View view, final ViewGroup parent) {
        StatHolder holder=this.listviews.get(position);
        if(this.listviews.get(position).button==null){
            final Karte current=this.winningcards.get(position);
            LayoutInflater inflater= LayoutInflater.from(this.context);
            view = inflater.inflate(R.layout.item_result, parent, false);
            holder.position=position;
            holder.imageView=(ImageView)view.findViewById(R.id.statImageView);
            File file=new File(this.context.getFilesDir().getAbsolutePath()+"/"+this.language+"_"+this.gamesetid+"/"+current.getCardid()+".png");
            if(current.imgexists() && !current.isdead()){
                holder.imageView.setImageDrawable(Utils.loadDrawable(this.context.getFilesDir().getAbsolutePath(), this.language + "_" + this.gamesetid, current.getCardid() + ".png"));
            }else if(current.imgexists() && current.isdead()){
                holder.imageView.setImageBitmap(Utils.toGrayscale(((BitmapDrawable)Utils.loadDrawable(this.context.getFilesDir().getAbsolutePath(), this.language + "_" + this.gamesetid, current.getCardid() + ".png")).getBitmap()));
            }else{
                holder.imageView.setImageResource(R.drawable.title);
            }
            holder.textView=(TextView)view.findViewById(R.id.statTextView);
            if(current.isdead()){
                holder.textView.setText(current.getName()+"\n"+this.context.getResources().getString(R.string.amount)
                        +" "+current.getCurrentamount()+"\n"+
                        ResultListAdapter.this.context.getResources().getString(R.string.points)+" "+current.getWinningDead());
            }else{
                holder.textView.setText(current.getName()+"\n"+this.context.getResources().getString(R.string.amount)
                        +" "+current.getCurrentamount()+"\n"+
                        ResultListAdapter.this.context.getResources().getString(R.string.points)+" "+current.getWinningAlive());
            }
            holder.spinner=(Spinner)view.findViewById(R.id.statSpinner);
            holder.spinner.setAdapter(new PlayerSpinnerAdapter(this.context, this.winners,position));
            holder.spinner.setId(position);
            holder.spinner.setOnItemSelectedListener(this.itemselect);
            holder.spinner.setSelection(position);
            holder.button=(Button)view.findViewById(R.id.statButton);
            holder.button.setId(holder.position);
            holder.button.setOnClickListener(this.onButtonClick);
            holder.button2=(Button)view.findViewById(R.id.statButton2);
            holder.button2.setId(holder.position);
            holder.button2.setOnClickListener(this.onButtonClick2);
            holder.view=view;

        }else if(this.listviews.get(position).view.getVisibility()==View.GONE){
            LayoutInflater inflater= LayoutInflater.from(this.context);
            view = inflater.inflate(R.layout.item_null, null);
        }else{
           view=holder.view;
        }
        return view;
    }
}
