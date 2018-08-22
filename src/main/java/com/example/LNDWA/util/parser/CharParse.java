package com.example.LNDWA.util.parser;

import android.text.Html;
import android.util.Log;
import com.example.LNDWA.cards.*;
import com.example.LNDWA.util.Tuple;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.*;


/**
 * A class for parsing and saving the XML character data.*
 */
public class CharParse extends DefaultHandler {
    private static final String ABILITY = "abb";
    private static final String ACTIONS = "actions";
    private static final String ACTION = "action";
    private static final String ACTIVE = "active";
    private static final String ALWAYSCHOOSEOTHER = "alwayschooseother";
    private static final String AMOUNT = "amount";
    private static final String AVAILABLEFROM = "availablefrom";
    private static final String AVAILABLEUNTIL = "availableuntil";
    private static final String CALLEVERYONE = "calleveryone";
    private static final String CARD = "Karte";
    private static final String CARDID = "cardid";
    private static final String CHECK = "check";
    private static final String CONCERNS = "concerns";
    private static final String COUNTERKILLING = "counterKilling";
    private static final String DEADCHARS = "deadchars";
    private static final String DELAY = "deathdelay";
    private static final String DEATHDELAY = "deathdelay";
    private static final String DEPENDS = "depends";
    private static final String DESCRIPTION = "description";
    private static final String DURATION = "duration";
    private static final String EVENT="event";
    private static final String EVENTID="id";
    private static final String EXTRA = "extra";
    private static final String FIXEDDEATH = "fixeddeath";
    private static final String FROMPLAYERS="fromPlayers";
    private static final String GAMESET = "gameset";
    private static final String GAMEMASTER = "gamemaster";
    private static final String GROUP = "group";
    private static final String IMG = "img";
    private static final String INTROTEXT="introtext";
    private static final String INTROTITLE="introtitle";
    private static final String WW="ww";
    private static final String KILLING = "killing";
    private static final String MAXAMOUNT = "maxamount";
    private static final String MINAMOUNT = "minamount";
    private static final String MUSTUSE = "mustuse";
    private static final String NAME = "name";
    private static final String NOPOINTS = "nopoints";
    private static final String OUTROTEXT="outrotext";
    private static final String OUTROTITLE="outrotitle";
    private static final String PLAYER = "player";
    private static final String POSITION = "position";
    private static final String POSITION2 = "position2";
    private static final String PROBABILITY = "probablity";
    private static final String ROUND = "round";
    private static final String TOPLAYERS="toPlayers";
    private static final String UNBOUNDED = "unbounded";
    private static final String BALANCE = "balance";
    private static final String CHANGEGROUP = "changegroup";
    private static final String EVERYROUND = "everyround";
    private static final String ONDEAD = "ondead";
    private static final String SELF = "self";
    private static final String SWITCHCHAR = "switchchar";
    private static final String SWITCHNEWCHAR = "switchnewchar";
    private static final String TITLE = "title";
    private static final String WAKESUPWITH="wakesupwith";
    private static final String WINNINGALIVE = "winningalive";
    private static final String WINNINGDEAD = "winningdead";
    private static final String WINSALONE = "winsalone";
    private String abbName;
    /**
     * The list of abilities.*
     */
    private transient Set<Ability> abilities;
    private String abilityid;
    private transient Map<String,Action> actionlist;
    private String backimg,curlang;
    /**
     * The list of cards to be returned.*
     */
    private transient List<Karte> cardlist;
    private String language;
    private Boolean wakesupw=false;
    private Set<String> wakesupwith;
    private List<Group> changeGroup;
    private Integer concerns;
    private Boolean counterKilling;
    private Boolean deadchars = false;
    private Integer deathdelay=0;
    private Integer delay;
    private Operator check;
    /**
     * The list of the dependencies.*
     */
    private transient List<String> depends;
    private Integer duration = 1, winningalive = 2, winningdead = 1, balance = 0;
    private Boolean everyround = false;
    private Integer fromPlayers=0;
    private String gamesetid;
    private String gamesetimg;
    private String gamesettitle;
    /**
     * The list of groups of the corresponding characters.
     */
    private transient Set<Group> groups;
    private transient Set<Event> events;
    private String introtext="",introtitle="",outrotitle="",outrotext="";
    private boolean introtextb,outrotextb;
    private Map<String,Group> uuidToGroup;
    private Event event;
    private Boolean hasBalanceValue;
    /**
     * .*
     */
    private transient Boolean karte=false, abb, dep, active, description=false, nopoints, calleveryone,alwaychooseother;
    private Boolean killing,play=false;
    private Boolean mustuseability = false;
    /**
     * Name, group, description and image path of the character.*
     */
    private transient String name, cardid, descriptiontext="", image, abbImage;
    private transient Map<String,String> langToDescription=new TreeMap<String,String>();
    private transient Group group;
    private Boolean ondead;
    /**
     * .*
     */
    private transient Integer round, amount, position, position2, minamount, maxamount,
            extra, fixeddeath, availableFrom, availableUntil, abilityProb, genProb;
    private Boolean self = false;
    private Boolean switchchar = false,player=false,gamemaster=false,action=false;
    private String switchnewchar = "-1";
    private Integer toPlayers=0;
    private Boolean winsalone;

