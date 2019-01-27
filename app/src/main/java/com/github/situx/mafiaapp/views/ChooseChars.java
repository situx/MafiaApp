package com.github.situx.mafiaapp.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.github.situx.mafiaapp.R;
import com.github.situx.mafiaapp.adapters.CardAdapter;
import com.github.situx.mafiaapp.adapters.CardAdapter2;
import com.github.situx.mafiaapp.cards.GameSet;
import com.github.situx.mafiaapp.cards.Group;
import com.github.situx.mafiaapp.cards.Karte;
import com.github.situx.mafiaapp.cards.Preset;
import com.github.situx.mafiaapp.util.SaveEnum;
import com.github.situx.mafiaapp.util.Utils;
import com.github.situx.mafiaapp.views.edit.EditChar;
import com.github.situx.mafiaapp.views.edit.EditPosition;
import com.github.situx.mafiaapp.views.fragment.AboutDialogFragment;
import com.github.situx.mafiaapp.views.fragment.SavePresetFragment;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static android.app.Activity.RESULT_OK;

/**
 * Created with IntelliJ IDEA.
 * User: timo
 * Date: 21.11.13
 * Time: 13:17
 * To change this template use File | Settings | File Templates.
 */
public class ChooseChars extends ViewUtils {

    private static final int CHAR_VIEW = 0;
    private static final int REARRANGE_CARDS = 1;
    private static final int EDIT_CHAR = 2;
    /**
     * The instance for this class.
     */
    private static ChooseChars instance;
    private boolean firstload=false,newintent=false;
    /**
     * The balance value of this game.
     */
    public Integer balance = 0;
    /**
     * The amount of extracards chosen for the game.
     */
    public Integer extracards = 0;
    /**
     * The amount of players_config chosen for the game.
     */
    public Integer players = 0;
    /**
     * Indicates if all characters need to be called in this game.
     */
    private Boolean calleveryone = false;
    /**
     * CardAdapter for displaying a view to choose characters for the game.
     */
    private CardAdapter cardAdapter;
    private Integer charviewpos = -1;
    /**
     * CardAdapter2 for displaying chars for different purposes.
     */
    private CardAdapter2 editAdapter;
    /**
     * Indicates if this view is in edit mode.
     */
    private Boolean editchars = false;
    /**
     * The GameSet to be used.
     */
    private GameSet gameset;
    /**
     * The list of groups contained in this gameset.
     */
    private Set<Group> grouplist;
    /**
     * Holder class for this Activity.
     */
    private final ChooseCharsHolder holder;
    private Intent intent;
    /**
     * The list of cards included by this gameset.
     */
    private List<Karte> originalcardlist;
    /**
     * Indicates if the round2 view is used.
     */
    private Boolean round2 = false;
    /**
     * The list of cards chosen to begin the game.
     */
    private List<Karte> roundcardlist;
    /**
     * Round counter.
     */
    private Integer rounds = 0;

    /**
     * Constructor for this class.
     */
    public ChooseChars() {
        if (ChooseChars.instance == null) {
            this.holder = new ChooseCharsHolder();
            this.firstload=true;
            turn = false;
            ChooseChars.instance = this;
        } else {
            this.holder = ChooseChars.instance.holder;
            this.gameset = ChooseChars.instance.gameset;
            this.grouplist = ChooseChars.instance.grouplist;
            this.originalcardlist = ChooseChars.instance.originalcardlist;
            this.roundcardlist = ChooseChars.instance.roundcardlist;
            this.rounds = ChooseChars.instance.rounds;
            this.calleveryone = ChooseChars.instance.calleveryone;
            this.editchars = ChooseChars.instance.editchars;
            this.players = ChooseChars.instance.players;
            this.extracards = ChooseChars.instance.extracards;
            this.charviewpos = ChooseChars.instance.charviewpos;
            this.round2 = ChooseChars.instance.round2;
            this.cardAdapter = ChooseChars.instance.cardAdapter;
            this.editAdapter = ChooseChars.instance.editAdapter;
            this.turn = true;
            this.firstload=false;
        }
    }

    @Override
    public void finish() {
        ChooseChars.instance = null;
        super.finish();
    }

