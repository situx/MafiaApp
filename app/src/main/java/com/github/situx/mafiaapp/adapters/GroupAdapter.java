package com.github.situx.mafiaapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.github.situx.mafiaapp.R;
import com.github.situx.mafiaapp.cards.Group;
import com.github.situx.mafiaapp.views.edit.EditGroup;
import com.github.situx.mafiaapp.views.edit.EditPlayer;

import java.util.*;

/**
 * Created by timo on 06.04.14.
 */
public class GroupAdapter extends BaseAdapter implements SpinnerAdapter{
    /**The context of this class.*/
    private final Context context;
    /**The list of players for this adapter.*/
    private final List<Group> groupList;
    private View.OnClickListener edit;

    private Boolean editOrChoose=true;

    public GroupAdapter(final Context context,final List<Group> groupList,final Boolean editOrChoose){
        this(context,groupList);
        this.editOrChoose=editOrChoose;
    }

    /**
     * Constructor for this class.
     * @param context the context of this adapter
     * @param groupList the list of players_config
     */
    public GroupAdapter(final Context context, final List<Group> groupList){
        this.context=context;
        this.groupList=groupList;
        Collections.sort(this.groupList, (p1, p2) -> (p1.getGroupname()).compareTo(p2.getGroupname()));
        this.edit=(View view)-> {
            final Intent intent = new Intent(GroupAdapter.this.context, EditPlayer.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            Bundle mBundle = new Bundle();
            mBundle.putParcelable("group", GroupAdapter.this.groupList.get(view.getId()));
            intent.putExtras(mBundle);
            GroupAdapter.this.context.startActivity(intent);
        };
        //Collections.sort(this.playerList, Collections.reverseOrder());
    }

    @Override
    public View getDropDownView(final int position, View view, final ViewGroup viewGroup) {
        final GroupListAdapterHolder holder;
        Group current=this.groupList.get(position);
        if(view==null){
            holder=new GroupListAdapterHolder();
            LayoutInflater inflater= LayoutInflater.from(this.context);
            view = inflater.inflate(R.layout.item_group,viewGroup,false);
            holder.textView=(TextView)view.findViewById(R.id.groupTextView);
            holder.editButton=(Button)view.findViewById(R.id.editGroupButton);
            holder.imageView=(ImageView)view.findViewById(R.id.groupImageView);
            holder.deleteButton=(Button)view.findViewById(R.id.removeGroupButton);
            holder.position=position;
            holder.view=view;
            holder.view.setId(position);
            view.setTag(holder);
        }else{
            holder=(GroupListAdapterHolder)view.getTag();
        }
        holder.textView.setText(current.getGroupname() + "("+current.getGroupdescription()+")");
        if(current.imgexists()){
            /*holder.imageView.setImageDrawable(GroupAdapter.this.context.getResources()
                    .getDrawable(GroupAdapter.this.context.getResources()
                            .getIdentifier(current.getGroupIcon(), "drawable", GroupAdapter.this.context.getPackageName())));*/
        }else{
            holder.imageView.setImageResource(R.drawable.heart);
        }

        holder.editButton.setOnClickListener(view12 -> {
            final Intent intent = new Intent(GroupAdapter.this.context, EditGroup.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            Bundle mBundle = new Bundle();
            mBundle.putParcelable("group", GroupAdapter.this.groupList.get(position));
            intent.putExtras(mBundle);
            GroupAdapter.this.context.startActivity(intent);
        });

        holder.deleteButton.setOnClickListener(view1 -> {
            final Intent intent = new Intent(GroupAdapter.this.context, EditGroup.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            Bundle mBundle = new Bundle();
            mBundle.putParcelable("group", GroupAdapter.this.groupList.get(position));
            intent.putExtras(mBundle);
            GroupAdapter.this.context.startActivity(intent);
        });
        if(!editOrChoose){
            holder.editButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void registerDataSetObserver(final DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(final DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return this.groupList.size();
    }

    @Override
    public Object getItem(final int i) {
        return this.groupList.get(i);
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
    public class GroupListAdapterHolder{
        ImageView imageView;
        TextView textView;
        Button editButton;
        Button deleteButton;
        View view;
        int position;
    }

    @Override
    public View getView(final int position, View view, final ViewGroup viewGroup) {
        GroupListAdapterHolder holder;
        Group current=this.groupList.get(position);
        if(view==null){
            holder=new GroupListAdapterHolder();
            LayoutInflater inflater= LayoutInflater.from(this.context);
            view = inflater.inflate(R.layout.item_group,viewGroup,false);
            holder.textView=(TextView)view.findViewById(R.id.groupTextView);
            holder.editButton=(Button)view.findViewById(R.id.editGroupButton);
            holder.deleteButton=(Button)view.findViewById(R.id.removeGroupButton);
            holder.imageView=(ImageView)view.findViewById(R.id.groupImageView);
            holder.position=position;
            holder.view=view;
            holder.view.setId(position);
            view.setTag(holder);
        }else{
            holder=(GroupListAdapterHolder)view.getTag();
        }
        holder.textView.setText(current.getGroupname() + "("+current.getGroupdescription()+")");
        if(current.imgexists()){
            /*holder.imageView.setImageDrawable(GroupAdapter.this.context.getResources()
                    .getDrawable(GroupAdapter.this.context.getResources()
                            .getIdentifier(current.getGroupIcon(), "drawable", GroupAdapter.this.context.getPackageName())));*/
        }else{
            holder.imageView.setImageResource(R.drawable.heart);
        }

        holder.editButton.setOnClickListener(view1 -> {
            final Intent intent = new Intent(GroupAdapter.this.context, EditGroup.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            Bundle mBundle = new Bundle();
            mBundle.putParcelable("group", GroupAdapter.this.groupList.get(position));
            intent.putExtras(mBundle);
            GroupAdapter.this.context.startActivity(intent);
        });

        holder.deleteButton.setOnClickListener(view12 -> {
            final Intent intent = new Intent(GroupAdapter.this.context, EditGroup.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            Bundle mBundle = new Bundle();
            mBundle.putParcelable("group", GroupAdapter.this.groupList.get(position));
            intent.putExtras(mBundle);
            GroupAdapter.this.context.startActivity(intent);
        });
        if(!editOrChoose){
            holder.editButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public int getItemViewType(final int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return this.groupList.size()==0?1:this.groupList.size();
    }

    @Override
    public boolean isEmpty() {
        return this.groupList.isEmpty();
    }
}
