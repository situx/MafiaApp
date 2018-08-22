package com.example.LNDWA.adapters;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.LNDWA.cards.Karte;
import com.example.LNDWA.util.Utils;
import com.example.LNDWA.views.CharView;
import com.example.LNDWA.views.ChooseChars;
import com.example.LNDWA.R;
import com.example.textdrawable.drawable.TextDrawable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: timo
 * Date: 21.11.13
 * Time: 13:54
 * To change this template use File | Settings | File Templates.
 */
public class CardAdapter extends BaseAdapter {

    private static final int CHARVIEW_RESULT = 0;
    private List<Karte> cardlist;
    /**The boolean values contained in the checkboxes.*/
    private List<Boolean> checkboxvalues;
    /**The holder classes needed for this adapter.*/
    private List<CardAdapterHolder> listviews;
    /**The context of this adapter.*/
    private ChooseChars context;

    private String sourcefile;

    private View.OnClickListener onclick,onclickcheck;

    public CardAdapter(final ChooseChars context,final List<Karte> cards,final List<Boolean> checkboxvalues,final String sourcefile,final String icon,final Boolean hasBalance){
        this(context,cards,sourcefile,icon,hasBalance);
        this.checkboxvalues=checkboxvalues;
    }

    public CardAdapter(final ChooseChars context,final List<Karte> cards,final String sourcefile,final String icon,final Boolean hasBalance){
        this.cardlist=cards;
        this.context=context;
        this.sourcefile=sourcefile;
        this.listviews= new LinkedList<>();
        this.checkboxvalues=new LinkedList<>();
        for(Karte card:cards){
            this.checkboxvalues.add(false);
            this.listviews.add(new CardAdapterHolder());
        }
        this.onclick=new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Intent intent = new Intent(CardAdapter.this.context,CharView.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                Bundle mBundle = new Bundle();
                mBundle.putParcelable("card", CardAdapter.this.cardlist.get(view.getId()));
                mBundle.putString("sourcefile",sourcefile);
                mBundle.putString("icon",icon);
                mBundle.putParcelableArrayList("cardlist",new ArrayList<Karte>(CardAdapter.this.cardlist));
                mBundle.putBoolean("edit", false);
                intent.putExtras(mBundle);
                context.setCharviewpos(view.getId());
                CardAdapter.this.context.startActivityForResult(intent, CHARVIEW_RESULT);
            }
        };
        this.onclickcheck=new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                CheckBox checkBox = (CheckBox)view;
                checkboxvalues.set(checkBox.getId(),!checkboxvalues.get(checkBox.getId()));
                CardAdapterHolder currentholder=CardAdapter.this.listviews.get(checkBox.getId());
                Karte currentcard=CardAdapter.this.cardlist.get(checkBox.getId());
                if(checkBox.isChecked()){
                    if(currentholder.editText!=null){
                        CardAdapter.this.context.players+=Integer.valueOf(currentholder.editText.getText().toString());
                        CardAdapter.this.context.balance+=Integer.valueOf(currentholder.editText.getText().toString())*currentcard.getBalancevalue();
                        currentcard.setCurrentamount(Integer.valueOf(currentholder.editText.getText().toString()));
                    }else{
                        CardAdapter.this.context.players+=currentcard.getMinAmount();
                        CardAdapter.this.context.balance+=currentcard.getBalancevalue();
                    }
                    CardAdapter.this.context.players-=currentcard.getExtra();
                    if(currentcard.getExtra()>0){
                        CardAdapter.this.context.extracards+=currentcard.getExtra()-1;
                    }
                }else{
                    if(currentholder.editText!=null){
                        CardAdapter.this.context.players-=Integer.valueOf(currentholder.editText.getText().toString());
                        CardAdapter.this.context.balance-=Integer.valueOf(currentholder.editText.getText().toString())*currentcard.getBalancevalue();
                        currentcard.setCurrentamount(currentcard.getMinAmount());
                    }else{
                        CardAdapter.this.context.players-=currentcard.getMinAmount();
                        CardAdapter.this.context.balance-=currentcard.getBalancevalue();
                    }
                    CardAdapter.this.context.players+=currentcard.getExtra();
                    if(currentcard.getExtra()>0){
                        CardAdapter.this.context.extracards-=currentcard.getExtra()-1;
                    }
                }
                checkBox.refreshDrawableState();
                TextView textview=(TextView)CardAdapter.this.context.findViewById(R.id.chooseCharsTextView);
                if(hasBalance){
                    textview.setText(context.getResources().getString(R.string.players) + context.players + " + " + context.getResources().getString(R.string.extracards) + context.extracards+" ("+context.getResources().getString(R.string.balancevalue)+": "+context.balance+")");
                }else{
                    textview.setText(context.getResources().getString(R.string.players) + context.players + " + " + context.getResources().getString(R.string.extracards) + context.extracards);
                }
            }
        };
    }

    @Override
    public int getCount() {
        return this.cardlist.size();
    }

    public List<Boolean> getCheckboxvalues() {
        return checkboxvalues;
    }

    public void setCheckboxvalues(final List<Boolean> checkboxvalues) {
        this.checkboxvalues = checkboxvalues;
    }

    @Override
    public Object getItem(final int i) {
        if(this.checkboxvalues.get(i)){
            return this.cardlist.get(i);
        }
        return null;

    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return (this.cardlist.size()>0)?this.cardlist.size():1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public class CardAdapterHolder{
        public ImageView imageView;
        public CheckBox checkbox;
        LinearLayout linear;
        TextView textView;
        EditText editText;
        View view;
        public int position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final CardAdapterHolder holder;
        View view;
        final Karte current=this.cardlist.get(position);
        if(this.listviews.get(position).checkbox==null){
            LayoutInflater inflater= LayoutInflater.from(this.context);
            holder=this.listviews.get(position);
            if(current.getMaxAmount()==-1){
                view=inflater.inflate(R.layout.item_choosechars_2,parent,false);
                holder.editText=(EditText)view.findViewById(R.id.charAmount);
                holder.editText.setText(current.getCurrentamount().toString());
                holder.editText.setId(position);
                holder.editText.addTextChangedListener(new TextWatcher() {
                    Integer previousvalue;
                    @Override
                    public void beforeTextChanged(final CharSequence charSequence, final int i, final int i2, final int i3) {
                        if(charSequence.toString().length()!=0)
                            previousvalue=Integer.valueOf(charSequence.toString());
                    }

                    @Override
                    public void onTextChanged(final CharSequence charSequence, final int i, final int i2, final int i3) {

                    }

                    @Override
                    public void afterTextChanged(final Editable editable) {
                        Karte currentcard=CardAdapter.this.cardlist.get(holder.editText.getId());
                        if(holder.editText.getText().toString().length()!=0 && holder.checkbox.isChecked()){
                            CardAdapter.this.context.players-=previousvalue;
                            CardAdapter.this.context.players+=Integer.valueOf(holder.editText.getText().toString());
                            CardAdapter.this.context.balance+=currentcard.getBalancevalue();
                            previousvalue=Integer.valueOf(holder.editText.getText().toString());
                            currentcard.setCurrentamount(Integer.valueOf(holder.editText.getText().toString()));
                        }
                    }
                });
            }else{
                view = inflater.inflate(R.layout.item_choosechars_1, parent, false);
            }
            holder.position=position;
            holder.checkbox=(CheckBox)view.findViewById(R.id.optionCheckBox);
            holder.checkbox.setId(position);
            holder.checkbox.setOnClickListener(this.onclickcheck);
            holder.checkbox.setChecked(CardAdapter.this.checkboxvalues.get(holder.position));
            holder.imageView=(ImageView)view.findViewById(R.id.imageView);
            if(current.imgexists()){
                holder.imageView.setImageDrawable(Utils.loadDrawable(context.getFilesDir().getAbsolutePath(),this.sourcefile, current.getCardid() + ".png"));
            }else{


                TextDrawable drawable=new TextDrawable(CardAdapter.this.context);
                drawable.setTextColor(Color.RED);

                drawable.setTextAlign(Layout.Alignment.ALIGN_CENTER);

                drawable.setText(current.getName());
                Log.e("TextDrawable Null?", drawable.toString());
                /*Path p = new Path();
                int origin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, CardAdapter.this.context.getResources().getDisplayMetrics());
                int radius = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, CardAdapter.this.context.getResources().getDisplayMetrics());
                int bound = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, CardAdapter.this.context.getResources().getDisplayMetrics());
                p.addCircle(origin, origin, radius, Path.Direction.CW);

                drawable.setTextPath(p);*/
//Must call setBounds() since we are using a Path
                drawable.setBounds(0,0,150,150);
                //view.setBackground(new ColorDrawable(Color.GRAY));
                //drawable.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                //drawable.setBounds(new Rect());
                holder.imageView.setImageDrawable(drawable);
                //holder.imageView.setImageResource(R.drawable.title);
            }
            //holder.imageView.setLayoutParams(new LinearLayout.LayoutParams(150,150));
            holder.imageView.setAdjustViewBounds(true);
            holder.imageView.setOnClickListener(onclick);
            holder.imageView.setId(holder.position);
            holder.textView=(TextView)view.findViewById(R.id.CharChooseTextView);
            holder.textView.setText(current.getName()+"("+current.getGroup()+")");
            holder.view=view;
        }else{
            holder=this.listviews.get(position);
            holder.checkbox.setChecked(CardAdapter.this.checkboxvalues.get(holder.position));
            if(holder.editText!=null){
                holder.editText.setText(current.getCurrentamount().toString());
            }
            view=holder.view;
        }
        view.setId(position);
        return view;
    }

    public List<Karte> getCurrentCardList(){
        return this.cardlist;
    }

    public List<Karte> getCardlist() {
        return cardlist;
    }

    public void setCardlist(final List<Karte> cardlist) {
        this.cardlist = cardlist;
    }
}