    public static String getAbility() {
        return ABILITY;
    }

    public String getLanguage() {
        return language;
    }

    public String getOutrotext() {
        return outrotext;
    }

    public String getOutrotitle() {
        return outrotitle;
    }

    public String getIntrotitle() {
        return introtitle;
    }

    public String getIntrotext() {
        return introtext;
    }

    /**
     * Constructor for CharParse.
     */
    public CharParse() {
        super();
        this.cardid = "";
        this.amount = 0;
        this.karte = false;
        this.abb = false;
        this.dep = false;
        this.cardlist = new LinkedList<>();
        this.depends = new LinkedList<>();
        this.groups = new TreeSet<>();
        this.events=new TreeSet<>();
        this.uuidToGroup=new TreeMap<>();
        this.abilities = new TreeSet<>();
        this.actionlist=new TreeMap<>();
        this.fixeddeath = 0;
        this.nopoints = false;

        this.calleveryone = false;
        this.deadchars = false;
    }

    @Override
    public void characters(final char[] chars, final int start, final int length) throws SAXException {
        if (this.abb) {
            this.abbName += new String(chars, start, length);
        }
        if (this.description) {
            this.descriptiontext = this.descriptiontext.concat(new String(chars).substring(start, start + length));
        }
        if(action && gamemaster){
            this.actionlist.get(((Integer)(actionlist.size())).toString()).setGamemaster(this.actionlist.get(((Integer) (actionlist.size())).toString()).getGamemaster() + new String(chars, start, length));
        }
        if(action && player){
            this.actionlist.get(((Integer)(actionlist.size())).toString()).setPlayer(this.actionlist.get(((Integer) (actionlist.size())).toString()).getPlayer() + new String(chars, start, length));
        }
        if(introtextb){
            this.introtext+=new String(chars,start,length);
        }
        if(outrotextb){
            this.outrotext+=new String(chars,start,length);
        }
        if(this.wakesupw){
            this.wakesupwith.add(new String(chars,start,length));
        }
    }

    @Override
    public void endElement(final String uri, final String localName,
                           final String qName) throws SAXException {
        if (this.karte && !this.abb && !this.dep) {
            this.cardlist.add(this.neueKarte());
            this.karte = false;
        }
        if (this.abb) {
            this.abilities.add(new Ability(this.abbName, this.active, this.amount,
                    this.availableFrom, this.availableUntil, this.abilityProb, this.abbImage,
                    this.concerns, this.mustuseability, this.duration, this.abilityid,
                    this.killing, this.counterKilling, this.self, this.everyround, this.changeGroup,
                    this.switchchar, this.switchnewchar,this.delay,this.alwaychooseother,this.ondead,this.check) {
                @Override
                public void action(final Karte card) {
                    if (--currentamount == 0)
                        this.setActive(false);
                }
            });
            this.abb = false;
        }
        if (this.dep)
            this.dep = false;
        if (this.description)
            this.description = false;
        switch(qName){
            case DESCRIPTION: if(play){
                this.event.setDescription(Html.fromHtml(this.descriptiontext).toString());
                this.description=false;}break;
            case GROUP: this.groups.add(this.group);this.uuidToGroup.put(this.group.getGroupIdentifier(),this.group);break;
            case EVENT: this.events.add(this.event);this.play=false;break;
            case ACTION: action=false;break;
            case GAMEMASTER:gamemaster=false;break;
            case PLAYER:player=false;break;
            case INTROTEXT:this.introtextb=false;break;
            case OUTROTEXT:this.outrotextb=false;break;
            default:
        }
    }

    @Override
    public void fatalError(final SAXParseException exp) throws SAXException {
        //System.out.println("In line "+exp.getLineNumber()+":\n"+exp.getMessage());
    }

    public String getBackimg() {
        return backimg;
    }

    public void setBackimg(final String backimg) {
        this.backimg = backimg;
    }

    public List<Karte> getCardList() {
        return this.cardlist;
    }

