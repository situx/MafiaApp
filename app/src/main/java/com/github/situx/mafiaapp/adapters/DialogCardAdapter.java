package com.github.situx.mafiaapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.github.situx.mafiaapp.R;
import com.github.situx.mafiaapp.cards.Karte;
import com.github.situx.mafiaapp.util.Utils;
import com.github.situx.mafiaapp.views.Round2;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by timo on 17.02.14.
 */
public class DialogCardAdapter extends BaseAdapter {
    private String sourcefile;
    private Button button;
    private Integer extracardamount;
    /**The context of this adapter.*/
    private final Round2 context;
    /**The cardlist of this adapter.*/
    private final Map<Integer,Karte> cardlist;

    public final Map<Integer, Karte> checkedItems=new TreeMap<>();

    private List<Boolean> checkvalues;
    /**THe list of holder classes for this adapter.*/
    private List<DialogCardAdapterHolder> listviews;

    private Integer numberofcheckedItems=0;

    private final Boolean checked;

    public Button getButton() {
        return button;
    }

    public List<DialogCardAdapterHolder> getListviews() {
        return listviews;
    }

    public void setButton(final Button button) {
        this.button = button;
        this.button.setEnabled(false);
    }

    public void setListviews(final List<DialogCardAdapterHolder> listviews) {
        this.listviews = listviews;
    }

    /**
     * Constructor for this class.
     * @param context  the context of this adapter
     * @param cardlist the list of cards for this adapter
     */
    public DialogCardAdapter(final Round2 context,final Map<Integer,Karte> cardlist){
        this.cardlist=cardlist;
        this.context=context;
        this.listviews=new LinkedList<>();

        for(Integer card:this.cardlist.keySet()){
            this.listviews.add(new DialogCardAdapterHolder());
        }
        this.checked=false;
    }

    /**
     * Constructor for this class.
     * @param context  the context of this adapter
     * @param cardlist the list of cards for this adapter
     */
    public DialogCardAdapter(final Round2 context,final List<Karte> cardlist,final String sourcefile){
        this.cardlist=new TreeMap<>();
        int i=0;
        for(Karte card:cardlist){
           this.cardlist.put(i++,card);
        }
        this.context=context;
        this.listviews=new LinkedList<>();
        this.checkvalues=new LinkedList<>();
        for(Integer card:this.cardlist.keySet()){
            this.listviews.add(new DialogCardAdapterHolder());
        }
        this.checked=false;
        this.sourcefile=sourcefile;
    }

    public List<Boolean> getCheckvalues() {
        return checkvalues;
    }

    public void setCheckvalues(final List<Boolean> checkvalues) {
        this.checkvalues = checkvalues;
    }

    public void setListView(final ListView listView) {
        final ListView listView1 = listView;
    }

    /**
     * Constructor for this class.
     * @param context  the context of this adapter
     * @param cardlist the list of cards for this adapter
     * @param sourcefile
     */
    public DialogCardAdapter(final Round2 context, final List<Karte> cardlist, Boolean checked, Integer extracardamount, final String sourcefile){
        this.cardlist=new TreeMap<>();
        int i=0;
        for(Karte card:cardlist){
            this.cardlist.put(i++,card);
        }
        this.context=context;
        this.listviews=new LinkedList<>();
        this.checkvalues=new LinkedList<>();
        for(Integer card:this.cardlist.keySet()){
            this.listviews.add(new DialogCardAdapterHolder());
        }
        for(Integer card:this.cardlist.keySet()){
            this.checkvalues.add(false);
        }
        this.checked=checked;
        this.extracardamount=extracardamount;
        this.sourcefile=sourcefile;
    }

    @Override
    public int getCount() {
        return this.cardlist.size();
    }

    @Override
    public Object getItem(final int i) {
        return this.cardlist.get(i);
    }

    @Override
    public long getItemId(final int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        final DialogCardAdapterHolder holder=this.listviews.get(i);
        Karte current=this.cardlist.get(i);
        if(holder.cardNameView==null){
            view= LayoutInflater.from(this.context).inflate(R.layout.item_card,viewGroup,false);
            holder.cardNameView=(TextView)view.findViewById(R.id.cardNameTextView);
            holder.cardNameView.setText(current.getName());
            holder.imageView=(ImageView)view.findViewById(R.id.cardImageView);
            if(current.imgexists()){
                holder.imageView.setImageDrawable(Utils.loadDrawable(this.context.getFilesDir().getAbsolutePath(),this.sourcefile.substring(0, this.sourcefile.lastIndexOf('_')),current.getCardid()+".png"));
            }else{
                holder.imageView.setImageResource(R.drawable.title);
            }
            holder.checkbox=(CheckBox)view.findViewById(R.id.cardNameCheckBox);
            if(checked){
                holder.checkbox.setChecked(this.checkvalues.get(i));
                View.OnClickListener click= view1 -> {
                    if (DialogCardAdapter.this.checkvalues.get(i)) {
                        numberofcheckedItems--;
                        DialogCardAdapter.this.checkvalues.set(i, false);
                        holder.checkbox.setChecked(false);
                        DialogCardAdapter.this.checkedItems.remove(i);
                    } else if (!DialogCardAdapter.this.checkvalues.get(i) && numberofcheckedItems < DialogCardAdapter.this.extracardamount) {
                        numberofcheckedItems++;
                        DialogCardAdapter.this.checkvalues.set(i, true);
                        holder.checkbox.setChecked(true);
                        DialogCardAdapter.this.checkedItems.put(i,DialogCardAdapter.this.cardlist.get(i));
                    } else if (!DialogCardAdapter.this.checkvalues.get(i) && numberofcheckedItems >= DialogCardAdapter.this.extracardamount) {
                        holder.checkbox.setChecked(false);
                        DialogCardAdapter.this.checkvalues.set(i, false);
                    }
                    if(numberofcheckedItems.equals(extracardamount)){
                        DialogCardAdapter.this.button.setEnabled(true);
                    }else{
                        DialogCardAdapter.this.button.setEnabled(false);
                    }
                    DialogCardAdapter.this.notifyDataSetChanged();
                };
                holder.imageView.setOnClickListener(click);
                holder.checkbox.setOnClickListener(click);
                holder.cardNameView.setOnClickListener(click);
                view.setOnClickListener(click);
            }else{
                holder.checkbox.setVisibility(View.GONE);
            }
            holder.view=view;
        }else{
            view=holder.view;
        }
        return view;
    }

    /**
     * Holder class for performance reasons.
     */
    public class DialogCardAdapterHolder{
        TextView cardNameView;
        public CheckBox checkbox;
        ImageView imageView;
        View view;
    }
}
