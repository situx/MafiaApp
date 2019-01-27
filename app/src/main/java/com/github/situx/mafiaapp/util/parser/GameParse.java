package com.github.situx.mafiaapp.util.parser;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.github.situx.mafiaapp.cards.Karte;
import com.github.situx.mafiaapp.cards.GameSet;
import com.github.situx.mafiaapp.util.Triple;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Parses a game from a savegame file.
 */
public class GameParse extends DefaultHandler {
    /**
     * Cardlist containing a list of cards with duplicates for the Round2 screen layouts.
     */
    private final List<Karte> cardlist;
    /**
     * Cardlist containing a list of cards without duplicates but with correct amount annotations.
     */
    private final List<Karte> cardlist2;
    /**
     * A list of players_config playing in this game.
     */
    private final List<String> players;
    /**
     * The list of extracards contained in this game.
     */
    private final List<Karte> extracards;
    /**
     * The array of extra card names as String.
     */
    private final List<String> extracardarray;
    /**
     * Maps the following information to represent card overlays:
     * RelativeLayoutID - Sourcecard ID - SourceCard Ability ID - Remaining duration of this ability
     */
    private Map<Integer, Map<Integer, Triple<String, Integer,Integer>>> cardTocardOverlays;
    /**
     * Card to relative layout map.
     */
    private final Map<Karte, Set<Integer>> cardToRel;
    /**
     * Relative Layout to cardid map.
     */
    private final Map<Integer, Integer> relToCard;
    private boolean gamefield = false;
    private String gamesetname;
    private GameSet gameset;
    /**
     * List of cardnames as StringList.
     */
    private final List<String> cardnames;
    /**
     * List of cardids as Integer.
     */
    private final List<Integer> cardids;
    /**
     * List of dead characters.
     */
    private final List<Boolean> deadlist;
    private final Context context;
    private boolean parseset;
    /**
     * Map from RelativeLayouts to ImageViews for Round2.
     */
    private final Map<Integer, Map<Integer, List<ImageView>>> relToImgView;
    /**
     * Map from cardnames to cardids.
     */
    private final Map<String, Integer> cardUUIDTocardId;
    /**
     * Map from playernames to playerids.
     */
    private final Map<String, Integer> playernameToPlayerId;
    private List<Integer> colors;
    private boolean extra = false;
    private boolean notassigned = false;
    private boolean overlay = false;
    private Integer rounds, counter;
    private boolean card;

    /**
     * Costructor for this class.
     *
     * @param context Context for accessing file paths.
     */
    public GameParse(Context context) {
        this.context = context;
        this.cardnames = new LinkedList<>();
        this.cardids = new LinkedList<>();
        this.cardlist = new LinkedList<>();
        this.cardlist2 = new LinkedList<>();
        this.players = new LinkedList<>();
        this.extracards = new LinkedList<>();
        this.extracardarray = new LinkedList<>();
        this.deadlist = new LinkedList<>();
        this.cardUUIDTocardId = new TreeMap<>();
        this.playernameToPlayerId = new TreeMap<>();
        this.cardTocardOverlays = new TreeMap<>();
        this.cardToRel = new TreeMap<>();
        this.relToCard = new TreeMap<>();
        this.relToImgView = new TreeMap<>();
        this.colors=new LinkedList<>();

    }

    @Override
    public void characters(final char[] ch, final int start, final int length) throws SAXException {
        super.characters(ch, start, length);
        if (card && extra) {
            this.extracardarray.add(new String(ch, start, length));
        } else if (card && notassigned) {
            this.cardnames.add(new String(ch, start, length));
        }/*else if(card && !extra){
            this.cardids.add(Integer.valueOf(new String(ch, start, length)));
        }*/

    }

    public GameSet getGameset() {
        return gameset;
    }

    public void setGameset(final GameSet gameset) {
        this.gameset = gameset;
    }

