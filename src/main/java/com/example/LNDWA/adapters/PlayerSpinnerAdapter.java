package com.example.LNDWA.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.example.LNDWA.R;
import com.example.LNDWA.cards.Player;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: timo
 * Date: 27.11.13
 * Time: 01:47
 * To change this template use File | Settings | File Templates.
 */
public class PlayerSpinnerAdapter implements SpinnerAdapter {
    /**The context of this adapter.*/
    private final Context context;
    /**The list of players_config.*/
    private final List<Player> playerList;
    private final List<PlayerHolder> listviews;
    private final Boolean statview;

    /**
     * Constructor for this class.
     * @param context the context to choose from
     * @param playerList the list of players_config to display
     */
    public PlayerSpinnerAdapter(final Context context, final List<Player> playerList,Integer position){
        this.playerList=playerList;
        this.context=context;
        this.statview=false;
        this.listviews=new LinkedList<>();
        for(Player pl:this.playerList){
            this.listviews.add(new PlayerHolder());
        }
    }

    @Override
    public View getDropDownView(final int i, View view, final ViewGroup viewGroup) {
        PlayerHolder holder=listviews.get(i);
        if(holder.textView==null){
            LayoutInflater inflater= LayoutInflater.from(this.context);
            view = inflater.inflate(com.example.LNDWA.R.layout.item_player, viewGroup, false);
            holder.textView=(TextView)view.findViewById(R.id.playerTextView);
            if(!statview){
                holder.textView.setText(this.playerList.get(i).getFirstname()+" "+this.playerList.get(i).getName());
            }else{
                holder.textView.setText(this.playerList.get(i).getFirstname()+" "+this.playerList.get(i).getName()+"\n"+this.context.getResources().getString(R.string.points));
            }
            holder.view=view;
        }else{
            view=holder.view;
        }
        return view;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void registerDataSetObserver(final DataSetObserver dataSetObserver) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void unregisterDataSetObserver(final DataSetObserver dataSetObserver) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getCount() {
        return this.playerList.size();
    }

    @Override
    public Object getItem(final int i) {
        return this.playerList.get(i);
    }

    @Override
    public long getItemId(final int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(final int i,  View view, final ViewGroup viewGroup) {
        PlayerHolder holder=listviews.get(i);
        if(holder.textView==null){
            LayoutInflater inflater= LayoutInflater.from(this.context);
            view = inflater.inflate(R.layout.item_player, viewGroup, false);
            holder.textView=(TextView)view.findViewById(R.id.playerTextView);
            holder.textView.setText(this.playerList.get(i).getFirstname()+" "+this.playerList.get(i).getName());
            holder.view=view;
        }else{
            view=holder.view;
        }
        return view;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getItemViewType(final int i) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getViewTypeCount() {
        return this.playerList.size();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isEmpty() {
        return this.playerList.isEmpty();  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Holder class.
     */
    class PlayerHolder{
        TextView textView;
        View view;
    }
}
