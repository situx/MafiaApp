package com.github.situx.mafiaapp.adapters;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.github.situx.mafiaapp.R;
import com.github.situx.mafiaapp.cards.Karte;
import com.github.situx.mafiaapp.cards.GameSet;
import com.github.situx.mafiaapp.util.Utils;
import com.github.situx.mafiaapp.views.CharView;
import com.github.situx.mafiaapp.views.ChooseChars;
import com.github.situx.mafiaapp.views.edit.EditChar;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: timo
 * Date: 21.11.13
 * Time: 18:09
 * To change this template use File | Settings | File Templates.
 */
public class CardAdapter2 extends BaseAdapter{


    private static final int EDIT_CHAR = 0;
    private static final int CHARVIEW_RESULT = 1;
    private List<Karte> cardlist;

        private final ChooseChars context;

        private final View.OnClickListener onButtonClick;
    private final View.OnClickListener onButtonClick2;
    private final View.OnClickListener onButtonClick3;

        private final String sourcefile;

        private final GameSet gameset;

        private final View.OnClickListener onImgClick;

        private Boolean editOrRound=false;

        public CardAdapter2(final ChooseChars context, final List<Karte> cards, String sourcefile, GameSet gameSet, final boolean editOrRound){
            this(context, cards,sourcefile,gameSet);
            this.editOrRound=editOrRound;
        }

