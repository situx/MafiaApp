package com.example.LNDWA.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.LNDWA.R;
import com.example.LNDWA.cards.Item;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by timo on 06.02.14.
 */
public class ItemAdapter extends BaseAdapter {

    private List<Item> items;

    private List<ItemHolder> listviews;

    private Context context;

    public ItemAdapter(final Context context,final List<Item> items){
        this.items=items;
        this.context=context;
        this.items=items;
        this.listviews=new LinkedList<>();
        for(Item item:this.items){
            this.listviews.add(new ItemHolder());
        }

    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(final int i) {
        return this.items.get(i);
    }

    @Override
    public long getItemId(final int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        LayoutInflater inflater=LayoutInflater.from(this.context);
        view=inflater.inflate(R.layout.item_item,viewGroup,false);
        ItemHolder holder=this.listviews.get(i);
        if(this.listviews.get(i).titleTextview==null){
            holder.titleTextview=(TextView)view.findViewById(R.id.eventItemTitleTextView);
            holder.titleTextview.setText(this.items.get(i).getName());
            holder.descriptionTextview=(TextView)view.findViewById(R.id.eventItemDescriptionTitleTextView);
            holder.descriptionTextview.setText(this.items.get(i).getDescription());
            holder.view=view;
        }else{
            view=holder.view;
        }
        return view;
    }


    private class ItemHolder{
        TextView titleTextview;
        TextView descriptionTextview;
        View view;
    }
}
