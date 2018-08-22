package com.example.LNDWA.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.LNDWA.R;
import com.example.LNDWA.cards.Player;
import com.example.LNDWA.cards.Game;
import com.example.LNDWA.views.GameStats;
import com.example.LNDWA.views.Round2;
import com.example.LNDWA.views.edit.EditPlayer;
import com.example.LNDWA.views.fragment.ChoosePlayerFragment;

import java.util.*;

/**
 * Created by timo on 14.01.14.
 */
public class PlayerListAdapter extends BaseAdapter implements AbstractFilterAdapter{
    private final View.OnClickListener choose;
    /**The context of this class.*/
    private final Context context;
    /**The list of players for this adapter.*/
    private final List<Player> playerList;

    private ChoosePlayerFragment fragment=null;

    private List<Player> filteredList;

    private List<PlayerListAdapterHolder> filteredlistviews;
    /**List of holders of this class.*/
    private List<PlayerListAdapterHolder> listviews;

    private View.OnClickListener edit,game;

    private Integer editOrGameStat=-1;


    /**
     * Constructor for this class.
     * @param context the context of this adapter
     * @param playerList the list of players
     * @param editOrGameStat edit flag
     */
    public PlayerListAdapter(final Context context,final ChoosePlayerFragment fragment, final List<Player> playerList,Integer editOrGameStat){
        this(context, playerList,2);
        this.editOrGameStat=editOrGameStat;
        this.fragment=fragment;
    }


    /**
     * Constructor for this class.
     * @param context the context of this adapter
     * @param playerList the list of players
     * @param editOrGameStat edit flag
     */
    public PlayerListAdapter(final Context context, final List<Player> playerList,Integer editOrGameStat){
        this(context, playerList);
        this.editOrGameStat=editOrGameStat;
    }

    /**
     * Constructor for this class.
     * @param context the context of this adapter
     * @param playerList the list of players_config
     */
    public PlayerListAdapter(final Context context, final List<Player> playerList){
        this.context=context;
        this.playerList=playerList;
        this.listviews=new LinkedList<>();

        if(editOrGameStat!=2) {
            Collections.sort(this.playerList, new Comparator<Player>() {
                public int compare(Player p1, Player p2) {
                    int result = p2.getTotal().compareTo(p1.getTotal());
                    if (result == 0) {
                        return (p1.getFirstname() + " " + p1.getName()).compareTo(p2.getFirstname() + " " + p2.getName());
                    }
                    return result;
                }
            });
        }else{
            Collections.sort(this.playerList, new Comparator<Player>() {
                public int compare(Player p1, Player p2) {
                        return (p1.getFirstname() + " " + p1.getName()).compareTo(p2.getFirstname() + " " + p2.getName());
                }
            });
        }
        this.filteredList=new LinkedList<>();
        this.filteredlistviews=new LinkedList<>();
        for(Player play:playerList){
            this.filteredList.add(new Player(play));
            listviews.add(new PlayerListAdapterHolder());
            this.filteredlistviews.add(new PlayerListAdapterHolder());
        }
        this.game=new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Intent intent = new Intent(PlayerListAdapter.this.context,GameStats.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                Bundle mBundle = new Bundle();
                mBundle.putString("name",PlayerListAdapter.this.playerList.get(view.getId()).getFirstname()+" "+PlayerListAdapter.this.playerList.get(view.getId()).getName());
                mBundle.putParcelableArrayList("games", new ArrayList<>(PlayerListAdapter.this.playerList.get(view.getId()).getGames()));
                intent.putExtras(mBundle);
                PlayerListAdapter.this.context.startActivity(intent);
            }
        };
        this.edit=new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Intent intent = new Intent(PlayerListAdapter.this.context,EditPlayer.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                Bundle mBundle = new Bundle();
                mBundle.putParcelable("player", PlayerListAdapter.this.playerList.get(view.getId()));
                intent.putExtras(mBundle);
                PlayerListAdapter.this.context.startActivity(intent);
            }
        };
        this.choose=new View.OnClickListener(){
            @Override
            public void onClick(final View view) {
                PlayerListAdapter.this.fragment.pickChoice(view.getId());
            }
        };

        //Collections.sort(this.playerList, Collections.reverseOrder());
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(final int i) {
        return true;
    }


    @Override
    public void registerDataSetObserver(final DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(final DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return this.filteredList.size();
    }

    @Override
    public Player getItem(final int i) {
        return this.filteredList.get(i);
    }

    @Override
    public long getItemId(final int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * The holder class for this adapter.
     */
    public class PlayerListAdapterHolder{
        TextView textView;
        View view;
        int position;
    }

    @Override
    public View getView(final int position, View view, final ViewGroup viewGroup) {
        PlayerListAdapterHolder holder=this.filteredlistviews.get(position);
        Log.e("FilteredList: ",this.filteredList.size()+"");
        final Player current=this.filteredList.get(position);
        Log.e("Current:  ",this.filteredList.get(position).toString());
        if(this.filteredlistviews.get(position).textView==null){
            LayoutInflater inflater= LayoutInflater.from(this.context);
            view = inflater.inflate(R.layout.item_player,viewGroup,false);
            holder.textView=(TextView)view.findViewById(R.id.playerTextView);
            if(editOrGameStat==2){
                holder.textView.setText(current.getFirstname() + " " + current.getName());
                holder.textView.setGravity(Gravity.CENTER);
                holder.textView.setTextSize(25);
            }else{
                holder.textView.setText((position + 1) + ". " + current.getFirstname() + " " + current.getName() + "\n" + this.context.getResources().getString(R.string.points) + current.getTotal());
            }
            holder.position=position;
            holder.view=view;
            holder.view.setId(position);
            if(this.editOrGameStat==0)
                holder.view.setOnClickListener(this.game);
            else if(this.editOrGameStat==1)
                holder.view.setOnClickListener(this.edit);
            else if(this.editOrGameStat==2){
                holder.view.setOnClickListener(this.choose);
            }
            //view.setTag(holder);
        }else{
            //holder=(PlayerListAdapterHolder)view.getTag();
            holder.textView.setText(current.getFirstname() + " " + current.getName());
            holder.textView.setGravity(Gravity.CENTER);
            holder.textView.setTextSize(25);
        }
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        this.filteredList.clear();
        this.filteredlistviews.clear();
        Log.e("FilteredList: ",filteredList.size()+"");
        Log.e("Chartext",charText+" "+charText.length());
        if (charText.length() == 0) {
            this.filteredList.addAll(this.playerList);
        } else {
            int i=0;
            for (Player wp : this.playerList) {
                /*Log.e("Filter",(wp.getFirstname().toLowerCase(Locale.getDefault()) + " " + wp.getName().toLowerCase(Locale.getDefault())) + " " + (wp.getFirstname().toLowerCase(Locale.getDefault()) + " " + wp.getName().toLowerCase(Locale.getDefault()))
                        .contains(charText));*/
                if ((wp.getFirstname().toLowerCase(Locale.getDefault())+" "+wp.getName().toLowerCase(Locale.getDefault()))
                        .contains(charText)) {
                    Log.e("Add to filteredList: ",wp.toString());
                    this.filteredList.add(wp);
                    this.filteredlistviews.add(listviews.get(i));
                    Log.e("Listview Content: ",listviews.get(i).toString());
                }
                i++;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(final int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return this.filteredList.size();
    }

    @Override
    public boolean isEmpty() {
        return this.filteredList.isEmpty();
    }

}
