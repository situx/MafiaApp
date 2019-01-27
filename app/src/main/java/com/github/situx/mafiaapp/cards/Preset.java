package com.github.situx.mafiaapp.cards;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.util.Xml;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by timo on 13.02.14.
 */
public class Preset implements Parcelable, GameElements {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Preset createFromParcel(Parcel in) {
            return new Preset(in);
        }

        public Preset[] newArray(int size) {
            return new Preset[size];
        }
    };
    /**
     * The preset list of cards.
     */
    private java.util.Map<String, Integer> cardlist;
    /**The name of the preset.*/
    private String presetName;
    /**The amount of players_config included in the preset.*/
    private Integer player;

    private String gamesetid;

    public String getGamesetid() {
        return gamesetid;
    }

    public void setGamesetid(final String gamesetid) {
        this.gamesetid = gamesetid;
    }

    /**
     * Constructor for this class.
     *
     * @param cardlist the list of cards
     */
    public Preset(final java.util.Map<String, Integer> cardlist,final String gamesetid,final Integer player,final String presetname) {
        this.cardlist = cardlist;
        this.gamesetid=gamesetid;
        this.player=player;
        this.presetName=presetname;
    }

    /**
     * Empty constructor for preset.
     */
    public Preset() {
        this.cardlist = new TreeMap<>();
    }

    /**
     * Parcel constructor for sending a preset to another activity.
     * @param in the received parcel
     */
    public Preset(final Parcel in) {
        this.presetName = in.readString();
        this.gamesetid=in.readString();
        this.player = in.readInt();
        int size = in.readInt();
        List<String> keylist = new LinkedList<>();
        in.readStringList(keylist);
        int[] mapvalues = new int[size];
        in.readIntArray(mapvalues);
        this.cardlist = new TreeMap<>();
        int i = 0;
        for (String key : keylist) {
            this.cardlist.put(key, mapvalues[i++]);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public java.util.Map<String, Integer> getCardlist() {
        return cardlist;
    }

    public void setCardlist(final java.util.Map<String, Integer> cardlist) {
        this.cardlist = cardlist;
    }

    public Integer getPlayer() {
        return player;
    }

    public void setPlayer(final Integer player) {
        this.player = player;
    }

    public String getPresetName() {
        return presetName;
    }

    public void setPresetName(final String presetName) {
        this.presetName = presetName;
    }

    @Override
    public boolean synchronize(final GameElements elem) {
        return false;
    }

    @Override
    public String toXML() {
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.setOutput(writer);
            serializer.startTag("", "preset");
            serializer.attribute("", "name", this.presetName);
            serializer.attribute("", "player", this.player.toString());
            serializer.attribute("","gamesetid",this.gamesetid);
            for (String card : this.cardlist.keySet()) {
                serializer.startTag("", "card");
                serializer.attribute("", "amount", this.cardlist.get(card).toString());
                serializer.text(card);
                serializer.endTag("", "card");
            }
            serializer.endTag("", "preset");
            serializer.flush();
            Log.e("Karte: ", writer.toString());
            return writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void writeToParcel(final Parcel parcel, final int i) {
        parcel.writeString(this.presetName);
        parcel.writeString(this.gamesetid);
        parcel.writeInt(this.player);
        parcel.writeInt(this.cardlist.size());
        parcel.writeStringList(new ArrayList<>(this.cardlist.keySet()));
        int[] cardvalues = new int[this.cardlist.size()];
        int j = 0;
        for (Integer val : this.cardlist.values()) {
            cardvalues[j++] = val;
        }
        parcel.writeIntArray(cardvalues);
    }
}
