package com.github.situx.mafiaapp.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import com.github.situx.mafiaapp.R;
import com.github.situx.mafiaapp.adapters.GroupAdapter;
import com.github.situx.mafiaapp.cards.Group;
import com.github.situx.mafiaapp.util.SaveEnum;
import com.github.situx.mafiaapp.util.Utils;
import com.github.situx.mafiaapp.views.edit.EditGroup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by timo on 05.02.14.
 * Views a list of players_config in this activity.
 */
public class ViewGroups extends ViewUtils {
    private static final int ADD_GROUP = 0;
    /**The instance of this class to prevent  recreation.*/
    private static ViewGroups instance;
    /**
     * The list of players_config to display.
     */
    private ArrayList<Group> groupList;
    /**
     * The listview to use to display the players_config.
     */
    private ListView groupListView;
    /**
     * The adapter to use to display the players_config.
     */
    private GroupAdapter groupListAdapter;
    /**
     * The maximum player id.
     */
    private Integer maxplayerid;
    /**
     * OnClickListener to add a new player to the game.
     */
    private View.OnClickListener addGroup = view -> {
        final Intent intent = new Intent(ViewGroups.this, EditGroup.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        Bundle mBundle = new Bundle();
        ViewGroups.this.maxplayerid = ViewGroups.this.groupList.size();
        mBundle.putParcelable("group", new Group());
        intent.putExtras(mBundle);
        ViewGroups.this.startActivityForResult(intent, ADD_GROUP);
    };

    /**
     * Empty constructor for ViewPlayers.
     * Manages instance creation.
     */
    public ViewGroups() {
        if (ViewGroups.instance == null) {
            this.turn = false;
            ViewGroups.instance = this;
        } else {
            this.groupList = ViewGroups.instance.groupList;
            this.groupListAdapter = ViewGroups.instance.groupListAdapter;
            this.maxplayerid = ViewGroups.instance.maxplayerid;
            this.turn = true;
        }
    }

    @Override
    public void finish() {
        ViewGroups.instance = null;
        super.finish();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == ADD_GROUP && resultCode == RESULT_OK) {
            if (data.hasExtra("group")) {
                this.groupList.add((Group) data.getExtras().getParcelable("group"));
                this.groupListView.setAdapter(new GroupAdapter(this, this.groupList));
                File subdir = new File(getFilesDir(), "config");
                File destination = new File(subdir + "/"+ Utils.PLAYERFILE);
                try {
                    Utils.saveFileToInternalStorage(this.groupList, SaveEnum.PLAYER, destination);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.viewgroups);
        Button addGroupButton = (Button) this.findViewById(R.id.addGroupButton);
        this.groupListView = (ListView) this.findViewById(R.id.groupListView);
        this.groupList = this.getIntent().getExtras().getParcelableArrayList("groups");
        if (!turn) {
            this.groupListAdapter = new GroupAdapter(this, this.groupList);
            groupListView.setAdapter(this.groupListAdapter);
        }
        addGroupButton.setOnClickListener(this.addGroup);
    }

    @Override
    public void onBackPressed() {
        Bundle conData = new Bundle();
        conData.putParcelableArrayList("grouplist", new ArrayList<Group>(this.groupList));
        Intent intent = new Intent();
        intent.putExtras(conData);
        setResult(RESULT_OK, intent);
        this.finish();
    }
}
