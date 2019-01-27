package com.github.situx.mafiaapp.cards;

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
 * Created with IntelliJ IDEA.
 * User: timo
 * Date: 25.11.13
 * Time: 18:03
 * To change this template use File | Settings | File Templates.
 */
public class Player implements Parcelable, GameElements, Comparable<Player> {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        public Player[] newArray(int size) {
            return new Player[size];
        }
    };
    /**
     * The current card of this player.
     */
    private Karte currentKarte;
    /**
     * The player's first name.
     */
    private String firstname;
    /**
     * List of games this player has played.
     */
    private List<Game> games;
    /**
     * List of items this player has collected.
     */
    private List<Item> items;
    /**
     * The players_config' last name.
     */
    private String name;
    /**
     * The id of this player.
     */
    private String playerid;
    /**
     * The total score of this player.
     */
    private Integer total;

    /**
     * Constructor for this class.
     *
     * @param name      the last name of the player
     * @param firstname the first name of the player
     */
    public Player(final String name, final String firstname) {
        this.name = name;
        this.firstname = firstname;
        this.games = new LinkedList<>();
        this.currentKarte = null;
        this.playerid = UUID.randomUUID().toString();
    }

    /**
     * Empty constructor for player using default values.
     */
    public Player() {
        this.name = "";
        this.firstname = "";
        this.total = 0;
        this.games = new LinkedList<>();
        this.items = new LinkedList<>();
        this.playerid = UUID.randomUUID().toString();
    }

    public Player(Player player){
        this.currentKarte=player.currentKarte;
        this.firstname=player.firstname;
        this.name=player.name;
        this.items=player.items;
        this.playerid=player.playerid;
        this.games=player.games;
        this.total=player.total;
    }

    /**
     * Parcel constructor for player for sending players_config to another class.
     *
     * @param in the Parcel to process
     */
    public Player(Parcel in) {
        this.name = in.readString();
        this.firstname = in.readString();
        this.total = in.readInt();
        this.playerid = in.readString();
        //in.readTypedList(games, Game.CREATOR);
        //in.readTypedList(items,Item.CREATOR);
        //this.currentKarte=in.readParcelable(Karte.CREATOR);
    }

    public void addGame(final Game game) {
        this.games.add(game);
    }

    @Override
    public int compareTo(Player o) {
        int result = this.total.compareTo(o.total);
        if (result == 0) {
            return (this.firstname + " " + this.name).compareTo(o.firstname + " " + o.name);
        }
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Player && this.playerid.equals(((Player) obj).playerid);
    }

    public Karte getCurrentKarte() {
        return currentKarte;
    }

    public void setCurrentKarte(final Karte currentKarte) {
        this.currentKarte = currentKarte;
    }

    public String getFirstname() {
        return this.firstname;
    }

    /**
     * Sets the first name of the corresponding player
     *
     * @param firstname the first name as String
     */
    public void setFirstname(final String firstname) {
        this.firstname = firstname;
    }

    public List<Game> getGames() {
        return this.games;
    }

    public String getName() {
        return this.name;
    }

    /**
     * Sets the last name of the corresponding player
     *
     * @param name the last name as String
     */
    public void setName(final String name) {
        this.name = name;
    }

    public String getPlayerid() {
        return playerid;
    }

    public void setPlayerid(final String playerid) {
        this.playerid = playerid;
    }

    public Integer getTotal() {
        return this.total;
    }

    public void setTotal(final Integer total) {
        this.total = total;
    }

    @Override
    public boolean synchronize(final GameElements elem) {
        Player player=(Player) elem;
        if(this.playerid.equals(player.getPlayerid())){
            this.name=player.name;
            this.firstname=player.firstname;
            this.total=player.total;
            /*for(Game game:player.games){
                game.synchronize()
            }*/
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return this.firstname + " " + this.name;
    }

    /**
     * Generates an XML represntation of the current class.
     *
     * @return the representation as String
     */
    @Override
    public String toXML() {
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.setOutput(writer);
            serializer.startTag("", "player");
            serializer.attribute("", "firstname", String.valueOf(this.firstname));
            serializer.attribute("", "name", String.valueOf(this.name));
            serializer.attribute("", "playerid", this.playerid);
            serializer.attribute("", "total", String.valueOf(this.total));
            for (Game game : this.games) {
                serializer.flush();
                writer.write(game.toXML());
            }
            serializer.endTag("", "player");
            serializer.flush();
            return writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void writeToParcel(final Parcel parcel, final int i) {
        parcel.writeString(name);
        parcel.writeString(firstname);
        parcel.writeInt(total);
        parcel.writeString(this.playerid);
        //parcel.writeTypedList(games);
        //parcel.writeTypedList(items);
    }
}
