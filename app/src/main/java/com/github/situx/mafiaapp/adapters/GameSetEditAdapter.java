package com.github.situx.mafiaapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.situx.mafiaapp.R;
import com.github.situx.mafiaapp.cards.GameSet;
import com.github.situx.mafiaapp.util.Utils;
import com.github.situx.mafiaapp.views.ChooseChars;
import com.github.situx.mafiaapp.views.ViewEvents;
import com.github.situx.mafiaapp.views.ViewGameSet;
import com.github.situx.mafiaapp.views.ViewGroups;
import com.github.situx.mafiaapp.views.edit.EditGameSet;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by timo on 07.02.14.
 */
public class GameSetEditAdapter extends BaseAdapter{

    private static final int EDIT_CHAR = 0;
    private static final int EDIT_EVENT = 2;
    private static final int EDIT_GAMESET = 1;
    private static final int EDIT_GROUP = 3;

        /**The context of this adapter class.*/
        private final ViewGameSet context;
        /**The list of GameSets to display.*/
        private List<GameSet> setList;

    /**
     * Constructor for this class.
     * @param context the context of this class
     * @param setList the list of sets to display
     */
        public GameSetEditAdapter(final ViewGameSet context, final List<GameSet> setList){
            this.setList=setList;
            this.context=context;
        }

        @Override
        public int getCount() {
            return this.setList.size();
        }

        @Override
        public Object getItem(final int i) {
            return this.setList.get(i);
        }

        @Override
        public long getItemId(final int i) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(final int i, View view, final ViewGroup viewGroup) {
            GameSetEditAdapterHolder holder;
            if(view==null){
                holder=new GameSetEditAdapterHolder();
                LayoutInflater inflater= LayoutInflater.from(this.context);
                view = inflater.inflate(R.layout.item_gameset, viewGroup, false);
                holder.setImageView= (ImageView)view.findViewById(R.id.setImageView);
                holder.setTextView=(TextView)view.findViewById(R.id.setTextView);

                holder.editCharsButton=(Button)view.findViewById(R.id.gameSetEditCharsButton);
                holder.editEventsButton=(Button)view.findViewById(R.id.gameSetEditEventsButton);
                holder.editGroupsButton=(Button)view.findViewById(R.id.gameSetEditGroupsButton);
                holder.setImageView.setId(i);

                holder.setView=view;
                view.setTag(holder);
            }else{
                holder=(GameSetEditAdapterHolder)view.getTag();
                view=holder.setView;
                view.setTag(holder);
            }
            holder.setTextView.setText(this.setList.get(i).getTitle());
            boolean test = new File(context.getFilesDir()+"/chars/" + this.setList.get(i).getLanguage()+"_"+this.setList.get(i).getGamesetid()+"/"+this.setList.get(i).getLanguage()+"_"+this.setList.get(i).getGamesetid()+".png").exists();
            Log.e("File exists?", context.getFilesDir() + "/chars/" + this.setList.get(i).getLanguage()+"_"+this.setList.get(i).getGamesetid() + "/" + this.setList.get(i).getLanguage()+"_"+this.setList.get(i).getGamesetid()+".png");
            if (test)
                holder.setImageView.setImageDrawable(Utils.loadDrawable(this.context.getFilesDir().getAbsolutePath(),this.setList.get(i).getLanguage()+"_"+this.setList.get(i).getGamesetid(), this.setList.get(i).getLanguage()+"_"+this.setList.get(i).getGamesetid() + ".png"));
            else{
                holder.setImageView.setImageResource(R.drawable.title);
            }
            holder.setImageView.setOnClickListener(view1 -> {
                final Intent intent = new Intent(GameSetEditAdapter.this.context, EditGameSet.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                Bundle mBundle = new Bundle();
                mBundle.putParcelable("gameset", GameSetEditAdapter.this.setList.get(i));
                mBundle.putInt("gamesetid", view1.getId());
                intent.putExtras(mBundle);
                GameSetEditAdapter.this.context.startActivityForResult(intent,EDIT_GAMESET);
            });

            holder.editCharsButton.setId(i);
            holder.editCharsButton.setOnClickListener(view12 -> {
                final Intent intent = new Intent(GameSetEditAdapter.this.context, ChooseChars.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                Bundle mBundle = new Bundle();
                mBundle.putParcelable("gameset", GameSetEditAdapter.this.setList.get(view12.getId()));
                mBundle.putInt("gamesetid", view12.getId());
                mBundle.putString("chars",GameSetEditAdapter.this.setList.get(i).getSourcefile());
                mBundle.putString("icon",GameSetEditAdapter.this.setList.get(i).getGamesetImg());
                mBundle.putBoolean("editchars",true);
                intent.putExtras(mBundle);
                GameSetEditAdapter.this.context.startActivityForResult(intent,EDIT_CHAR);
            });

            holder.editEventsButton.setId(i);
            holder.editEventsButton.setOnClickListener(view13 -> {
                final Intent intent = new Intent(GameSetEditAdapter.this.context, ViewEvents.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                Bundle mBundle = new Bundle();
                mBundle.putParcelableArrayList("events", new ArrayList<>(GameSetEditAdapter.this.setList.get(i).getEvents()));
                mBundle.putString("icon",GameSetEditAdapter.this.setList.get(i).getGamesetImg());
                mBundle.putInt("gamesetid", view13.getId());
                intent.putExtras(mBundle);
                GameSetEditAdapter.this.context.startActivityForResult(intent,EDIT_EVENT);
            });

            holder.editGroupsButton.setId(i);
            holder.editGroupsButton.setOnClickListener(view14 -> {
                final Intent intent = new Intent(GameSetEditAdapter.this.context, ViewGroups.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                Bundle mBundle = new Bundle();
                mBundle.putParcelableArrayList("groups", new ArrayList<>(GameSetEditAdapter.this.setList.get(i).getGroups()));
                mBundle.putString("icon",GameSetEditAdapter.this.setList.get(i).getGamesetImg());
                mBundle.putInt("gamesetid", view14.getId());
                intent.putExtras(mBundle);
                GameSetEditAdapter.this.context.startActivityForResult(intent,EDIT_GROUP);
            });

            return view;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public int getItemViewType(final int i) {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return this.setList.isEmpty();
        }

    /**
     * HOlder class.
     */
        private class GameSetEditAdapterHolder{
          Button editCharsButton;
          Button editEventsButton;
          Button editGroupsButton;
          TextView setTextView;
          ImageView setImageView;
          View setView;
    }



}