        private CardAdapter2(final ChooseChars context, final List<Karte> cards, String sourcefile, GameSet gameSet){
            this.gameset=gameSet;
            this.cardlist=cards;
            Collections.sort(this.cardlist, (karte, karte2) -> {
                int stringResult = karte.isdead().compareTo(karte2.isdead());
                if (stringResult == 0) {
                    // Strings are equal, sort by date
                    return karte.getName().compareTo(karte2.getName());
                }
                else {
                    return stringResult;
                }
            });
            this.context=context;
            this.sourcefile=sourcefile;
            this.onButtonClick= view -> {
                Button button=(Button)view;
                Karte currentcard=CardAdapter2.this.cardlist.get(button.getId());
                /*if(currentcard.getCurrentamount()>1){
                    currentcard.setCurrentamount(currentcard.getCurrentamount()-1);
                    CardAdapter2.this.listviews.get(button.getId()).textView.setText(currentcard.getName()+"("+currentcard.getGroup()+")\n[ "+CardAdapter2.this.context.getResources().getString(R.string.amount)+" "+currentcard.getCurrentamount()+" ]");
                }else{
                    currentcard.dead();
                    button.setEnabled(false);
                    if(CardAdapter2.this.cardlist.get(button.getId()).imgexists()){
                        CardAdapter2.this.listviews.get(button.getId()).imageView.setImageBitmap(Utils.toGrayscale(((BitmapDrawable) CardAdapter2.this.context.getResources().getDrawable(CardAdapter2.this.context.getResources().getIdentifier(CardAdapter2.this.cardlist.get(button.getId()).getImg(), "drawable", CardAdapter2.this.context.getPackageName()))).getBitmap()));
                    }else{
                        CardAdapter2.this.listviews.get(button.getId()).imageView.setImageBitmap(Utils.toGrayscale(((BitmapDrawable) CardAdapter2.this.context.getResources().getDrawable(R.drawable.title)).getBitmap()));
                    }
                    CardAdapter2.this.context.setCharviewpos(view.getId());
                    CardAdapter2.this.listviews.get(button.getId()).textView.setText(currentcard.getName()+"("+currentcard.getGroup()+")");
                }*/
            };
            this.onButtonClick2= view -> {
                final Intent intent = new Intent(CardAdapter2.this.context, EditChar.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                Bundle mBundle = new Bundle();
                mBundle.putParcelable("card", CardAdapter2.this.cardlist.get(view.getId()));
                mBundle.putString("sourcefile", CardAdapter2.this.sourcefile);
                mBundle.putParcelable("gameset", CardAdapter2.this.gameset);
                mBundle.putString("icon", CardAdapter2.this.gameset.getGamesetImg());
                mBundle.putParcelableArrayList("cardlist", new ArrayList<Parcelable>(CardAdapter2.this.cardlist));
                intent.putExtras(mBundle);
                CardAdapter2.this.context.setCharviewpos(view.getId());
                CardAdapter2.this.context.startActivityForResult(intent, EDIT_CHAR);
            };
            this.onButtonClick3= view -> {
                final Intent intent = new Intent(CardAdapter2.this.context,EditChar.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                Bundle mBundle = new Bundle();
                mBundle.putParcelable("card", CardAdapter2.this.cardlist.get(view.getId()));
                mBundle.putString("sourcefile", CardAdapter2.this.sourcefile);
                mBundle.putParcelable("gameset", CardAdapter2.this.gameset);
                mBundle.putString("icon", CardAdapter2.this.gameset.getGamesetImg());
                mBundle.putParcelableArrayList("cardlist", new ArrayList<Parcelable>(CardAdapter2.this.cardlist));
                intent.putExtras(mBundle);
                CardAdapter2.this.context.setCharviewpos(view.getId());
                CardAdapter2.this.context.startActivityForResult(intent, EDIT_CHAR);
            };
            this.onImgClick= view -> {
                final Intent intent = new Intent(CardAdapter2.this.context, CharView.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                Bundle mBundle = new Bundle();
                mBundle.putParcelable("card", CardAdapter2.this.cardlist.get(view.getId()));
                mBundle.putString("sourcefile", CardAdapter2.this.sourcefile);
                mBundle.putString("icon", CardAdapter2.this.gameset.getGamesetImg());
                mBundle.putParcelableArrayList("cardlist",new ArrayList<Karte>(CardAdapter2.this.cardlist));
                mBundle.putBoolean("edit", false);
                CardAdapter2.this.context.setCharviewpos(view.getId());
                intent.putExtras(mBundle);
                context.setCharviewpos(view.getId());
                CardAdapter2.this.context.startActivityForResult(intent, CHARVIEW_RESULT);
            };
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
            return i;
        }

        private class CardAdapter2Holder{
            TextView textView;
            Button button,button2;
            ImageView imageView;
            Integer position;
            View view;
        }

        @Override
        public View getView(final int position, View view, final ViewGroup parent) {
            CardAdapter2Holder holder;
            final Karte current=this.cardlist.get(position);
            if(view==null){
                LayoutInflater inflater= LayoutInflater.from(this.context);
                holder=new CardAdapter2Holder();
                if(this.editOrRound){
                    view = inflater.inflate(R.layout.item_choosechars_3, parent, false);
                    holder.button=(Button)view.findViewById(R.id.charViewEditCharBTN);
                    holder.button2=(Button)view.findViewById(R.id.deleteCharButton);
                }
                else{
                    view = inflater.inflate(R.layout.item_roundchars, parent, false);
                        holder.button=(Button)view.findViewById(R.id.optionCheckBoxButton2);
                    }
                holder.position=position;
                holder.imageView=(ImageView)view.findViewById(R.id.imageView);
                holder.textView=(TextView)view.findViewById(R.id.CharChooseTextView);

                holder.view=view;
                view.setTag(holder);
            }else{
                holder=(CardAdapter2Holder)view.getTag();
                //view=holder.view;
            }
            if(current.imgexists() && !current.isdead()){
                holder.imageView.setImageDrawable(Utils.loadDrawable(this.context.getFilesDir().getAbsolutePath(),this.sourcefile.substring(0, this.sourcefile.lastIndexOf('_')), current.getCardid() + ".png"));
            }else if(current.imgexists() && current.isdead()){
                holder.imageView.setImageBitmap(new BitmapDrawable(Utils.toGrayscale(((BitmapDrawable) Utils.loadDrawable(this.context.getFilesDir().getAbsolutePath(),gameset.getSourcefile().substring(0, gameset.getSourcefile().lastIndexOf('_')), current.getCardid() + ".png")).getBitmap())).getBitmap());
            }else{
                holder.imageView.setImageResource(R.drawable.title);
            }
            holder.imageView.setOnClickListener(this.onImgClick);
            holder.imageView.setId(holder.position);


            holder.button.setId(position);
            if(this.editOrRound){
                holder.button.setOnClickListener(this.onButtonClick2);
                holder.button2.setOnClickListener(this.onButtonClick3);
            }else{
                holder.button.setText(R.string.kill);
                holder.button.setEnabled(!current.isdead());
                holder.button.setOnClickListener(this.onButtonClick);
            }

            if(current.getCurrentamount()>1){
                holder.textView.setText(current.getName()+"("+current.getGroup()+")\n[ "+CardAdapter2.this.context.getResources().getString(R.string.amount)+" "+current.getCurrentamount()+" ]");
            }else{
                holder.textView.setText(current.getName()+"("+current.getGroup()+")");
            }
            return view;
        }

    public List<Karte> getCardlist() {
        return cardlist;
    }

    public void setCardlist(final List<Karte> cardlist) {
        this.cardlist = cardlist;
    }
}