    public Integer getFromPlayers() {
        return fromPlayers;
    }

    public void setFromPlayers(final Integer fromPlayers) {
        this.fromPlayers = fromPlayers;
    }

    public String getGamesetid() {
        return gamesetid;
    }

    public void setGamesetid(final String gamesetid) {
        this.gamesetid = gamesetid;
    }

    public String getGamesetimg() {
        return gamesetimg;
    }

    public String getGamesettitle() {
        return gamesettitle;
    }

    public Set<Group> getGroups() {
        return this.groups;
    }

    public Boolean getHasBalanceValue() {
        return hasBalanceValue;
    }

    public Integer getToPlayers() {
        return toPlayers;
    }

    public void setToPlayers(final Integer toPlayers) {
        this.toPlayers = toPlayers;
    }

    /**
     * reads the input File for the werewolf characters and build the requested classes.
     *
     * @return the new character
     * @throws IOException
     */
    Karte neueKarte() {
        return new Karte(this.name, this.cardid, this.round, this.group, this.descriptiontext, this.abilities,
                this.depends, this.image, this.position, this.minamount, this.maxamount, this.position2, this.extra, this.fixeddeath,
                this.nopoints, this.calleveryone, this.deadchars, this.winsalone, this.winningalive, this.winningdead,
                this.balance,this.deathdelay,this.actionlist,this.wakesupwith);
    }

    /**
     * Parses chars from a given file.
     *
     * @param file the file to parse
     * @return the parsed cardlist
     */
    public List<Karte> parseChars(final File file) {
        try {
            SAXParserFactory.newInstance().newSAXParser().parse(file, this);
        } catch (SAXException | ParserConfigurationException | IOException e) {
            e.toString();
        }
        return this.cardlist;
    }

    /**
     * The parsing function for the characters.
     *
     * @param fileName the name of the file to be parsed.
     * @return the list of characters.*
     */
    public List<Karte> parseChars(final InputSource fileName) {
        try {
            SAXParserFactory.newInstance().newSAXParser().parse(fileName, this);
        } catch (SAXException | ParserConfigurationException | IOException e) {
            e.toString();
        }
        return this.cardlist;
    }

