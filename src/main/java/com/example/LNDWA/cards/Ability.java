package com.example.LNDWA.cards;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Xml;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Abstract class for implementing abilities.*
 */
public class Ability implements Parcelable, GameElements, AbilityAPI, Comparable<Ability> {
    public Operator getCheck() {
        return check;
    }

    public Boolean getOnlyCheck() {
        return this.check.getOnlyCheck();
    }

    public void setCheck(final Operator check) {
        this.check = check;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Ability createFromParcel(Parcel in) {
            return new Ability(in);
        }

        public Ability[] newArray(int size) {
            return new Ability[size];
        }
    };
    /**
     * .*
     */
    public Integer currentamount;
    /**
     * The id of this ability.
     */
    private String abilityId;
    /**
     * .*
     */
    private Boolean active;
    private Boolean alwaysChooseOther;
    /**
     * Describes from which round this ability is available.
     */
    private Integer availableFrom;
    /**
     * Describes until which round this ability is available.
     */
    private Integer availableUntil;
    /**
     * If a group should be changed by using this ability the new group name is specified here.
     */
    private List<Group> changeGroup;
    /**
     * The amount of players_config which are concerned by using this ability.
     */
    private Integer concerns;
    /**
     * Indicates that the ability has a counterKilling function of some kind.
     */
    private Boolean counterKilling;
    private Integer delay;
    /**
     * The description of this ability.*
     */
    private String description;
    /**
     * The duration of this ability.
     */
    private Integer duration;
    /**
     * If this ability can be again chosen in every round. (will be resetted)
     */
    private Boolean everyRound;
    /**
     * The image path of this ability.
     */
    private String image;
    /**
     * Indicates if this ability forces a player to leave the game.
     */
    private Boolean killing;
    /**
     * Describes if the player has to use the ability if it is available.
     */
    private Boolean mustuse;
    private Boolean ondead;
    /**
     * .*
     */
    private Integer originalamount;
    /**
     * Describes the probability of this ability to succeed.
     */
    private Integer probability;
    /**
     * Indicates if the ability can be used on the executing character.
     */
    private Boolean self;
    /**
     * Indicates if the ability is used to switch an exisiting character with another existing character in game.
     */
    private Boolean switchchar;
    /**
     * Indicates if the ability is used to switch an already exisiting character to another new character.
     */
    private String switchnewchar;

    private Operator check;

    /**
     * Constructor for a new ability.
     *
     * @param description .
     * @param active      .
     * @param fkl         .*
     */
    public Ability(final String description, final boolean active, final int fkl,
                   final Integer availableFrom, final Integer availableUntil, final Integer probability,
                   final String image, final Integer concerns, Boolean mustuseability,
                   final Integer duration, final String abilityId, final Boolean killing, final Boolean counterKilling,
                   final Boolean self, final Boolean everyRound, final List<Group> changeGroup, final Boolean switchchar,
                   final String switchnewchar, final Integer delay, final Boolean alwaysChooseOther, final Boolean ondead,final Operator check) {
        super();
        this.description = description;
        this.active = active;
        this.killing = killing;
        this.delay = delay;
        this.counterKilling = counterKilling;
        this.currentamount = fkl;
        this.originalamount = fkl;
        this.availableFrom = availableFrom;
        this.availableUntil = availableUntil;
        this.probability = probability;
        this.image = image;
        this.concerns = concerns;
        this.mustuse = mustuseability;
        this.duration = duration;
        this.abilityId = abilityId;
        this.self = self;
        this.everyRound = everyRound;
        this.changeGroup = changeGroup;
        this.switchchar = switchchar;
        this.switchnewchar = switchnewchar;
        this.alwaysChooseOther = alwaysChooseOther;
        this.ondead = ondead;
        this.check=check;
    }

    /**
     * Empty constructor for ability using default values.
     */
    public Ability() {
        super();
        this.description = "";
        this.active = false;
        this.killing = false;
        this.counterKilling = false;
        this.currentamount = 0;
        this.originalamount = 0;
        this.availableFrom = 0;
        this.availableUntil = -1;
        this.probability = 100;
        this.image = "";
        this.delay = 0;
        this.concerns = 0;
        this.mustuse = false;
        this.duration = 1;
        this.abilityId = UUID.randomUUID().toString();
        this.self = false;
        this.changeGroup = new LinkedList<>();
        this.switchchar = false;
        this.switchnewchar = "-1";
        this.everyRound = false;
        this.alwaysChooseOther = false;
        this.ondead=false;
        this.check=new Operator();
    }

