package com.github.situx.mafiaapp.cards;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.util.Xml;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by timo on 26.04.14.
 */
public class Action implements Comparable<Action>,Parcelable,GameElements {

    private String gamemaster;

    private String player;

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    private String title;

    private String actionid;

    private Integer round;

    private Integer position;

    private Boolean ondead;

    public Boolean getOndead() {
        return ondead;
    }

    public void setOndead(final Boolean ondead) {
        this.ondead = ondead;
    }

    @Override
    public int compareTo(final Action action) {
        return position.compareTo(action.position);
    }

    @Override
    public int describeContents() {
        return 0;
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
            serializer.startTag("", "action");
            serializer.attribute("", "id", this.actionid);
            serializer.attribute("", "title", this.title);
            serializer.attribute("", "position", this.position.toString());
            serializer.attribute("", "round", this.round.toString());
            serializer.startTag("", "gamemaster");
            serializer.text(this.gamemaster);
            serializer.endTag("","gamemaster");
            serializer.startTag("","player");
            serializer.text(this.player);
            serializer.endTag("","player");
            serializer.endTag("","action");
            serializer.flush();
            return writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void writeToParcel(final Parcel parcel, final int i) {
          parcel.writeString(actionid);
          parcel.writeInt(position);
          parcel.writeInt(round);
          parcel.writeString(gamemaster);
          parcel.writeString(player);
          parcel.writeString(title);
          parcel.writeByte((byte) (this.ondead ? 1 : 0));
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(final Integer position) {
        this.position = position;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Action createFromParcel(Parcel in) {
            return new Action(in);
        }

        public Action[] newArray(int size) {
            return new Action[size];
        }
    };

    public Action(){
          this.title="";
          this.gamemaster="";
          this.player="";
          this.position=-1;
          this.round=-1;
          this.ondead=false;
    }

    public Action(Parcel in){
        this.actionid=in.readString();
        this.position=in.readInt();
        this.round=in.readInt();
        this.gamemaster=in.readString();
        this.player=in.readString();
        this.title=in.readString();
        this.ondead = in.readByte() != 0;
    }

    public String getGamemaster() {
        return gamemaster;
    }

    public void setGamemaster(final String gamemaster) {
        this.gamemaster = gamemaster;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(final String player) {
        this.player = player;
    }

    public String getActionid() {
        return actionid;
    }

    public void setActionid(final String actionid) {
        this.actionid = actionid;
    }

    public Integer getRound() {
        return round;
    }

    public void setRound(final Integer round) {
        this.round = round;
    }

    @Override
    public String toString() {
        return this.title+" "+this.actionid+" "+this.position+" "+this.round;
    }

    @Override
    public boolean equals(final Object o) {
        return o instanceof Action && ((Action)o).position.equals(position) && ((Action)o).actionid.equals(actionid);
    }
}
