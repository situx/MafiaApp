package com.example.LNDWA.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.example.LNDWA.R;
import com.example.LNDWA.adapters.*;
import com.example.LNDWA.cards.*;
import com.example.LNDWA.util.Utils;
import com.example.LNDWA.util.parser.PlayerParse;
import com.example.LNDWA.views.fragment.AboutDialogFragment;
import com.example.LNDWA.views.fragment.ChoosePlayerFragment;
import com.example.LNDWA.views.fragment.GameLogFragment;
import com.example.LNDWA.views.fragment.Round2Fragment;

import java.util.*;

/**
 * Round2 View for displaying round status.
 */
public class Round2 extends ViewUtils implements
        ActionBar.TabListener, GameFieldAPI {
    private static final int RANDOMASSIGN = 0;
    public static Round2 instance;
    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;

    private String dialogdisplaytag;
    /**
     * The icon of this GameSet to display in the ActionBar.
     */
    protected Boolean end=false,turn=false;

    /**
     * Array of player names.
     */
    public List<Player> playerarray;

    /**
     * Parser class to parse players_config available for playing.
     */
    protected PlayerParse players;


    Integer currentfragmentid = 0;

    public Round2Fragment currentfragment;
    private LinearLayout fragContainer;
    private LinearLayout ll;
    /**
     * Constructor for this class.
     */
    public Round2() {
        if(instance==null){
            instance=this;
            //Utils.initarray();
            this.turn=false;
        }else{
            this.fragContainer=instance.fragContainer;
            this.currentfragmentid=instance.currentfragmentid;
            this.mAdapter=instance.mAdapter;
            this.actionBar=instance.actionBar;
            this.viewPager=instance.viewPager;
            this.turn=true;
        }
    }

    @Override
    public void finish() {
        instance = null;
        super.finish();
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public TabsPagerAdapter getmAdapter() {
        return mAdapter;
    }

    @Override
    public void loadGame(final String savegamepath) {
        Round2Fragment fragment = new Round2Fragment();
        Bundle bundle = this.getIntent().getExtras();
        bundle.putString("savegamepath",savegamepath);
        fragment.setArguments(bundle);
        this.mAdapter.addFragment(fragment,mAdapter.getCount()-1,bundle);
        this.currentfragment=(Round2Fragment)mAdapter.getFragment(mAdapter.getCount()-1);
        actionBar.addTab(actionBar.newTab().setText((mAdapter.getCount())+"").setTabListener(this));
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if(resultCode==RESULT_OK) {
            switch (requestCode) {
                case RANDOMASSIGN: List<Karte> cardlist=data.getExtras().getParcelableArrayList("cardlist");
                                   if(data.getExtras().getBoolean("shuffle")){
                                       this.currentfragment.randomAssignment(cardlist);
                                   }
                                   break;
                default:
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (!end) {
            Toast.makeText(getBaseContext(), "Resetting one step, long press for exit!", Toast.LENGTH_SHORT).show();
            //Utils.round2fragments[currentfragmentid].goBackOneStep();
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.round2tab);
        if (savedInstanceState != null) {
            this.currentfragmentid=savedInstanceState.getInt("position");
            if(savedInstanceState.containsKey("displayedDialog")){
                this.getSupportFragmentManager().beginTransaction()
                        .show(this.getSupportFragmentManager().findFragmentByTag(savedInstanceState.get("displayedDialog").toString())).commit();
            }
            //mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
            //mAdapter.addFragment((Round2Fragment) getSupportFragmentManager().getFragment(
                //savedInstanceState, Round2Fragment.class.getName()),0,null);
        }else{
            this.currentfragmentid=0;
        }

        //setContentView(R.layout.activity_main);
        if(!turn) {
            // Initilization
            viewPager = (ViewPager) findViewById(R.id.pager);
            viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    actionBar.setSelectedNavigationItem(position);
                    Round2.this.currentfragmentid=position;
                    Round2.this.currentfragment=(Round2Fragment)Round2.this.mAdapter.getFragment(position);
                    Round2.this.getSupportActionBar().setIcon(Utils.loadDrawable(Round2.this.getFilesDir().getAbsolutePath(),Round2.this.currentfragment.getGameset().getLanguage()+"_"+Round2.this.currentfragment.getGameset().getGamesetid(),Round2.this.currentfragment.getGameset().getLanguage()+"_"+Round2.this.currentfragment.getGameset().getGamesetid()+".png"));
                    Round2.this.setTitle(Round2.this.getResources().getString(R.string.round)+" "+Round2.this.currentfragment.getRounds());
                }
            });
            actionBar = getSupportActionBar();
            mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
            mAdapter.addFragment(new Round2Fragment(),0,this.getIntent().getExtras());
            viewPager.setAdapter(mAdapter);
            actionBar.setHomeButtonEnabled(false);
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            for(int i=0;i<this.mAdapter.getCount();i++){
                actionBar.addTab(actionBar.newTab().setText(this.getResources().getString(R.string.game)+" "+(i+1)+"").setTabListener(this));
            }
            Log.e("Add to ActionBar","NOW");
            this.currentfragment=(Round2Fragment)mAdapter.getFragment(this.currentfragmentid);
        }else{
            viewPager = (ViewPager) findViewById(R.id.pager);
            viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    actionBar.setSelectedNavigationItem(position);
                    Round2.this.currentfragmentid=position;
                    Round2.this.currentfragment=(Round2Fragment)Round2.this.mAdapter.getFragment(position);
                    Round2.this.getSupportActionBar().setIcon(Utils.loadDrawable(Round2.this.getFilesDir().getAbsolutePath(),Round2.this.currentfragment.getGameset().getLanguage()+"_"+Round2.this.currentfragment.getGameset().getGamesetid(),Round2.this.currentfragment.getGameset().getLanguage()+"_"+Round2.this.currentfragment.getGameset().getGamesetid()+".png"));
                    Round2.this.setTitle(Round2.this.getResources().getString(R.string.round)+" "+Round2.this.currentfragment.getRounds());
                }
            });
            actionBar = getSupportActionBar();
            viewPager.setAdapter(mAdapter);
            for(int i=0;i<this.mAdapter.getCount();i++){
                actionBar.addTab(actionBar.newTab().setText(this.getResources().getString(R.string.game)+" "+(i+1)+"").setTabListener(this));
            }
            this.currentfragment=(Round2Fragment)mAdapter.getFragment(this.currentfragmentid);
            actionBar.setHomeButtonEnabled(false);
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        }
        this.getSupportActionBar().hide();
        actionBar.hide();
    }

    public void chooseDialogs(DialogEnum denum){
        DialogFragment dialog;
        FragmentManager fm = getSupportFragmentManager();
        if(fm.findFragmentByTag(denum.getName())!=null){
            fm.beginTransaction().remove(fm.findFragmentByTag(denum.getName())).commit();
        }
        switch(denum){
            case ABOUT:dialog=new AboutDialogFragment();

                break;
          case CHOOSEPLAYER:this.playerarray = this.currentfragment.getCurrentPlayersArray();

              dialog = new ChoosePlayerFragment();

              break;
            case GAMELOG: dialog=new GameLogFragment(this.currentfragment.getGameLog());break;
          /*case CHOOSECHARS:break;
          */
            default: dialog=new AboutDialogFragment();
        }
        this.dialogdisplaytag=denum.getName();
        fm.beginTransaction().add(dialog,denum.getName()).commit();
        fm.beginTransaction().show(dialog).commit();
        //fm.beginTransaction().epdialog.show(fm, denum.getName());
    }

    @Override
    protected Dialog onCreateDialog(final int id) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (id) {
            case 0:
                builder.setMessage(Utils.dialogtext)
                        .setCancelable(true)
                        .setIcon(this.getResources().getDrawable(R.drawable.title))
                        .setTitle(this.getResources().getString(R.string.about))
                        .setPositiveButton(this.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int buttonid) {
                                dialog.cancel();
                            }
                        });
                break;
            case 1:
                builder.setTitle(R.string.pick_char)
                        .setAdapter(new DialogCardAdapter(this, this.currentfragment.getCurrentCardsArray(),this.currentfragment.getSourcefile()), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Round2.this.currentfragment.setCardsDialog(which,true);
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                break;
            case 2:
                builder.setTitle(R.string.pick_player)
                        .setAdapter(new PlayerListAdapter(this, this.playerarray, 2), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(final DialogInterface dialogInterface, final int i) {
                                Round2.this.currentfragment.setPlayerDialog(i);
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                break;
            case 3:
                final DialogCardAdapter adapter = new DialogCardAdapter(this, this.currentfragment.getCurrentCardsArray(), true, this.currentfragment.getExtracardamount(),this.currentfragment.getSourcefile());
                AlertDialog dialog = builder.setTitle(this.getResources().getString(R.string.chooseextracards) + " (" + this.currentfragment.getExtracardamount() + ")")

                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Round2.this.finish();
                            }
                        }).setAdapter(adapter, null).create();
                dialog.setButton(Dialog.BUTTON_POSITIVE, Round2.this.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, final int i) {
                        Log.e("Cardarray", Round2.this.currentfragment.getCurrentCardsArray().toString());
                        ArrayList<Integer> keys = new ArrayList<>(adapter.checkedItems.keySet());
                        for (int j = keys.size() - 1; j >= 0; j--) {
                            Round2.this.currentfragment.getCurrentExtraCardsArray().add(Round2.this.currentfragment.getCurrentCardsArray().get(keys.get(j)));
                            Round2.this.currentfragment.getCurrentCardsArray().remove(keys.get(j).intValue());
                        }
                        Log.e("Cardarray", Round2.this.currentfragment.getCurrentCardsArray().toString());
                        Log.e("Extracardslist", Round2.this.currentfragment.getCurrentExtraCardsArray().toString());
                        adapter.checkedItems.clear();
                    }
                });
                dialog.getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                adapter.setListView(dialog.getListView());
                return dialog;
            case 4:
                builder.setTitle(this.getResources().getString(R.string.extracards))
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }).setAdapter(new DialogCardAdapter(Round2.this, Round2.this.currentfragment.getCurrentExtraCardsArray(),this.currentfragment.getSourcefile()), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, final int i) {

                    }
                });
                break;
            case 5:
                String message = this.currentfragment.getNewEvent();
                builder.setTitle(this.getResources().getString(R.string.event) + " " + message.substring(0, message.indexOf('\n')))
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }).setMessage(Html.fromHtml(this.currentfragment.getNewEvent().substring(message.indexOf('\n'))));

                break;
            case 6:
                builder.setTitle(this.getResources().getString(R.string.kill))
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Round2.this.currentfragment.killCharacter(Round2.this.currentfragment.getLastcheckedrelId());
                            }
                        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }).setMessage(this.getResources().getString(R.string.reallykill) + "?");
                break;
            case 7:
                builder.setTitle(this.getResources().getString(R.string.switchchar))
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }).setAdapter(new DialogCardAdapter(Round2.this, this.currentfragment.getCurrentExtraCardsArray(),this.currentfragment.getSourcefile()), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, final int i) {
                        Karte current = Round2.this.currentfragment.getCurrentcard();
                        Karte toChange = Round2.this.currentfragment.getCurrentExtraCardsArray().get(i);
                        Round2.this.currentfragment.switchExtraCharWithChar(current, toChange, true, Round2.this.currentfragment.getLastactivecharacterrel().iterator().next().getId(), true, false);
                    }
                });

                break;
            case 8:
                builder.setTitle(this.getResources().getString(R.string.deadcards))
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }).setAdapter(new DialogCardAdapter(Round2.this, Round2.this.currentfragment.getDeadCardsArray(),this.currentfragment.getSourcefile()), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, final int i) {

                    }
                });
                break;
            case 9:
                builder.setTitle(this.getResources().getString(R.string.chooseability))
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }).setAdapter(new DialogAbilityAdapter(Round2.this, Round2.this.currentfragment.getAbilitydialoglist(),this.currentfragment.getGameset().getGamesetid(),this.currentfragment.getGameset().getLanguage(),this.currentfragment.getCurrentcard().getCardid()), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, final int i) {
                        Round2.this.currentfragment.executeAbility(Round2.this.currentfragment.getCardlist().get(
                                Round2.this.currentfragment.getRelToCard().get(Round2.this.currentfragment.getLastcheckedrel().getId())), Round2.this.currentfragment.getLastcheckedrel(), Round2.this.currentfragment.getAbilitydialoglist().get(i), currentfragment.getCounter() - 1);
                    }
                });
                break;
            case 10:
                Karte currentcard = this.currentfragment.getCardlist().get(this.currentfragment.getRelToCard().get(this.currentfragment.getLastcheckedrelId()));
                String displaymessage = "Dead? " + Round2.this.currentfragment.getCardToRel().get(currentcard).get(this.currentfragment.getLastcheckedrelId()) + "\n";
                Log.e("LastCheckedRel", this.currentfragment.getLastcheckedrelId() + "");
                Log.e("CardToCardOvery Output:", this.currentfragment.getCardTocardOverlays().toString());
                Log.e("Beep: ", this.currentfragment.getRelToCard().get(this.currentfragment.getLastcheckedrelId()).toString());
                Log.e("Card: ", this.currentfragment.getCardlist().get(this.currentfragment.getRelToCard().get(this.currentfragment.getLastcheckedrelId())).toString());
                List<Ability> abilitylist = new LinkedList<>();
                Ability ability = new Ability();
                ability.setImage(currentcard.getImg());
                ability.setDescription("Dead? " + Round2.this.currentfragment.getCardToRel().get(currentcard).get(this.currentfragment.getLastcheckedrelId()));
                abilitylist.add(ability);
                for (Ability abb : currentcard.getabblist()) {
                    abilitylist.add(abb);
                }
                Log.e("CardToCardOverlays",this.currentfragment.getCardTocardOverlays().toString());
                if(this.currentfragment.getCardTocardOverlays().keySet().contains(this.currentfragment.getLastcheckedrelId())) {
                    for (Integer abb : this.currentfragment.getCardTocardOverlays().get(this.currentfragment.getLastcheckedrelId()).keySet()) {
                        abilitylist.add(this.currentfragment.getCardlist().get(abb).getUuidToAbility().get(this.currentfragment.getCardTocardOverlays().get(this.currentfragment.getLastcheckedrelId()).get(abb).getOne()));
                        displaymessage += this.currentfragment.getCardlist().get(abb).getUuidToAbility().get(this.currentfragment.getCardTocardOverlays().get(this.currentfragment.getLastcheckedrelId()).get(abb).getOne()).getDescription() + "\n";
                    }
                }
                builder.setTitle(currentcard.getName() + "(" + currentcard.getGroup() + ")")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }).setAdapter(new DialogAbilityAdapter(this, new LinkedList<>(currentcard.getabblist()),this.currentfragment.getGameset().getGamesetid(),this.currentfragment.getGameset().getLanguage(),currentcard.getCardid()), null)
                        .setIcon(Utils.loadDrawable(this.getFilesDir().getAbsolutePath(),this.currentfragment.getGameset().getLanguage()+"_"+this.currentfragment.getGameset().getGamesetid(),currentcard.getCardid()+".png"));
                break;
            case 11:
                builder.setTitle(this.getResources().getString(R.string.deadcards))
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }).setAdapter(new DialogCardAdapter(Round2.this, Round2.this.currentfragment.getDeadCardsArray(),this.currentfragment.getSourcefile()), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, final int i) {
                        Karte current = Round2.this.currentfragment.getCurrentcard();
                        Karte toChange = Round2.this.currentfragment.getDeadCardsArray().get(i);
                        Round2.this.currentfragment.switchExtraCharWithChar(current, toChange, true,
                                Round2.this.currentfragment.getCardToRel().get(current).keySet().iterator().next(), false, false);
                    }
                });
                break;
            case 12:
                builder.setTitle(this.getResources().getString(R.string.choosegroup))
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }).setAdapter(new GroupAdapter(this, Round2.this.currentfragment.getGroupdialogarray()), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, final int i) {
                        Round2.this.currentfragment.setGroup(i);
                    }
                });
                break;
            case 13:
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setIcon(this.getResources().getDrawable(R.drawable.title))
                        .setTitle(this.getResources().getString(R.string.savegamedialog)
                        ).setPositiveButton(this.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int buttonid) {
                        Round2.this.saveGame();
                        Toast toast = Toast.makeText(Round2.this, Round2.this.getResources().getString(R.string.gamesaved), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }).setNegativeButton(this.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, final int i) {
                        dialogInterface.cancel();
                    }
                }).setView(input);
                break;
            case 14:
                builder.setItems(this.currentfragment.getGameLog(),null)
                        .setCancelable(true)
                        .setTitle(this.getResources().getString(R.string.showgamelog))
                        .setPositiveButton(this.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int buttonid) {
                                dialog.cancel();
                            }
                        });
                break;
            case 15:

                Integer result=this.currentfragment.getCurrentcheck().evaluate(this.currentfragment.getCardlist()
                        .get(this.currentfragment.getRelToCard().get(this.currentfragment.getLastcheckedrelId())),this.currentfragment.getCardlist(),this.currentfragment.getDeadCardsArray());
                LayoutInflater inflater=LayoutInflater.from(this);
                View view=inflater.inflate(R.layout.dialogpicture,(LinearLayout)this.findViewById(R.id.pictureDialogLayout),false);
                ImageView imgview=(ImageView)view.findViewById(R.id.pictureDiaologImageView);
                if(result==0 && !this.currentfragment.getCurrentcheck().getCount()){
                    imgview.setImageDrawable(this.getResources().getDrawable(R.drawable.thumbdown));
                }else if(result==1 && !this.currentfragment.getCurrentcheck().getCount()){
                    imgview.setImageDrawable(this.getResources().getDrawable(R.drawable.thumbup));
                }
                imgview.setAdjustViewBounds(true);
                builder.setView(view)
                        .setCancelable(true)
                        .setTitle(this.getResources().getString(R.string.results))
                        .setPositiveButton(this.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int buttonid) {
                                dialog.cancel();
                            }
                        });
                break;
            default:
        }
        Dialog dialog=builder.create();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 1.0f;

        return dialog;
    }

    @Override
    protected void onNewIntent(final Intent intent) {
        Round2Fragment fragment = new Round2Fragment();
        Bundle bundle = intent.getExtras();
        fragment.setArguments(bundle);
        this.mAdapter.addFragment(fragment,mAdapter.getCount()-1,bundle);
        this.currentfragment=(Round2Fragment)mAdapter.getFragment(mAdapter.getCount()-1);
        this.getSupportActionBar().setIcon(Utils.loadDrawable(this.getFilesDir().getAbsolutePath(),this.currentfragment.getGameset().getLanguage()+"_"+this.currentfragment.getGameset().getGamesetid(),this.currentfragment.getGameset().getLanguage()+"_"+this.currentfragment.getGameset().getGamesetid()+".png"));
        actionBar.addTab(actionBar.newTab().setText(this.getResources().getString(R.string.game) + " " + (mAdapter.getCount()) + "").setTabListener(this));
        viewPager.setCurrentItem(mAdapter.getCount()-1);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        menu.add(0, 0, 0, this.getResources().getString(R.string.about));
        menu.add(0, 0, 0, this.getResources().getString(R.string.savegame));
        menu.add(0, 0, 0, this.getResources().getString(R.string.extracards));
        menu.add(0, 0, 0, this.getResources().getString(R.string.deadcards));
        menu.add(0, 0, 0, "Test Rotation");
        menu.add(0, 0, 0, "End Round");
        menu.add(0, 0, 0, "New Round2");
        menu.add(0,0,0,"Left Characters");
        menu.add(0,0,0,"Character Test");
        menu.add(0,0,0,this.getResources().getString(R.string.showgamelog));
        menu.add(0,0,0,"Quit");
        menu.add(0,0,0,"RandomAssignView");
        menu.add(0,0,0,"RoundAssignedView");
        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                Utils.dialogtext = Round2.this.
                        getResources().getString(R.string.copyright);
                Round2.this.chooseDialogs(DialogEnum.ABOUT);
                return true;
            }
        });
        menu.getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                Round2.this.showDialog(13);
                return true;
            }
        });
        menu.getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                Round2.this.showDialog(4);
                return true;
            }
        });
        menu.getItem(3).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                Round2.this.showDialog(8);
                return true;
            }
        });
        menu.getItem(4).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                Round2.this.currentfragment.changeCharsInDirection(0, false, 1);
                return true;
            }
        });
        menu.getItem(5).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                Round2.this.currentfragment.goToRoundEnd();
                return true;
            }
        });
        menu.getItem(6).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                Round2Fragment fragment=new Round2Fragment();

                final Intent intent = new Intent(Round2.this, LNDWA.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                Round2.this.startActivity(intent);
                return true;
            }
        });
        menu.getItem(7).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                Round2.this.showDialog(8);
                return true;
            }
        });
        menu.getItem(8).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                Round2.this.chooseDialogs(DialogEnum.GAMELOG);
                return true;
            }
        });
        menu.getItem(9).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final MenuItem menuItem) {
                //Utils.currentimgpath=Round2.this.currentfragment.getCardlist().get(Round2.this.currentfragment.getRelToCard().get(0)).getImg();
                Round2.this.showDialog(15);
                return true;
            }
        });
        menu.getItem(10).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final MenuItem menuItem) {
                Round2.this.finish();
                return true;
            }
        });
        menu.getItem(11).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final MenuItem menuItem) {

                final Intent intent = new Intent(Round2.this, RandomAssignView.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                Bundle mBundle = new Bundle();
                mBundle.putParcelableArrayList("cardlist", new ArrayList<Parcelable>(Round2.this.currentfragment.getCurrentCardsArray()));
                mBundle.putStringArrayList("playerlist", new ArrayList<String>(Round2.this.currentfragment.getTextViews()));
                mBundle.putString("gamesetid", Round2.this.currentfragment.getGameset().getGamesetid());
                mBundle.putString("language", Round2.this.currentfragment.getGameset().getLanguage());
                mBundle.putBoolean("shuffle", true);
                intent.putExtras(mBundle);
                Round2.this.startActivityForResult(intent, RANDOMASSIGN);
                return true;
            }
        });
        menu.getItem(12).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final MenuItem menuItem) {

                final Intent intent = new Intent(Round2.this, RandomAssignView.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                Bundle mBundle = new Bundle();
                List<Karte> cardlist=new LinkedList<Karte>();
                for(int i=0;i<Round2.this.currentfragment.getRelToCard().keySet().size();i++){
                    cardlist.add(Round2.this.currentfragment.getCardlist().get(Round2.this.currentfragment.getRelToCard().get(i)));
                }
                mBundle.putParcelableArrayList("cardlist", new ArrayList<Parcelable>(cardlist));
                mBundle.putStringArrayList("playerlist", new ArrayList<String>(Round2.this.currentfragment.getTextViews()));
                mBundle.putString("gamesetid", Round2.this.currentfragment.getGameset().getGamesetid());
                mBundle.putString("language", Round2.this.currentfragment.getGameset().getLanguage());
                mBundle.putBoolean("shuffle", false);
                intent.putExtras(mBundle);
                Round2.this.startActivityForResult(intent, RANDOMASSIGN);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onKeyLongPress(final int keyCode, final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            if(this.getSupportActionBar().isShowing()){
                 this.getSupportActionBar().hide();
            }else{
                this.getSupportActionBar().show();
            }
        return true;
    }

    @Override
    protected void onPrepareDialog(final int id, final Dialog dialog) {
        switch (id) {
            case 3:
                AlertDialog dia = (AlertDialog) dialog;
                ((DialogCardAdapter) dia.getListView().getAdapter()).setButton(dia.getButton(Dialog.BUTTON_POSITIVE));
                break;
            default:
                removeDialog(id);
        }
    }

    @Override
    public void onTabSelected(final ActionBar.Tab tab, final FragmentTransaction fragmentTransaction) {
        this.viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(final ActionBar.Tab tab, final FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(final ActionBar.Tab tab, final FragmentTransaction fragmentTransaction) {

    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position",currentfragmentid);
        outState.putString("dialogDisplayed", this.dialogdisplaytag);
    }


    /**
     * Prepares the game for saving to the harddrive.
     */
    @Override
    public void saveGame() {
        this.currentfragment.saveGame(true);
    }


    public void setPlayers(List<Player> players) {
        Log.e("SetPlayers", players.toString());
        this.playerarray = players;
    }

    public void setFragmentId(Round2Fragment fragment){
         //this.currentfragmentid=Utils.round2fragments.indexOf(fragment);
    }



}