    /**
     * Generates a list of cards to play corresponding to the selection given.
     *
     * @return the list if it is valid, null otherwise
     */
    public List<Karte> generateRoundCardList() {
        boolean fraction = false, dependency = false;
        String lastfraction = "";
        List<String> dependencies = new LinkedList<>();
        ChooseChars.this.calleveryone = false;
        ChooseChars.this.roundcardlist = new ArrayList<>();
        for (int i = 0; i < holder.listview.getAdapter().getCount(); i++) {
            if (holder.listview.getAdapter().getItem(i) != null) {
                if ("".equals(lastfraction)) {
                    lastfraction = ChooseChars.this.originalcardlist.get(i).getGroup().getGroupname();
                } else if (!"".equals(lastfraction) && !lastfraction.equals(ChooseChars.this.originalcardlist.get(i).getGroup().getGroupname())) {
                    fraction = true;
                } else if (ChooseChars.this.originalcardlist.get(i).getCalleveryone()) {
                    ChooseChars.this.calleveryone = true;
                }
                int savecurrentamount = ChooseChars.this.originalcardlist.get(i).getCurrentamount();
                ChooseChars.this.originalcardlist.get(i).reset();
                dependencies.addAll(ChooseChars.this.originalcardlist.get(i).getdepends());
                ChooseChars.this.originalcardlist.get(i).setCurrentamount(savecurrentamount);
                ChooseChars.this.roundcardlist.add(ChooseChars.this.originalcardlist.get(i));
            }
        }
        for (Iterator<String> iter = dependencies.iterator(); iter.hasNext(); ) {
            String depend = iter.next();
            for (Karte card : ChooseChars.this.roundcardlist) {
                if (card.getName().equals(depend)) {
                    iter.remove();
                    break;
                }
            }
        }
        if (dependencies.isEmpty()) {
            dependency = true;
        }
        if (ChooseChars.this.roundcardlist.size() < ChooseChars.this.gameset.getFromPlayers()) {
            Utils.dialogtext = ChooseChars.this.getResources().getString(R.string.nocards) + " " + ChooseChars.this.gameset.getFromPlayers()
                    + " " + ChooseChars.this.getResources().getString(R.string.nocards2);
            return null;
        } else if (!fraction) {
            Utils.dialogtext = ChooseChars.this.getResources().getString(R.string.onefraction);
            return null;
        } else if (!dependency) {
            Utils.dialogtext = dependencies.get(0) + " " + ChooseChars.this.getResources().getString(R.string.dependency);
            return null;
        }
        return roundcardlist;
    }

    public int getCharviewpos() {
        return charviewpos;
    }

    public void setCharviewpos(final int charviewpos) {
        this.charviewpos = charviewpos;
    }

