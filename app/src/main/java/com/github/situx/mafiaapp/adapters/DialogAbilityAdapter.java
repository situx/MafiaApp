package com.github.situx.mafiaapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.situx.mafiaapp.R;
import com.github.situx.mafiaapp.cards.Ability;
import com.github.situx.mafiaapp.util.Utils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by timo on 17.03.14.
 */
public class DialogAbilityAdapter extends BaseAdapter{

    /**The context of this adapter.*/
    private final Context context;
    /**The abilityList of this adapter.*/
    private final List<Ability> abilityList;
    /**THe list of holder classes for this adapter.*/
    private final List<DialogAbilityAdapterHolder> listviews;

    private String language,gamesetid,cardid;

    /**
     * Constructor for this class.
     * @param context  the context of this adapter
     * @param cardlist the list of cards for this adapter
     */
    public DialogAbilityAdapter(final Context context,final List<Ability> cardlist,String gamesetid,String language,String cardid){
        this.abilityList =cardlist;
        this.context=context;
        this.listviews=new LinkedList<>();
        for(Ability ability:this.abilityList){
            this.listviews.add(new DialogAbilityAdapterHolder());
        }
        this.gamesetid=gamesetid;
        this.language=language;
        this.cardid=cardid;
    }

    @Override
    public int getCount() {
        return this.abilityList.size();
    }

    @Override
    public Object getItem(final int i) {
        return this.abilityList.get(i);
    }

    @Override
    public long getItemId(final int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        DialogAbilityAdapterHolder holder=this.listviews.get(i);
        Ability current=this.abilityList.get(i);
        if(holder.cardNameView==null){
            view= LayoutInflater.from(this.context).inflate(R.layout.item_card,viewGroup,false);
            holder.cardNameView=(TextView)view.findViewById(R.id.cardNameTextView);
            holder.cardNameView.setText(current.getDescription()+" ("+current.getCurrentamount()+")");
            holder.imageView=(ImageView)view.findViewById(R.id.cardImageView);
            File file=new File(context.getFilesDir().getAbsolutePath()+"/chars/"+this.language+"_"+this.gamesetid+"/overlay/"+current.getAbilityId()+".png");
            if(file.exists()){
                holder.imageView.setImageDrawable(Utils.loadDrawable(context.getFilesDir().getAbsolutePath(),this.language+"_"+this.gamesetid+"/overlay/",current.getAbilityId()+".png"));
            }else{
                File file2=new File(context.getFilesDir().getAbsolutePath()+"/chars/"+this.language+"_"+this.gamesetid+"/"+this.cardid+".png");
                if(file2.exists()){
                    holder.imageView.setImageDrawable(Utils.loadDrawable(context.getFilesDir().getAbsolutePath(),this.language+"_"+this.gamesetid,this.cardid+".png"));
                }else{
                    holder.imageView.setImageResource(R.drawable.title);
                }
            }
            holder.checkbox=(CheckBox)view.findViewById(R.id.cardNameCheckBox);
            holder.checkbox.setVisibility(View.GONE);
            holder.view=view;
        }else{
            view=holder.view;
        }
        return view;
    }

    /**
     * Holder class for performance reasons.
     */
    class DialogAbilityAdapterHolder{
        TextView cardNameView;
        ImageView imageView;
        CheckBox checkbox;
        View view;
    }
}
