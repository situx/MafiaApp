package com.example.LNDWA.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.LNDWA.R;
import com.example.LNDWA.cards.Action;
import com.example.LNDWA.cards.Karte;
import com.example.LNDWA.util.Tuple;
import com.example.LNDWA.util.Utils;

import java.util.*;

/**
 * Created by timo on 29.01.14.
 */
public class PositionAdapter extends BaseAdapter {

        private final List<Karte> cardlist;
        private final List<Tuple<Action,Karte>> actionmap=new LinkedList<Tuple<Action,Karte>>();
        private final Map<Integer,PositionHolder> listviews;
        private final Context context;
        private String language,gamesetid;

        private Integer currentactioncounter=0,actionoverhead=0;

        public PositionAdapter(final Context context,final List<Karte> cards,String gamesetid,String language) {
            this.cardlist=cards;
            this.context=context;
            this.language=language;
            this.gamesetid=gamesetid;
            this.listviews=new TreeMap<>();
            int i=0;
            for(Karte card:cards){
                if(card.getActionlist().isEmpty()){
                    Action action=new Action();
                    action.setPosition(card.getPosition());
                    this.actionmap.add(new Tuple<Action, Karte>(action, card));
                    this.listviews.put(i++, new PositionHolder());
                }else{
                    for(Action action:card.getActionlist().values()){
                        this.actionmap.add(new Tuple<Action,Karte>(action,card));
                        this.listviews.put(i++, new PositionHolder());
                    }
                }
            }
            Collections.sort(this.actionmap, new Comparator<Tuple<Action,Karte>>() {
                public int compare(Tuple<Action,Karte> s1, Tuple<Action,Karte> s2) {
                    return s1.getOne().getPosition().compareTo(s2.getOne().getPosition());
                }
            });
        }

    public List<Tuple<Action, Karte>> getActionMap() {
        return this.actionmap;
    }


    @Override
        public int getCount() {
            return this.actionmap.size();
        }

    @Override
    public Object getItem(final int i) {
        return this.actionmap.get(i);
    }


    @Override
    public long getItemId(final int i) {
        return i;
    }

    public class PositionHolder{
        public ImageView imageView;
        TextView textView;
        View view;
        public int position;
    }



    @Override
    public View getView(final int pos, View view, final ViewGroup viewGroup) {
        final PositionHolder holder=this.listviews.get(pos);
        final Karte current=this.actionmap.get(pos).getTwo();
        final Action currentaction=this.actionmap.get(pos).getOne();
        if(holder.textView==null){
            LayoutInflater inflater=LayoutInflater.from(this.context);
            view=inflater.inflate(R.layout.item_position,viewGroup,false);
            if(current.getRound()==-1){
                view.setBackgroundColor(Color.DKGRAY);
            }else{
                view.setBackgroundColor(Color.TRANSPARENT);
            }
            holder.textView=(TextView)view.findViewById(R.id.positionTextView);

            holder.imageView=(ImageView)view.findViewById(R.id.positionImageView);

            holder.imageView.setAdjustViewBounds(true);
            view.setId(pos);
            holder.view=view;

            holder.position=pos;

        }else{
            view=holder.view;
        }
        holder.textView.setText(current.getName()+"("+currentaction.toString()+")");
        if(current.imgexists()){
            holder.imageView.setImageDrawable(Utils.loadDrawable(this.context.getFilesDir().getAbsolutePath(),language+"_"+gamesetid,current.getCardid()+".png"));
        }else{
            holder.imageView.setImageResource(R.drawable.title);
        }

        return view;
    }

}
