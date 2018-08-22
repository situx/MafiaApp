package com.example.LNDWA.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.LNDWA.R;
import com.example.LNDWA.cards.Ability;
import com.example.LNDWA.views.edit.EditAbility;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: timo
 * Date: 22.11.13
 * Time: 15:41
 * To change this template use File | Settings | File Templates.
 */
public class AbilityAdapter extends ArrayAdapter<Ability> {
        /**The list of abilities to display.*/
        private final List<Ability> abilityList;

        private final List<AbilityAdapterHolder> listviews;
        /**The context of this adapter.*/
        private final Context context;
        /**Randomising element of this adapter.*/
        private final Random random;
        /**The layout id and the round id of this view.*/
        private final Integer layoutid;
    private Integer round;

        private final Boolean considerFromUntil;

    /**
     * Constructor for this class.
     * @param context the context of this adapter
     * @param abb the ability list to be displayed
     * @param layoutid the layoutid to use
     */
        public AbilityAdapter(final Context context,final Set<Ability> abb, int layoutid){
            super(context,0,new LinkedList<Ability>(abb));
            this.abilityList=new LinkedList<>(abb);
            this.context=context;
            this.layoutid=layoutid;
            this.considerFromUntil=false;
            this.random=new Random(System.currentTimeMillis());
            this.listviews=new LinkedList<>();
            for(Ability abi:this.abilityList){
                this.listviews.add(new AbilityAdapterHolder());
            }
        }

    /**
     * Constructor for this class.
     * @param context the context of this adapter
     * @param abb the ability list to be displayed
     * @param layoutid the layoutid to use
     * @param considerFromUntil
     * @param round
     */
        public AbilityAdapter(final Context context,final Set<Ability> abb, int layoutid,Boolean considerFromUntil,int round){
            super(context,0,new LinkedList<Ability>(abb));
            this.abilityList=new LinkedList<>(abb);
            this.context=context;
            this.layoutid=layoutid;
            this.considerFromUntil=considerFromUntil;
            this.round=round;
            this.random=new Random(System.currentTimeMillis());
            this.listviews=new LinkedList<>();
            for(Ability abi:this.abilityList){
                this.listviews.add(new AbilityAdapterHolder());
            }
        }

        @Override
        public int getCount() {
            return this.abilityList.size();
        }

        @Override
        public Ability getItem(final int i) {
              return this.abilityList.get(i);
        }

        @Override
        public long getItemId(final int i) {
            return 0;
        }

        @Override
        public View getView(final int position, View view, final ViewGroup parent) {
            AbilityAdapterHolder holder=this.listviews.get(position);
            if(holder.textView==null){
                final Ability current=this.abilityList.get(position);
                LayoutInflater inflater= LayoutInflater.from(this.context);
                view = inflater.inflate(this.layoutid, parent, false);
                if(considerFromUntil && current.checkFromUntil(this.round) || !considerFromUntil){
                    if(this.layoutid==R.layout.ability){
                        holder.button=(Button)view.findViewById(R.id.useAbilityButton);
                        holder.button.setId(position);
                        if(current.getMustuse()){
                            holder.button.setVisibility(View.GONE);
                        }
                        if((random.nextInt()%100)>current.getProbability()){
                            holder.button.setEnabled(false);
                            holder.button.setText("Not usable (%)");
                        }else{
                            holder.button.setEnabled(true);
                            holder.button.setText(R.string.useAbility);
                        }
                        if(current.currentamount ==0){
                            holder.button.setEnabled(false);
                        }
                        holder.button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View view) {
                                Button button=(Button)view;
                                AbilityAdapter.this.abilityList.get(button.getId()).currentamount--;
                                if(AbilityAdapter.this.abilityList.get(button.getId()).currentamount ==0){
                                    button.setEnabled(false);
                                }
                                TextView textView=(TextView)view.findViewById(R.id.charViewRoundAbility);
                                textView.setText(AbilityAdapter.this.abilityList.get(button.getId()).getDescription()+": "+AbilityAdapter.this.abilityList.get(button.getId()).currentamount +"x");
                            }
                        });
                    }
                    if(this.layoutid==R.layout.item_ability){
                        holder.button=(Button)view.findViewById(R.id.editAbilityButton);
                        holder.button.setId(position);
                        if(current.currentamount ==0){
                            holder.button.setEnabled(false);
                        }
                        holder.button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View view) {
                                final Intent intent = new Intent(AbilityAdapter.this.context,EditAbility.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                Bundle mBundle = new Bundle();
                                mBundle.putParcelable("ability", current);
                                intent.putExtras(mBundle);
                                AbilityAdapter.this.context.startActivity(intent);
                            }
                        });
                        holder.button2=(Button)view.findViewById(R.id.deleteAbilityButton);
                        holder.button2.setId(position);
                        holder.button2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View view) {
                                AbilityAdapter.this.abilityList.remove(view.getId());
                                AbilityAdapter.this.notifyDataSetInvalidated();
                            }
                        });
                    }
                    holder.textView=(TextView)view.findViewById(R.id.charViewRoundAbility);
                    if(current.getProbability()!=100){
                        holder.textView.setText(current.getDescription()+": "+current.getCurrentamount()+"x ("+current.getProbability()+"%)");
                    }else{
                        holder.textView.setText(current.getDescription()+": "+current.getCurrentamount()+"x");
                    }
                }
                holder.view=view;
            }else{
               view=holder.view;
            }

            return view;
        }

      class AbilityAdapterHolder{
          TextView textView;
          Button button;
          Button button2;
          View view;
      }

}
