package com.example.LNDWA.cards;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Xml;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: timo
 * Date: 25.11.13
 * Time: 18:03
 * Represents a game having taken place.
 */
public class Game implements Parcelable, GameElements {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        public Game[] newArray(int size) {
            return new Game[size];
        }
    };
    /**
     * The character being used in this game.
     */
    private Karte character;
    /**
     * The itemList being used by this character in this game.
     */
    private List<Item> itemList;
    /**
     * The points this player got in this game.
     */
    private final Integer points;
    /**
     * The id of this game.
     */
    private final String gameid;

    /**
     * Constructor for this class.
     *
     * @param character the character being played in this game
     * @param points    the points reached in this game
     * @param id        the gameid
     */
    public Game(final String character, final Integer points, final String id) {
        super();
        this.character = new Karte();
        this.character.setName(character);
        this.points = points;
        this.gameid = id;
        this.itemList = new LinkedList<>();
    }

    /**
     * Gameid constructor for this game initializing it with default values.
     *
     * @param id the gameid
     */
    public Game(final String id) {
        this.gameid = id;
        this.points = 0;
        this.character = new Karte();
        this.itemList = new LinkedList<>();
    }

    /**
     * Parcel constructor for this game.
     *
     * @param in the parcel received for reconstruction
     */
    public Game(final Parcel in) {
        this.character = in.readParcelable(Karte.class.getClassLoader());
        this.itemList = in.readArrayList(Game.class.getClassLoader());
        this.points = in.readInt();
        this.gameid = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Gets the character being played in this game.
     *
     * @return the character
     */
    public Karte getCharacter() {
        return this.character;
    }

    /**
     * Sets the character of this game.
     *
     * @param character the character to set
     */
    public void setCharacter(final Karte character) {
        this.character = character;
    }

    /**
     * Gets the id of this game.
     *
     * @return the id as Integer
     */
    public String getGameid() {
        return this.gameid;
    }

    /**
     * Gets the list of items being used by the character in this game.
     *
     * @return the list of items
     */
    public List<Item> getItemList() {
        return itemList;
    }

    /**
     * Sets the list of items being used by the character in this game.
     *
     * @param itemList the list of items
     */
    public void setItemList(final List<Item> itemList) {
        this.itemList = itemList;
    }

    /**
     * Gets the points reached in this game.
     *
     * @return the points as Integer
     */
    public Integer getPoints() {
        return this.points;
    }

    @Override
    public boolean synchronize(final GameElements elem) {

        return false;
    }

    /**
     * Generates an xml representation of this class.
     *
     * @return the xml representation as String
     */
    @Override
    public String toXML() {
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.setOutput(writer);
            serializer.startTag("", "game");
            serializer.attribute("", "id", this.gameid);
            serializer.attribute("", "points", this.points.toString());
            serializer.attribute("", "character", this.character.getName());
            serializer.endTag("", "game");
            serializer.flush();
            return writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void writeToParcel(final Parcel parcel, final int i) {
        parcel.writeParcelable(this.character, i);
        parcel.writeTypedList(new ArrayList<Item>(this.itemList));
        parcel.writeInt(this.points);
        parcel.writeString(this.gameid);
    }

    @Override
    public boolean equals(final Object o) {
        return o instanceof Game && this.gameid.equals(((Game) o).gameid);
    }
}
