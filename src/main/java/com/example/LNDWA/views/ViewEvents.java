package com.example.LNDWA.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import com.example.LNDWA.R;
import com.example.LNDWA.adapters.EventAdapter;
import com.example.LNDWA.cards.Event;
import com.example.LNDWA.cards.Karte;
import com.example.LNDWA.views.edit.EditEvent;

import java.util.*;

/**
 * Created by timo on 06.02.14.
 * Views Events in a listview.
 */
public class ViewEvents extends ViewUtils{
    /**
     * Flag for calling the EditEvent activity in add mode.
     */
    private static final int ADD_EVENT = 0;
    /**The list of events to be displayed.*/
    private List<Event> eventlist;
    /**The highest event id.*/
    private Integer maxeventid;
    /**The icon of the current GameSet to display in the ActionBar.*/
    private String icon;

    private static ViewEvents instance;

    /**
     * Empty constructor for ViewEvents.
     */
    public ViewEvents(){
        if(ViewEvents.instance!=null){
            this.maxeventid=ViewEvents.instance.maxeventid;
            this.eventlist=ViewEvents.instance.eventlist;
            this.icon=ViewEvents.instance.icon;
            this.turn=true;
        }else{
            ViewEvents.instance=this;
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.viewevents);
        this.eventlist=this.getIntent().getExtras().getParcelableArrayList("events");
        this.icon=this.getIntent().getExtras().getString("icon");
        if (this.getResources().getIdentifier(this.icon, "drawable", this.getPackageName()) != 0) {
            this.getSupportActionBar().setIcon(this.getResources().getIdentifier(this.icon, "drawable", this.getPackageName()));
        } else {
            this.getSupportActionBar().setIcon(R.drawable.title);
        }
        Collections.sort(this.eventlist, new Comparator<Event>() {
            public int compare(Event s1, Event s2) {
                return s1.getTitle().compareTo(s2.getTitle());
            }
        });
        this.maxeventid=this.eventlist.size();
        Button addEventButton=(Button)this.findViewById(R.id.addEventButton);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Intent intent = new Intent(ViewEvents.this, EditEvent.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                Bundle mBundle = new Bundle();
                ViewEvents.this.maxeventid = ViewEvents.this.eventlist.size();
                mBundle.putInt("eventid", ViewEvents.this.maxeventid + 1);
                mBundle.putBoolean("addevent", true);
                mBundle.putString("icon",ViewEvents.this.icon);
                Event event=new Event();
                event.setId(ViewEvents.this.maxeventid+1);
                mBundle.putParcelable("event",event);
                intent.putExtras(mBundle);
                ViewEvents.this.startActivityForResult(intent, ADD_EVENT);
            }
        });
        ListView eventListView=(ListView)this.findViewById(R.id.eventListView);
        eventListView.setAdapter(new EventAdapter(this,eventlist,this.icon));
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if(requestCode==ADD_EVENT && resultCode==RESULT_OK){
            if(data.hasExtra("event")){
                this.eventlist.add((Event)data.getExtras().getParcelable("event"));
            }

        }
    }

    @Override
    public void onBackPressed() {
        Bundle conData = new Bundle();
        conData.putParcelableArrayList("eventlist", new ArrayList<Event>(this.eventlist));
        Intent intent = new Intent();
        intent.putExtras(conData);
        setResult(RESULT_OK, intent);
        this.finish();
    }

    @Override
    public void finish() {
        ViewEvents.instance=null;
        super.finish();
    }
}