    public String[] getPresetArray() {
        List<String> result = new LinkedList<>();
        for (Preset preset : this.gameset.getPresets()) {
            result.add(preset.getPresetName() + " (" + preset.getPlayer() + ")");
        }
        /*Collections.sort(result, new Comparator<String>() {
            public int compare(String s1, String s2) {
                int compareval = Integer.valueOf(s1.substring(s1.indexOf('(') + 1, s1.lastIndexOf(')'))).
                        compareTo(Integer.valueOf(s2.substring(s2.indexOf('(') + 1, s2.lastIndexOf(')'))));
                if (compareval == 0) {
                    return s1.substring(0, s1.indexOf('(')).compareTo(s2.substring(0, s2.indexOf('(')));
                }
                return compareval;
            }
        });*/
        return result.toArray(new String[result.size()]);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case ChooseChars.EDIT_CHAR:break;
                case ChooseChars.CHAR_VIEW:this.originalcardlist.set(this.charviewpos, (Karte) data.getExtras().getParcelable("card"));
                    //holder.listview.invalidateViews();
                    //((CardAdapter) holder.listview.getAdapter()).notifyDataSetChanged();
                    this.charviewpos = -1;break;
                case REARRANGE_CARDS: List<Karte> newcardlist=data.getExtras().getParcelableArrayList("cardlist");
                    Log.e("Newcardlist",newcardlist.toString());
                    this.originalcardlist=newcardlist;
                    Collections.sort(this.originalcardlist, new Comparator<Karte>() {
            public int compare(Karte s1, Karte s2) {
                return s1.getName().compareTo(s2.getName());
            }
        });
                    this.editAdapter=new CardAdapter2(this,this.originalcardlist,this.gameset.getSourcefile(),this.gameset,true);
                    this.editAdapter.notifyDataSetChanged();
                    this.editAdapter.notifyDataSetInvalidated();
                    holder.listview.setAdapter(this.editAdapter);
                    holder.listview.invalidateViews();
                    //this.editAdapter.setCardlist(this.gameset.getCards());
                    //this.editAdapter.notifyDataSetChanged();
                    break;
                default:
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (this.editchars) {
            try {
                File subdir = new File(getFilesDir(), "chars");
                File destination = new File(subdir + "/" + gameset.getSourcefile());
                gameset.setCards(this.originalcardlist);
                List<GameSet> gamesetlist = new LinkedList<>();
                gamesetlist.add(gameset);
                Utils.saveFileToInternalStorage(gamesetlist, SaveEnum.GAMESET, destination);
                Log.e("File written", destination.getAbsolutePath());
                Bundle conData = new Bundle();
                conData.putParcelableArrayList("cardlist", new ArrayList<Karte>(this.originalcardlist));
                conData.putInt("gamesetid", this.getIntent().getExtras().getInt("gamesetid"));
                Intent intent = new Intent();
                intent.putExtras(conData);
                setResult(RESULT_OK, intent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.finish();
    }



    @Override
    public void onCreate(final Bundle savedInstance) {
        if(this.firstload) {
            super.onCreate(savedInstance);
            firstload=false;
        }
        this.setContentView(R.layout.choosechars);
        holder.textview = (TextView) this.findViewById(R.id.chooseCharsTextView);
        holder.button = (Button) this.findViewById(R.id.startgamebutton);
        holder.listview = (ListView) this.findViewById(R.id.listView);
        if (!turn) {
            if(!this.newintent){
                this.intent=this.getIntent();
            }
            this.gameset = this.intent.getParcelableExtra("gameset");
            this.grouplist = this.gameset.getGroups();
        }
        if (new File(this.getFilesDir()+"/chars/"+gameset.getLanguage()+"_"+gameset.getGamesetid()+"/"+gameset.getLanguage()+"_"+gameset.getGamesetid()+".png").exists()) {
            this.getSupportActionBar().setIcon(Utils.loadDrawable(getFilesDir().getAbsolutePath(),gameset.getLanguage()+"_"+gameset.getGamesetid(),gameset.getLanguage()+"_"+gameset.getGamesetid()+".png"));
        } else {
            this.getSupportActionBar().setIcon(R.drawable.title);
        }
        if (turn) {
            this.setTitle(ChooseChars.instance.getTitle());
            if (!this.intent.hasExtra("editchars")) {
                if (gameset.getHasBalance()) {
                    holder.textview.setText(ChooseChars.this.getResources().getString(R.string.players) + ChooseChars.this.players + " + " + ChooseChars.this.getResources().getString(R.string.extracards) + ChooseChars.this.extracards + " (" + ChooseChars.this.getResources().getString(R.string.balancevalue) + ": " + ChooseChars.this.balance + ")");
                } else {
                    holder.textview.setText(ChooseChars.this.getResources().getString(R.string.players) + ChooseChars.this.players + " + " + ChooseChars.this.getResources().getString(R.string.extracards) + ChooseChars.this.extracards);
                }
                holder.listview.setAdapter(this.cardAdapter);
            } else {
                holder.listview.setAdapter(this.editAdapter);
                holder.textview.setVisibility(View.GONE);
                holder.button.setText(R.string.addchar);
                holder.button.setOnClickListener(view -> {
                    final Intent intent = new Intent(ChooseChars.this, EditChar.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    Bundle mBundle = new Bundle();
                    mBundle.putParcelable("card", new Karte());
                    mBundle.putBoolean("addchar", true);
                    mBundle.putString("sourcefile", gameset.getSourcefile());
                    mBundle.putString("icon", gameset.getGamesetImg());
                    mBundle.putParcelable("gameset",gameset);
                    intent.putExtras(mBundle);
                    ChooseChars.this.startActivity(intent);
                });
                this.setTitle(R.string.charview);
            }
        } else {
            if (!this.intent.hasExtra("editchars")) {
                this.originalcardlist = gameset.getCards();
                Collections.sort(this.originalcardlist, (s1, s2) -> s1.getName().compareTo(s2.getName()));
                this.cardAdapter = new CardAdapter(this, this.originalcardlist, gameset.getLanguage()+"_"+gameset.getGamesetid(), gameset.getGamesetImg(), gameset.getHasBalance());
                holder.listview.setAdapter(cardAdapter);
                if (gameset.getHasBalance()) {
                    holder.textview.setText(ChooseChars.this.getResources().getString(R.string.players) + ChooseChars.this.players + " + " + ChooseChars.this.getResources().getString(R.string.extracards) + ChooseChars.this.extracards + " (" + ChooseChars.this.getResources().getString(R.string.balancevalue) + ": " + ChooseChars.this.balance + ")");
                } else {
                    holder.textview.setText(ChooseChars.this.getResources().getString(R.string.players) + ChooseChars.this.players + " + " + ChooseChars.this.getResources().getString(R.string.extracards) + ChooseChars.this.extracards);
                }
                holder.button.setText(R.string.ChooseCharsStartGame);
                holder.button.setOnClickListener(view -> {
                    final Intent intent;
                    if (ChooseChars.this.round2) {
                        intent = new Intent(ChooseChars.this, Round2.class);
                    } else {
                        intent = new Intent(ChooseChars.this, Round2.class);
                    }
                    if (ChooseChars.this.rounds != 0) {
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        Bundle mBundle = new Bundle();

                        mBundle.putParcelableArrayList("cardlist", new ArrayList<Parcelable>(ChooseChars.this.roundcardlist));
                        mBundle.putParcelableArrayList("groups", new ArrayList<>(ChooseChars.this.grouplist));
                        mBundle.putParcelable("gameset",ChooseChars.this.gameset);
                        mBundle.putInt("rounds", ChooseChars.this.rounds);
                        mBundle.putString("icon", gameset.getGamesetImg());
                        mBundle.putString("sourcefile", gameset.getSourcefile());
                        mBundle.putBoolean("calleveryone", ChooseChars.this.calleveryone);
                        intent.putExtras(mBundle);

                        ChooseChars.this.startActivity(intent);
                    } else {
                        ChooseChars.this.roundcardlist = ChooseChars.this.generateRoundCardList();
                        if (ChooseChars.this.roundcardlist == null) {
                            ChooseChars.this.showDialog(0);
                        } else {
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            Bundle mBundle = new Bundle();
                            Collections.sort(ChooseChars.this.roundcardlist, (s1, s2) -> s1.getPosition().compareTo(s2.getPosition()));
                            mBundle.putBoolean("loadgame", false);
                            mBundle.putParcelable("gameset", ChooseChars.this.gameset);
                            mBundle.putParcelableArrayList("cardlist", (ArrayList<Karte>) ChooseChars.this.roundcardlist);
                            mBundle.putParcelableArrayList("groups", new ArrayList<>(ChooseChars.this.grouplist));
                            mBundle.putInt("rounds", ChooseChars.this.rounds);
                            mBundle.putInt("extracards", ChooseChars.this.extracards);
                            mBundle.putString("sourcefile", gameset.getSourcefile());
                            mBundle.putBoolean("calleveryone", ChooseChars.this.calleveryone);
                            intent.putExtras(mBundle);
                            ChooseChars.this.startActivity(intent);
                        }
                    }
                });
                this.setTitle(R.string.choosechars);
            } else {
                this.editchars = true;
                this.grouplist = new TreeSet<>();
                this.originalcardlist = new ArrayList<>(this.gameset.getCards());
                Collections.sort(this.originalcardlist, (s1, s2) -> s1.getName().compareTo(s2.getName()));
                this.editAdapter = new CardAdapter2(this, this.originalcardlist, gameset.getLanguage()+"_"+gameset.getGamesetid(), gameset, true);
                holder.listview.setAdapter(this.editAdapter);
                holder.textview.setVisibility(View.GONE);
                holder.button.setText(R.string.addchar);
                holder.button.setOnClickListener(view -> {
                    final Intent intent = new Intent(ChooseChars.this, EditChar.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    Bundle mBundle = new Bundle();
                    mBundle.putParcelable("card", new Karte());
                    mBundle.putBoolean("addchar", true);
                    mBundle.putString("sourcefile", gameset.getSourcefile());
                    mBundle.putParcelable("gameset",gameset);
                    mBundle.putString("icon", gameset.getGamesetImg());
                    intent.putExtras(mBundle);
                    ChooseChars.this.startActivity(intent);
                });
                this.setTitle(R.string.charview);
            }
        }

    }

    @Override
    protected Dialog onCreateDialog(final int id) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (id) {
            case 0:
                builder.setMessage(Utils.dialogtext)
                        .setCancelable(true)
                        .setIcon(this.getResources().getDrawable(R.drawable.failure))
                        .setTitle(this.getResources().getString(R.string.startfailed))
                        .setPositiveButton(this.getResources().getString(R.string.ok), (dialog, buttonid) -> dialog.cancel());
                break;
            case 1:
                builder.setMessage(Utils.dialogtext)
                        .setCancelable(true)
                        .setIcon(this.getResources().getDrawable(R.drawable.title))
                        .setTitle(this.getResources().getString(R.string.about))
                        .setPositiveButton(this.getResources().getString(R.string.ok), (dialog, buttonid) -> dialog.cancel());
                break;
            case 2:
                if (!this.gameset.getPresets().isEmpty()) {
                    builder.setItems(this.getPresetArray(), (dialogInterface, i) -> {
                        Preset preset = ChooseChars.this.gameset.getPresets().get(i);
                        ChooseChars.this.players = 0;
                        ChooseChars.this.extracards = 0;
                        ChooseChars.this.balance = 0;
                        //Log.e("Preset", "Start Preset");
                        List<Karte> cardlist = ChooseChars.this.gameset.getCards();
                        CardAdapter adapter = (CardAdapter) holder.listview.getAdapter();
                        for (int j = 0; j < holder.listview.getAdapter().getCount(); j++) {
                            String cardid = cardlist.get(j).getCardid();
                            if (adapter.getItem(j) == null && preset.getCardlist().keySet().contains(cardid) ||
                                    adapter.getItem(j) != null && preset.getCardlist().keySet().contains(cardid)) {
                                adapter.getCheckboxvalues().set(j, true);
                                Log.e(cardid, preset.getCardlist().get(cardid) + "");
                                cardlist.get(j).setCurrentamount(preset.getCardlist().get(cardid));
                                int cardamount = preset.getCardlist().get(cardid);
                                ChooseChars.this.players += cardamount;
                                ChooseChars.this.balance += cardlist.get(j).getBalancevalue() * cardamount;
                                if (cardlist.get(j).getExtra() > 0) {
                                    ChooseChars.this.extracards += cardlist.get(j).getExtra();
                                }
                            } else if (adapter.getItem(j) != null && !preset.getCardlist().keySet().contains(cardid)) {
                                adapter.getCheckboxvalues().set(j, false);
                                //cardlist.get(j).setCurrentamount(preset.getCardlist().get(cardlist.get(j).getName()));
                            }
                        }
                        ChooseChars.this.players -= ChooseChars.this.extracards;
                        if (gameset.getHasBalance()) {
                            holder.textview.setText(ChooseChars.this.getResources().getString(R.string.players) + ChooseChars.this.players + " + " + ChooseChars.this.getResources().getString(R.string.extracards) + ChooseChars.this.extracards + " (" + ChooseChars.this.getResources().getString(R.string.balancevalue) + ": " + ChooseChars.this.balance + ")");
                        } else {
                            holder.textview.setText(ChooseChars.this.getResources().getString(R.string.players) + ChooseChars.this.players + " + " + ChooseChars.this.getResources().getString(R.string.extracards) + ChooseChars.this.extracards);
                        }
                        holder.listview.setAdapter(new CardAdapter(ChooseChars.this, cardlist, adapter.getCheckboxvalues(), gameset.getLanguage()+"_"+gameset.getGamesetid(), gameset.getGamesetImg(), gameset.getHasBalance()));
                    })
                            .setCancelable(true)
                            .setIcon(Utils.loadDrawable(this.getFilesDir().getAbsolutePath(),this.gameset.getLanguage()+"_"+this.gameset.getGamesetid(),this.gameset.getLanguage()+"_"+this.gameset.getGamesetid()+".png"))
                            .setTitle(this.getResources().getString(R.string.choosepreset))
                            .setNegativeButton(this.getResources().getString(R.string.cancel), (dialog, buttonid) -> dialog.cancel());
                } else {
                    builder.setMessage(ChooseChars.this.getResources().getString(R.string.nopresets))
                            .setCancelable(true)
                            .setIcon(this.getResources().getIdentifier(gameset.getGamesetImg(), "drawable", this.getPackageName()))
                            .setTitle(this.getResources().getString(R.string.choosepreset))
                            .setNegativeButton(this.getResources().getString(R.string.cancel), (dialog, buttonid) -> dialog.cancel());
                }
                break;
            case 3:
                builder.setMessage(Utils.dialogtext)
                        .setCancelable(true)
                        .setIcon(this.getResources().getDrawable(R.drawable.title))
                        .setTitle(this.getResources().getString(R.string.novalidpreset))
                        .setPositiveButton(this.getResources().getString(R.string.ok), (dialog, buttonid) -> dialog.cancel());
                break;
            default:
        }
        return builder.create();
    }

    @Override
    public void chooseDialogs(final DialogEnum denum) {
        DialogFragment dialog;
        FragmentManager fm = getSupportFragmentManager();
        switch(denum){
            case ABOUT:dialog=new AboutDialogFragment();break;
           case SAVEPRESET: dialog=new SavePresetFragment(this.gameset);break;
            default:dialog=new AboutDialogFragment();
        }
        dialog.show(fm, denum.getName());
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        menu.add(0, 0, 0, this.getResources().getString(R.string.about));
        menu.getItem(0).setOnMenuItemClickListener(item -> {
            Utils.dialogtext = ChooseChars.this.
                    getResources().getString(R.string.copyright);
            ChooseChars.this.chooseDialogs(DialogEnum.ABOUT);
            return true;
        });
        if (!this.intent.hasExtra("editchars")) {
            menu.add(0, 0, 0, this.getResources().getString(R.string.statistics));
            menu.add(0, 0, 0, this.getResources().getString(R.string.savepreset));
            menu.add(0, 0, 0, this.getResources().getString(R.string.openpreset));
            menu.getItem(1).setOnMenuItemClickListener(item -> {
                final Intent intent = new Intent(ChooseChars.this, ViewStats.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                ChooseChars.this.startActivity(intent);
                return true;
            });
            menu.getItem(2).setOnMenuItemClickListener(item -> {
                ChooseChars.this.chooseDialogs(DialogEnum.SAVEPRESET);
                return true;
            });
            menu.getItem(3).setOnMenuItemClickListener(item -> {
                ChooseChars.this.showDialog(2);
                return true;
            });
        }else{
            menu.add(0,0,0,this.getResources().getString(R.string.rearrangePositions));
            menu.getItem(1).setOnMenuItemClickListener(menuItem -> {
                final Intent intent = new Intent(ChooseChars.this, EditPosition.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                Bundle mBundle = new Bundle();
                mBundle.putParcelableArrayList("cardlist", new ArrayList<Karte>(ChooseChars.this.originalcardlist));
                mBundle.putString("gamesetid",ChooseChars.this.gameset.getGamesetid());
                mBundle.putString("language",ChooseChars.this.gameset.getLanguage());
                intent.putExtras(mBundle);
                ChooseChars.this.startActivityForResult(intent, REARRANGE_CARDS);
                return true;
            });
        }
        return true;
    }

    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        this.newintent=true;
        this.intent=intent;
        this.players=0;
        this.extracards=0;
        this.onCreate(null);
        this.supportInvalidateOptionsMenu();
    }

    /**
     * Holder class for this Activity.
     */
    private class ChooseCharsHolder {
        Button button;
        ListView listview;
        TextView textview;
    }
}