    /**
     * Parcel constructor for ability for sending this ability to another activity.
     *
     * @param in the received parcel
     */
    public Ability(Parcel in) {
        this.active = in.readByte() != 0;
        this.mustuse = in.readByte() != 0;
        this.killing = in.readByte() != 0;
        this.counterKilling = in.readByte() != 0;
        this.self = in.readByte() != 0;
        this.everyRound = in.readByte() != 0;
        this.switchchar = in.readByte() != 0;
        this.alwaysChooseOther = in.readByte() != 0;
        this.ondead=in.readByte() !=0;
        this.currentamount = in.readInt();
        this.originalamount = in.readInt();
        this.availableFrom = in.readInt();
        this.availableUntil = in.readInt();
        this.probability = in.readInt();
        this.concerns = in.readInt();
        this.duration = in.readInt();
        this.delay = in.readInt();
        this.abilityId = in.readString();
        this.image = in.readString();
        this.description = in.readString();
        this.changeGroup=new LinkedList<>();
        in.readTypedList(this.changeGroup,Group.CREATOR);
        this.switchnewchar = in.readString();
        this.check=in.readParcelable(Operator.class.getClassLoader());
    }

    @Override
    public void action(final Karte card) {

    }

    public Boolean checkFromUntil(final Integer round) {
        return round >= availableFrom && (round <= availableUntil || availableUntil == -1);
    }

    @Override
    public int compareTo(final Ability ability) {
        return this.description.compareTo(ability.description);
    }

    @Override
    public int describeContents() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean equals(final Object o) {
        return o instanceof Ability && this.abilityId.equals(((Ability) o).getAbilityId());
    }

    /**
     * Gets the ability id of this ability.
     *
     * @return the ability id as Integer
     */
    public String getAbilityId() {
        return abilityId;
    }

