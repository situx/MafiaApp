package com.example.LNDWA.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.example.LNDWA.R;
import com.example.LNDWA.util.Tuple;
import com.example.LNDWA.util.Utils;

import java.io.File;
import java.util.List;

/**
 * Created by timo on 15.01.14.
 */
public class SetAdapter implements SpinnerAdapter{
    /**The context of this adapter.*/
    private final Context context;
    /**The list of gameset names as String list.*/
    private List<Tuple<String,String>> setList;
    private Boolean statview;

    /**
     * Constructor for this class.
     * @param context the context being used
     * @param setList the list of gameset names
     */
    public SetAdapter(final Context context, final List<Tuple<String,String>> setList){
        this.setList=setList;
        this.context=context;
        this.statview=false;
    }

    @Override
    public View getDropDownView(final int i, View view, final ViewGroup viewGroup) {
        SetAdapterHolder holder;
        if(view==null){
            holder=new SetAdapterHolder();
            LayoutInflater inflater= LayoutInflater.from(this.context);
            view = inflater.inflate(R.layout.item_gameset2, viewGroup, false);
            holder.textView=(TextView)view.findViewById(R.id.setTextView);
            holder.imageView= (ImageView)view.findViewById(R.id.setImageView);
            view.setTag(holder);
        }else{
            holder=(SetAdapterHolder)view.getTag();
        }
        String directory=this.setList.get(i).getTwo();
        String file=this.setList.get(i).getTwo()+".png";
        boolean test = new File(context.getFilesDir()+"/chars/" + directory+"/"+file).exists();
        Log.e("File exists?", context.getFilesDir() + "/chars/" + directory + "/" + file);
        if (test)
            holder.imageView.setImageDrawable(Utils.loadDrawable(this.context.getFilesDir().getAbsolutePath(),directory, file));
        else{
            holder.imageView.setImageResource(R.drawable.title);
        }
        holder.textView.setText(this.setList.get(i).getOne());
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
        return this.setList.size();
    }

    @Override
    public Object getItem(final int i) {
        return this.setList.get(i);
    }

    @Override
    public long getItemId(final int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        SetAdapterHolder holder;
        if(view==null){
            holder=new SetAdapterHolder();
            LayoutInflater inflater= LayoutInflater.from(this.context);
            view = inflater.inflate(R.layout.item_gameset2, viewGroup, false);
            holder.textView=(TextView)view.findViewById(R.id.setTextView);
            holder.imageView= (ImageView)view.findViewById(R.id.setImageView);
            view.setTag(holder);
        }else{
            holder=(SetAdapterHolder)view.getTag();
        }
        String directory=this.setList.get(i).getTwo();
        String file=this.setList.get(i).getTwo()+".png";
        boolean test = new File(context.getFilesDir()+"/chars/" + directory+"/"+file).exists();
        Log.e("File exists?", context.getFilesDir() + "/chars/" + directory + "/" + file);
        if (test)
            holder.imageView.setImageDrawable(Utils.loadDrawable(this.context.getFilesDir().getAbsolutePath(),directory, file));
        else{
            holder.imageView.setImageResource(R.drawable.title);
        }
        holder.textView.setText(this.setList.get(i).getOne());
        return view;
    }

    @Override
    public int getItemViewType(final int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return this.setList.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    class SetAdapterHolder{
        ImageView imageView;
        TextView textView;
        View view;
    }
}