    @Override
    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        switch (qName) {
            case "gamefield":
                List<Karte> roundcardlist = new LinkedList<>();
                int i = 0;
                boolean found = false;
                Log.e("GameParse CardNameList", this.cardnames.toString());
                for (String cardname : this.cardnames) {
                    found = false;
                    for (Karte card : gameset.getCards()) {
                        if (card.getCardid().equals(cardname)) {
                            found = true;
                            Karte cardd = card;
                            cardd.setCurrentamount(1);
                            if (deadlist.get(i++)) {
                                cardd.dead();
                            }
                            this.cardlist.add(cardd);
                            if (!cardlist2.contains(cardd)) {
                                this.cardlist2.add(new Karte(cardd));
                            }else{
                                this.cardlist2.get(this.cardlist2.indexOf(cardd)).setCurrentamount(this.cardlist2.get(this.cardlist2.indexOf(cardd)).getCurrentamount()+1);
                            }

                        }
                    }
                }
                Collections.sort(this.cardlist2, new Comparator<Karte>() {
                    public int compare(Karte s1, Karte s2) {
                        return s1.getPosition().compareTo(s2.getPosition());
                    }
                });
                Log.e("Card2list",this.cardlist2.toString());
                break;

            case "notassigned":
                this.notassigned = false;
                break;
            case "extra":for(Karte card:this.gameset.getCards()){
                if(this.extracardarray.contains(card.getName())){
                    this.extracards.add(card);
                }
            }

            default:
        }
    }

    public Map<Integer, Map<Integer, Triple<String, Integer,Integer>>> getCardTocardOverlays() {
        return cardTocardOverlays;
    }

    public List<Boolean> getDeadlist() {
        return deadlist;
    }

    public void setCardTocardOverlays(final Map<Integer, Map<Integer, Triple<String, Integer,Integer>>> cardTocardOverlays) {
        this.cardTocardOverlays = cardTocardOverlays;
    }

    public List<Integer> getCardids() {
        return cardids;
    }

    public List<Karte> getCardlist() {
        return cardlist;
    }

    public List<Karte> getCardlist2() {
        return cardlist2;
    }

    public Map<String, Integer> getCardUUIDTocardId() {
        return cardUUIDTocardId;
    }

    /**
     * Returns the card names found in this savegame.
     *
     * @return the card names as list of String
     */
    public List<String> getCardnames() {
        return cardnames;
    }

    /**
     * Gets the counter of this savegame.
     *
     * @return the counter as Integer
     */
    public Integer getCounter() {
        return counter;
    }

    public List<String> getExtracardarray() {
        return extracardarray;
    }

    public List<Karte> getExtracards() {
        return extracards;
    }

    public String getGamesetname() {
        return gamesetname;
    }

    /**
     * Gets the players_config of this savegame.
     *
     * @return the list of players_config as String
     */
    public List<String> getPlayers() {
        return players;
    }

    public Map<Integer, Integer> getRelToCard() {
        return relToCard;
    }

    /**
     * Gets the rounds of this savegame.
     *
     * @return the rounds as Integer
     */
    public Integer getRounds() {
        return rounds;
    }

    /**
     * Parses a game file and fills the corresponding fields in this class.
     *
     * @param file the file to parse
     */
    public void parseGame(final File file) {
        Log.e("Start parsing Game ba","ya");
        try {
            SAXParserFactory.newInstance().newSAXParser().parse(file, this);
        } catch (SAXException e) {
            e.toString();
        } catch (IOException e) {
            e.toString();
        } catch (ParserConfigurationException e) {
            e.toString();
        }
    }

    /**
     * Parses a game file and fills the corresponding fields in this class.
     *
     * @param file the file to parse
     */
    public void parseGame(final InputSource file) {
        this.parseset=false;
        try {
            SAXParserFactory.newInstance().newSAXParser().parse(file, this);
        } catch (SAXException e) {
            e.toString();
        } catch (IOException e) {
            e.toString();
        } catch (ParserConfigurationException e) {
            e.toString();
        }
    }

    public List<Integer> getColors() {
        return colors;
    }

    public void setColors(final List<Integer> colors) {
        this.colors = colors;
    }

    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        switch (qName) {
            case "SaveGame":
                    this.gamesetname = attributes.getValue("gameset");
                    Log.e("GameSet", this.context.getFilesDir() + "/chars/" + gamesetname + ".xml");
                    try {
                        this.gameset = new GameSet(new File(this.context.getFilesDir() + "/chars/" + gamesetname + ".xml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    }
                this.rounds = Integer.valueOf(attributes.getValue("round"));
                this.counter = Integer.valueOf(attributes.getValue("counter"));
                break;
            case "gamefield":
                gamefield = true;
                break;
            case "overlay":
                overlay = true;
                Map<Integer, Triple<String, Integer,Integer>> abilitymap = new TreeMap<>();
                abilitymap.put(Integer.valueOf(attributes.getValue("from")), new Triple<>(attributes.getValue("abb"), Integer.valueOf(attributes.getValue("duration")),0));
                this.cardTocardOverlays.put(this.relToCard.keySet().size()-1, abilitymap);
                break;
            case "field":
                this.colors.add(Integer.valueOf(attributes.getValue("color").substring(2),16));
                this.cardids.add(Integer.valueOf(attributes.getValue("charid")));
                this.cardnames.add(attributes.getValue("cardname"));
                //this.cardUUIDTocardId.put(attributes.getValue("cardname"), Integer.valueOf(attributes.getValue("charid")));
                this.relToCard.put(Integer.valueOf(attributes.getValue("relId")), Integer.valueOf(attributes.getValue("charid")));
                this.players.add(attributes.getValue("player"));
                //this.playernameToPlayerId.put(attributes.getValue("player"),)
                this.deadlist.add(Boolean.valueOf(attributes.getValue("dead")));
                break;
            case "extra":
                this.extra = true;
                Log.e("Extra", "Active");
                break;
            case "notassigned":
                if (gamefield) this.notassigned = true;
                break;
            case "card":
                this.card = true;
                Log.e("Card", "Active");
                break;
            default:
        }
    }


}
