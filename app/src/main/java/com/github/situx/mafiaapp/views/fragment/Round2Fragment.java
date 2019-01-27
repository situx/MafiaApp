package com.github.situx.mafiaapp.views.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayout;
import android.text.Html;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.github.situx.mafiaapp.R;
import com.github.situx.mafiaapp.adapters.AbilityAdapter;
import com.github.situx.mafiaapp.adapters.TabsPagerAdapter;
import com.github.situx.mafiaapp.cards.Ability;
import com.github.situx.mafiaapp.cards.Action;
import com.github.situx.mafiaapp.cards.Event;
import com.github.situx.mafiaapp.cards.GameSet;
import com.github.situx.mafiaapp.cards.Group;
import com.github.situx.mafiaapp.cards.Karte;
import com.github.situx.mafiaapp.cards.Operator;
import com.github.situx.mafiaapp.cards.Player;
import com.github.situx.mafiaapp.util.Round2Enum;
import com.github.situx.mafiaapp.util.Triple;
import com.github.situx.mafiaapp.util.Tuple;
import com.github.situx.mafiaapp.util.Utils;
import com.github.situx.mafiaapp.util.parser.GameParse;
import com.github.situx.mafiaapp.util.parser.PlayerParse;
import com.github.situx.mafiaapp.views.DialogEnum;
import com.github.situx.mafiaapp.views.Results;
import com.github.situx.mafiaapp.views.Round2;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.*;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by timo on 11.04.14.
 */
public class Round2Fragment extends Fragment {

    private final List<RelativeLayout> backrellist = new LinkedList<>();
    private final Boolean calleveryone = false;
    private final List<String> gamelog = new LinkedList<>();
    /**
     * List of abilities to show in a dialog.
     */
    private List<Ability> abilitydialoglist;
    private Long back_pressed = 0L;
    /**
     * Remembers exchanges of characters and their duration.
     */
    private Map<Integer, Tuple<Karte, Integer>> cardExchangeMap;
    /**
     * Map from card to RelativeLayoutIDs.
     */
    private Map<Karte, Map<Integer, Boolean>> cardToRel;
    /**
     * Map from cards to cardOverlays.
     */
    private Map<Integer, Map<Integer, Triple<String, Integer, Integer>>> cardTocardOverlays;
    /**
     * Map from cardname to cardId.
     */
    private Map<String, Set<Integer>> cardUUIDTocardId;
    /**
     * List of cards.
     */
    private List<Karte> cardarray;

    public Operator getCurrentcheck() {
        return currentcheck;
    }

    public GameSet getGameset() {
        return gameset;
    }

    public String getSourcefile() {
        return this.gameset.getSourcefile();
    }

    public List<String> getTextViews() {
        List<String> result=new LinkedList<>();
        for(TextView txtview:this.textviewlist){
            result.add(txtview.getText().toString());
        }
        return result;
    }

    public void randomAssignment(final List<Karte> cardlist) {
        this.cardarray=cardlist;
        int j=-1;
        for(int i=0;i<cardlist.size();i++){
            j++;
            while(this.relToCard.get(j)!=-1 && j<cardlist.size()-1){
                j++;
            }
            this.currentRelId=j;
            this.setCardsDialog(i, false);
        }
        this.cardarray.clear();
    }

    public void setCurrentcheck(final Operator currentcheck) {
        this.currentcheck = currentcheck;
    }

