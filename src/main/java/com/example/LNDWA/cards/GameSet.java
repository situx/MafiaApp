package com.example.LNDWA.cards;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.util.Xml;
import com.example.LNDWA.util.parser.CharParse;
import com.example.LNDWA.util.parser.EventParse;
import com.example.LNDWA.util.parser.PresetParse;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

/**
 * Created by timo on 07.02.14.
 */
public class GameSet implements Parcelable, GameElements,Comparable<GameSet> {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public GameSet createFromParcel(Parcel in) {
            return new GameSet(in);
        }

        public GameSet[] newArray(int size) {
            return new GameSet[size];
        }
    };
    /**
     * The list of cards used by this GameSet.
     */
    private List<Karte> cards;


    private Map<String,Karte> uuidToCard;

    private Map<String,Event> uuidToEvent;

    public Map<String, Karte> getUuidToCard() {
        return uuidToCard;
    }

    public void setIntroText(final String introText) {
        this.introtext = introText;
    }

    public void setIntroTitle(final String introTitle) {
        this.introtitle = introTitle;
    }

    public void setOutroText(final String outroText) {
        this.outrotext = outroText;
    }

    public void setOutroTitle(final String outroTitle) {
        this.outrotitle = outroTitle;
    }

    public void setUuidToCard(final Map<String, Karte> uuidToCard) {
        this.uuidToCard = uuidToCard;
    }

    public Map<String, Event> getUuidToEvent() {
        return uuidToEvent;
    }

    public void setUuidToEvent(final Map<String, Event> uuidToEvent) {
        this.uuidToEvent = uuidToEvent;
    }

    /**
     * The list of events used by this GameSet.
     */
    private List<Event> events;
    /**
     * The list of items used by this GameSet.
     */
    private List<Item> items;
    /**
     * The list of presets used by this GameSet.
     */
    private List<Preset> presets;
    /**
     * The set of groups included in this GameSet.
     */
    private Set<Group> groups;
    /**
     * The title of this GameSet.
     */
    private String title;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(final String language) {
        this.language = language;
    }

    /**
     * The front cover of this GameSet.
     */
    private String gamesetImg;
    /**
     * The card back image of all the cards in this GameSet.
     */
    private String backImg;
    /**
     * The xml sourcefile path to this GameSet.
     */
    private String sourcefile;
    /**
     * The minimum amount of players_config to play using this GameSet.
     */
    private Integer fromPlayers;
    /**
     * The maximum amount of players_config to play using this GameSet.
     */
    private Integer toPlayers;
    /**
     * Indicates if this GameSet uses a balance valuese to indicates the equalness of certain parties.
     */
    private Boolean hasBalance = true;

    private String gamesetid;

    private String language;

    private String introtext,introtitle,outrotext,outrotitle;

    /**
     * Parcel constructor for GameSet.
     *
     * @param in the parcel to use
     */
    public GameSet(final Parcel in) {
        this.title = in.readString();
        this.sourcefile = in.readString();
        this.gamesetImg = in.readString();
        this.backImg = in.readString();
        this.fromPlayers = in.readInt();
        this.toPlayers = in.readInt();
        this.hasBalance = in.readByte() != 0;
        this.gamesetid=in.readString();
        this.introtext=in.readString();
        this.introtitle=in.readString();
        this.outrotext=in.readString();
        this.outrotitle=in.readString();
        this.language=in.readString();
        this.cards = new LinkedList<>();
        in.readTypedList(cards, Karte.CREATOR);
        this.events = new LinkedList<>();
        in.readTypedList(events, Event.CREATOR);
        this.items = new LinkedList<>();
        LinkedList<Group> grouplist = new LinkedList<>();
        in.readTypedList(grouplist, Group.CREATOR);
        this.groups = new TreeSet<>(grouplist);
        this.presets = new LinkedList<>();
        in.readTypedList(this.presets, Preset.CREATOR);
    }

    public String getGamesetid() {
        return gamesetid;
    }

    /**
     * Empty constructor for GameSet.
     */
    public GameSet() {
        this.cards = new LinkedList<>();
        this.events = new LinkedList<>();
        this.gamesetImg = "";
        this.title = "";
        this.items = new LinkedList<>();
        this.presets = new LinkedList<>();
        this.sourcefile = "";
        this.fromPlayers = 0;
        this.toPlayers = 0;
        this.groups = new TreeSet<>();
        this.backImg = "";
        this.gamesetid=UUID.randomUUID().toString();
        this.introtext="";
        this.introtitle="";
        this.outrotext="";
        this.outrotitle="";
        this.language="";
        this.hasBalance=false;

    }

    public String getIntrotext() {
        return introtext;
    }

    public String getIntrotitle() {
        return introtitle;
    }

    public String getOutrotext() {
        return outrotext;
    }

    public String getOutrotitle() {
        return outrotitle;
    }

    /**
     * Constructor for gameset.
     *
     * @param cards
     * @param events
     * @param items
     * @param title
     * @param gamesetImg
     * @param sourcefile
     */
    public GameSet(List<Karte> cards, List<Event> events, List<Item> items, String title, String gamesetImg, String sourcefile,String gamesetid,

    String introtext,String outrotext,String introtitle,String outrotitle,String language) {
        this.cards = cards;
        this.events = events;
        this.gamesetImg = gamesetImg;
        this.title = title;
        this.items = items;
        this.sourcefile = sourcefile;
        this.fromPlayers = 0;
        this.toPlayers = 0;
        this.groups = new TreeSet<>();
        this.gamesetid=gamesetid;
        this.introtext=introtext;
        this.introtitle=introtitle;
        this.outrotext=outrotext;
        this.outrotitle=outrotitle;
        this.language=language;
        this.hasBalance=false;
        this.fromPlayers=0;
        this.toPlayers=0;
    }

    /**
     * File constructor for GameSet.
     *
     * @param file the gameset file
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public GameSet(final File file) throws IOException, SAXException, ParserConfigurationException {
        CharParse charparse = new CharParse();
        Log.e("Creating a new GameSet","NOW");

        Log.e("Parsing Chars","NOW");
        this.cards=new LinkedList<>();
        this.cards = charparse.parseChars(file);
        this.cards=charparse.getCardList();
        Collections.sort(this.cards, new Comparator<Karte>() {
            public int compare(Karte s1, Karte s2) {
                return s1.getName().compareTo(s2.getName());
            }
        });
        this.items = new LinkedList<>();
        this.title = charparse.getGamesettitle();
        this.gamesetImg = charparse.getGamesetimg();
        this.gamesetid=charparse.getGamesetid();
        this.hasBalance=charparse.getHasBalanceValue();
        this.sourcefile = file.getName();
        this.backImg = charparse.getBackimg();
        this.fromPlayers = charparse.getFromPlayers();
        if(fromPlayers==null)
            this.fromPlayers=0;
        this.toPlayers = charparse.getToPlayers();
        if(toPlayers==null)
            this.toPlayers=0;
        this.groups = charparse.getGroups();
        this.outrotext=charparse.getOutrotext();
        this.outrotitle=charparse.getOutrotitle();
        this.introtitle=charparse.getIntrotitle();
        this.introtext=charparse.getIntrotext();
        this.language=charparse.getLanguage();
        this.uuidToCard=new TreeMap<String, Karte>();
        for(Karte card:this.cards){
            this.uuidToCard.put(card.getCardid(),card);
        }
        this.events = new LinkedList<>();
        this.events = new LinkedList<>(charparse.getEvents());
        Collections.sort(this.events, new Comparator<Event>() {
            public int compare(Event s1, Event s2) {
                return s1.getTitle().compareTo(s2.getTitle());
            }
        });
        Log.e("Parsing Chars","Finished");

        Log.e("Cards:",this.cards.toString());
        this.presets=new LinkedList<>();
        String presetfile=file.getAbsolutePath().substring(0,file.getAbsolutePath().lastIndexOf("/"));
        presetfile=presetfile.substring(0,presetfile.lastIndexOf("/"));
        presetfile+="/presets/";
        presetfile+=this.gamesetid+"_presets.xml";
        Log.e("Presetfile?",presetfile);
        if(new File(presetfile).exists()){
            this.presets = new PresetParse().parsePreset(new File(presetfile));
            Log.e("Presets?", "" + this.presets.size());
        }

    }

    /**
     * File constructor for GameSet.
     *
     * @param file the gameset file
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public GameSet(final String file) throws IOException, SAXException, ParserConfigurationException {
        CharParse charparse = new CharParse();
        Log.e("Creating a new GameSet","NOW");

        Log.e("Parsing Chars","NOW");
        this.cards=new LinkedList<>();
        this.cards = charparse.parseChars(new InputSource(file));
        this.cards=charparse.getCardList();
        this.title = charparse.getGamesettitle();
        this.gamesetImg = charparse.getGamesetimg();
        this.hasBalance=charparse.getHasBalanceValue();
        this.gamesetid=charparse.getGamesetid();
        this.sourcefile = file;
        this.backImg = charparse.getBackimg();
        this.fromPlayers = charparse.getFromPlayers();
        this.toPlayers = charparse.getToPlayers();
        this.groups = new TreeSet<>(charparse.getGroups());
        this.outrotext=charparse.getOutrotext();
        this.outrotitle=charparse.getOutrotitle();
        this.introtitle=charparse.getIntrotitle();
        this.introtext=charparse.getIntrotext();
        this.language=charparse.getLanguage();
        Log.e("Parsing Chars","Finished");
        Log.e("Cards:",this.cards.toString());
        this.events = new LinkedList<>();
        this.events = new EventParse().parseEvents(new InputSource(file));
        this.presets=new LinkedList<>();
        this.items = new LinkedList<>();
        this.uuidToCard=new TreeMap<String, Karte>();
        for(Karte card:this.cards){
            this.uuidToCard.put(card.getCardid(),card);
        }
        File filee=new File(file);
        String presetfile=filee.getAbsolutePath().substring(0,filee.getAbsolutePath().lastIndexOf("/"));
        presetfile=presetfile.substring(0,presetfile.lastIndexOf("/"));
        presetfile+="presets/";
        presetfile+=this.gamesetid+"_presets.xml";
        if(new File(presetfile).exists()){
            this.presets = new PresetParse().parsePreset(new File(presetfile));
            Log.e("Presets?", "" + this.presets.size());
        }
        Log.e("Presets?", "" + this.presets.size());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Gets the card back image of all the cards in this GameSet.
     *
     * @return the image path as String.
     */
    public String getBackImg() {
        return backImg;
    }

    /**
     * Sets the card back image path of all the cards in this GameSet.
     *
     * @param backImg the image path as String
     */
    public void setBackImg(final String backImg) {
        this.backImg = backImg;
    }

    public List<Karte> getCards() {
        return cards;
    }

    public void setCards(final List<Karte> cards) {
        this.cards = cards;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(final List<Event> events) {
        this.events = events;
    }

    public Integer getFromPlayers() {
        return fromPlayers;
    }

    public void setFromPlayers(final Integer fromPlayers) {
        this.fromPlayers = fromPlayers;
    }

    public String getGamesetImg() {
        return gamesetImg;
    }

    public void setGamesetImg(final String gamesetImg) {
        this.gamesetImg = gamesetImg;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(final Set<Group> groups) {
        this.groups = groups;
    }

    public Boolean getHasBalance() {
        return hasBalance;
    }

    public void setHasBalance(final Boolean hasBalance) {
        this.hasBalance = hasBalance;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(final List<Item> items) {
        this.items = items;
    }

    public List<Preset> getPresets() {
        return presets;
    }

    public void setPresets(final List<Preset> presets) {
        this.presets = presets;
    }

    public String getSourcefile() {
        return sourcefile;
    }

    public void setSourcefile(final String sourcefile) {
        this.sourcefile = sourcefile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public Integer getToPlayers() {
        return toPlayers;
    }

    public void setToPlayers(final Integer toPlayers) {
        this.toPlayers = toPlayers;
    }

    @Override
    public boolean synchronize(final GameElements elem) {
        GameSet set=(GameSet)elem;
        this.backImg=set.backImg;
        this.cards=set.cards;
        this.events=set.events;
        this.fromPlayers=set.fromPlayers;
        this.toPlayers=set.toPlayers;
        this.groups=set.groups;
        this.introtext=set.introtext;
        this.introtitle=set.introtitle;
        this.outrotitle=set.outrotitle;
        this.outrotext=set.outrotext;
        return false;
    }

    /**
     * Creates an XML string from this gameset.
     *
     * @return the xml String
     */
    @Override
    public String toXML() {
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.setOutput(writer);
            serializer.startTag("", "gameset");
            serializer.attribute("", "gamesetid", this.gamesetid);
            serializer.attribute("", "title", this.title);
            serializer.attribute("","language",this.language);
            serializer.attribute("","balance",this.hasBalance.toString());
            serializer.attribute("", "fromPlayers", this.fromPlayers.toString());
            serializer.attribute("", "toPlayers", this.toPlayers.toString());
            serializer.attribute("", "img", this.gamesetImg);
            serializer.attribute("", "backimg", this.backImg);
            serializer.flush();
            serializer.startTag("", "introtext");
            serializer.attribute("", "introtitle", this.introtitle);
            serializer.text(this.introtext);
            serializer.endTag("", "introtext");
            serializer.startTag("","outrotext");
            serializer.attribute("","outrotitle",this.outrotitle);
            serializer.text(this.outrotext);
            serializer.endTag("","outrotext");
            for(Group group:this.groups){
                serializer.flush();
                writer.write(group.toXML());
            }
            for (Karte card : this.cards) {
                serializer.flush();
                writer.write(card.toXML());
            }
            for (Event event : this.events) {
                serializer.flush();
                writer.write(event.toXML());
            }
            serializer.endTag("", "gameset");
            serializer.flush();
            return writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void writeToParcel(final Parcel parcel, final int i) {
        parcel.writeString(title);
        parcel.writeString(sourcefile);
        parcel.writeString(gamesetImg);
        parcel.writeString(backImg);
        parcel.writeInt(fromPlayers);
        parcel.writeInt(toPlayers);
        if(this.hasBalance==null)
            this.hasBalance=false;
        parcel.writeByte((byte) (this.hasBalance ? 1 : 0));
        parcel.writeString(this.gamesetid);
        parcel.writeString(this.introtext);
        parcel.writeString(this.introtitle);
        parcel.writeString(this.outrotext);
        parcel.writeString(this.outrotitle);
        parcel.writeString(this.language);
        parcel.writeTypedList(cards);
        parcel.writeTypedList(events);
        parcel.writeTypedList(new ArrayList<Group>(this.groups));
        parcel.writeTypedList(presets);
    }

    @Override
    public String toString() {
        return this.title;
    }

    @Override
    public int compareTo(GameSet o) {
        return this.gamesetid.compareTo(o.gamesetid);
    }
}
