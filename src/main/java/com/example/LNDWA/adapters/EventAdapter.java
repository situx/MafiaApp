package com.example.LNDWA.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.LNDWA.R;
import com.example.LNDWA.cards.Event;
import com.example.LNDWA.views.edit.EditEvent;

import java.util.List;

/**
 * Created by timo on 06.02.14.
 */
public class EventAdapter extends BaseAdapter {
    /**The list of events to display.*/
    private List<Event> events;
    /**The icon of the gameset to display.*/
    private String icon;
    /**The context of this adapter.*/
    private Context context;

    /**
     * Constructor for this class.
     * @param context The context of this adapter
     * @param events The events of this adapter
     * @param icon the icon of the GameSet of this adapter
     */
    public EventAdapter(final Context context,final List<Event> events,String icon){
        this.events=events;
        this.context=context;
        this.icon=icon;
    }

    @Override
    public int getCount() {
        return this.events.size();
    }

    @Override
    public Object getItem(final int i) {
        return this.events.get(i);
    }

    @Override
    public long getItemId(final int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        final EventHolder holder;
        if(view==null){
            holder=new EventHolder();
            LayoutInflater inflater=LayoutInflater.from(this.context);
            view=inflater.inflate(R.layout.item_event,viewGroup,false);
            holder.activeCheckBox=(CheckBox)view.findViewById(R.id.eventActivecheckBox);
            holder.titleTextview=(TextView)view.findViewById(R.id.eventItemTitleTextView);
            holder.descriptionTextview=(TextView)view.findViewById(R.id.eventItemDescriptionTitleTextView);
            holder.editEventButton=(Button)view.findViewById(R.id.editEventButton);
            holder.view=view;
            view.setTag(holder);
        }else{
            holder=(EventHolder)view.getTag();
        }
        holder.descriptionTextview.setText(Html.fromHtml(this.events.get(i).getDescription()));
        holder.activeCheckBox.setChecked(this.events.get(i).getActive());
        holder.titleTextview.setText(this.events.get(i).getTitle());
        holder.editEventButton.setId(i);
        holder.editEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Intent intent = new Intent(EventAdapter.this.context, EditEvent.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                Bundle mBundle = new Bundle();
                mBundle.putParcelable("event",EventAdapter.this.events.get(view.getId()));
                mBundle.putString("icon",EventAdapter.this.icon);
                intent.putExtras(mBundle);
                EventAdapter.this.context.startActivity(intent);
            }
        });
        return view;
    }

    /**
     * Holder class for this Adapter.
     */
    private class EventHolder{
        TextView titleTextview;
        Button editEventButton;
        TextView descriptionTextview;
        CheckBox activeCheckBox;
        View view;
    }
}