    private Operator currentcheck;
    /**
     * The list of cards for this activity.
     */
    private List<Karte> cardlist;
    private List<Karte> cardlist2;
    private Map<Integer, Karte> checkedItems;
    /**
     * OnClickListener to choose a player.
     */
    private View.OnClickListener choosePlayer;
    private Integer concernedtemp = 0;
    private Integer counter = 0, position2counter = 0, rounds = 0, currentplayerid = 0, currentRelId = 0,
            cardcount = -1, extracardamount = 0, currentevent = -1, loadgameskipcount = 0;
    /**
     * Temporary cards for saving temporary status data.
     */
    private Karte currentcard, lastcard;
    private List<Karte> deadcardarray;
    private Boolean end = false;
    /**
     * List of events for this class.
     */
    private List<Event> events;
    /**
     * The list of extracardamount for this activity.
     */
    private List<Karte> extracardlist;
    private Boolean firstload = true;
    /**
     * List of games available to play.
     */
    private GameSet gameset;
    /**
     * The gridlayout to use.
     */
    private GridLayout gridLayout;
    /**
     * List of string terms to put into a dialog.
     */
    private List<Group> groupdialogarray = new LinkedList<>();
    /**
     * Map of groups with an exisiting indicator.
     */
    private Map<Group, Integer> groupmap;
    /**
     * Holder class for this Activity.
     */
    private Round2Holder holder;
    /**
     * The icon of this GameSet to display in the ActionBar.
     */
    private String icon;
    private Boolean initfinished = true;
    private Integer instanceid;
    private List<RelativeLayout> lastactivecharacterrel = new LinkedList<>();
    private RelativeLayout lastcheckedrel;
    private List<Integer> lastrelcolor;
    /**
     * List of loaded players_config if loaded from a file.
     */
    private List<String> loadedplayers;
    private RelativeLayout mainbackrel;
    private Integer numberofcards;
    private Integer numberofviews;
    private List<Player> playerList;
    /**
     * Array of player names.
     */
    private List<Player> playerarray;
    /**
     * Map from playername to playerId.
     */
    private Map<String, Integer> playernameToPlayerId;
    /**
     * Parser class to parse players_config available for playing.
     */
    private PlayerParse players;
    /**
     * List of position2.
     */
    private List<Karte> position2list;
    /**
     * Random object to generate randomised events.
     */
    private Random random;
    /**
     * Map from RelativeLayoutID to cardid.
     */
    private Map<Integer, Integer> relToCard;
    /**
     * Maps from a card id to its relid and the containing overlay imageview.
     */
    private Map<Integer, Map<Integer, List<ImageView>>> relToImgView;
    /**
     * Map from a relation id to a player.
     */
    private Map<Integer, Player> relToPlayer;
    /**
     * List of RelativeLayouts used in this activity.
     */
    private List<RelativeLayout> relviewlist;
    private Round2 round2;
    private Boolean roundEnd = false;
    /**
     * The ScrollView which includes the whole Activity.
     */
    private ScrollView scrollView;
    /**
     * Status enum for setting the status of a used ability.
     */
    private Round2Enum status;
    private final View.OnClickListener roundCharOnClick = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            boolean ondead=false;
            Round2Fragment.this.lastcheckedrel = (RelativeLayout) view;
            Karte current = Round2Fragment.this.cardlist.get(Round2Fragment.this.relToCard.get(view.getId()));
            Integer source = 0;
            if (Round2Fragment.this.roundEnd && !current.isdead() && Round2Fragment.this.status != Round2Enum.ON_DEAD_MUST_CHOOOSE) {
                //this.round2.setDeadCards(this.getDeadCardsArray());
                Round2Fragment.this.round2.showDialog(6);
            } else {
                Log.e("CurrentCard", currentcard.getabblist().toString());
                if (Round2Fragment.this.status != Round2Enum.ON_DEAD_MUST_CHOOOSE) {
                    Round2Fragment.this.abilitydialoglist.clear();
                    for (Ability abb : currentcard.getabblist()) {
                        if (abb.getActive() && abb.checkFromUntil(Round2Fragment.this.rounds)
                                && abb.getCurrentamount() > 0 && !abb.getOndead()) {
                            Round2Fragment.this.abilitydialoglist.add(abb);
                        }
                    }
                    source = counter - 1;
                } else {
                    Round2Fragment.this.status = Round2Enum.MUST_CHOOSE_CONCERNED;
                    source = Round2Fragment.this.relToCard.get(lastactivecharacterrel.get(0).getId());
                    ondead=true;
                    Log.e("ON DEAD MUST CHOOSE", source.toString());
                }
                Log.e("Execute Ability: ", Round2Fragment.this.abilitydialoglist.toString());
                if (Round2Fragment.this.abilitydialoglist.isEmpty()) {
                    Round2Fragment.this.round2.showDialog(10);
                } else if (Round2Fragment.this.abilitydialoglist.size() == 1) {
                    Round2Fragment.this.executeAbility(current, (RelativeLayout) view, Round2Fragment.this.abilitydialoglist.get(0), source);
                } else {
                    Round2Fragment.this.round2.showDialog(9);
                }
                if(ondead && Round2Fragment.this.status==Round2Enum.ROUND){
                    Round2Fragment.this.lastcheckedrel=Round2Fragment.this.lastactivecharacterrel.get(0);
                    Round2Fragment.this.killCharacter(Round2Fragment.this.lastcheckedrel.getId());
                }
            }

        }
    };
    private final View.OnClickListener viewOnClick = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            if (Round2Fragment.this.counter == cardlist.size() && Round2Fragment.this.position2counter == Round2Fragment.this.position2list.size()) {
                Round2Fragment.this.executeRoundEnd();
            } else {
                if (Round2Fragment.this.status == Round2Enum.CAN_CHOOSE_CONCERNED) {
                    Round2Fragment.this.status = Round2Enum.ROUND;
                }
                if (Round2Fragment.this.currentcard != null) {
                    Round2Fragment.this.lastcard = Round2Fragment.this.currentcard;
                }
                if (Round2Fragment.this.counter == Round2Fragment.this.cardlist.size()) {
                    Round2Fragment.this.getActivity().setTitle(Round2Fragment.this.getResources().getString(R.string.round) + " " + Round2Fragment.this.rounds);
                    currentcard = Round2Fragment.this.position2list.get(position2counter++);
                    Round2Fragment.this.counter--;
                } else {
                    currentcard = Round2Fragment.this.cardlist.get(Round2Fragment.this.counter);
                }
                if ((currentcard.isdead() && !Round2Fragment.this.calleveryone)
                        || (currentcard.getRound() != 0
                        && !currentcard.getRound().equals(Round2Fragment.this.rounds))
                        || (currentcard.getRound() < -1 && (Round2Fragment.this.rounds % (currentcard.getRound() * -1)) != 0)) {
                    Round2Fragment.this.counter++;
                    onClick(holder.view);
                    return;
                }
                Round2Fragment.this.executeNextStep(view, false);
            }
        }
    };
    private List<Tuple<Karte, Integer>> switchchartempmap;
    /**
     * List of TextViews used in this activity.
     */
    private List<TextView> textviewlist;
    private Boolean turn = false;
    /**
     * The winning group as String.
     */
    private String winninggroup;

    @SuppressLint("ValidFragment")
    public Round2Fragment(Round2 round2, Boolean firstload, Boolean turn) {
        this.round2 = round2;
        this.firstload = firstload;
        this.turn = turn;
    }

    public Round2Fragment() {
        this.round2 = (Round2) this.getActivity();
        //this.lastcheckedrel=new RelativeLayout(this.round2);
        //this.lastcheckedrel.setId(0);
    }

    /**
     * Adds an overlay to the given character.
     *
     * @param charid  the character id
     * @param ability the ability to get the overlay from
     */
    private void addOverlayToChar(final Integer charid, final Ability ability, final Integer source) {
        Log.e("Add Overlay To Char:", ability.toString() + " " + source + " " + charid);
        ImageView imageView = new ImageView(this.getActivity());
        File file=new File(this.getActivity().getFilesDir().getAbsolutePath()+"/chars/"+this.gameset.getLanguage()+"_"+this.gameset.getGamesetid()+"/overlay/"+ability.getAbilityId()+".png");
        if (!file.exists()) {
            File file2=new File(this.getActivity().getFilesDir().getAbsolutePath()+"/chars/"+this.gameset.getLanguage()+"_"+this.gameset.getGamesetid()+"/"+this.cardlist.get(source).getCardid()+".png");
            Log.e("File2",file2.getAbsolutePath());
            if(!file2.exists()) {
                imageView.setImageResource(R.drawable.heart);
            }else{
                 imageView.setImageDrawable(Utils.loadDrawable(this.getActivity().getFilesDir().getAbsolutePath(),this.gameset.getLanguage()+"_"+this.gameset.getGamesetid(),this.cardlist.get(source).getCardid()+".png"));
            }
        } else {
            imageView.setImageDrawable(Utils.loadDrawable(this.getActivity().getFilesDir().getAbsolutePath(),this.gameset.getLanguage()+"_"+this.gameset.getGamesetid()+"/overlay",ability.getAbilityId()+".png"));
        }
        RelativeLayout.LayoutParams parameters = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        parameters.setMargins(0, 0, 0, 0);

        int sum = 0;
        Log.e("RelToImgView", this.relToImgView.toString());
        for (Integer entry : this.relToImgView.get(charid).keySet()) {
            sum += this.relToImgView.get(charid).get(entry).size();
        }
        parameters.leftMargin = sum * 20;
        imageView.setMaxWidth(30);
        imageView.setMaxHeight(30);
        //imageView.setX();
        imageView.setAdjustViewBounds(true);
        imageView.setLayoutParams(parameters);
        if (this.relToImgView.get(charid).get(ability.getDuration()) == null) {
            this.relToImgView.get(charid).put(ability.getDuration(), new LinkedList<ImageView>());
        }
        if (!this.cardTocardOverlays.containsKey(charid))
            this.cardTocardOverlays.put(charid, new java.util.TreeMap<Integer, Triple<String, Integer, Integer>>());
        if (ability.getKilling()) {
            this.cardTocardOverlays.get(charid).put(source, new Triple<>(ability.getAbilityId(), ability.getDuration() + ability.getDelay() + this.cardlist.get(this.relToCard.get(charid)).getDeathdelay(), (this.rounds * this.cardlist.size() + this.position2list.size()) + counter));
        } else {
            this.cardTocardOverlays.get(charid).put(source, new Triple<>(ability.getAbilityId(), ability.getDuration() + ability.getDelay(), (this.rounds * this.cardlist.size() + this.position2list.size()) + counter));
        }
        this.relToImgView.get(charid).get(ability.getDuration()).add(imageView);
        this.relviewlist.get(charid).addView(imageView);
        ((RelativeLayout) relviewlist.get(charid).getParent()).setTag(this.calculateBackgroundColor(charid, ability, source));
        ((RelativeLayout) relviewlist.get(charid).getParent()).setBackgroundColor(this.calculateBackgroundColor(charid, ability, source));
    }

    public void addPosition(Player player, Karte card, Integer relid) {
        Integer cardlistindex;
        if (this.cardlist.contains(card)) {
            cardlistindex = this.cardlist.indexOf(card);
            this.cardlist.get(cardlistindex).setCurrentamount(this.cardlist.get(cardlistindex).getCurrentamount() + 1);
        } else {
            this.cardlist.add(card);
            cardlistindex = this.cardlist.size() - 1;
        }
        this.relToCard.put(relid, cardlistindex);
        if (!cardToRel.containsKey(card)) {
            this.cardToRel.put(card, new TreeMap<Integer, Boolean>());
        }
        this.cardToRel.get(card).put(relid, false);
        this.relToPlayer.put(relid, player);
        this.cardTocardOverlays.put(relid, new TreeMap<Integer, Triple<String, Integer, Integer>>());
    }

    /**
     * Creates a secondary group on users choice.
     */
    public void addSecondaryGroup(Group group, Player player) {

    }

    /**
     * Calculates the background color for this activity.
     *
     * @param charid  the rel id of the concerned character
     * @param ability the ability to get the overlay from
     * @param source  the rel id of the card casting the ability
     * @return the background color to be set
     */
    private Integer calculateBackgroundColor(final Integer charid, final Ability ability, final Integer source) {
        Integer killCounterkillcounter = 0;
        Boolean delay = false, killnokillabb = false;
        Ability concernedabb;
        for (Integer src : this.cardTocardOverlays.get(charid).keySet()) {
            Log.e("Src", src.toString());
            Log.e("Card Concerned Abb", this.cardlist.get(src).toString());
            Log.e("Card Concerned Abb", this.cardlist.get(src).getUuidToAbility().toString());
            Log.e("CardToCardOverlays", this.cardTocardOverlays.toString());
            concernedabb = this.cardlist.get(src).getUuidToAbility().get(this.cardTocardOverlays.get(charid).get(src).getOne());
            if (concernedabb.getKilling()) {
                killnokillabb = true;
                killCounterkillcounter++;
            }
            if (concernedabb.getKilling() && (concernedabb.getDelay() > 0 || this.cardlist.get(this.relToCard.get(charid)).getDeathdelay() > 0)) {
                delay = true;
            }
            if (concernedabb.getCounterKilling()) {
                killnokillabb = true;
                killCounterkillcounter--;
            }
        }
        if (delay && killCounterkillcounter > 0) {
            return 0xffFFA500;
        }
        if (killCounterkillcounter <= 0 && killnokillabb) {
            return Color.GREEN;
        }
        if (killCounterkillcounter > 0) {
            return Color.RED;
        }
        return Color.TRANSPARENT;
    }

    /**
     * Calculates the destination view needed for the changeCharsInDirection method ignoring already dead characters in the process.
     *
     * @param changeAmount the amount of characters to skip
     * @param position     the original position
     * @return the destination as Integer
     */
    private Integer calculateDestinationView(Integer changeAmount, final Integer position, final Boolean clockwise) {
        Integer destination = 0;
        for (int i = position; changeAmount != 0; i++) {
            Log.e("Position", i % (this.relviewlist.size()) + "");
            Log.e("Card", this.cardlist.get(this.relToCard.get(i % (this.relviewlist.size()))) + "");
            if (!this.cardlist.get(this.relToCard.get((position + destination) % (this.relviewlist.size() - 1))).isdead()) {
                destination++;
                if (clockwise) {
                    changeAmount--;
                } else {
                    changeAmount++;
                }
            } else {
                destination++;
            }
        }
        for (int i = 0; i < 1; ) {
            if (!this.cardlist.get(this.relToCard.get((position + destination) % (this.relviewlist.size() - 1))).isdead()) {
                i++;
            } else {
                destination++;
            }
        }
        Log.e("calcDestinationView", (position + destination) % this.relviewlist.size() + "");
        return (position + destination) % this.relviewlist.size();
    }

    /**
     * Changes chars in a given direction.
     *
     * @param startrelid   the relid to start the move from
     * @param clockwise    clockwise or counterclockwise direction
     * @param changeAmount the amount of seats to move
     */
    public void changeCharsInDirection(final Integer startrelid, final Boolean clockwise, Integer changeAmount) {
        Log.e("ChangeCharsInDirection", startrelid + " Clockwise:" + clockwise + " " + changeAmount);
        Log.e("RelviewList.size()", this.relviewlist.size() + "");
        Log.e("RelToCard", this.relToCard.toString());
        if (!clockwise) {
            changeAmount *= -1;
        }
        Integer destinationview;
        Set<Integer> alreadyupdated = new TreeSet<>();
        Drawable tempdrawable, tempdrawable2;
        List<Integer> colorlist = new LinkedList<>();
        for (RelativeLayout rel : this.relviewlist) {
            colorlist.add((Integer) ((RelativeLayout) rel.getParent()).getTag());
        }
        Integer tobeupdated = this.relviewlist.size() - this.getDeadCardsArray().size();
        for (int i = startrelid; alreadyupdated.size() < tobeupdated; i++) {
            if (!this.cardlist.get(this.relToCard.get(i % this.relviewlist.size())).isdead()) {
                destinationview = this.calculateDestinationView(changeAmount, i, clockwise);
                tempdrawable = this.getResources().getDrawable(this.getResources()
                        .getIdentifier(this.cardlist.get(this.relToCard.get(i % this.relviewlist.size())).getImg(), "drawable", this.getActivity().getPackageName()));
                tempdrawable2 = this.getResources().getDrawable(this.getResources()
                        .getIdentifier(this.cardlist.get(this.relToCard.get(destinationview)).getImg(), "drawable", this.getActivity().getPackageName()));
                ((RelativeLayout) this.relviewlist.get(destinationview).getParent()).setTag(colorlist.get(i % this.relviewlist.size()));
                ((RelativeLayout) this.relviewlist.get(destinationview).getParent()).setBackgroundColor(colorlist.get(i % this.relviewlist.size()));
                this.relviewlist.get(destinationview).setBackgroundDrawable(tempdrawable);
                tempdrawable = tempdrawable2;
                alreadyupdated.add(destinationview);
            }
        }
        alreadyupdated.clear();
        for (int i = startrelid; alreadyupdated.size() < tobeupdated; i++) {
            if (!this.cardlist.get(this.relToCard.get(i % this.relviewlist.size())).isdead()) {
                destinationview = this.calculateDestinationView(changeAmount, i, clockwise);
                this.cardToRel.get(this.cardlist.get(this.relToCard.get((i % (this.relviewlist.size()))))).remove(i % (this.relviewlist.size()));
                this.cardToRel.get(this.cardlist.get(this.relToCard.get((i % (this.relviewlist.size()))))).put(destinationview, false);
                alreadyupdated.add(destinationview);
            }
        }
        alreadyupdated.clear();
        Map<Integer, Integer> newrelToCard = new TreeMap<>();
        for (int i = startrelid; alreadyupdated.size() < this.relviewlist.size(); i++) {
            destinationview = this.calculateDestinationView(changeAmount, i, clockwise);
            newrelToCard.put(destinationview, this.relToCard.get(i % (this.relviewlist.size())));
            alreadyupdated.add(destinationview);
        }
        this.relToCard = newrelToCard;
        Integer destinationview2;
        Map<Integer, Map<Integer, Triple<String, Integer, Integer>>> newcardTocardOverlays = new TreeMap<>();
        for (Integer relid : this.cardTocardOverlays.keySet()) {
            Log.e("Is in RelToCard?", this.relToCard.toString() + "");
            Log.e("Is in RelToCard?", relid + "");
            Log.e("Is in RelToCard?", relid + " " + this.relToCard.get(relid));
            Log.e("Is in RelToCard?", relid + " " + this.relToCard.get(relid) + " " + this.cardlist.get(this.relToCard.get(relid)));
            if (!this.cardlist.get(this.relToCard.get(relid)).isdead()) {
                destinationview = this.calculateDestinationView(changeAmount, relid, clockwise);
                Log.e("NewCardToCardOverlays", destinationview + " " + " " + this.cardTocardOverlays.get(destinationview));
                newcardTocardOverlays.put(destinationview, new TreeMap<Integer, Triple<String, Integer, Integer>>());
                for (Integer sourceid : this.cardTocardOverlays.get(relid).keySet()) {
                    destinationview2 = this.calculateDestinationView(changeAmount, sourceid, clockwise);
                    Log.e("NewCardToCardOverlays", destinationview + " " + destinationview2 + " " + this.cardTocardOverlays.get(destinationview) + " " + this.cardTocardOverlays.get(destinationview).get(destinationview2));
                    newcardTocardOverlays.get(destinationview).put(destinationview2, this.cardTocardOverlays.get(destinationview).get(destinationview2));
                    //this.addOverlayToChar(destinationview,this.cardlist.get(this.relToCard.get(destinationview2))
                    //        .getabblist().get(newcardTocardOverlays.get(destinationview).get(destinationview2).getOne()),destinationview2);
                }
            }
        }
        Log.e("CardToCardOverlays", this.cardTocardOverlays.toString());
        Log.e("CardToRel ", this.cardToRel.toString());
        Log.e("RelToCard", this.relToCard.toString());
    }

    /**
     * Checks if some fraction has won the game.
     *
     * @return
     */
    private String checkIfWon() {
        Integer counter = 0;
        Group lasttrue = null;
        Log.e("Groupmap", this.groupmap.toString());
        for (Group group : this.groupmap.keySet()) {
            if (this.groupmap.get(group) > 0 && group.getWinsgame()) {
                lasttrue = group;
                counter++;
            }
        }
        if (counter == 1) {
            return lasttrue.getGroupIdentifier();
        }
        return null;
    }

    /**
     * Creates a layout for a not yet assigned card.
     *
     * @param i
     * @param screenwidth
     * @param avgcardwidth
     * @param avgcardheight
     * @param view
     * @return
     */
    private RelativeLayout createLayouts(final int i, final int screenwidth, final int avgcardwidth, final int avgcardheight, final View view, final String backimg) {
        final RelativeLayout backrel = new RelativeLayout(this.getActivity());
        backrel.setBackgroundColor(Color.TRANSPARENT);
        backrel.setTag(Color.TRANSPARENT);
        final RelativeLayout rel2 = new RelativeLayout(this.getActivity());
        if(new File(this.getActivity().getBaseContext().getFilesDir()+"/"+gameset.getLanguage()+"_"+gameset.getGamesetid()+"/"+gameset.getLanguage()+"_"+gameset.getGamesetid()+"_back.png").exists())
            rel2.setBackgroundDrawable(Utils.loadDrawable(this.getActivity().getBaseContext().getFilesDir().getAbsolutePath(),gameset.getLanguage()+"_"+gameset.getGamesetid(),gameset.getLanguage()+"_"+gameset.getGamesetid()+"_back.png"));
        else{
            rel2.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.back));
        }
        rel2.setLayoutParams(new RelativeLayout.LayoutParams((avgcardwidth) - 4, avgcardheight - 4));
        rel2.setId(i);
        backrel.setId(i);
        backrel.addView(rel2);
        rel2.setOnClickListener(this.choosePlayer);
        this.relToImgView.put(i, new TreeMap<Integer, List<ImageView>>());
        this.cardTocardOverlays.put(i, new TreeMap<Integer, Triple<String, Integer, Integer>>());
        this.relToCard.put(i, -1);
        TextView textview = new TextView(this.getActivity());
        textview.setText(this.getResources().getString(R.string.players) + " " + i);
        textview.setBackgroundColor(Color.argb(50, 255, 0, 0));
        RelativeLayout.LayoutParams parameters = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        parameters.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, view.getId());
        textview.setLayoutParams(parameters);
        this.textviewlist.add(textview);
        this.relviewlist.add(rel2);
        rel2.addView(textview);
        return backrel;
    }

    /**
     * Executes an ability on a character.
     *
     * @param current        the current card
     * @param view           the corresponding RelativeLayout
     * @param currentability the ability to use
     */
    public void executeAbility(final Karte current, final RelativeLayout view, final Ability currentability, final Integer src) {
        Log.e("AlwaysChooseCheck", this.cardTocardOverlays.toString());
        Log.e("Relview Id", view.getId() + "");
        Log.e("RelToCard", this.relToCard.get(view.getId()).toString());
        Log.e("CardToCardOverlays", this.cardTocardOverlays.toString());
        Log.e("Chosen", this.cardTocardOverlays.get(view.getId()).toString());
        Log.e("Round2Enum", this.status.toString());
        //Log.e("Source?",this.cardTocardOverlays.get(view.getId()).get(counter - 1).toString());
        if (currentability.getAlwaysChooseOther() && !this.cardTocardOverlays.get(view.getId()).isEmpty() &&
                this.cardTocardOverlays.get(view.getId()).containsKey(counter - 1) && this.cardTocardOverlays.get(view.getId()).get(counter - 1).getTwo() <= 0) {
            Toast toast = Toast.makeText(this.getActivity(), this.getResources().getString(R.string.cannotbechosenbecauseofalwayschooseother), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if ((this.status == Round2Enum.MUST_SELF_CHOOSE || this.status == Round2Enum.CAN_SELF_CHOOSE)
                && this.currentcard.getExtra() > 0) {
            this.getActivity().showDialog(7);
        } else if ((this.status == Round2Enum.MUST_SELF_CHOOSE || this.status == Round2Enum.CAN_SELF_CHOOSE)
                && currentability.getChangeGroup().size() != 0) {
            Log.e("ChangeGroup", currentability.getChangeGroup().toString());
            if (currentability.getChangeGroup().contains(";")) {
                this.groupdialogarray = currentability.getChangeGroup();
                this.getActivity().showDialog(12);
            }
            Log.e("Set new Group ", this.cardlist.get(this.relToCard.get(view.getId())).toString());
            this.groupmap.put(this.cardlist.get(this.relToCard.get(view.getId())).getGroup(), this.groupmap.get(this.cardlist.get(this.relToCard.get(view.getId())).getGroup()) - 1);
            this.cardlist.get(this.relToCard.get(view.getId())).setGroup(currentability.getChangeGroup().get(0));
            if (!this.groupmap.containsKey(currentability.getChangeGroup().get(0)))
                this.groupmap.put(currentability.getChangeGroup().get(0), 0);
            this.groupmap.put(currentability.getChangeGroup().get(0), this.groupmap.get(currentability.getChangeGroup().get(0)) + 1);
            holder.listView.invalidateViews();
        } else if ((this.status == Round2Enum.MUST_SELF_CHOOSE || this.status == Round2Enum.CAN_SELF_CHOOSE)
                && current.getDeadchars() && currentability.getSwitchchar() && !this.getDeadCardsArray().isEmpty()) {
            this.getActivity().showDialog(11);
        } else if ((this.status == Round2Enum.MUST_CHOOSE_CONCERNED || this.status == Round2Enum.CAN_CHOOSE_CONCERNED && !current.isdead() && concernedtemp>0)
                && currentability.getCheck().hasCheck() && currentability.getOnlyCheck()) {
            this.currentcheck=currentability.getCheck();
            this.getActivity().showDialog(15);
        } else if ((this.status == Round2Enum.MUST_CHOOSE_CONCERNED || this.status == Round2Enum.CAN_CHOOSE_CONCERNED) && !current.isdead() && concernedtemp > 0
                && (!currentability.getCheck().hasCheck() || (currentability.getCheck().hasCheck() && currentability.getCheck().evaluate(currentcard,this.cardlist,this.deadcardarray)>0))
                && ((currentability.getSelf()) || (!currentability.getSelf() && !this.cardlist.get(this.relToCard.get(view.getId())).getCardid().equals(currentcard.getCardid())))) {
            holder.listView.invalidateViews();

            String source = "";
            for (Integer relid : this.cardToRel.get(currentcard).keySet()) {
                source += this.textviewlist.get(relid).getText().toString() + ",";
            }
            source = source.substring(0, source.length() - 1);
            String destination = this.textviewlist.get(view.getId()).getText().toString();
            Log.e("GameLog", this.getResources().getString(R.string.round) + " " + this.rounds + ": " + currentcard.getName() + "(" + source
                    + ") " + this.getResources().getString(R.string.uses) + " " + currentability.getDescription() + "(" + currentability.getCurrentamount() + ") " + this.getResources().getString(R.string.on) + " "
                    + current.getName() + " (" + destination + ")\n");
            gamelog.add(this.getResources().getString(R.string.round) + " " + this.rounds + ": " + currentcard.getName() + "(" + source
                    + ") " + this.getResources().getString(R.string.uses) + " " + currentability.getDescription() + "(" + currentability.getCurrentamount() + ") " + this.getResources().getString(R.string.on) + " "
                    + current.getName() + " (" + destination + ")");
            if (!"-1".equals(currentability.getSwitchnewchar())) {
                this.replaceCharWithNewChar(this.cardlist.get(this.relToCard.get(view.getId())), this.cardlist.get(this.cardUUIDTocardId.get(currentability.getSwitchnewchar()).iterator().next()), view.getId());
            }
            concernedtemp--;
            if (this.concernedtemp == 0 && currentability.getSwitchchar() && currentability.getConcerns() == 1) {
                this.addOverlayToChar(view.getId(), currentability, src);
            } else {
                this.addOverlayToChar(view.getId(), currentability, src);
            }
            if (this.concernedtemp == 0) {
                if (currentability.getSwitchchar()) {
                    if (this.switchchartempmap.isEmpty()) {
                        this.switchLivingCharWithChar(this.cardlist.get(this.relToCard.get(view.getId())), currentcard, view.getId(), this.cardToRel.get(currentcard).keySet().iterator().next());
                    } else {
                        this.switchLivingCharWithChar(this.cardlist.get(this.relToCard.get(view.getId())), this.switchchartempmap.get(0).getOne(), view.getId(), this.switchchartempmap.get(0).getTwo());
                        this.switchchartempmap.clear();
                    }
                }
                this.status = Round2Enum.ROUND;
                if (!currentability.getEveryRound())
                    currentability.setCurrentamount(currentability.getCurrentamount() - 1);
                holder.view.setEnabled(true);
            } else if (currentability.getSwitchchar()) {
                this.switchchartempmap.add(new Tuple<>(this.cardlist.get(this.relToCard.get(view.getId())), view.getId()));
            }
        }
    }

    /**
     * Proceeds with the next step of the game.
     *
     * @param view the main view
     */
    private void executeNextStep(final View view, final Boolean backwards) {
        holder.characternametextview.setText(Html.fromHtml(currentcard.getName()));
        if (!currentcard.getActionlist().isEmpty()) {
            holder.gamemastertextview.setText(Html.fromHtml(currentcard.getActionlist().get("1").getGamemaster()));
            holder.playertextview.setText(Html.fromHtml(currentcard.getActionlist().get("1").getPlayer()));
        } else {
            holder.gamemastertextview.setText(" ");
            holder.playertextview.setText(" ");
        }
        holder.descriptiontextview.setText(Html.fromHtml(currentcard.getDescription()));
        if (!currentcard.getabblist().isEmpty())
            holder.listView.setAdapter(new AbilityAdapter(this.getActivity(), currentcard.getabblist(), R.layout.abilitycharview));
        else {
            holder.listView.setAdapter(new AbilityAdapter(this.getActivity(), new TreeSet<Ability>(), R.layout.abilitycharview));
        }
        if (!(Collections.frequency(this.extracardlist, currentcard) == currentcard.getCurrentamount()) && this.cardToRel.containsKey(currentcard)) {
            for (Ability abb : currentcard.getabblist()) {
                if (this.rounds >= abb.getAvailableFrom() && (abb.getAvailableUntil() <= this.rounds || abb.getAvailableUntil() == -1)) {
                    if (abb.getMustuse()) {
                        Log.e("Mark red", currentcard + " " + currentcard.getabblist());
                        if (!backwards)
                            view.setEnabled(false);
                        if (abb.getConcerns() > 0) {
                            this.status = Round2Enum.MUST_CHOOSE_CONCERNED;
                            this.concernedtemp = currentcard.getabblist().iterator().next().getConcerns();
                        } else {
                            this.status = Round2Enum.MUST_SELF_CHOOSE;
                        }
                    } else {
                        if (abb.getConcerns() > 0) {
                            this.status = Round2Enum.CAN_CHOOSE_CONCERNED;
                            this.concernedtemp = currentcard.getabblist().iterator().next().getConcerns();
                        } else {
                            this.status = Round2Enum.CAN_SELF_CHOOSE;
                        }
                    }
                }
            }
        }
        if (!this.lastactivecharacterrel.isEmpty() && !this.lastrelcolor.isEmpty()) {
            int i = 0;
            for (RelativeLayout rel : this.lastactivecharacterrel) {
                Log.e("LastRelColor", this.lastrelcolor.toString());
                Log.e("Color ", this.lastrelcolor.get(i) + " " + Color.TRANSPARENT);
                if (this.lastrelcolor.get(i) == Color.TRANSPARENT) {
                    rel.setBackgroundResource(Color.blue(Color.BLUE));
                    rel.setTag(Color.BLUE);
                    i++;
                } else {
                    rel.setTag(this.lastrelcolor.get(i));
                    rel.setBackgroundDrawable(new ColorDrawable(this.lastrelcolor.get(i++)));
                }
            }
            this.lastrelcolor.clear();
            if (lastcard.getFixeddeath().equals(this.rounds)) {
                lastcard.dead();
                if (!this.extracardlist.contains(lastcard))
                    this.setCharacter(lastcard, this.cardToRel.get(lastcard).keySet().iterator().next(), false);
            }
        }
        if (Collections.frequency(this.extracardlist, currentcard) == currentcard.getCurrentamount()) {
            this.counter++;
        } else if (this.cardToRel.containsKey(currentcard)) {
            this.lastactivecharacterrel.clear();
            for (Integer cardid : this.cardToRel.get(currentcard).keySet()) {
                if (!this.cardToRel.get(currentcard).get(cardid)) {
                    this.lastactivecharacterrel.add(((RelativeLayout) this.relviewlist.get(cardid).getParent()));
                    this.lastrelcolor.add((Integer) ((RelativeLayout)
                            this.relviewlist.get(cardid).getParent()).getTag());
                    ((RelativeLayout) this.relviewlist.get(cardid).getParent())
                            .setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                    ((RelativeLayout) this.relviewlist.get(cardid).getParent())
                            .setTag(Color.WHITE);
                }
            }
            if(!currentcard.getWakesUpWith().isEmpty()){
                Log.e("WakesUpWith ",currentcard.getWakesUpWith().toString());
                Log.e("CardUUIDToCardId",this.cardUUIDTocardId.toString());
                for(String cardid:currentcard.getWakesUpWith()){
                    if(this.cardUUIDTocardId.containsKey(cardid) && !this.cardlist.get(this.cardUUIDTocardId.get(cardid).iterator().next()).isdead()){
                        int relid=this.cardToRel.get(this.cardlist.get(this.cardUUIDTocardId.get(cardid).iterator().next())).keySet().iterator().next();
                        this.lastrelcolor.add((Integer) ((RelativeLayout)
                                this.relviewlist.get(relid).getParent()).getTag());
                        ((RelativeLayout) this.relviewlist.get(relid).getParent())
                                .setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                        ((RelativeLayout) this.relviewlist.get(relid).getParent())
                                .setTag(Color.WHITE);
                    }
                }
            }
            for(String wakeup:currentcard.getWakesUpWith()){
                if(this.cardUUIDTocardId.containsKey(wakeup)){
                    for(Integer cardid:this.cardUUIDTocardId.get(wakeup)){
                        for(Integer relid:this.cardToRel.get(this.cardlist.get(cardid)).keySet()){
                            this.lastactivecharacterrel.add((RelativeLayout)this.relviewlist.get(relid).getParent());
                        }
                    }
                }
            }
            //(RelativeLayout)this.relviewlist.get(this.cardToRel.get(currentcard).iterator().next()).getParent());
            this.counter++;
        } else if (!this.extracardlist.contains(currentcard) && !this.cardToRel.containsKey(currentcard)) {
            Toast toast = Toast.makeText(this.getActivity(), currentcard.getName() + " " + this.getResources().getString(R.string.notpositioned), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    /**
     * Executes an ability when a character is about to die.
     *
     * @param card  the concerned card
     * @param relid the id of the RelativeLayout
     * @return success indicator
     */
    private Boolean executeOnDeadAbilities(final Karte card, final Integer relid) {
        this.abilitydialoglist.clear();
        for (Ability abb : card.getabblist()) {
            if (abb.getOndead() && abb.getCurrentamount() > 0) {
                this.abilitydialoglist.add(abb);
            }
        }
        return !this.abilitydialoglist.isEmpty();
    }

    /**
     * Executes features to be scheduled at the end of a round.
     */
    private void executeRoundEnd() {
        int lovers = 0, flute = 0, alive = 0;
        this.abilitydialoglist.clear();
        if (!this.lastactivecharacterrel.isEmpty()) {
            for (RelativeLayout rel : this.lastactivecharacterrel) {
                int i = 0;
                Log.e("Color ", this.lastrelcolor.get(i) + " " + Color.TRANSPARENT);
                if (this.lastrelcolor.get(i) == Color.blue(Color.BLUE)) {
                    rel.setBackgroundResource(Color.TRANSPARENT);
                    rel.setTag(Color.TRANSPARENT);
                    i++;
                } else {
                    rel.setTag(this.lastrelcolor.get(i));
                    rel.setBackgroundDrawable(new ColorDrawable(this.lastrelcolor.get(i++)));

                }
            }
            this.lastrelcolor.clear();
            lastactivecharacterrel.clear();
        }
        List<Karte> winsalonelist = new LinkedList<>();
        for (Karte card : this.cardlist) {
            if (end) {
                final Intent intent = new Intent(this.getActivity(), Results.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                for (Karte extra : this.extracardlist) {
                    this.cardlist.remove(extra);
                }
                Collections.sort(this.cardlist, (s1, s2) -> {
                    int result = s1.isdead().compareTo(s2.isdead());
                    if (result == 0) {
                        return s1.getName().compareTo(s2.getName());
                    } else {
                        return result;
                    }
                });
                ArrayList<Player> winnerlist = new ArrayList<>();
                ArrayList<Karte> winnercards = new ArrayList<>();
                for (Iterator<Karte> iter = this.cardlist.iterator(); iter.hasNext(); ) {
                    Karte current = iter.next();
                    if (current.getNopoints()) {
                        iter.remove();
                    } else {
                        Log.e("CardToRel", this.cardToRel.toString());
                        Log.e("Current", current.toString());
                        if (this.cardToRel.containsKey(current)) {
                            for (Integer relid : this.cardToRel.get(current).keySet()) {
                                if (current.getGroup().getGroupIdentifier().equals(winninggroup)) {
                                    winnerlist.add(
                                            this.relToPlayer.
                                                    get(relid)
                                    );
                                    Karte carrd = new Karte(current);
                                    carrd.setCurrentamount(1);
                                    winnercards.add(carrd);
                                }
                            }
                        }
                    }
                }
                Log.e("Final Gamelog", this.gamelog.toString());
                Log.e("Players:", this.playerList.toString());
                Log.e("PlayerNameToPlayerId:", this.playernameToPlayerId.toString());
                Log.e("Cards:", this.cardlist.toString());
                Log.e("RelToPlayer:", this.relToPlayer.toString());
                Log.e("RelToCard:", this.relToCard.toString());
                Log.e("CardToRel:", this.cardToRel.toString());
                Log.e("Winnercards", winnercards.toString());
                Log.e("Winnerlist", winnerlist.toString());
                Log.e("Winning Cards", winnercards.size() + " - " + winnerlist.size());
                Bundle mBundle = new Bundle();
                mBundle.putParcelableArrayList("cards", winnercards);
                mBundle.putParcelableArrayList("winners", winnerlist);
                mBundle.putBoolean("end", true);
                mBundle.putString("icon", this.icon);
                mBundle.putString("language",this.gameset.getLanguage());
                mBundle.putString("gamesetid",this.gameset.getGamesetid());
                mBundle.putString("winninggroup", this.winninggroup);
                intent.putExtras(mBundle);
                this.startActivity(intent);
                ((Round2) this.getActivity()).getSupportActionBar().removeTabAt(TabsPagerAdapter.tabs.indexOf(this));
                ((Round2) this.getActivity()).getmAdapter().removeFragment(this);
                this.getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
                if (((Round2) this.getActivity()).getmAdapter().getCount() > 0) {
                    ((Round2) this.getActivity()).getViewPager().setCurrentItem(0);
                } else {
                    this.getActivity().finish();
                }
                this.setRetainInstance(false);
                this.finish();
                return;
            }

            if (!card.isdead() && Collections.frequency(this.extracardlist, card) != card.getCurrentamount()) {
                alive++;
                /*switch (card.getGroup().getGroupIdentifier()) {
                    case "U":
                        break;
                    default:
                        Log.e("GroupIdentifier", card.getGroup().getGroupIdentifier());
                        Log.e("GroupIdentifier", card.getGroup().getGroupname());
                        Log.e("GroupIdentifier", card.getGroup().getGroupdescription());
                        Log.e("Groupmap", this.groupmap.toString());
                        if (this.groupmap.containsKey(card.getGroup()))
                            this.groupmap.put(card.getGroup(), true);
                } */
            }
            if (card.getWinsalone()) {
                winsalonelist.add(card);
            }
            if (!card.isverzaubert()) {
                flute++;
            }
        }
        for (Karte card : winsalonelist) {
            if (this.cardTocardOverlays.get(this.cardUUIDTocardId.get(card.getCardid()).iterator().next()).size() == alive - 1) {
                holder.characternametextview.setText(card.getName() + " " + this.getResources().getString(R.string.won) + "\n\n");
                holder.gamemastertextview.setText(Html.fromHtml("<b>" + this.getResources().getString(R.string.congrattext) + "</b>") + "\n");
                holder.playertextview.setText("");
                holder.descriptiontextview.setText("");
                holder.showgamelogbutton.setVisibility(VISIBLE);
                this.end = true;
                this.winninggroup = card.getGroup().getGroupIdentifier();
            }
        }
        String temp;
                /*if (flute == (alive - 1)) {
                    holder.characternametextview.setText(this.getResources().getString(R.string.flute) + " " + this.getResources().getString(R.string.won) + "\n\n");
                    holder.gamemastertextview.setText(Html.fromHtml("<b>" + this.getResources().getString(R.string.congrattext) + "</b>")+"\n");
                    this.end=true;
                    this.winninggroup="F";
                }else*/
        if (lovers == 2 && this.cardlist.size() == 2) {
            holder.characternametextview.setText(this.getResources().getString(R.string.lovers) + " " + this.getResources().getString(R.string.won) + "\n\n");
            holder.gamemastertextview.setText(this.getResources().getString(R.string.congrattext) + "\n\n");
            holder.playertextview.setText("");
            holder.descriptiontextview.setText("");
            holder.showgamelogbutton.setVisibility(VISIBLE);
            this.end = true;
            this.winninggroup = "";
        } else if ((temp = checkIfWon()) != null) {
            holder.characternametextview.setText(this.getResources().getString(getResources()
                    .getIdentifier(temp, "string", this.getActivity().getPackageName())) + " " + this.getResources().getString(R.string.won) + "\n\n");
            holder.gamemastertextview.setText(this.getResources().getString(R.string.congrattext) + "\n\n");
            holder.showgamelogbutton.setVisibility(VISIBLE);
            this.end = true;
            this.winninggroup = temp;
        } else {
            Log.e("Back", "To ChooseChars 1");
            holder.characternametextview.setText(this.gameset.getOutrotitle() + "\n\n");
            holder.playertextview.setText("");
            holder.descriptiontextview.setText("");
            holder.gamemastertextview.setText(this.gameset.getOutrotext() + "\n\n");
            holder.listView.setAdapter(new AbilityAdapter(this.getActivity(), new TreeSet<Ability>(), R.layout.ability));
            if (!this.events.isEmpty())
                holder.showeventbutton.setVisibility(VISIBLE);

            if (this.roundEnd) {
                Log.e("CardExchangeMap", this.cardExchangeMap.toString());
                if (!this.cardExchangeMap.isEmpty()) {
                    for (Integer cardid : this.cardExchangeMap.keySet()) {
                        if (this.cardExchangeMap.get(cardid).getTwo() > 1) {
                            this.cardExchangeMap.get(cardid).setTwo(this.cardExchangeMap.get(cardid).getTwo() - 1);
                        } else {
                            this.switchExtraCharWithChar(this.cardlist.get(this.relToCard.get(cardid)),
                                    this.cardExchangeMap.get(cardid).getOne()
                                    , false, cardid, true, false);
                            this.cardExchangeMap.remove(cardid);
                        }
                    }
                }
                holder.showeventbutton.setVisibility(GONE);
                for (Integer rel : relToImgView.keySet()) {
                    for (Integer img : relToImgView.get(rel).keySet()) {
                        this.currentevent = -1;
                        relToImgView.get(rel).put(img, relToImgView.get(rel).get(img));
                        if (img == 1) {
                            for (ImageView imgview : relToImgView.get(rel).get(img)) {
                                RelativeLayout parent = (RelativeLayout) imgview.getParent();
                                parent.removeView(imgview);
                            }
                            relToImgView.get(rel).put(1, new LinkedList<ImageView>());
                        } else if (img > 1) {
                            relToImgView.get(rel).put(img - 1, relToImgView.get(rel).get(img));
                        }
                    }
                }
                this.roundEnd = false;
                this.counter = 0;
                this.position2counter = 0;
                this.rounds++;
                this.getActivity().setTitle(this.getResources().getString(R.string.round) + " " + this.rounds);
                for (RelativeLayout rel : this.relviewlist) {
                    Boolean remaincolor = false;
                    if (this.relToCard.containsKey(rel.getId()) && this.cardTocardOverlays.containsKey(rel.getId())) {
                        for (Iterator<Triple<String, Integer, Integer>> iter = this.cardTocardOverlays.get(this.relToCard.get(rel.getId())).values().iterator(); iter.hasNext(); ) {
                            Triple<String, Integer, Integer> durationvalue = iter.next();
                            Log.e("RoundEnd Choose:", this.cardTocardOverlays.get(rel.getId()) + " " + durationvalue);
                            Karte source = this.cardlist.get(this.cardTocardOverlays.get(this.relToCard.get(rel.getId())).keySet().iterator().next());
                            if ((durationvalue.getTwo() == -1 && source.getUuidToAbility().get(durationvalue.getOne()).getCounterKilling())
                                    || source.getUuidToAbility().get(durationvalue.getOne()).getKilling() && durationvalue.getTwo() > 0) {
                                remaincolor = true;
                            }
                            Log.e("Status", durationvalue.getTwo() + "");
                            if (durationvalue.getTwo() == 0) {
                                iter.remove();
                            }
                            durationvalue.setTwo(durationvalue.getTwo() - 1);
                            if (durationvalue.getTwo() > 0)
                                remaincolor = true;
                            Log.e("New Durationvalue:", durationvalue.toString());
                        }
                        if (!remaincolor) {
                            ((RelativeLayout) rel.getParent()).setTag(Color.TRANSPARENT);
                            ((RelativeLayout) rel.getParent()).setBackgroundColor(Color.TRANSPARENT);

                        }
                    }
                }
                holder.characternametextview.setText(this.gameset.getIntrotitle() + "\n\n");
                holder.gamemastertextview.setText(this.gameset.getIntrotext() + "\n\n");
                holder.playertextview.setText("");
                holder.descriptiontextview.setText("");
            } else {
                this.roundEnd = true;
                for (RelativeLayout rel : this.relviewlist) {
                    if(((Integer)((RelativeLayout) rel.getParent()).getTag())==Color.RED){
                        if(this.executeOnDeadAbilities(this.cardlist.get(this.relToCard.get(rel.getId())), rel.getId())){
                            this.roundEnd = false;

                        }else{
                            ((RelativeLayout) rel.getParent()).setTag(Color.TRANSPARENT);
                            ((RelativeLayout) rel.getParent()).setBackgroundColor(Color.TRANSPARENT);
                        }
                        this.killCharacter(rel.getId());

                    }
                }
            }
        }
    }

    private void finish() {
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    public List<Ability> getAbilitydialoglist() {

        return abilitydialoglist;
    }

    public Map<Karte, Map<Integer, Boolean>> getCardToRel() {
        return cardToRel;
    }

    public Map<Integer, Map<Integer, Triple<String, Integer, Integer>>> getCardTocardOverlays() {
        return cardTocardOverlays;
    }

    public List<Karte> getCardlist() {
        return cardlist;
    }

    /**
     * Gets the array of card names extracted from the cardlist.
     *
     * @return the array of card names
     */
    private List<Karte> getCardsArray(boolean loadgame) {
        this.cardarray = new LinkedList<>();
        List<Karte> cardlist;
        if (loadgame) {
            cardlist = this.cardlist;
        } else {
            cardlist = this.cardlist;
        }
        int i = 0;
        for (Karte card : cardlist) {
            if (!this.cardUUIDTocardId.containsKey(card.getCardid())) {
                this.cardUUIDTocardId.put(card.getCardid(), new TreeSet<Integer>());
            }
            this.cardUUIDTocardId.get(card.getCardid()).add(i++);
            if (card.getCurrentamount() > 0) {
                int j = 1;
                while (j < card.getCurrentamount()) {
                    if (!this.extracardlist.contains(card)) {
                        this.cardarray.add(card);
                    }
                    j++;
                }
            }
            if (!this.extracardlist.contains(card)) {
                this.cardarray.add(card);
            }
        }
        Collections.sort(cardarray, (s1, s2) -> s1.getName().compareTo(s2.getName()));
        return this.cardarray;
    }

    public Integer getCounter() {

        return counter;
    }

    public List<Karte> getCurrentCardsArray() {
        return this.cardarray;
    }

    public List<Karte> getCurrentExtraCardsArray() {
        return this.extracardlist;
    }

    public List<Player> getCurrentPlayersArray() {
        return this.playerarray;
    }

    public Karte getCurrentcard() {

        return currentcard;
    }

    /**
     * Gets the list of players_config as array for the choosing dialog.
     *
     * @return the array of Players as String
     */
    public List<Karte> getDeadCardsArray() {
        this.deadcardarray = new LinkedList<>();
        for (Karte card : cardlist) {
            if (card.isdead()) {
                deadcardarray.add(card);
            }
        }
        Collections.sort(deadcardarray, (s1, s2) -> s1.getName().compareTo(s2.getName()));
        return deadcardarray;
    }

    public List<Karte> getDeadcardarray() {
        return deadcardarray;
    }

    /**
     * Gets the array of card names extracted from the cardlist.
     *
     * @return the array of card names
     */
    public List<Karte> getExtraCardsArray() {
        final List<Karte> extracardarray = new LinkedList<>();
        int i = this.cardlist.size() - this.extracardamount;
        for (Karte card : this.extracardlist) {
            if (!this.cardUUIDTocardId.containsKey(card.getCardid())) {
                this.cardUUIDTocardId.put(card.getCardid(), new TreeSet<Integer>());
            }
            this.cardUUIDTocardId.get(card.getCardid()).add(i++);
            if (card.getCurrentamount() > 0) {
                int j = 1;
                while (j < card.getCurrentamount()) {
                    extracardarray.add(card);
                    j++;
                }
            }

            extracardarray.add(card);
        }
        Collections.sort(extracardarray, (s1, s2) -> s1.getName().compareTo(s2.getName()));
        return extracardarray;
    }

    public Integer getExtracardamount() {

        return extracardamount;
    }

    public String[] getGameLog() {
        return this.gamelog.toArray(new String[0]);
    }

    public List<Group> getGroupdialogarray() {

        return groupdialogarray;
    }

    public Boolean getInitfinished() {
        return initfinished;
    }

    public List<RelativeLayout> getLastactivecharacterrel() {

        return lastactivecharacterrel;
    }

    public RelativeLayout getLastcheckedrel() {

        return lastcheckedrel;
    }

    public Integer getLastcheckedrelId() {

        return lastcheckedrel.getId();
    }

    /**
     * Gets a new event to display at random.
     *
     * @return the event text as String
     */
    public String getNewEvent() {
        if (this.currentevent == -1) {
            this.currentevent = this.random.nextInt() % this.events.size();
            if (this.currentevent < 0) {
                this.currentevent *= -1;
            }
        }
        Event eve = this.events.get(this.currentevent);
        return eve.getTitle() + "\n" + eve.getDescription();
    }

    /**
     * Gets the list of players_config as array for the choosing dialog.
     *
     * @return the array of Players as String
     */
    public List<Player> getPlayersArray() {
        this.playerarray = new LinkedList<>();
        int i = 0;
        List<Player> playerlist = this.playerList;
        for (Player player : playerlist) {
            this.playernameToPlayerId.put(player.getFirstname() + " " + player.getName(), i++);
            this.playerarray.add(player);
        }
        Collections.sort(playerarray, new Comparator<Player>() {
            public int compare(Player s1, Player s2) {
                return (s1.getFirstname() + " " + s1.getName()).compareTo((s2.getFirstname() + " " + s2.getName()));
            }
        });
        return this.playerarray;
    }

    public Map<Integer, Integer> getRelToCard() {
        return relToCard;
    }

    public Integer getRounds() {
        return rounds;
    }

    /**
     * Reverts one step of the round process.
     */
    public void goBackOneStep() {
        if (counter > 0) {
            counter -= 1;
            currentcard = this.cardlist.get(counter);
            while (counter > 0 && (this.extracardlist.contains(currentcard) || currentcard.isdead() || (currentcard.getRound() != 0 && !currentcard.getRound().equals(rounds)
                    && !currentcard.getRound().equals(rounds * -1)))) {
                Log.e("CurrentCard", currentcard.toString());
                Log.e("getRound!=0?", (currentcard.getRound() != 0) + " " + currentcard.getRound());
                Log.e("!getRound()==rounds)?", (!currentcard.getRound().equals(rounds)) + " " + rounds);
                Log.e("!getRound()==rounds*=-1", (!currentcard.getRound().equals(rounds * -1)) + " " + rounds * -1);
                Log.e("!CurrentCard.isdead()?", currentcard.isdead() + "");
                Log.e("Extracardlist?", this.extracardlist.contains(currentcard) + "");

                counter -= 1;
                currentcard = this.cardlist.get(counter);
            }
            Integer abilityoccurancecounter = 0;
            Boolean nothingchanged = true, switchchar = false;
            List<Integer> concernedcharrels = new LinkedList<>();
            Log.e("CurrentCard", currentcard.toString());
            Log.e("Counter", (counter) + "");
            Log.e("CardToCardOverlays", this.cardTocardOverlays.toString());
            Integer relid = this.lastactivecharacterrel.iterator().next().getId();
            Log.e("RelId", relid.toString());
            Log.e("CardExchangeMap", this.cardExchangeMap.toString());
            if (cardExchangeMap.containsKey(relid) && currentcard.equals(cardExchangeMap.get(relid).getOne())) {
                if (currentcard.getExtra() > 0) {
                    this.switchExtraCharWithChar(currentcard, this.cardExchangeMap.get(relid).getOne(), true, relid, true, true);
                    nothingchanged = false;
                    //TODO Revert extra char choose (Dieb) charexchangemap
                }
                if (currentcard.getDeadchars()) {
                    this.switchExtraCharWithChar(currentcard, this.cardExchangeMap.get(relid).getOne(), true, relid, false, true);
                    nothingchanged = false;
                    //TODO Revert Dead Char Choose
                }
            }
            for (Integer rel : this.cardTocardOverlays.keySet()) {
                if (this.cardTocardOverlays.get(rel).containsKey(counter)) {
                    Log.e("CardToRel", this.cardTocardOverlays.get(rel).toString());
                    if (this.cardTocardOverlays.get(rel).get(counter).getThree().equals((rounds * this.cardlist.size() + this.position2list.size()) + counter + 1)) {
                        Log.e("Relviewlist", this.relviewlist.get(rel).toString());
                        Log.e("Relviewlist2", this.relviewlist.get(rel).getChildCount() + "");
                        this.relviewlist.get(rel).removeViewAt(this.relviewlist.get(rel).getChildCount() - 1);
                        this.relToImgView.get(rel).remove(1);
                        Log.e("Abilities", this.currentcard.getabblist().toString());
                        Log.e("Beep", this.cardTocardOverlays.get(rel).toString());
                        Ability abb = this.currentcard.getUuidToAbility().get(this.cardTocardOverlays.get(rel).get(counter).getOne());
                        Log.e("Abb", abb.toString());
                        abilityoccurancecounter++;
                        if (abilityoccurancecounter % abb.getConcerns() == 0 && !abb.getEveryRound())
                            abb.setCurrentamount(abb.getCurrentamount() + 1);
                        concernedcharrels.add(rel);
                        this.cardTocardOverlays.get(rel).remove(counter);
                        ((RelativeLayout) this.relviewlist.get(rel).getParent()).setTag(this.calculateBackgroundColor(rel, abb, counter));
                        ((RelativeLayout) this.relviewlist.get(rel).getParent()).setBackgroundColor(this.calculateBackgroundColor(rel, abb, counter));
                        Log.e("ConcernedCharrels", concernedcharrels.toString());
                        if (abb.getSwitchchar()) {
                            switchchar = true;
                        }
                        if (switchchar && concernedcharrels.size() == 2) {
                            this.switchLivingCharWithChar(this.cardlist.get(this.relToCard.get(concernedcharrels.get(0))),
                                    this.cardlist.get(this.relToCard.get(concernedcharrels.get(1))), concernedcharrels.get(0), concernedcharrels.get(1));
                            concernedcharrels.clear();
                        }
                        if (!"-1".equals(abb.getSwitchnewchar())) {
                            this.switchExtraCharWithChar(this.cardlist.get(this.relToCard.get(concernedcharrels.get(0))), this.cardExchangeMap.get(rel).getOne(), true, rel, true, true);
                        }
                        nothingchanged = false;
                    }
                }
            }
            if (abilityoccurancecounter > 0 && concernedcharrels.size() > 0 && switchchar) {
                Log.e("CurrentCard", currentcard.toString());
                Log.e("ToReplaceCard", this.cardlist.get(this.relToCard.get(concernedcharrels.get(0))).toString());
                this.switchLivingCharWithChar(
                        this.cardlist.get(this.relToCard.get(concernedcharrels.get(0))), currentcard, concernedcharrels.get(0), this.cardToRel.get(currentcard).keySet().iterator().next());
                lastactivecharacterrel.clear();
                lastactivecharacterrel.add((RelativeLayout) this.relviewlist.get(this.cardToRel.get(currentcard).keySet().iterator().next()).getParent());
                lastrelcolor.clear();
                lastrelcolor.add(Color.TRANSPARENT);
            }
            if (nothingchanged && counter > 0) {
                Log.e("CurrentCard", currentcard.toString());
                Log.e("getRound()!=0?", (currentcard.getRound() != 0) + " " + currentcard.getRound());
                Log.e("!getRound==rounds)?", (!currentcard.getRound().equals(rounds)) + " " + rounds);
                Log.e("!getRound==rounds*=-1)?", (!currentcard.getRound().equals(rounds * -1)) + " " + rounds * -1);
                Log.e("!CurrentCard.isdead()?", currentcard.isdead() + "");
                Log.e("Extracardlist?", this.extracardlist.contains(currentcard) + "");
                counter -= 1;
                currentcard = this.cardlist.get(counter);
                while (counter > 0 && (this.extracardlist.contains(currentcard) || currentcard.isdead() || (currentcard.getRound() != 0 && !currentcard.getRound().equals(rounds)
                        && !currentcard.getRound().equals(rounds * -1)))) {
                    Log.e("CurrentCard", currentcard.toString());
                    Log.e("getRound()!=0?", (currentcard.getRound() != 0) + " " + currentcard.getRound());
                    Log.e("!getRound==(rounds)", (!currentcard.getRound().equals(rounds)) + " " + rounds);
                    Log.e("!getRound==(rounds*=-1)", (!currentcard.getRound().equals(rounds * -1)) + " " + rounds * -1);
                    Log.e("!CurrentCard.isdead()?", currentcard.isdead() + "");
                    Log.e("Extracardlist?", this.extracardlist.contains(currentcard) + "");
                    counter -= 1;
                    currentcard = this.cardlist.get(counter);
                }
                this.executeNextStep(holder.view, true);
                holder.view.setEnabled(true);
            } else if (!nothingchanged && counter > 0) {
                this.gamelog.remove(this.gamelog.size() - 1);
                this.executeNextStep(holder.view, false);
            }
            Log.e("CardToCardOverlays", this.cardTocardOverlays.toString());
        }

    }

    public void goToRoundEnd() {
        this.counter = this.cardlist.size();
        this.position2counter = this.position2list.size();
        this.viewOnClick.onClick(holder.view);
    }

    public void initView(Integer id, Bundle bundle) {
        this.counter = 0;
        this.position2list = new LinkedList<>();
        this.cardExchangeMap = new TreeMap<>();
        this.lastcard = new Karte();
        this.lastrelcolor = new LinkedList<>();
        this.lastrelcolor.add(Color.TRANSPARENT);
        this.lastcheckedrel=new RelativeLayout(this.getActivity());
        this.lastcheckedrel.setId(0);
        this.currentcard = new Karte();
        this.holder = new Round2Holder();
        this.relToImgView = new TreeMap<>();
        this.textviewlist = new LinkedList<>();
        this.relviewlist = new LinkedList<>();
        this.cardUUIDTocardId = new TreeMap<>();
        this.relToPlayer = new TreeMap<>();
        this.abilitydialoglist = new LinkedList<>();
        this.cardTocardOverlays = new TreeMap<>();
        this.loadedplayers = new ArrayList<>();
        this.checkedItems = new TreeMap<>();
        this.cardToRel = new TreeMap<>();
        this.relToCard = new TreeMap<>();
        this.extracardlist = new LinkedList<>();
        this.playernameToPlayerId = new TreeMap<>();
        this.playerarray = new LinkedList<>();
        this.players = new PlayerParse();
        this.random = new Random(System.currentTimeMillis());
        this.groupmap = new TreeMap<>();
        this.switchchartempmap = new LinkedList<>();
        this.status = Round2Enum.ROUND;
        this.turn = false;
    }
          /*
        if (id == -1 && Utils.currentnum < Utils.round2fragments.length) {
            id = Utils.currentnum;
        } else if (id == -1) {
            id = 0;
        }
        this.instanceid = id;
        Log.e("InstanceId", id.toString());
        Log.e("Init Finished?", Utils.round2fragments[id].getInitfinished().toString());
        if (Utils.round2fragments[id].getInitfinished()) {

            Utils.addFragment(this);
        } else {
            /*Round2Fragment instance = Utils.round2fragments[id];
            Log.e("Round2Fragments", Utils.round2fragments.toString());

            this.abilitydialoglist = bundle.getParcelableArrayList("abilitydialoglist");
            this.calleveryone = bundle.getBoolean("calleveryone");
            this.cardarray = bundle.getParcelableArrayList("cardarray");
            this.cardlist = bundle.getParcelableArrayList("cardlist");
            this.cardExchangeMap = instance.cardExchangeMap;
            this.cardTocardOverlays = instance.cardTocardOverlays;
            this.cardToRel = instance.cardToRel;
            this.cardUUIDTocardId = instance.cardUUIDTocardId;
            this.checkedItems = instance.checkedItems;
            this.choosePlayer = instance.choosePlayer;
            this.concernedtemp=bundle.getInt("concernedtemp");
            this.counter = bundle.getInt("counter");
            this.currentcard = bundle.getParcelable("currentcard");
            this.currentevent = bundle.getInt("currentevent");
            this.currentplayerid = bundle.getInt("currentplayerid");
            this.currentRelId = bundle.getInt("currentrelid");
            this.end=bundle.getBoolean("end");
            this.extracardlist = bundle.getParcelableArrayList("extracardlist");
            this.extracardamount=bundle.getInt("extracardamount");
            this.firstload = bundle.getBoolean("firstload");
            this.gameset = bundle.getParcelable("gameset");
            this.gamelog = bundle.getString("gamelog");
            this.groupdialogarray=bundle.getParcelableArrayList("groupdialogarray");
            this.groupmap = instance.groupmap;
            this.holder=instance.holder;
            this.icon = bundle.getString("icon");
            this.lastactivecharacterrel = instance.lastactivecharacterrel;
            this.lastcard = bundle.getParcelable("lastcard");
            this.lastrelcolor = bundle.getIntegerArrayList("lastrelcolor");
            this.playernameToPlayerId = new TreeMap<>();
            this.playerarray = bundle.getParcelableArrayList("playerarray");
            this.playerList = bundle.getParcelableArrayList("playerlist");
            this.position2list = bundle.getParcelableArrayList("position2list");
            this.position2counter = bundle.getInt("position2counter");
            List<Integer> relToCardKeys=bundle.getIntegerArrayList("reltocardkeys");
            List<Integer> relToCardVals=bundle.getIntegerArrayList("reltocardvals");
            this.relToCard=new TreeMap<>();
            int i=0;
            for(Integer key:relToCardKeys){
                this.relToCard.put(key,relToCardVals.get(i++));
            }
            this.relToImgView = instance.relToImgView;
            List<Integer> relToPlayerKeys=bundle.getIntegerArrayList("reltoplayerkeys");
            List<Player> relToPlayerVals=bundle.getParcelableArrayList("reltoplayervals");
            this.relToPlayer=new TreeMap<>();
            i=0;
            for(Integer key:relToPlayerKeys){
               this.relToPlayer.put(key,relToPlayerVals.get(i++));
            }
            this.relviewlist = instance.relviewlist;
            this.random = instance.random;
            this.roundEnd = bundle.getBoolean("roundend");
            this.rounds=bundle.getInt("rounds");
            this.status = Round2Enum.values()[bundle.getInt("status")];
            this.switchchartempmap = instance.switchchartempmap;
            this.textviewlist = instance.textviewlist;
            this.winninggroup = bundle.getString("winninggroup");
            Log.e("Cardlist",this.cardlist.toString());
            this.turn = true;
        }
    }*/

    public void killCharacter(Integer relid) {
        Karte current = this.cardlist.get(this.relToCard.get(relid));
        Log.e(current.toString(), "Amount: " + current.getCurrentamount());
        if (this.executeOnDeadAbilities(current, relid)) {
            Toast toast = Toast.makeText(this.getActivity(), this.getResources().getString(R.string.ondeadability)+this.abilitydialoglist.get(0).getDescription(), Toast.LENGTH_SHORT);
            toast.show();
            ((RelativeLayout) this.relviewlist.get(relid).getParent()).setTag(Color.WHITE);
            ((RelativeLayout) this.relviewlist.get(relid).getParent()).setBackgroundColor(Color.WHITE);
            this.lastactivecharacterrel.add((RelativeLayout) this.relviewlist.get(relid).getParent());
            if (this.abilitydialoglist.get(0).getConcerns() > 0) {
                this.status = Round2Enum.ON_DEAD_MUST_CHOOOSE;
                this.concernedtemp = this.abilitydialoglist.get(0).getConcerns();
                this.currentcard = current;
                this.currentRelId = relid;
            } else {
                this.status = Round2Enum.ON_DEAD_MUST_CHOOOSE;
                this.currentcard = current;
                this.currentRelId = relid;
            }
        } else {
            if (current.getCurrentamount() > 1) {
                current.setCurrentamount(current.getCurrentamount() - 1);
                Log.e("Dead status true for ", relid + "");
                this.cardToRel.get(current).put(relid, true);
            } else {
                this.cardToRel.get(current).put(relid, true);
                current.dead();
            }
            this.groupmap.put(current.getGroup(), this.groupmap.get(current.getGroup()) - 1);
            Log.e("GameLog", this.getResources().getString(R.string.round) + " " + this.rounds + ": " + current.getName()
                    + "(" + this.textviewlist.get(relid).getText().toString()
                    + ") was killed by the assembly \n");
            gamelog.add(this.getResources().getString(R.string.round) + " " + this.rounds + ": " + current.getName()
                    + "(" + this.textviewlist.get(relid).getText().toString()
                    + ") was killed by the assembly ");
            Log.e("OnClick SetCharacter", this.cardToRel.get(current).toString());
            this.setCharacter(current, relid, false);
            this.abilitydialoglist.clear();
            for (RelativeLayout lin : this.lastactivecharacterrel) {
                lin.setBackgroundColor(Color.TRANSPARENT);
            }
            this.lastactivecharacterrel.clear();
        }
    }

    public List<Integer> loadGame(String savegamepath) {
        GameParse gameParse = new GameParse(this.getActivity());
        File loadGameFile = new File(savegamepath);
        gameParse.parseGame(loadGameFile);
        Log.e("Chars", this.getActivity().getFilesDir() + "/chars/" + gameParse.getGamesetname() + ".xml");
        this.gameset = gameParse.getGameset();
        this.counter = gameParse.getCounter();
        this.rounds = gameParse.getRounds();
        this.cardlist = gameParse.getCardlist();
        Log.e("Playerlist", gameParse.getCardlist().toString());
        this.cardlist2 = gameParse.getCardlist2();
        this.icon = this.gameset.getGamesetImg();
        this.lastrelcolor = gameParse.getColors();
        this.events = this.gameset.getEvents();
        this.extracardamount = gameParse.getExtracards().size();
        this.extracardlist = gameParse.getExtracards();
        this.cardTocardOverlays = gameParse.getCardTocardOverlays();
        for (Karte card : this.cardlist) {
            if (!this.groupmap.containsKey(card.getGroup())) {
                this.groupmap.put(card.getGroup(), 0);
            }
            this.groupmap.put(card.getGroup(), this.groupmap.get(card.getGroup()) + card.getCurrentamount());
        }
        this.loadedplayers = gameParse.getPlayers();
        this.playerarray = this.getPlayersArray();
        for (Player player : this.players.getPlayers()) {
            if (loadedplayers.contains(player.getPlayerid())) {
                this.playerarray.remove(player);
            }
        }
        Log.e("Load Game Playerarray", this.playerarray.toString());
        this.getActivity().setTitle(this.getResources().getString(R.string.round) + " " + this.rounds);
        this.cardarray = this.getCardsArray(true);
        this.lastcard = new Karte();
        this.currentcard = this.cardlist.get(counter - 1);
        return gameParse.getCardids();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //add this line
        setRetainInstance(true);
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);

        this.round2 = (Round2) this.getActivity();
        if (savedInstanceState != null && savedInstanceState.keySet().contains("round2id")) {
            //this.initView(savedInstanceState.getInt("round2id"), savedInstanceState);
        } else {
            this.initView(-1, savedInstanceState);
        }
        this.choosePlayer = view -> {
            Round2Fragment.this.initfinished = false;
            Round2Fragment.this.currentplayerid = view.getId();
            Round2Fragment.this.round2.chooseDialogs(DialogEnum.CHOOSEPLAYER);
        };
        View.OnClickListener chooseCharacter = view -> {
            Round2Fragment.this.initfinished = false;
            Round2Fragment.this.currentRelId = view.getId();
            Round2Fragment.this.round2.showDialog(1);
        };
        Boolean loadgame = false;
        if (this.firstload) {
            if (this.getArguments().containsKey("loadgame") && this.getArguments().getBoolean("loadgame")) {
                this.turn = false;
                loadgame = true;
            }
            this.firstload = false;
        }
        Log.e("Turn", this.turn.toString());
        Log.e("Counter", counter.toString());
        List<Integer> cardidlist = new ArrayList<>();
        if (!this.turn) {
            InputStream is = this.getResources().openRawResource(R.raw.players_config);
            this.playerarray = this.players.parsePlayer(is);
            this.playerList = this.playerarray;
            Log.e("ParsePlayers", "Parsing");
            if (loadgame) {
                Bundle bundle = this.getArguments();
                cardidlist = this.loadGame(bundle.getString("savegamepath"));
            } else {
                Bundle bundle = this.getArguments();
                this.rounds = 1;
                this.gameset = bundle.getParcelable("gameset");
                this.cardlist = bundle.getParcelableArrayList("cardlist");
                Map<Integer, Karte> positionlist = new TreeMap<>();
                for (Karte card : this.cardlist) {
                    if (card.getActionlist().isEmpty()) {
                        positionlist.put(card.getPosition(), card);
                    } else {
                        for (Action action : card.getActionlist().values()) {
                            positionlist.put(action.getPosition(), card);
                        }
                    }
                }
                Log.e("ActionList", positionlist.toString());
                List<Group> grouplist = bundle.getParcelableArrayList("groups");
                for (Karte card : cardlist) {
                    if (!this.groupmap.containsKey(card.getGroup())) {
                        this.groupmap.put(card.getGroup(), 0);
                    }
                    this.groupmap.put(card.getGroup(), this.groupmap.get(card.getGroup()) + card.getCurrentamount());
                }
                for (Karte card : this.cardlist) {
                    if (card.getPosition2() != -1) {
                        this.position2list.add(card);
                    }
                }
                Collections.sort(this.position2list, (s1, s2) -> s1.getPosition2().compareTo(s2.getPosition2()));
                this.events = this.gameset.getEvents();
                this.extracardamount = bundle.getInt("extracards");
                this.playerarray = this.getPlayersArray();
                this.cardarray = this.getCardsArray(false);
                this.icon = gameset.getGamesetImg();
            }
        } else {
            //Bundle bundle = savedInstanceState;
            //cardidlist = this.loadGame(bundle.getString("savegamepath"),false,bundle);
            if (((this.relviewlist.get(0).getParent()).getParent()) != null)
                ((GridLayout) (this.relviewlist.get(0).getParent()).getParent()).removeAllViews();
        }
        Log.e("Round2 Cardlist", this.cardlist.toString());
        if (this.getResources().getIdentifier(this.icon, "drawable", this.round2.getPackageName()) != 0) {
            this.round2.getSupportActionBar().setIcon(this.getResources().getIdentifier(this.icon, "drawable", this.round2.getPackageName()));
        } else {
            this.round2.getSupportActionBar().setIcon(R.drawable.title);
        }
        Point point = new Point(this.getActivity().getWindowManager().getDefaultDisplay().getWidth(), this.getActivity().getWindowManager().getDefaultDisplay().getHeight());
        Integer screenwidth = point.x;
        Integer screenheight = point.y;
        this.numberofcards = 0;
        for (Karte card : this.cardlist) {
            if (card.getCurrentamount() > 1) {
                numberofcards += card.getCurrentamount();
            } else {
                numberofcards++;
            }
        }
        numberofcards -= this.extracardamount;
        Log.e("Number Of Cards", numberofcards + "");
        int leftright = ((numberofcards - 6) / 2) < 3 ? 3 : ((numberofcards - 6) / 2);
        int avgcardheight = (screenheight - 25) / (leftright);
        int maxcardheight = ((Double) (screenheight * 0.2)).intValue();
        int avgcardwidth = screenwidth / 5;
        this.numberofviews = numberofcards < 12 ? 12 : numberofcards;
        int extraviews = numberofviews - numberofcards;
        this.scrollView = new ScrollView(this.getActivity());
        this.gridLayout = new GridLayout(this.getActivity());
        gridLayout.setOrientation(GridLayout.HORIZONTAL);
        gridLayout.setColumnCount(screenwidth / avgcardwidth);
        GridLayout.LayoutParams lparam = new GridLayout.LayoutParams();
        lparam.height = GridLayout.LayoutParams.FILL_PARENT;
        lparam.width = GridLayout.LayoutParams.FILL_PARENT;
        gridLayout.setLayoutParams(lparam);
        scrollView.addView(gridLayout);
        LayoutInflater inflater = LayoutInflater.from(this.getActivity());
        View mainview = inflater.inflate(R.layout.round2, gridLayout, false);
        GridLayout.Spec colspecs = android.support.v7.widget.GridLayout.spec(1, 3);
        GridLayout.Spec rowspecs = android.support.v7.widget.GridLayout.spec(1, leftright);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowspecs, colspecs);
        params.setGravity(Gravity.CENTER);
        params.width = ((screenwidth * 3) / 5) - 4;
        if (avgcardheight >= maxcardheight) {
            params.height = (screenheight - (2 * maxcardheight)) - 4;
            params.topMargin = maxcardheight;//(maxcardheight - (2 * (maxcardheight - 80)));
        } else {
            params.height = (screenheight - (2 * avgcardheight)) - 4;
            params.topMargin = avgcardheight;
        }
        this.mainbackrel = new RelativeLayout(this.getActivity());
        mainbackrel.setPadding(2, 2, 2, 2);
        mainbackrel.setLayoutParams(params);
        mainbackrel.setBackgroundColor(Color.DKGRAY);
        mainbackrel.setTag(Color.DKGRAY);
        if (!turn) {
            holder.characternametextview = (TextView) mainview.findViewById(R.id.roundTextView);
            holder.characternametextview.setText(this.gameset.getIntrotitle());
            holder.gamemastertextview = (TextView) mainview.findViewById(R.id.round2GameMasterTextView);
            holder.gamemastertextview.setText(this.gameset.getIntrotext());
            holder.playertextview = (TextView) mainview.findViewById(R.id.round2PlayerTextView);
            holder.playertextview.setText("");
            holder.descriptiontextview = (TextView) mainview.findViewById(R.id.roundDescriptionTextView);
            holder.descriptiontextview.setText("");
            holder.listView = (ListView) mainview.findViewById(R.id.abbListView);
            holder.showeventbutton = (Button) mainview.findViewById(R.id.showEventButton);
            holder.showeventbutton.setVisibility(GONE);
            holder.showeventbutton.setOnClickListener(view -> {
                Round2Fragment.this.round2.setPlayers(Round2Fragment.this.getPlayersArray());
                Round2Fragment.this.round2.showDialog(5);
            });
            holder.showgamelogbutton = (Button) mainview.findViewById(R.id.showGameLog);
            holder.showgamelogbutton.setVisibility(GONE);
            holder.showgamelogbutton.setOnClickListener(view -> {
                Utils.dialogtext = "";
                for (String logpart : Round2Fragment.this.gamelog) {
                    Utils.dialogtext += Round2Fragment.this.gamelog;
                }
                Round2Fragment.this.round2.showDialog(14);
            });
            holder.view = mainview;
            holder.view.setOnClickListener(this.viewOnClick);
            cardcount = -1;
            mainbackrel.addView(mainview);
            gridLayout.addView(mainbackrel);
        } else {
            //holder.view = this.round2.holder.view;
            ((RelativeLayout) holder.view.getParent()).removeView(holder.view);
            mainbackrel.addView(this.holder.view);
            gridLayout.addView(mainbackrel);
        }
        for (int i = 1; i < gridLayout.getColumnCount() - 1 && cardcount < numberofviews - 1; i++) {
            cardcount++;
            GridLayout.Spec colspec2 = android.support.v7.widget.GridLayout.spec(i, 1);
            GridLayout.Spec rowspec2 = android.support.v7.widget.GridLayout.spec(0, 1);
            GridLayout.LayoutParams param2 = new GridLayout.LayoutParams(rowspec2, colspec2);
            if (avgcardheight >= maxcardheight)
                param2.height = maxcardheight;
            else
                param2.height = avgcardheight;
            param2.width = avgcardwidth;
            param2.setGravity(Gravity.RIGHT);
            RelativeLayout backrel;
            if (turn && cardcount < numberofcards) {
                this.relviewlist.get(cardcount).setLayoutParams(new RelativeLayout.LayoutParams((avgcardwidth) - 4, avgcardheight - 4));
                backrel = (RelativeLayout) this.relviewlist.get(cardcount).getParent();
            } else if (!turn && loadgame && cardcount < numberofcards) {
                backrel = recreateLayouts(cardcount, screenwidth, avgcardwidth, avgcardheight, mainview, this.cardlist.get(cardcount).isdead(), cardidlist.get(cardcount), this.gameset.getBackImg());
                backrel.setBackgroundColor(this.lastrelcolor.get(cardcount));
                backrel.setTag(this.lastrelcolor.get(cardcount));
            } else if (cardcount < numberofcards) {
                backrel = createLayouts(cardcount, screenwidth, avgcardwidth, avgcardheight, mainview, this.gameset.getBackImg());
            } else {
                backrel = new RelativeLayout(this.getActivity());
            }
            backrel.setLayoutParams(param2);
            backrel.setPadding(2, 2, 2, 2);
            this.backrellist.add(backrel);
            gridLayout.addView(backrel);
        }
        for (int i = 0; i < leftright && cardcount < numberofviews - 1; i++) {
            cardcount++;
            android.support.v7.widget.GridLayout.Spec colspec2 = android.support.v7.widget.GridLayout.spec(4, 1);
            android.support.v7.widget.GridLayout.Spec rowspec2 = android.support.v7.widget.GridLayout.spec(i, 1);
            GridLayout.LayoutParams param2 = new GridLayout.LayoutParams(rowspec2, colspec2);
            param2.height = avgcardheight;
            param2.width = avgcardwidth;
            param2.setGravity(Gravity.RIGHT);
            RelativeLayout backrel;
            if (turn && cardcount < numberofcards) {
                this.relviewlist.get(cardcount).setLayoutParams(new RelativeLayout.LayoutParams((avgcardwidth) - 4, avgcardheight - 4));
                backrel = (RelativeLayout) this.relviewlist.get(cardcount).getParent();
            } else if (!turn && loadgame && cardcount < numberofcards) {
                backrel = recreateLayouts(cardcount, screenwidth, avgcardwidth, avgcardheight, mainview, this.cardlist.get(cardcount).isdead(), cardidlist.get(cardcount), this.gameset.getBackImg());
                backrel.setBackgroundColor(this.lastrelcolor.get(cardcount));
                backrel.setTag(this.lastrelcolor.get(cardcount));
            } else if (cardcount < numberofcards) {
                backrel = createLayouts(cardcount, screenwidth, avgcardwidth, avgcardheight, mainview, this.gameset.getBackImg());
            } else {
                backrel = new RelativeLayout(this.getActivity());
            }
            backrel.setLayoutParams(param2);
            backrel.setPadding(2, 2, 2, 2);
            this.backrellist.add(backrel);
            gridLayout.addView(backrel);
        }
        for (int i = (gridLayout.getColumnCount() - 2); i > 0 && cardcount < numberofviews - 1; i--) {
            cardcount++;
            android.support.v7.widget.GridLayout.Spec colspec2 = android.support.v7.widget.GridLayout.spec(i, 1);
            android.support.v7.widget.GridLayout.Spec rowspec2 = android.support.v7.widget.GridLayout.spec(leftright - 1, 1);
            GridLayout.LayoutParams param2 = new GridLayout.LayoutParams(rowspec2, colspec2);
            if (avgcardheight >= maxcardheight)
                param2.height = maxcardheight;
            else
                param2.height = avgcardheight;
            param2.width = avgcardwidth;
            param2.setGravity(Gravity.BOTTOM);
            RelativeLayout backrel;
            if (turn && cardcount < numberofcards) {
                this.relviewlist.get(cardcount).setLayoutParams(new RelativeLayout.LayoutParams((avgcardwidth) - 4, avgcardheight - 4));
                backrel = (RelativeLayout) this.relviewlist.get(cardcount).getParent();
            } else if (!turn && loadgame && cardcount < numberofcards) {
                backrel = recreateLayouts(cardcount, screenwidth, avgcardwidth, avgcardheight, mainview,
                        this.cardlist.get(cardcount).isdead(), cardidlist.get(cardcount), this.gameset.getBackImg());
                backrel.setBackgroundColor(this.lastrelcolor.get(cardcount));
                backrel.setTag(this.lastrelcolor.get(cardcount));
            } else if (cardcount < numberofcards) {
                backrel = createLayouts(cardcount, screenwidth, avgcardwidth, avgcardheight, mainview, this.gameset.getBackImg());
            } else {
                backrel = new RelativeLayout(this.getActivity());
            }
            backrel.setLayoutParams(param2);
            backrel.setPadding(2, 2, 2, 2);
            this.backrellist.add(backrel);
            gridLayout.addView(backrel);
        }
        for (int i = leftright - 1; i >= 0 && cardcount < numberofviews - 1; i--) {
            cardcount++;
            android.support.v7.widget.GridLayout.Spec colspec2 = android.support.v7.widget.GridLayout.spec(0, 1);
            android.support.v7.widget.GridLayout.Spec rowspec2 = android.support.v7.widget.GridLayout.spec(i, 1);
            GridLayout.LayoutParams param2 = new GridLayout.LayoutParams(rowspec2, colspec2);
            param2.height = avgcardheight;
            param2.width = avgcardwidth;
            param2.setGravity(Gravity.RIGHT);
            RelativeLayout backrel;
            if (turn && cardcount < numberofcards) {
                backrel = (RelativeLayout) this.relviewlist.get(cardcount).getParent();
                this.relviewlist.get(cardcount).setLayoutParams(new RelativeLayout.LayoutParams((avgcardwidth) - 4, avgcardheight - 4));
            } else if (!turn && loadgame && cardcount < numberofcards) {
                backrel = recreateLayouts(cardcount, screenwidth, avgcardwidth, avgcardheight, mainview, this.cardlist.get(cardcount).isdead(), cardidlist.get(cardcount), this.gameset.getBackImg());
                backrel.setBackgroundColor(this.lastrelcolor.get(cardcount));
                backrel.setTag(this.lastrelcolor.get(cardcount));
            } else if (cardcount < numberofcards) {
                backrel = createLayouts(cardcount, screenwidth, avgcardwidth, avgcardheight, mainview, this.gameset.getBackImg());
            } else {
                backrel = new RelativeLayout(this.getActivity());
            }
            backrel.setLayoutParams(param2);
            backrel.setPadding(2, 2, 2, 2);
            this.backrellist.add(backrel);
            gridLayout.addView(backrel);
        }
        this.getActivity().setTitle(this.getResources().getString(R.string.round) + " " + this.rounds);
        if (this.extracardamount > 0 && !turn && !loadgame) {
            this.getActivity().showDialog(3);
        }
        if (loadgame) {
            this.cardlist = this.cardlist2;
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.scrollView.getParent() != null)
            ((FrameLayout) this.scrollView.getParent()).removeAllViews();
        Point point = new Point(this.getActivity().getWindowManager().getDefaultDisplay().getWidth(), this.getActivity().getWindowManager().getDefaultDisplay().getHeight());
        Integer screenwidth = point.x;
        Integer screenheight = point.y;
        int leftright = ((numberofcards - 6) / 2) < 3 ? 3 : ((numberofcards - 6) / 2);
        int avgcardheight = (screenheight - 25) / (leftright);
        int maxcardheight = ((Double) (screenheight * 0.15)).intValue();
        int avgcardwidth = screenwidth / 5;
        GridLayout.Spec colspecs = android.support.v7.widget.GridLayout.spec(1, 3);
        GridLayout.Spec rowspecs = android.support.v7.widget.GridLayout.spec(1, leftright);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowspecs, colspecs);
        params.setGravity(Gravity.CENTER);
        params.width = ((screenwidth * 3) / 5) - 4;
        if (avgcardheight >= maxcardheight) {
            params.height = (screenheight - (2 * maxcardheight)) - 4;
            params.topMargin = (maxcardheight) * -1/*(150 - (2 * (avgcardheight - 80)))*/;
        } else {
            params.height = (screenheight - (2 * avgcardheight)) - 4;
            params.topMargin = avgcardheight * -1;
        }
        this.mainbackrel.setLayoutParams(params);
        cardcount = -1;
        for (int i = 1; i < gridLayout.getColumnCount() - 1 && cardcount < numberofviews - 1; i++) {
            cardcount++;
            android.support.v7.widget.GridLayout.Spec colspec2 = android.support.v7.widget.GridLayout.spec(i, 1);
            android.support.v7.widget.GridLayout.Spec rowspec2 = android.support.v7.widget.GridLayout.spec(0, 1);
            GridLayout.LayoutParams param2 = new GridLayout.LayoutParams(rowspec2, colspec2);
            if (avgcardheight >= maxcardheight)
                param2.height = maxcardheight;
            else
                param2.height = avgcardheight;
            param2.width = avgcardwidth;
            param2.setGravity(Gravity.RIGHT);
            if (cardcount < numberofcards)
                this.relviewlist.get(cardcount).setLayoutParams(new RelativeLayout.LayoutParams((avgcardwidth) - 4, avgcardheight - 4));
            this.backrellist.get(cardcount).setLayoutParams(param2);
        }
        for (int i = 0; i < leftright && cardcount < numberofviews - 1; i++) {
            cardcount++;
            android.support.v7.widget.GridLayout.Spec colspec2 = android.support.v7.widget.GridLayout.spec(4, 1);
            android.support.v7.widget.GridLayout.Spec rowspec2 = android.support.v7.widget.GridLayout.spec(i, 1);
            GridLayout.LayoutParams param2 = new GridLayout.LayoutParams(rowspec2, colspec2);
            param2.height = avgcardheight;
            param2.width = avgcardwidth;
            param2.setGravity(Gravity.RIGHT);
            if (cardcount < numberofcards)
                this.relviewlist.get(cardcount).setLayoutParams(new RelativeLayout.LayoutParams((avgcardwidth) - 4, avgcardheight - 4));
            this.backrellist.get(cardcount).setLayoutParams(param2);
        }
        for (int i = (gridLayout.getColumnCount() - 2); i > 0 && cardcount < numberofviews - 1; i--) {
            cardcount++;
            android.support.v7.widget.GridLayout.Spec colspec2 = android.support.v7.widget.GridLayout.spec(i, 1);
            android.support.v7.widget.GridLayout.Spec rowspec2 = android.support.v7.widget.GridLayout.spec(leftright - 1, 1);
            GridLayout.LayoutParams param2 = new GridLayout.LayoutParams(rowspec2, colspec2);
            if (avgcardheight >= maxcardheight)
                param2.height = maxcardheight;
            else
                param2.height = avgcardheight;
            param2.width = avgcardwidth;
            param2.setGravity(Gravity.BOTTOM);
            if (cardcount < numberofcards)
                this.relviewlist.get(cardcount).setLayoutParams(new RelativeLayout.LayoutParams((avgcardwidth) - 4, avgcardheight - 4));
            this.backrellist.get(cardcount).setLayoutParams(param2);
        }
        for (int i = leftright - 1; i >= 0 && cardcount < numberofviews - 1; i--) {
            cardcount++;
            android.support.v7.widget.GridLayout.Spec colspec2 = android.support.v7.widget.GridLayout.spec(0, 1);
            android.support.v7.widget.GridLayout.Spec rowspec2 = android.support.v7.widget.GridLayout.spec(i, 1);
            GridLayout.LayoutParams param2 = new GridLayout.LayoutParams(rowspec2, colspec2);
            param2.height = avgcardheight;
            param2.width = avgcardwidth;
            param2.setGravity(Gravity.RIGHT);
            if (cardcount < numberofcards)
                this.relviewlist.get(cardcount).setLayoutParams(new RelativeLayout.LayoutParams((avgcardwidth) - 4, avgcardheight - 4));

            this.backrellist.get(cardcount).setLayoutParams(param2);
        }
        return this.scrollView;
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        /*outState.putInt("counter", counter);
        outState.putBoolean("calleveryone", calleveryone);*/
        outState.putBoolean("firstload", firstload);
        outState.putBoolean("loadgame", false);
        /*outState.putBoolean("end",this.end);

        outState.putBoolean("turn",true);
        outState.putBoolean("roundend",this.roundEnd);
        outState.putParcelable("currentcard", this.currentcard);
        outState.putParcelable("lastcard",this.lastcard);
        outState.putParcelable("gameset",this.gameset);
        outState.putParcelableArrayList("abilitydialoglist",new ArrayList<>(this.abilitydialoglist));
        outState.putParcelableArrayList("cardlist", new ArrayList<>(this.cardlist));
        outState.putParcelableArrayList("extracardlist",new ArrayList<>(this.extracardlist));
        outState.putParcelableArrayList("playerarray",new ArrayList<>(this.playerarray));
        outState.putParcelableArrayList("playerlist",new ArrayList<>(this.playerList));
        outState.putParcelableArrayList("cardarray",new ArrayList<>(this.cardarray));
        outState.putParcelableArrayList("groupdialogarray",new ArrayList<>(this.groupdialogarray));
        outState.putParcelableArrayList("position2list",new ArrayList<>(this.position2list));
        outState.putString("savegamepath",this.saveGame(false));
        outState.putInt("position2counter",this.position2counter);
        outState.putInt("currentevent",this.currentevent);
        outState.putInt("currentplayerid", this.currentplayerid);
        outState.putInt("currentrelid",this.currentRelId);
        outState.putInt("extracardamount",this.extracardamount);
        outState.putInt("counter",this.counter);
        outState.putInt("rounds",this.rounds);
        outState.putInt("concernedtemp",this.concernedtemp);
        outState.putInt("status",this.status.ordinal());
        outState.putIntegerArrayList("lastrelcolor", new ArrayList<>(this.lastrelcolor));
        ArrayList<Integer> relToPlayerKeys=new ArrayList<>(relToPlayer.keySet());
        ArrayList<Player> relToPlayerVals=new ArrayList<>(relToPlayer.values());
        outState.putIntegerArrayList("reltoplayerkeys",relToPlayerKeys);
        outState.putParcelableArrayList("reltoplayervals",relToPlayerVals);
        ArrayList<Integer> relToCardKeys=new ArrayList<>(relToCard.keySet());
        ArrayList<Integer> relToCardVals=new ArrayList<>(relToCard.values());
        outState.putIntegerArrayList("reltocardkeys",relToCardKeys);
        outState.putIntegerArrayList("reltocardvals",relToCardVals);
/*        for(RelativeLayout layout:this.lastactivecharacterrel){
             relidlist.add(layout.getId());
        }
        outState.putIntegerArrayList("relidlist",relidlist);
        outState.putString("icon", this.icon);
        outState.putString("gamelog",this.gamelog);
        outState.putString("winninggroup",this.winninggroup);
        outState.putInt("round2id", instanceid); */
    }

    /**
     * Recreates an already existing layout on loading a game.
     *
     * @param i
     * @param screenwidth
     * @param avgcardwidth
     * @param avgcardheight
     * @param view
     * @param dead
     * @param cardid
     * @return
     */
    private RelativeLayout recreateLayouts(final int i, final int screenwidth, final int avgcardwidth, final int avgcardheight,
                                           final View view, boolean dead, Integer cardid, final String backimg) {
        Log.e("Cardid", cardid.toString());
        Log.e("Cardlist", this.cardlist.toString());
        Log.e("Img", this.cardlist.get(i).toString());
        if (cardid == -1) {
            this.loadgameskipcount++;
            return this.createLayouts(i, screenwidth, avgcardwidth, avgcardheight, view, backimg);
        }
        final RelativeLayout backrel = new RelativeLayout(this.getActivity());
        backrel.setBackgroundColor(Color.TRANSPARENT);
        backrel.setTag(Color.TRANSPARENT);
        final RelativeLayout rel2 = new RelativeLayout(this.getActivity());
        if (dead) {
            rel2.setBackgroundDrawable(new BitmapDrawable(Utils.toGrayscale(((BitmapDrawable) this.getResources()
                    .getDrawable(this.getResources().getIdentifier(this.cardlist.get(i - loadgameskipcount).getImg(), "drawable", this.round2.getPackageName()))).getBitmap())));
            rel2.setEnabled(false);
        } else {
            rel2.setBackgroundDrawable(this.getResources().getDrawable(this.getResources().getIdentifier(this.cardlist.get(i - loadgameskipcount).getImg(), "drawable", this.round2.getPackageName())));
        }

        int cardlist2index = 0;
        for (Karte card : this.cardlist2) {
            if (card.equals(this.cardlist.get(i - loadgameskipcount))) {
                break;
            }
            cardlist2index++;
        }
        cardid = cardlist2index;
        this.relToCard.put(i, cardlist2index);
        this.cardarray.remove(this.cardlist.get(i - loadgameskipcount));
        if (!cardToRel.containsKey(this.cardlist.get(i - loadgameskipcount)))
            this.cardToRel.put(this.cardlist.get(i - loadgameskipcount), new TreeMap<Integer, Boolean>());
        cardToRel.get(this.cardlist.get(i - loadgameskipcount)).put(i, dead);
        if (!this.cardUUIDTocardId.containsKey(this.cardlist.get(i - loadgameskipcount).getCardid())) {
            this.cardUUIDTocardId.put(this.cardlist.get(i - loadgameskipcount).getCardid(), new TreeSet<Integer>());
        }
        this.cardUUIDTocardId.get(this.cardlist.get(i - loadgameskipcount).getCardid()).add(cardlist2index);
        rel2.setLayoutParams(new RelativeLayout.LayoutParams((avgcardwidth) - 4, avgcardheight - 4));
        backrel.setPadding(2, 2, 0, 0);
        rel2.setId(i);
        backrel.setId(i);
        backrel.addView(rel2);
        rel2.setOnClickListener(this.roundCharOnClick);
        this.relToImgView.put(i, new TreeMap<Integer, List<ImageView>>());
        TextView textview = new TextView(this.getActivity());
        String currentplayer = this.loadedplayers.get(i);
        for (Player player : this.playerList) {
            if (currentplayer.equals(player.getPlayerid())) {
                this.relToPlayer.put(i, player);
                break;
            }
        }
        textview.setText(this.relToPlayer.get(i).toString());
        textview.setBackgroundColor(Color.argb(50, 255, 0, 0));
        RelativeLayout.LayoutParams parameters = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        parameters.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, view.getId());
        textview.setLayoutParams(parameters);
        this.textviewlist.add(textview);
        this.relviewlist.add(rel2);
        rel2.addView(textview);
        if (!this.cardTocardOverlays.containsKey(i)) {
            this.cardTocardOverlays.put(i, new TreeMap<Integer, Triple<String, Integer, Integer>>());
        }
        for (Integer sourcecard : this.cardTocardOverlays.get(i).keySet()) {
            this.addOverlayToChar(i, this.cardlist2.get(sourcecard).getUuidToAbility().
                    get(this.cardTocardOverlays.get(i).get(sourcecard).getOne()), sourcecard);
        }
        return backrel;
    }

    public void removePosition(Integer relid) {
        Integer cardlistid = this.relToCard.get(relid);
        if (this.cardlist.get(cardlistid).getCurrentamount() > 1) {
            this.cardlist.get(cardlistid).setCurrentamount(this.cardlist.get(cardlistid).getCurrentamount() - 1);
        } else {
            this.cardlist.remove(this.relToCard.get(relid).intValue());
        }
        this.cardToRel.get(this.cardlist.get(cardlistid)).remove(relid);
        this.relToCard.remove(relid);
        this.relToPlayer.remove(relid);
        this.relToImgView.remove(relid);
        this.relviewlist.get(relid).setBackgroundDrawable(
                this.getResources().getDrawable(this.getResources().getIdentifier(this.gameset.getBackImg(), "drawable", this.getActivity().getPackageName())));
    }

    /**
     * Replaces a living character using a new card not yet to be found in the game
     *
     * @param toReplace   the card to be replaced
     * @param replacement the replacement of the card
     * @param relid       the id of the layout
     */
    private void replaceCharWithNewChar(final Karte toReplace, final Karte replacement, final Integer relid) {
        Log.e("Replace ", toReplace + " with " + replacement + " " + relid);
        if (toReplace.getCurrentamount() > 1) {
            toReplace.setCurrentamount(toReplace.getCurrentamount() - 1);
            this.cardToRel.get(toReplace).remove(relid);
        } else {
            toReplace.dead();
            this.cardToRel.remove(toReplace);
        }
        this.cardExchangeMap.put(relid, new Tuple<>(toReplace, -1));
        this.relToCard.remove(relid);
        if (this.cardlist.contains(replacement)) {
            this.cardlist.get(this.cardlist.indexOf(replacement)).setCurrentamount(this.cardlist.get(this.cardlist.indexOf(replacement)).getCurrentamount() + 1);
        } else {
            this.cardlist.add(replacement);
            this.cardToRel.put(replacement, new TreeMap<Integer, Boolean>());
        }
        this.cardToRel.get(replacement).put(relid, false);
        this.relToCard.put(relid, this.cardlist.indexOf(replacement));
        this.setCharacter(replacement, relid, false);
    }

    public void replacePosition(Player player, Karte card, Integer relid) {
        if (!this.cardlist.contains(card)) {
            this.cardlist.add(card);
        } else {
            Integer cardlistiindex = this.cardlist.indexOf(card);
            this.cardlist.get(cardlistiindex).setCurrentamount(this.cardlist.get(cardlistiindex).getCurrentamount() + 1);
        }
        this.replaceCharWithNewChar(this.cardlist.get(this.relToCard.get(relid)), card, relid);
        this.relToPlayer.put(relid, player);
        this.textviewlist.get(relid).setText(player.getFirstname() + " " + player.getName());
    }

    public String saveGame(Boolean save) {
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.setOutput(writer);
            serializer.startTag("", "SaveGame");
            serializer.attribute("", "date", String.valueOf(System.currentTimeMillis()));
            serializer.attribute("", "round", this.rounds.toString());
            serializer.attribute("", "counter", this.counter.toString());
            serializer.attribute("", "version", "1.0");
            serializer.attribute("", "gameset", this.gameset.getGamesetid());
            serializer.flush();
            serializer.startTag("", "gamefield");
            for (Integer relid : this.relToCard.keySet()) {
                serializer.startTag("", "field");
                serializer.attribute("", "relId", relid.toString());
                serializer.attribute("", "color", ((RelativeLayout) this.relviewlist.get(relid).getParent()).getTag() + "");
                if (relToCard.get(relid) == -1) {
                    serializer.attribute("", "charid", "-1");
                    serializer.attribute("", "cardname", "back");
                    serializer.attribute("", "dead", "false");
                } else {
                    serializer.attribute("", "charid", this.relToCard.get(relid).toString());
                    serializer.attribute("", "cardname", this.cardlist.get(this.relToCard.get(relid)).getCardid());
                    serializer.attribute("", "dead", this.cardlist.get(this.relToCard.get(relid)).isdead().toString());
                }
                Log.e("RelToPlayer", this.relToPlayer.toString());
                serializer.attribute("", "player", this.relToPlayer.get(relid).getPlayerid());
                if (this.cardTocardOverlays.containsKey(relid)) {
                    for (Integer sourcecardid : this.cardTocardOverlays.get(relid).keySet()) {
                        serializer.startTag("", "overlay");
                        Log.e("Overlay", "from " + this.cardlist.get(sourcecardid).getName() + " ability " + this.cardTocardOverlays.get(relid).get(sourcecardid).getOne() + " duration: " + this.cardTocardOverlays.get(relid).get(sourcecardid).getTwo());
                        serializer.attribute("", "from", sourcecardid.toString());
                        serializer.attribute("", "abb", this.cardTocardOverlays.get(relid).get(sourcecardid).getOne());
                        serializer.attribute("", "duration", this.cardTocardOverlays.get(relid).get(sourcecardid).getTwo().toString());
                        serializer.endTag("", "overlay");
                    }
                }
                serializer.endTag("", "field");
            }
            serializer.startTag("", "notassigned");
            for (Karte card : this.cardlist) {
                if (!this.cardToRel.containsKey(card)) {
                    serializer.startTag("", "card");
                    serializer.text(card.getName());
                    serializer.endTag("", "card");
                }
            }
            serializer.endTag("", "notassigned");
            serializer.endTag("", "gamefield");
            serializer.startTag("", "extra");
            for (Karte card : this.extracardlist) {
                serializer.startTag("", "card");
                serializer.text(card.toString());
                serializer.endTag("", "card");
            }
            serializer.endTag("", "extra");
            serializer.endTag("", "SaveGame");
            serializer.flush();
            Log.e("SaveGame: ", writer.toString());
            writer.toString();
            if (save) {
                //this.openFileOutput("save-"+System.currentTimeMillis()+"",0);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }

    public void setCardsDialog(Integer which,Boolean delete) {
        Karte card = this.cardarray.get(which);
        Log.e("Card", this.cardUUIDTocardId.get(card.getCardid()).toString());
        if (this.cardToRel.get(this.cardlist.get(this.cardUUIDTocardId.get(card.getCardid())
                .iterator().next())) == null) {
            this.cardToRel.put(this.cardlist.get(this.cardUUIDTocardId.get(card.getCardid())
                    .iterator().next()), new TreeMap<Integer, Boolean>());
        }
        this.cardToRel.get(this.cardlist.get(this.cardUUIDTocardId.get(card.getCardid())
                .iterator().next())).put(this.currentRelId, false);
        this.relToCard.put(this.currentRelId, this.cardUUIDTocardId.get(card.getCardid())
                .iterator().next());
        this.setCharacter(this.cardlist.get(this.cardUUIDTocardId.get(card.getCardid())
                .iterator().next()), this.currentRelId, false);
        if(delete)
            this.cardarray.remove(which.intValue());
        //this.setCharacter(new Karte(),view.getId());
        if(this.relToPlayer.containsKey(currentRelId)){
            this.relviewlist.get(this.currentRelId).setOnClickListener(roundCharOnClick);
        }else{
            this.relviewlist.get(this.currentRelId).setOnClickListener(this.choosePlayer);
        }

    }

    /**
     * Sets the character imageview for the given character.
     *
     * @param card      the card to use
     * @param cardrelid its id
     */
    private void setCharacter(final Karte card, final Integer cardrelid, final Boolean back) {
        Log.e("Cardid", cardrelid.toString());
        Log.e("Card", card.toString());
        Log.e("CardToRel ", this.cardToRel.toString());
        Log.e("GameSetSourcefile", gameset.getSourcefile());
        if (back) {
            if (card.imgexists())
                this.relviewlist.get(cardrelid).setBackgroundDrawable(Utils.loadDrawable(this.getActivity().getBaseContext().getFilesDir().getAbsolutePath(),gameset.getLanguage()+"_"+gameset.getGamesetid(), card.getCardid() + ".png"));
                //this.relviewlist.get(cardrelid).setBackgroundDrawable(this.getResources().getDrawable(this.getResources()
                //        .getIdentifier(card.getImg(), "drawable", this.getActivity().getPackageName())));
            else
                this.relviewlist.get(cardrelid).setBackgroundDrawable(this.getResources().getDrawable(R.drawable.title));
        } else {
            Log.e("SetCharacter", this.cardToRel.get(card).toString());
            if (this.cardToRel.get(card).get(cardrelid)) {
                if (card.imgexists())
                    this.relviewlist.get(cardrelid).setBackgroundDrawable(new BitmapDrawable(Utils.toGrayscale(((BitmapDrawable) Utils.loadDrawable(this.getActivity().getBaseContext().getFilesDir().getAbsolutePath(),gameset.getLanguage()+"_"+gameset.getGamesetid(), card.getCardid() + ".png")).getBitmap())));
                    //this.relviewlist.get(cardrelid).setBackgroundDrawable(new BitmapDrawable(Utils.toGrayscale(((BitmapDrawable) this.getResources()
                    //.getDrawable(this.getResources().getIdentifier(card.getImg(), "drawable", this.getActivity().getPackageName()))).getBitmap())));
                else
                    this.relviewlist.get(cardrelid).setBackgroundDrawable(new BitmapDrawable(Utils.toGrayscale(((BitmapDrawable) this.getResources()
                            .getDrawable(this.getResources().getIdentifier("title", "drawable", this.getActivity().getPackageName()))).getBitmap())));
            } else {
                if (card.imgexists())
                    this.relviewlist.get(cardrelid).setBackgroundDrawable(Utils.loadDrawable(this.getActivity().getBaseContext().getFilesDir().getAbsolutePath(),gameset.getLanguage()+"_"+gameset.getGamesetid(), card.getCardid() + ".png"));
                    //this.relviewlist.get(cardrelid).setBackgroundDrawable(this.getResources().getDrawable(this.getResources()
                    //.getIdentifier(card.getImg(), "drawable", this.getActivity().getPackageName())));
                else
                    this.relviewlist.get(cardrelid).setBackgroundDrawable(this.getResources().getDrawable(R.drawable.title));
            }
        }
    }

    public void setGroup(Integer position) {
        this.currentcard.setGroup(this.groupdialogarray.get(position));
        holder.view.setEnabled(true);
    }

    public void setPlayerDialog(Integer i) {
        Player player = this.playerarray.get(i);
        Log.e("Player", player.toString());
        this.relToPlayer.put(this.currentplayerid, player);
        Log.e("Add to relToPlayer", player.getName() + " " + this.currentRelId.toString());
        this.textviewlist.get(this.currentplayerid).setText(player.getFirstname() + " " + player.getName());
        this.relviewlist.get(this.currentplayerid).setId(this.currentplayerid);
        this.playerarray.remove(i.intValue());
        Log.e("relToCard",relToCard.toString());
        if(this.relToCard.get(currentplayerid)!=-1){
            this.relviewlist.get(this.currentplayerid).setOnClickListener(roundCharOnClick);
        }else{
            this.relviewlist.get(this.currentplayerid).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    Round2Fragment.this.currentRelId = view.getId();
                    Round2Fragment.this.round2.showDialog(1);
                }
            });
        }
    }

    /**
     * Replaces a character card with another using the character card menu.
     *
     * @param current  the first character
     * @param toChange the second character
     */
    public void switchExtraCharWithChar(final Karte current, final Karte toChange, final Boolean addnew, final Integer relId, final Boolean extra, final Boolean back) {
        Log.e("Current", current.toString());
        Log.e("toChange", toChange.toString());
//        Log.e("Lastactivecharacterrel", this.lastactivecharacterrel.iterator().next().toString());
        Log.e("Relid", relId.toString());
        Log.e("CardToRel", this.cardToRel.toString());
        if (addnew) {
            this.cardExchangeMap.put(relId, new Tuple<>(current, current.getabblist().iterator().next().getDuration()));
            Log.e("GameLog", this.getResources().getString(R.string.round) + " " + this.rounds + ": "
                    + current.getName() + "(" + this.textviewlist.get(this.lastcheckedrel.getId()).getText().toString() +
                    ") " + this.getString(R.string.uses) + " " + current.getabblist().iterator().next().getDescription() + "(" + current.getabblist().iterator().next().getCurrentamount() + ") " + this.getResources().getString(R.string.tochangeto) + " "
                    + toChange.getName() + " (" + this.textviewlist.get(this.lastcheckedrel.getId()).getText().toString() + ")\n");
            gamelog.add(this.getResources().getString(R.string.round) + " " + this.rounds + ": "
                    + current.getName() + "(" + this.textviewlist.get(this.lastcheckedrel.getId()).getText().toString() +
                    ") " + this.getString(R.string.uses) + " " + current.getabblist().iterator().next().getDescription() + "(" + current.getabblist().iterator().next().getCurrentamount() + ") " + this.getResources().getString(R.string.tochangeto) + " "
                    + toChange.getName() + " (" + this.textviewlist.get(this.lastcheckedrel.getId()).getText().toString() + ")");
        }
        if (extra)
            this.extracardlist.add(current);
        else {
            toChange.setdead(false);
        }
        this.relToCard.remove(relId);
        this.relToCard.put(relId, this.cardUUIDTocardId.get(toChange.getCardid()).iterator().next());
        if (this.cardToRel.containsKey(toChange)) {
            this.cardToRel.get(toChange).put(relId, false);
        } else if (back) {
            this.cardToRel.put(toChange, new TreeMap<Integer, Boolean>());
            this.cardToRel.get(toChange).put(relId, false);
        } else {
            this.cardToRel.put(toChange, this.cardToRel.get(current));
        }
        if (this.cardToRel.get(current).size() > 1) {
            this.cardToRel.get(current).remove(relId);
        } else {
            this.cardToRel.remove(current);
        }
        if (extra && !back)
            this.extracardlist.remove(toChange);
        else {
            current.dead();
        }
        toChange.setdead(false);
        this.setCharacter(toChange, relId, true);
        this.status = Round2Enum.ROUND;
        holder.view.setEnabled(true);
    }

    private void switchLivingCharGroups(final Karte current, final Karte toChange){
        Group group=current.getGroup();
        current.setGroup(toChange.getGroup());
        toChange.setGroup(group);
    }


    private void switchLivingCharOverlays(final Karte current, final Karte toChange){
            //TODO Switch icons like the lovers icons
    }

    /**
     * Switches the positions of two living characters in the game.
     *
     * @param current  the first character
     * @param toChange the second character
     * @param relId    the first relation id
     * @param relId2   the second relation id
     */
    private void switchLivingCharWithChar(final Karte current, final Karte toChange, final Integer relId, final Integer relId2) {
        Log.e("SwitchLivingChar", current.toString() + " " + toChange.toString() + " " + relId.toString() + " " + relId2.toString());
        RelativeLayout currentlayout = (RelativeLayout) this.relviewlist.get(relId).getParent();
        RelativeLayout toChangelayout = (RelativeLayout) this.relviewlist.get(relId2).getParent();
        Integer currentcolor = (Integer) currentlayout.getTag();
        Integer toChangecolor = (Integer) toChangelayout.getTag();
        if (this.lastactivecharacterrel.contains(currentlayout)) {
            this.lastactivecharacterrel.remove(currentlayout);
            this.lastactivecharacterrel.add(toChangelayout);
        }
        if (this.lastactivecharacterrel.contains(toChangelayout)) {
            this.lastactivecharacterrel.remove(toChangelayout);
            this.lastactivecharacterrel.add(currentlayout);
        }
        currentlayout.setBackgroundColor(toChangecolor);
        currentlayout.setTag(toChangecolor);
        toChangelayout.setBackgroundColor(currentcolor);
        toChangelayout.setTag(currentcolor);
        this.relToCard.remove(relId);
        this.relToCard.remove(relId2);
        this.relToCard.put(relId,
                this.cardUUIDTocardId.get(toChange.getCardid())
                        .iterator().next()
        );
        this.relToCard.put(relId2,
                this.cardUUIDTocardId.get(current.getCardid())
                        .iterator().next()
        );
        final Map<Integer, Boolean> currentset = new TreeMap<>();
        final Map<Integer, Boolean> tochangeset = new TreeMap<>();
        for (Integer value : this.cardToRel.get(current).keySet()) {
            if (!value.equals(relId))
                currentset.put(value, this.cardToRel.get(current).get(value));
        }
        for (Integer value : this.cardToRel.get(toChange).keySet()) {
            if (!value.equals(relId2))
                tochangeset.put(value, this.cardToRel.get(toChange).get(value));
        }
        currentset.put(relId2, false);
        tochangeset.put(relId, false);
        this.cardToRel.put(toChange, tochangeset);
        this.cardToRel.put(current, currentset);
        this.setCharacter(current, relId2, false);
        this.setCharacter(toChange, relId, false);
    }

    /**
     * Holder class for Round2
     */
    private class Round2Holder {
        public Button showeventbutton;
        public Button showgamelogbutton;
        TextView characternametextview;
        TextView descriptiontextview;
        TextView gamemastertextview;
        ListView listView;
        TextView playertextview;
        View view;
    }
}
