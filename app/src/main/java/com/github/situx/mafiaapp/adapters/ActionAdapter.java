package com.github.situx.mafiaapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.github.situx.mafiaapp.R;
import com.github.situx.mafiaapp.cards.Action;
import com.github.situx.mafiaapp.views.edit.EditAction;
import com.github.situx.mafiaapp.views.edit.EditChar;

import java.util.List;
import java.util.Map;

/**
 * Created by timo on 29.04.14.
 */
public class ActionAdapter extends BaseAdapter {

    private Map<String, Action> actionList;

    private Context context;

    public ActionAdapter(Map<String,Action> actions,final EditChar context){
        this.actionList=actions;
        this.context=context;
    }

    @Override
    public int getCount() {
        return actionList.size();
    }

    @Override
    public Object getItem(final int i) {
        return this.actionList.get(i);
    }

    @Override
    public long getItemId(final int i) {
        return 0;
    }

    @Override
    public View getView(final int i,  View view, final ViewGroup viewGroup) {
        ActionAdapterHolder holder;
        if(view==null){
            LayoutInflater inflater=LayoutInflater.from(this.context);
            view=inflater.inflate(R.layout.item_action,viewGroup,false);
            holder=new ActionAdapterHolder();
            holder.editActionButton=(Button)view.findViewById(R.id.editActionButton);
            holder.removeActionButton=(Button)view.findViewById(R.id.deleteActionButton);
            holder.showAction=(TextView)view.findViewById(R.id.charViewRoundAction);
            view.setTag(holder);
        }else{
           holder=(ActionAdapterHolder)view.getTag();
        }
        holder.showAction.setText(this.actionList.get("1").getTitle());
        holder.editActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Intent intent = new Intent(ActionAdapter.this.context, EditAction.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                Bundle mBundle = new Bundle();
                mBundle.putParcelable("action", ActionAdapter.this.actionList.get(i+""));
                intent.putExtras(mBundle);
                ActionAdapter.this.context.startActivity(intent);
            }
        });
        return view;
    }

    class ActionAdapterHolder{
         TextView showAction;
         Button editActionButton;
         Button removeActionButton;
    }
}
