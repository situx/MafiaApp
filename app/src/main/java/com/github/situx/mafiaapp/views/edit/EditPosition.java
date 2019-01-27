package com.github.situx.mafiaapp.views.edit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.github.situx.mafiaapp.R;
import com.github.situx.mafiaapp.adapters.PositionAdapter;
import com.github.situx.mafiaapp.cards.Action;
import com.github.situx.mafiaapp.cards.Karte;
import com.github.situx.mafiaapp.util.Tuple;
import com.github.situx.mafiaapp.util.Utils;
import com.github.situx.mafiaapp.views.TouchInterceptor;

import java.util.*;

/**
 * Created by timo on 29.01.14.
 */
public class EditPosition extends ListActivity {
        private PositionAdapter sortAdapter;
        private List<Karte> cardlist;

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.editposition);
            if(savedInstanceState!=null && savedInstanceState.containsKey("cardlist")){
                this.cardlist=savedInstanceState.getParcelableArrayList("cardlist");
            } else{
                this.cardlist=this.getIntent().getExtras().getParcelableArrayList("cardlist");
            }
            Collections.sort(this.cardlist, (s1, s2) -> s1.getPosition().compareTo(s2.getPosition()));
            sortAdapter = new PositionAdapter(this,cardlist,this.getIntent().getExtras().getString("gamesetid"),this.getIntent().getExtras().getString("language"));
            setListAdapter(sortAdapter);
            mList = (TouchInterceptor) getListView();
            mList.setDropListener(mDropListener);
            registerForContextMenu(mList);
        }


    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        outState.putParcelableArrayList("cardlist",new ArrayList<Karte>(this.cardlist));
        super.onSaveInstanceState(outState);
    }

    @Override


    protected void onListItemClick(ListView l, View v, int position, long id) {
        String selection = this.sortAdapter.getItem(position).toString();
        Toast.makeText(this, selection, Toast.LENGTH_SHORT).show();
    }
    private TouchInterceptor mList;


    private TouchInterceptor.DropListener mDropListener =
            new TouchInterceptor.DropListener() {


                public void drop(int from, int to) {
                    Log.e("DragDrop","Droplisten from:"+from+" to:"+to);
                    Log.e("DragDrop","Droplisten from:"+EditPosition.this.cardlist.get(from).toString()+" to:"+EditPosition.this.cardlist.get(to).toString());
//Assuming that item is moved up the list
                    int direction = -1;
                    int loop_start = from;
                    int loop_end = to;
//For instance where the item is dragged down the list
                    if(from < to) {
                        direction = 1;
                    }
                    Karte target = EditPosition.this.cardlist.get(from);
                    for(int i=loop_start;i!=loop_end;i=i+direction){
                        EditPosition.this.cardlist.set(i,EditPosition.this.cardlist.get(i + direction));
                    }
                    EditPosition.this.cardlist.set(to,target);//Array[to] = target;
                    Log.e("DragDrop", "Changed array is:" + EditPosition.this.cardlist.toString());
                    EditPosition.this.sortAdapter.notifyDataSetChanged();
                    EditPosition.this.sortAdapter.notifyDataSetInvalidated();
                    EditPosition.this.sortAdapter=new PositionAdapter(EditPosition.this,cardlist, EditPosition.this.getIntent().getExtras().getString("gamesetid"),EditPosition.this.getIntent().getExtras().getString("language"));
                    setListAdapter(sortAdapter);
                }


            };

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        menu.add(0,0,0,this.getResources().getString(R.string.about));
        menu.getItem(0).setOnMenuItemClickListener(item -> {
            Utils.dialogtext=EditPosition.this.
                    getResources().getString(R.string.copyright);
            EditPosition.this.showDialog(0);
            return true;
        });
        return true;
    }

    @Override
    protected Dialog onCreateDialog(final int id) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch(id){
            case 0:         builder.setMessage(Utils.dialogtext)
                    .setCancelable(true)
                    .setIcon(this.getResources().getDrawable(R.drawable.title))
                    .setTitle(this.getResources().getString(R.string.about))
                    .setPositiveButton(this.getResources().getString(R.string.ok), (dialog, buttonid) -> dialog.cancel());break;
            default:
        }


        return builder.create();
    }

    @Override
    public void onBackPressed() {
        this.savePositions();
        Bundle conData = new Bundle();
        conData.putParcelableArrayList("cardlist", new ArrayList<Karte>(this.cardlist));
        Intent intent = this.getIntent();
        intent.putExtras(conData);
        setResult(RESULT_OK, intent);
        this.finish();
    }


    public void savePositions(){
         int i=0;
         Log.e("Save Positions",this.cardlist.toString());
          for(Tuple<Action,Karte> card:this.sortAdapter.getActionMap()){
              Log.e(card.getOne().getTitle(),"is now Position "+i);
              if(card.getOne().getActionid()!=null){
                  card.getTwo().getActionlist().get(card.getOne().getActionid()).setPosition(i);
              }else{
                  card.getTwo().setPosition(i);
              }
              i++;
          }
    }
}
