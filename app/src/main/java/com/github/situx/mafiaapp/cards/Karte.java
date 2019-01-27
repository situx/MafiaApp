package com.github.situx.mafiaapp.cards;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

/**
 * .*
 */
public class Karte implements Parcelable, Comparable<Karte>, GameElements {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Karte createFromParcel(Parcel in) {
            return new Karte(in);
        }

        public Karte[] newArray(int size) {
            return new Karte[size];
        }
    };
    /**
     * The list of abilities .*
     */
    private transient Set<Ability> abblist;

    private transient Set<String> wakesupwith;

    private transient Map<String,Action> actionlist;
    /**
     * The name description players_config name group and image path of the character .*
     */
    private String name, description, spieler,image,  cardid;

    private Group group,originalgroup;
    /**
     * The list of dependencies .*
     */
    private transient List<String> depends;

    /**
     * The list of dependencies .*
     */
    private transient List<Group> secondarygroups;
    private Map<String,Ability> uuidToAbility;
    /**
     * Indicators for the death, the love and the security of the character .*
     */
    private Boolean dead, verzaubert, isprotected, nopoints, calleveryone, deadchars, winsalone;
    /**
     * Indicators for the cardid, the love and the character's round .*
     */
    private Integer deathdelay;
    /**The round position.*/
    private Integer round;
    /**The position of this card.*/
    private Integer position;
    /**The position2 of this card if available.*/
    private Integer position2;
    /**Minimum amount of this card.*/
    private Integer minamount;
    /**Maximum amount of this card.*/
    private Integer maxamount;
    /**Current amount of this card.*/
    private Integer currentamount;
    /**Amount of extra cards needed to play with this character.*/
    private Integer extra;
    /**Karte will die in the corresponding round.*/
    private Integer fixeddeath;
    /**The winning alive points of this character.*/
    private Integer winningalive;
    /**The balance value of this character.*/
    private Integer balancevalue;
    /**The winning alive points of this character.*/
    private Integer winningdead;

    /**
     * Constructor for Karte.
     *
     * @param cardname the card's name*
     */
    public Karte(final String cardname) {
        super();
        this.name = cardname;
        this.depends = new LinkedList<>();
        this.abblist = new TreeSet<>();
    }

    /**
     * constructor for a character.
     *
     * @param name        its name
     * @param cardid      indicates in which order the characters are called
     * @param abblist     the amount of its abilities
     * @param round       in which round they are called
     * @param group       its group
     * @param description its description
     * @param img         the image path
     * @param depends     the dependency list
     */
    public Karte(final String name, final String cardid, final int round, final Group group,
                 final String description, final Set<Ability> abblist, final List<String> depends,
                 final String img, final int position, final int minamount, final int maxamount,
                 final int position2, final int extra, final int fixeddeath, final boolean nopoints,
                 final boolean calleveryone, final boolean deadchars, final boolean winsalone, final Integer winningalive,
                 final Integer winningdead, final Integer balancevalue, final Integer deathdelay, final Map<String,Action> actionlist
    , final Set<String> wakesupwith) {
        super();
        this.name = name;
        this.dead = false;
        this.abblist = abblist;
        this.uuidToAbility=new TreeMap<>();
        for(Ability abb:abblist){
            this.uuidToAbility.put(abb.getAbilityId(),abb);
        }
        this.actionlist=actionlist;
        if(!actionlist.isEmpty())
            Log.e("ActionList",this.actionlist.toString());
        this.cardid = cardid;
        this.description = description;
        this.round = round;
        this.group = group;
        this.originalgroup = group;
        this.verzaubert = false;
        this.deathdelay=deathdelay;
        this.depends = depends;
        this.image = img;
        this.position = position;
        this.position2 = position2;
        this.extra = extra;
        this.fixeddeath = fixeddeath;
        this.nopoints = nopoints;
        this.calleveryone = calleveryone;
        this.isprotected = false;
        this.minamount = minamount;
        this.maxamount = maxamount;
        this.deadchars = deadchars;
        this.winsalone = winsalone;
        this.winningalive = winningalive;
        this.winningdead = winningdead;
        this.balancevalue = balancevalue;
        this.currentamount = minamount;
        if (this.abblist == null) {
            this.abblist = new TreeSet<>();
        }
        if (this.depends == null) {
            this.depends = new LinkedList<>();
        }
        if(this.secondarygroups==null){
            this.secondarygroups=new LinkedList<>();
        }
        this.wakesupwith=wakesupwith;

    }

    public Karte() {
        this.name = "";
        this.dead = false;
        this.originalgroup = new Group();
        this.verzaubert = false;
        this.deathdelay=0;
        this.cardid = UUID.randomUUID().toString();
        this.description = "";
        this.round = 0;
        this.group = new Group();
        this.depends = new LinkedList<>();
        this.image = "";
        this.position = -1;
        this.position2 = -1;
        this.extra = 0;
        this.fixeddeath = 0;
        this.nopoints = false;
        this.calleveryone = false;
        this.isprotected = false;
        this.deadchars = false;
        this.minamount = 1;
        this.maxamount = 1;
        this.currentamount = minamount;
        this.deadchars = false;
        this.winsalone = false;
        this.winningalive = 2;
        this.winningdead = 1;
        this.balancevalue = 0;
        this.abblist = new TreeSet<>();
        this.depends = new LinkedList<>();
        this.secondarygroups = new LinkedList<>();
        this.uuidToAbility=new TreeMap<>();
        this.actionlist=new TreeMap<>();
        this.wakesupwith=new TreeSet<>();
    }

    public Map<String,Action> getActionlist() {
        return actionlist;
    }

    public Karte(Parcel in) {
        this.nopoints = in.readByte() != 0;
        this.calleveryone = in.readByte() != 0;
        this.dead = in.readByte() != 0;
        this.verzaubert = in.readByte() != 0;
        this.isprotected = in.readByte() != 0;
        this.deadchars = in.readByte() != 0;
        this.winsalone = in.readByte() != 0;
        this.deathdelay = in.readInt();
        this.round = in.readInt();
        this.position = in.readInt();
        this.minamount = in.readInt();
        this.maxamount = in.readInt();
        this.position2 = in.readInt();
        this.extra = in.readInt();
        this.fixeddeath = in.readInt();
        this.currentamount = in.readInt();
        this.winningalive = in.readInt();
        this.winningdead = in.readInt();
        this.balancevalue = in.readInt();
        this.cardid = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        this.image = in.readString();
        this.spieler = in.readString();
        this.group = in.readParcelable(Group.class.getClassLoader());
        this.originalgroup = in.readParcelable(Group.class.getClassLoader());
        this.depends = new ArrayList<>();
        in.readStringList(this.depends);
        List<Ability> abbs=new LinkedList<>();
        in.readTypedList(abbs, Ability.CREATOR);
        this.abblist=new TreeSet<>(abbs);
        this.uuidToAbility=new TreeMap<>();
        for(Ability abb:abblist){
            this.uuidToAbility.put(abb.getAbilityId(),abb);
        }
        this.secondarygroups = new ArrayList<>();
        in.readTypedList(this.secondarygroups,Group.CREATOR);
        this.actionlist=new TreeMap<>();
        ArrayList<String> master=new ArrayList<>();
        in.readStringList(master);
        ArrayList<Action> player=new ArrayList<>();
        in.readTypedList(player,Action.CREATOR);
        Iterator<Action> iter=player.iterator();
        for(String mas:master){
            this.actionlist.put(mas,iter.next());
        }
        List<String> wakeupl=new LinkedList<>();
        in.readStringList(wakeupl);
        this.wakesupwith=new TreeSet<>(wakeupl);

    }

    public Karte(Karte card){
        this.cardid=card.getCardid();
        this.abblist=new TreeSet<>();
        this.depends=new LinkedList<>();
        this.wakesupwith=new TreeSet<>();
        this.synchronize(card);
    }

    /**
     * Changes the group of a character if the game requires it.
     *
     * @param toGroup the group to change the character to
     */
    public void changeGroup(final Group toGroup) {
        this.group = toGroup;
    }

    @Override
    public int compareTo(final Karte o) {

            return this.position.compareTo(o.position);

    }

    /**
     * Kills the character.
     */
    public void dead() {
        this.dead = true;
    }

    @Override
    public int describeContents() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean equals(final Object card) {
        return card instanceof Karte && this.cardid.equals(((Karte) card).cardid);
    }

    public Integer getBalancevalue() {
        return balancevalue;
    }

    public Set<String> getWakesUpWith() {
        return this.wakesupwith;
    }

    public void setBalancevalue(final Integer balancevalue) {
        this.balancevalue = balancevalue;
    }

    public Boolean getCalleveryone() {
        return calleveryone;
    }

    public void setCalleveryone(final Boolean calleveryone) {
        this.calleveryone = calleveryone;
    }

    /**
     * Gets the cardid of the character.
     *
     * @return the characters cardid
     */
    public String getCardid() {
        return this.cardid;
    }

    public Integer getCurrentamount() {
        return this.currentamount;
    }

    public void setCurrentamount(Integer currentamount) {
        this.currentamount = currentamount;
    }

    public Boolean getDeadchars() {
        return deadchars;
    }

    public void setDeadchars(final Boolean deadchars) {
        this.deadchars = deadchars;
    }

    /**
     * Gets the description of the character.
     *
     * @return the character description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets the character's description.
     *
     * @param description the description*
     */
    public void setDescription(final String description) {
        this.description = description.replaceAll("\n", "<br/>");
    }

    public Integer getExtra() {
        return this.extra;
    }

    public void setExtra(final Integer extra) {

        this.extra = extra;
    }

    public Integer getFixeddeath() {
        return fixeddeath;
    }

    public void setFixeddeath(final Integer fixeddeath) {
        this.fixeddeath = fixeddeath;
    }

    /**
     * Gets the group of the character.
     *
     * @return the group string
     */
    public Group getGroup() {
        return this.group;
    }

    /**
     * Sets the group of the character.
     *
     * @param group the group string*
     */

    public void setGroup(final Group group) {
        this.group = group;
    }

    /**
     * Gets the image path of the character.
     *
     * @return a String: the image path*
     */
    public String getImg() {
        return this.image;
    }

    /**
     * Sets the image path of the character.
     *
     * @param str the image path*
     */
    public void setImg(final String str) {
        this.image = str;
    }

    public Integer getMaxAmount() {
        return this.maxamount;
    }

    public Integer getMaxamount() {

        return maxamount;
    }

    public void setMaxamount(final Integer maxamount) {
        this.maxamount = maxamount;
    }

    public Integer getMinAmount() {
        return this.minamount;
    }

    public Integer getMinamount() {
        return minamount;
    }

    public void setMinamount(final Integer minamount) {
        this.minamount = minamount;
    }

    /**
     * Gets the name of the character.
     *
     * @return the characters name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of the character.
     *
     * @param named the name
     */
    public void setName(final String named) {
        this.name = named;
    }

    public Boolean getNopoints() {
        return nopoints;
    }

    public void setNopoints(final Boolean nopoints) {
        this.nopoints = nopoints;
    }

    public Integer getPosition() {
        return this.position;
    }

    public void setPosition(final Integer position) {

        this.position = position;
    }

    public Integer getPosition2() {
        return this.position2;
    }

    /**
     * .
     *
     * @return the round parameter*
     */
    public Integer getRound() {
        return this.round;
    }

    public void setRound(final Integer round) {
        this.round = round;
    }

    /**
     * Gets the status of the character.
     *
     * @return a string containing status information*
     */
    public String getStatus() {
        int i = 1;
        final StringBuilder buf = new StringBuilder(256);
        if (this.verzaubert)
            buf.append("Ist verzaubert<br>");
        if (this.abblist.size() > 0)
            buf.append("Fähigkeiten:<br>");
        for (Ability ability : this.abblist) {
            if (ability.isAktiv()) {
                buf.append("F" + i++ + " " + ability + " verfügbar<br>");
            } else {
                buf.append("F" + i++ + " " + ability + " nicht verfügbar<br>");
            }
        }
        return buf.toString();
    }

    public Integer getWinningAlive() {
        return winningalive;
    }

    public void setWinningAlive(final Integer winningalive) {
        this.winningalive = winningalive;
    }

    public Integer getWinningDead() {
        return winningdead;
    }

    public void setWinningDead(final Integer winningdead) {
        this.winningdead = winningdead;
    }

    public Boolean getWinsalone() {
        return winsalone;
    }

    public void setWinsalone(final Boolean winsalone) {
        this.winsalone = winsalone;
    }

    /**
     * Gets the list of abilities for this character.
     *
     * @return the list of abilities*
     */
    public Set<Ability> getabblist() {
        return this.abblist;
    }

    /**
     * Gets the dependencies of this character.
     *
     * @return a list of the dependency characters*
     */
    public List<String> getdepends() {
        return this.depends;
    }

    /**
     * Checks if an image path already exists.
     *
     * @return true or false*
     */
    public Boolean imgexists() {
        return !" ".equals(this.image);
    }

    /**
     * Indicates if the character is already dead.
     *
     * @return true or false
     */
    public Boolean isdead() {

        return this.dead;
    }

    public Boolean isprotected() {
        return this.isprotected;
    }

    /**
     * Indicates if a character is enchanted.
     *
     * @return true or false
     */
    public Boolean isverzaubert() {
        return this.verzaubert;
    }

    /**
     * Resets the character to the state as it was build.
     */
    public void reset() {
        for (Ability f : this.abblist)
            f.reset();
        this.verzaubert = false;
        this.isprotected = false;
        this.dead = false;
        this.spieler = null;
        this.group = this.originalgroup;
        this.currentamount = minamount;
        this.secondarygroups.clear();
    }

    /**
     * Sets the character's protected state.
     *
     * @param isprotected indicates if the character is protected.*
     */
    public void setProtected(final boolean isprotected) {
        this.isprotected = isprotected;
    }

    /**
     * Sets the death indicator of the character.
     *
     * @param death the new state of life.*
     */
    public void setdead(final boolean death) {
        this.dead = death;
    }


    @Override
    public String toString() {
        return this.name+" "+this.position.toString();
    }

    public void setCardid(final String cardid) {
        this.cardid = cardid;
    }


    public Map<String, Ability> getUuidToAbility() {
        return uuidToAbility;
    }

    public void setUuidToAbility(final Map<String, Ability> uuidToAbility) {
        this.uuidToAbility = uuidToAbility;
    }

    /**

     * Synchronises this card with another card.
     * @param cardi the card to synchronize
     */
    @Override
    public boolean synchronize(final GameElements cardi){
          Karte card=(Karte)cardi;
          if(!card.getCardid().equals(cardid)){
              return false;
          }else{
              for(Ability abb:this.abblist){
                  abb.synchronize(card.getUuidToAbility().get(abb.getAbilityId()));
              }
              this.balancevalue=card.balancevalue;
              this.minamount=card.minamount;
              this.maxamount=card.maxamount;
              this.calleveryone=card.calleveryone;
              this.name=card.name;
              this.deadchars=card.deadchars;
              this.description=card.description;
              this.extra=card.extra;
              this.fixeddeath=card.fixeddeath;
              this.group=card.group;
              this.dead =card.dead;
              this.deathdelay=card.deathdelay;
              this.verzaubert=card.verzaubert;
              this.isprotected=card.isprotected;
              this.nopoints=card.nopoints;
              this.image=card.image;
              this.originalgroup=card.originalgroup;
              this.position=card.position;
              this.position2=card.position2;
              this.round=card.round;
              this.winningdead=card.winningdead;
              this.winningalive=card.winningalive;
              this.winsalone=card.winsalone;
              this.currentamount=card.currentamount;
              this.actionlist=card.actionlist;
              this.secondarygroups=card.secondarygroups;
              return true;
          }
    }

    @Override
    public String toXML() {
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.setOutput(writer);
            serializer.startTag("", "Karte");
            serializer.attribute("", "cardid", this.cardid);
            serializer.attribute("", "name", this.name);
            serializer.attribute("", "group", this.group.getGroupIdentifier());
            serializer.attribute("", "round", String.valueOf(this.round));
            serializer.attribute("", "img", this.image);
            serializer.attribute("", "minamount", String.valueOf(this.minamount));
            serializer.attribute("", "maxamount", String.valueOf(this.maxamount));
            serializer.attribute("", "position", String.valueOf(this.position));
            serializer.attribute("", "position2", String.valueOf(this.position2));
            serializer.attribute("", "extra", String.valueOf(this.extra));
            serializer.attribute("", "fixeddeath", String.valueOf(this.fixeddeath));
            serializer.attribute("", "nopoints", this.nopoints.toString());
            serializer.attribute("", "calleveryone", this.calleveryone.toString());
            serializer.attribute("", "winsalone", this.winsalone.toString());
            serializer.attribute("", "winningalive", this.winningalive.toString());
            serializer.attribute("", "winningdead", this.winningdead.toString());
            serializer.attribute("", "balance", this.balancevalue.toString());
            serializer.flush();
            for (String depends : this.depends) {
                serializer.startTag("", "depends");
                serializer.attribute("", "A", depends);
                serializer.endTag("", "depends");
                serializer.flush();
            }
            for (Ability ability : this.abblist) {
                writer.write(ability.toXML());
            }
            serializer.startTag("", "description");
            serializer.text(this.description);
            serializer.endTag("", "description");
            serializer.flush();
            int i=0;
            if(!this.actionlist.isEmpty()){
                serializer.startTag("","actions");
                serializer.flush();
            }
            for(Action action:this.actionlist.values()){
               writer.write(action.toXML());
            }
            serializer.flush();
            if(!this.actionlist.isEmpty()){
                serializer.endTag("", "actions");
                serializer.flush();
            }
            if(!this.wakesupwith.isEmpty()){
                serializer.startTag("","wakesupwith");
                serializer.flush();
            }
            for(String wakeup:this.wakesupwith){
                serializer.startTag("","ww");
                serializer.text(wakeup);
                serializer.endTag("","ww");
            }
            if(!this.wakesupwith.isEmpty()){
                serializer.endTag("","wakesupwith");
            }
            serializer.endTag("", "Karte");
            serializer.flush();
            Log.e("Karte: ", writer.toString());
            return writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Puts a spell on the character.
     */
    public void verzaubern() {
        this.verzaubert = true;
    }

    public Integer getDeathdelay() {
        return deathdelay;
    }

    public void setDeathdelay(final Integer deathdelay) {
        this.deathdelay = deathdelay;
    }

    @Override
    public void writeToParcel(final Parcel parcel, final int i) {
        parcel.writeByte((byte) (this.nopoints ? 1 : 0));
        parcel.writeByte((byte) (this.calleveryone ? 1 : 0));
        parcel.writeByte((byte) (this.dead ? 1 : 0));
        parcel.writeByte((byte) (this.verzaubert ? 1 : 0));
        parcel.writeByte((byte) (this.isprotected ? 1 : 0));
        parcel.writeByte((byte) (this.deadchars ? 1 : 0));
        parcel.writeByte((byte) (this.winsalone ? 1 : 0));
        parcel.writeInt(this.deathdelay);
        parcel.writeInt(this.round);
        parcel.writeInt(this.position);
        parcel.writeInt(this.minamount);
        parcel.writeInt(this.maxamount);
        parcel.writeInt(this.position2);
        parcel.writeInt(this.extra);
        parcel.writeInt(this.fixeddeath);
        parcel.writeInt(this.currentamount);
        parcel.writeInt(this.winningalive);
        parcel.writeInt(this.winningdead);
        parcel.writeInt(this.balancevalue);
        parcel.writeString(this.cardid);
        parcel.writeString(this.name);
        parcel.writeString(this.description);
        parcel.writeString(this.image);
        parcel.writeString(this.spieler);
        parcel.writeParcelable(this.group,0);
        parcel.writeParcelable(this.originalgroup,0);
        parcel.writeStringList(this.depends);
        parcel.writeTypedList(new ArrayList<>(this.abblist));
        parcel.writeTypedList(new ArrayList<>(this.secondarygroups));
        ArrayList<String> master=new ArrayList<>();
        ArrayList<Action> player=new ArrayList<>();
        for(String action:this.actionlist.keySet()){
           master.add(action);
           player.add(this.actionlist.get(action));
        }
        parcel.writeStringList(master);
        parcel.writeTypedList(player);
        parcel.writeStringList(new ArrayList<String>(this.wakesupwith));
    }

}