    @Override
    public void startElement(final String uri, final String localName,
                             final String nameOf, final Attributes attributes) throws SAXException {
        switch (nameOf) {
            case GAMESET:
                this.gamesettitle=attributes.getValue(TITLE);
                if (attributes.getValue(BALANCE) != null)
                    this.hasBalanceValue = Boolean.valueOf(attributes.getValue(BALANCE));
                else
                    this.hasBalanceValue = false;
                this.fromPlayers = Integer.valueOf(attributes.getValue(FROMPLAYERS));
                if (attributes.getValue("gamesetid") == null) {
                    this.gamesetid = UUID.randomUUID().toString();
                } else {
                    this.gamesetid = attributes.getValue("gamesetid");
                }
                this.language=attributes.getValue("language");
                this.toPlayers = Integer.valueOf(attributes.getValue(TOPLAYERS));
                this.backimg = attributes.getValue("backimg");
                this.gamesetimg = attributes.getValue(IMG);
                break;
            case CARD:
                this.abilities = new TreeSet<>();
                this.depends = new LinkedList<>();
                this.wakesupwith=new TreeSet<>();
                this.actionlist=new TreeMap<>();
                this.descriptiontext = "";
                this.image = null;
                this.karte = true;
                if (attributes.getValue(IMG) != null) {
                    this.image = attributes.getValue(IMG);
                } else {
                    this.image = " ";
                }
                this.name = attributes.getValue(NAME);
                if (attributes.getValue(CARDID) != null) {
                    this.cardid = attributes.getValue(CARDID);
                } else {
                    this.cardid = UUID.randomUUID().toString();
                }
                if(this.uuidToGroup.containsKey(attributes.getValue(GROUP))){
                       this.group=this.uuidToGroup.get(attributes.getValue(GROUP));
                }else{
                    this.group=new Group(attributes.getValue(GROUP));
                }
                this.position = Integer.valueOf(attributes.getValue(POSITION));
                this.position2 = -1;
                if (attributes.getValue(POSITION2) != null) {
                    this.position2 = Integer.valueOf(attributes.getValue(POSITION2));
                }
                this.round = Integer.valueOf(attributes.getValue(ROUND));
                this.minamount = Integer.valueOf(attributes.getValue(MINAMOUNT));
                if (attributes.getValue(MAXAMOUNT).equals(UNBOUNDED)) {
                    this.maxamount = -1;
                } else {
                    this.maxamount = Integer.valueOf(attributes.getValue(MAXAMOUNT));
                }
                this.extra = Integer.valueOf(attributes.getValue(EXTRA));
                if (attributes.getValue(FIXEDDEATH) != null) {
                    this.fixeddeath = Integer.valueOf(attributes.getValue(FIXEDDEATH));
                } else {
                    this.fixeddeath = 0;
                }
                if (attributes.getValue(NOPOINTS) != null) {
                    this.nopoints = Boolean.valueOf(attributes.getValue(NOPOINTS));
                } else {
                    this.nopoints = false;
                }
                if (attributes.getValue(CALLEVERYONE) != null) {
                    this.calleveryone = Boolean.valueOf(attributes.getValue(CALLEVERYONE));
                } else {
                    this.calleveryone = false;
                }
                if (attributes.getValue(DEADCHARS) != null) {
                    this.deadchars = Boolean.valueOf(attributes.getValue(DEADCHARS));
                } else {
                    this.deadchars = false;
                }
                if (attributes.getValue(WINSALONE) != null) {
                    this.winsalone = Boolean.valueOf(attributes.getValue(WINSALONE));
                } else {
                    this.winsalone = false;
                }
                if (attributes.getValue(WINNINGALIVE) != null) {
                    this.winningalive = Integer.valueOf(attributes.getValue(WINNINGALIVE));
                } else {
                    this.winningalive = 2;
                }
                if (attributes.getValue(WINNINGDEAD) != null) {
                    this.winningdead = Integer.valueOf(attributes.getValue(WINNINGDEAD));
                } else {
                    this.winningdead = 1;
                }
                if (attributes.getValue(BALANCE) != null) {
                    this.balance = Integer.valueOf(attributes.getValue(BALANCE));
                } else {
                    this.balance = 0;
                }
                if (attributes.getValue(DEATHDELAY) != null) {
                    this.deathdelay = Integer.valueOf(attributes.getValue(DEATHDELAY));
                } else {
                    this.deathdelay= 0;
                }
                break;
            case ABILITY:
                this.abb = true;
                this.abbName = "";
                this.changeGroup=new LinkedList<>();
                this.active = Boolean.valueOf(attributes.getValue(ACTIVE));
                this.amount = Integer.valueOf(attributes.getValue(AMOUNT));
                if(attributes.getValue("abilityid")==null){
                    this.abilityid=UUID.randomUUID().toString();
                }else{
                    this.abilityid=attributes.getValue("abilityid");
                }
                if (attributes.getValue(AVAILABLEFROM) == null) {
                    this.availableFrom = 0;
                } else {
                    this.availableFrom = Integer.valueOf(attributes.getValue(AVAILABLEFROM));
                }
                if (attributes.getValue(AVAILABLEUNTIL) == null) {
                    this.availableUntil = -1;
                } else {
                    this.availableUntil = Integer.valueOf(attributes.getValue(AVAILABLEUNTIL));
                }
                if (attributes.getValue(PROBABILITY) == null) {
                    this.abilityProb = 100;
                } else {
                    this.abilityProb = Integer.valueOf(attributes.getValue(PROBABILITY));
                }
                if (attributes.getValue(IMG) == null) {
                    this.abbImage = "";
                } else {
                    this.abbImage = attributes.getValue(IMG);
                }
                if (attributes.getValue(CONCERNS) == null) {
                    this.concerns = 0;
                } else {
                    this.concerns = Integer.valueOf(attributes.getValue(CONCERNS));
                }
                if (attributes.getValue(MUSTUSE) == null) {
                    this.mustuseability = false;
                } else {
                    this.mustuseability = Boolean.valueOf(attributes.getValue(MUSTUSE));
                }
                if (attributes.getValue(DURATION) == null) {
                    this.duration = 1;
                } else {
                    this.duration = Integer.valueOf(attributes.getValue(DURATION));
                }
                if (attributes.getValue(KILLING) == null) {
                    this.killing = false;
                } else {
                    this.killing = Boolean.valueOf(attributes.getValue(KILLING));
                }
                if(attributes.getValue(CHECK)==null || "".equals(attributes.getValue(CHECK))){
                    this.check=new Operator();
                }else{
                    this.check=new Operator(attributes.getValue(CHECK));
                }
                if (attributes.getValue(COUNTERKILLING) == null) {
                    this.counterKilling = false;
                } else {
                    this.counterKilling = Boolean.valueOf(attributes.getValue(COUNTERKILLING));
                }
                if (attributes.getValue(SELF) == null) {
                    this.self = false;
                } else {
                    this.self = Boolean.valueOf(attributes.getValue(SELF));
                }
                if (attributes.getValue(EVERYROUND) == null) {
                    this.everyround = false;
                } else {
                    this.everyround = Boolean.valueOf(attributes.getValue(EVERYROUND));
                }
                if (attributes.getValue(CHANGEGROUP) == null) {
                    this.changeGroup = new LinkedList<>();
                } else {
                    if(attributes.getValue(CHANGEGROUP).contains(";")){
                        for(String group:attributes.getValue(CHANGEGROUP).split(";")){
                            this.changeGroup.add(this.uuidToGroup.get(group));
                        }
                    }else{
                        if(this.uuidToGroup.containsKey(attributes.getValue(CHANGEGROUP))){
                            this.changeGroup.add(this.uuidToGroup.get(attributes.getValue(CHANGEGROUP)));
                        }else{
                            this.changeGroup.add(new Group(attributes.getValue(CHANGEGROUP)));
                        }
                    }
                }
                if (attributes.getValue(SWITCHCHAR) == null) {
                    switchchar = false;
                } else {
                    this.switchchar = Boolean.valueOf(attributes.getValue(SWITCHCHAR));
                }
                if (attributes.getValue(SWITCHNEWCHAR) == null) {
                    this.switchnewchar = "-1";
                } else {
                    this.switchnewchar = attributes.getValue(SWITCHNEWCHAR);
                }
                if (attributes.getValue(DELAY) == null) {
                    this.delay = 0;
                } else {
                    this.delay = Integer.valueOf(attributes.getValue(DELAY));
                }
                if (attributes.getValue(ALWAYSCHOOSEOTHER) == null) {
                    this.alwaychooseother = false;
                } else {
                    this.alwaychooseother = Boolean.valueOf(attributes.getValue(ALWAYSCHOOSEOTHER));
                }
                if (attributes.getValue(ONDEAD) == null) {
                    this.ondead = false;
                } else {
                    this.ondead = Boolean.valueOf(attributes.getValue(ONDEAD));
                }
                break;
            case DEPENDS:
                this.dep = true;
                for (int j = 0; j < attributes.getLength(); j++)
                    this.depends.add(attributes.getValue(j));
                break;
            case GROUP:this.group=new Group();
                group.setGroupIdentifier(attributes.getValue("gid"));
                if(attributes.getValue("id")!=null){
                    group.setGroupId(attributes.getValue("id"));
                }
                if(attributes.getValue("icon")!=null){
                    group.setGroupIcon(attributes.getValue("icon"));
                }
                group.setGroupname(attributes.getValue("name"));
                group.setWinsgame(Boolean.valueOf(attributes.getValue("canwin")));
                break;
            case EVENT: this.event=new Event();
                this.event.setId(Integer.valueOf(attributes.getValue(EVENTID)));
                this.event.setTitle(attributes.getValue(TITLE));
                if(attributes.getValue(PROBABILITY)==null){
                    this.event.setProbability(100);
                }else{
                    this.event.setProbability(Integer.valueOf(attributes.getValue(PROBABILITY)));
                }
                if(attributes.getValue(ACTIVE)==null){
                    this.event.setActive(false);
                }else{
                    this.event.setActive(Boolean.valueOf(attributes.getValue(ACTIVE)));
                }
                this.play=true;break;
            case DESCRIPTION:
                this.description = true;
                if(attributes.getValue("lang")!=null){
                    this.curlang=attributes.getValue("lang");
                }
                if(play) {
                    this.descriptiontext = "";
                }
            break;
            case ACTIONS:break;
            case ACTION: action=true;
                Action action=new Action();
                action.setActionid(attributes.getValue("id"));
                if(attributes.getValue(POSITION)!=null)
                    action.setPosition(Integer.valueOf(attributes.getValue(POSITION)));
                if(attributes.getValue(ROUND)!=null)
                    action.setRound(Integer.valueOf(attributes.getValue(ROUND)));
                action.setTitle(attributes.getValue("title"));
                if(attributes.getValue("ondead")==null){
                    action.setOndead(false);
                }else{
                    action.setOndead(Boolean.valueOf(attributes.getValue("ondead")));
                }
                this.actionlist.put(attributes.getValue("id"),action);
            break;
            case GAMEMASTER:gamemaster=true;break;
            case PLAYER:player=true;break;
            case INTROTEXT:this.introtitle=attributes.getValue(INTROTITLE); this.introtextb=true;break;
            case OUTROTEXT:this.outrotitle=attributes.getValue(OUTROTITLE);this.outrotextb=true;break;
            case WAKESUPWITH: this.wakesupw=true;break;
            default:
    }}

    public Set<Event> getEvents(){
        return this.events;
    }

}
