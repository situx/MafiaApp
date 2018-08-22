package com.example.LNDWA.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.LNDWA.R;
import com.example.LNDWA.cards.Game;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by timo on 15.01.14.
 */
public class GameAdapter extends BaseAdapter{
    /**The context of this adapter.*/
    private final Context context;
    /**The list of games for this adapter.*/
    private final List<Game> gameList;
    /**The list of holder classes for this adapter.*/
    private List<GameAdapterHolder> listviews;

    /**
     * Constructor for this class.
     * @param context the context of this adapter
     * @param gameList the list of games
     */
    public GameAdapter(final Context context, final List<Game> gameList){
        this.context=context;
        this.gameList=gameList;
        this.listviews=new LinkedList<>();
        for(Game game:gameList){
            listviews.add(new GameAdapterHolder());
        }
        Collections.sort(this.gameList, new Comparator<Game>() {
            public int compare(Game p1, Game p2) {
                return p2.getGameid().compareTo(p1.getGameid());
            }
        });
    }


    @Override
    public int getCount() {
        return this.gameList.size();
    }

    @Override
    public Object getItem(final int i) {
        return this.gameList.get(i);
    }

    @Override
    public long getItemId(final int i) {
        return i;
    }

    public class GameAdapterHolder{
        TextView textView;
        View view;
        int position;
    }

    @Override
    public View getView(final int position, View view, final ViewGroup viewGroup) {
        final GameAdapterHolder holder=this.listviews.get(position);
        if(this.listviews.get(position).textView==null){
            LayoutInflater inflater= LayoutInflater.from(this.context);
            view = inflater.inflate(R.layout.item_game,viewGroup,false);
            holder.textView=(TextView)view.findViewById(R.id.gameTextView);
            holder.textView.setText(this.context.getResources().getString(R.string.game)+this.gameList.get(position).getGameid()+": "+this.gameList.get(position).getCharacter()+" "+this.context.getResources().getString(R.string.points)+this.gameList.get(position).getPoints());
            holder.position=position;
            holder.view=view;
        }else{
            view=holder.view;
        }
        return view;
    }
}