    public Boolean getActive() {
        return this.active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(final boolean active) {
        this.active = active;
    }

    public Boolean getAlwaysChooseOther() {
        return true;
    }

    public Integer getAvailableFrom() {
        return availableFrom;
    }

    public void setAvailableFrom(final Integer availableFrom) {
        this.availableFrom = availableFrom;
    }

    public Integer getAvailableUntil() {
        return availableUntil;
    }

    public void setAvailableUntil(final Integer availableUntil) {
        this.availableUntil = availableUntil;
    }

    public List<Group> getChangeGroup() {
        return changeGroup;
    }

    /**
     * Gets the ammount of players_config concerned by using this ability.
     *
     * @return the amount as Integer
     */
    public Integer getConcerns() {
        return concerns;
    }

    /**
     * Sets the amount of players_config concerned by using this ability
     *
     * @param concerns the amount of players_config as Integer
     */
    public void setConcerns(final Integer concerns) {
        this.concerns = concerns;
    }

    public Boolean getCounterKilling() {
        return counterKilling;
    }

    public void setCounterKilling(final Boolean counterKilling) {
        this.counterKilling = counterKilling;
    }

    public Integer getCurrentamount() {
        return currentamount;
    }

    public void setCurrentamount(final Integer currentamount) {
        this.currentamount = currentamount;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(final Integer delay) {
        this.delay = delay;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Gets the duration of this ability.
     *
     * @return -1 if permanent, >0 for the number of rounds
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * Sets the duration of this ability
     *
     * @param duration -1 for permanent, >0 for the number of rounds.
     */
    public void setDuration(final Integer duration) {
        this.duration = duration;
    }

    public Boolean getEveryRound() {
        return everyRound;
    }

    /**
     * Gets the image path of this ability.
     *
     * @return the image path as String
     */
    public String getImage() {
        return image;
    }

    /**
     * Sets the image path of this ability.
     *
     * @param image the image path as String
     */
    public void setImage(final String image) {
        this.image = image;
    }

    public Boolean getKilling() {
        return killing;
    }

    public void setKilling(final Boolean killing) {
        this.killing = killing;
    }

    /**
     * Gets the indication if this ability has to be used on availability.
     *
     * @return the indication as Boolean
     */
    public Boolean getMustuse() {
        return mustuse;
    }

    public void setMustuse(final Boolean mustuse) {
        this.mustuse = mustuse;
    }

    public Boolean getOndead() {
        return ondead;
    }

    public void setOndead(final Boolean ondead) {
        this.ondead = ondead;
    }

    public Integer getOriginalamount() {
        return originalamount;
    }

    public void setOriginalamount(final Integer originalamount) {
        this.originalamount = originalamount;
    }

    public Integer getProbability() {
        return probability;
    }

    public void setProbability(final Integer probability) {
        this.probability = probability;
    }

    public Boolean getSelf() {
        return self;
    }

    public void setSelf(final Boolean self) {
        this.self = self;
    }

    public Boolean getSwitchchar() {
        return switchchar;
    }

    public void setSwitchchar(final Boolean switchchar) {
        this.switchchar = switchchar;
    }

    public String getSwitchnewchar() {
        return switchnewchar;
    }

    public void setSwitchnewchar(final String switchnewchar) {
        this.switchnewchar = switchnewchar;
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
     * @return the active
     */
    public Boolean isAktiv() {
        return this.active;
    }

    /**
     * Resets the ability to the default values.*
     */
    public void reset() {
        this.currentamount = this.originalamount;
        this.setActive(true);
    }

    @Override
    public boolean synchronize(final GameElements elem) {
        Ability ability = (Ability) elem;
        this.active = ability.active;
        this.alwaysChooseOther = ability.alwaysChooseOther;
        this.availableFrom = ability.availableFrom;
        this.availableUntil = ability.availableUntil;
        this.changeGroup = ability.changeGroup;
        this.concerns = ability.concerns;
        this.counterKilling = ability.counterKilling;
        this.currentamount = ability.currentamount;
        this.description = ability.description;
        this.duration = ability.duration;
        this.delay = ability.delay;
        this.everyRound = ability.everyRound;
        this.image = ability.image;
        this.killing = ability.killing;
        this.mustuse = ability.mustuse;
        this.originalamount = ability.originalamount;
        this.probability = ability.probability;
        this.self = ability.self;
        this.switchchar = ability.switchchar;
        this.switchnewchar = ability.switchnewchar;
        this.ondead=ability.ondead;
        this.check=ability.check;
        return true;
    }

    @Override
    public String toString() {
        return this.description;
    }

    /**
     * Creates a String representation of the current class as XML.
     *
     * @return the XML String
     */
    @Override
    public String toXML() {
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.setOutput(writer);
            serializer.startTag("", "abb");
            serializer.attribute("", "active", String.valueOf(this.active));
            serializer.attribute("", "amount", String.valueOf(this.originalamount));
            serializer.attribute("", "availablefrom", String.valueOf(this.availableFrom));
            serializer.attribute("", "availableuntil", String.valueOf(this.availableUntil));
            serializer.attribute("", "probability", String.valueOf(this.probability));
            serializer.attribute("", "img", this.image);
            serializer.attribute("", "concerns", this.concerns.toString());
            serializer.attribute("", "mustuse", this.mustuse.toString());
            serializer.attribute("", "duration", this.duration.toString());
            serializer.attribute("", "killing", this.killing.toString());
            serializer.attribute("", "counterKilling", this.counterKilling.toString());
            serializer.attribute("", "self", this.self.toString());
            serializer.attribute("","check",this.check.toString());
            serializer.attribute("", "everyround", this.everyRound.toString());
            serializer.attribute("", "changegroup", this.changeGroup.isEmpty()?"":this.changeGroup.get(0).getGroupIdentifier());
            serializer.attribute("", "delay", this.delay.toString());
            serializer.attribute("","ondead",this.ondead.toString());
            serializer.text(this.description);
            serializer.endTag("", "abb");
            serializer.flush();
            return writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void writeToParcel(final Parcel parcel, final int method) {
        parcel.writeByte((byte) (this.active ? 1 : 0));
        parcel.writeByte((byte) (this.mustuse ? 1 : 0));
        parcel.writeByte((byte) (this.killing ? 1 : 0));
        parcel.writeByte((byte) (this.counterKilling ? 1 : 0));
        parcel.writeByte((byte) (this.self ? 1 : 0));
        parcel.writeByte((byte) (this.everyRound ? 1 : 0));
        parcel.writeByte((byte) (this.switchchar ? 1 : 0));
        parcel.writeByte((byte) (this.alwaysChooseOther ? 1 : 0));
        parcel.writeByte((byte) (this.ondead?1:0));
        parcel.writeInt(this.currentamount);
        parcel.writeInt(this.originalamount);
        parcel.writeInt(this.availableFrom);
        parcel.writeInt(this.availableUntil);
        parcel.writeInt(this.probability);
        parcel.writeInt(this.concerns);
        parcel.writeInt(this.duration);
        parcel.writeInt(this.delay);
        parcel.writeString(this.abilityId);
        parcel.writeString(this.image);
        parcel.writeString(this.description);
        parcel.writeTypedList(this.changeGroup);
        parcel.writeString(this.switchnewchar);
        parcel.writeParcelable(this.check,0);
    }
}
