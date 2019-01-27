package com.github.situx.mafiaapp.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.github.situx.mafiaapp.R;
import com.github.situx.mafiaapp.adapters.PlayerListAdapter;
import com.github.situx.mafiaapp.cards.Player;
import com.github.situx.mafiaapp.util.SaveEnum;
import com.github.situx.mafiaapp.util.Utils;
import com.github.situx.mafiaapp.util.network.Synchronizer;
import com.github.situx.mafiaapp.util.parser.PlayerParse;
import com.github.situx.mafiaapp.views.edit.EditPlayer;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by timo on 05.02.14.
 * Views a list of players_config in this activity.
 */
public class ViewPlayers extends ViewUtils {
    private static final int ADD_PLAYER = 0;
    /**The instance of this class to prevent  recreation.*/
    private static ViewPlayers instance;
    /**
     * The list of players_config to display.
     */
    private List<Player> playerList;
    /**
     * The listview to use to display the players_config.
     */
    private ListView playerListView;
    /**
     * The adapter to use to display the players_config.
     */
    private PlayerListAdapter playerListAdapter;
    /**
     * The maximum player id.
     */
    private Integer maxplayerid;
    /**
     * OnClickListener to add a new player to the game.
     */
    private View.OnClickListener addPlayer = view -> {
        final Intent intent = new Intent(ViewPlayers.this, EditPlayer.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        Bundle mBundle = new Bundle();
        ViewPlayers.this.maxplayerid = ViewPlayers.this.playerList.size();
        mBundle.putInt("playerid", ViewPlayers.this.maxplayerid + 1);
        intent.putExtras(mBundle);
        ViewPlayers.this.startActivityForResult(intent, ADD_PLAYER);
    };

    /**
     * Empty constructor for ViewPlayers.
     * Manages instance creation.
     */
    public ViewPlayers() {
        if (ViewPlayers.instance == null) {
            this.turn = false;
            ViewPlayers.instance = this;
        } else {
            this.playerList = ViewPlayers.instance.playerList;
            this.playerListAdapter = ViewPlayers.instance.playerListAdapter;
            this.maxplayerid = ViewPlayers.instance.maxplayerid;
            this.turn = true;
        }
    }

    @Override
    public void finish() {
        ViewPlayers.instance = null;
        super.finish();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == ADD_PLAYER && resultCode == RESULT_OK) {
            if (data.hasExtra("player")) {
                this.playerList.add((Player) data.getExtras().getParcelable("player"));
                this.playerListView.setAdapter(new PlayerListAdapter(this, this.playerList));
                File subdir = new File(getFilesDir(), "config");
                File destination = new File(subdir + "/"+Utils.PLAYERFILE);
                try {
                    Utils.saveFileToInternalStorage(this.playerList, SaveEnum.PLAYER, destination);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.viewplayers);
        Button addPlayerButton = (Button) this.findViewById(R.id.addPlayerButton);
        this.playerListView = (ListView) this.findViewById(R.id.playerListView);
        if (!turn) {
            PlayerParse playerParse = new PlayerParse();
            playerParse.parsePlayer(new File(this.getFilesDir()+"/config/"+ Utils.PLAYERFILE));
            this.playerList = playerParse.getPlayers();
            this.playerListAdapter = new PlayerListAdapter(this, this.playerList, 1);
        }
        playerListView.setAdapter(this.playerListAdapter);
        addPlayerButton.setOnClickListener(this.addPlayer);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        menu.add(0,0,0,this.getResources().getString(R.string.about));
        menu.add(0,0,0,this.getResources().getString(R.string.syncplayers));
        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                Utils.dialogtext=ViewPlayers.this.
                        getResources().getString(R.string.copyright);
                ViewPlayers.this.chooseDialogs(DialogEnum.ABOUT);
                return true;
            }
        });
        menu.getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                Synchronizer synchronizer=new Synchronizer();
                    List<Player> playerlist=synchronizer.loadPlayers(ViewPlayers.this);
                    if(synchronizer.getErrorflag()){
                        Toast toast = Toast.makeText(ViewPlayers.this, ViewPlayers.this.getResources().getString(R.string.syncfailed)+synchronizer.getErrormessage(), Toast.LENGTH_LONG);
                        toast.show();
                        return false;
                    }
                    Log.e("Playerlist",playerlist.toString());
                    for(Player player:playerlist){
                        if(!ViewPlayers.this.playerList.contains(player)){
                            Log.e("Sync Player", "Not in list: " + player.toString());
                            ViewPlayers.this.playerList.add(player);
                        }else{
                            Log.e("Sync Player", "Already in list: " + player.toString());
                            for(Player player2:ViewPlayers.this.playerList){
                                if(player2.equals(player)){
                                    player.synchronize(player2);
                                    break;
                                }
                            }
                        }
                    }
                    Toast toast = Toast.makeText(ViewPlayers.this, ViewPlayers.this.getResources().getString(R.string.playerssynchronized), Toast.LENGTH_LONG);
                    toast.show();
                    ViewPlayers.this.playerListView.invalidateViews();
                return true;
            }
        });
        return true;
    }
}
