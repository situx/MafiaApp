package com.github.situx.mafiaapp.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.github.situx.mafiaapp.R;
import com.github.situx.mafiaapp.adapters.ItemAdapter;
import com.github.situx.mafiaapp.cards.Item;
import com.github.situx.mafiaapp.views.edit.EditPlayer;

import java.util.List;

/**
 * Created by timo on 06.02.14.
 * Shows a list of items in this activity.
 */
public class ViewItems extends ViewUtils {

    private static final int ADD_ITEM = 0;
    /**
     * The instance of this class.
     */
    private static ViewItems instance;
    /**
     * The list of items.
     */
    private List<Item> itemlist;
    /**
     * The maximum item id.
     */
    private Integer maxitemid;

    /**
     * Constructor for this class.
     */
    public ViewItems() {
        if (ViewItems.instance == null) {
            ViewItems.instance = this;
            this.turn = false;
        } else {
            this.itemlist = ViewItems.instance.itemlist;
            this.maxitemid = ViewItems.instance.maxitemid;
            this.turn = true;
        }
    }

    @Override
    public void finish() {
        ViewItems.instance = null;
        super.finish();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == ADD_ITEM && resultCode == RESULT_OK) {
            if (data.hasExtra("item")) {
                this.itemlist.add((Item) data.getExtras().getParcelable("item"));
            }

        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.viewitems);
        this.itemlist = this.getIntent().getExtras().getParcelableArrayList("items");
        this.maxitemid = this.itemlist.size();
        Button addItemButton = (Button) this.findViewById(R.id.addItemButton);
        addItemButton.setOnClickListener(view -> {
            final Intent intent = new Intent(ViewItems.this, EditPlayer.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            Bundle mBundle = new Bundle();
            ViewItems.this.maxitemid = ViewItems.this.itemlist.size();
            mBundle.putInt("itemid", ViewItems.this.maxitemid + 1);
            intent.putExtras(mBundle);
            ViewItems.this.startActivityForResult(intent, ADD_ITEM);
        });
        ListView eventListView = (ListView) this.findViewById(R.id.itemListView);
        eventListView.setAdapter(new ItemAdapter(this, itemlist));
    }
}
