package com.example.LNDWA.cards;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.util.Xml;
import com.example.LNDWA.util.Tuple;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

/**
 * Created by timo on 25.05.14.
 */
public class GameResult implements GameElements,Parcelable{
    public String gameid;

    public List<Game> game=new LinkedList<Game>();

    public List<Tuple<String,Integer>> gamelog;
    public GameSet gameset;

    public Map<Karte,Boolean> characters;
    public Group winninggroup;

    public String gametitle;

    public Integer rounds;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel parcel, final int i) {
        parcel.writeInt(this.rounds);
        parcel.writeString(this.gameid);
        parcel.writeString(this.gametitle);
        parcel.writeString(this.time);
        parcel.writeParcelable(this.winninggroup, 0);
        parcel.writeParcelable(this.gameset,0);
        parcel.writeTypedList(this.game);
        parcel.writeTypedList(new LinkedList<Parcelable>(characters.keySet()));
        parcel.writeTypedList(this.gamelog);
    }

    public GameResult(Parcel in){
        this.rounds=in.readInt();
        this.gameid=in.readString();
        this.gametitle=in.readString();
        this.time=in.readString();
        this.winninggroup=in.readParcelable(Group.class.getClassLoader());
        this.gameset=in.readParcelable(GameSet.class.getClassLoader());
        this.game=new LinkedList<>();
        in.readTypedList(this.game,Game.CREATOR);
        Map<Karte,Boolean> newcharacters=new TreeMap<>();
        List<Karte> cards=new LinkedList<>();
        in.readTypedList(cards,Karte.CREATOR);

    }

    public Integer getRounds() {
        return rounds;
    }

    public void setRounds(Integer rounds) {
        this.rounds = rounds;
    }

    public String getGameid() {
        return gameid;
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
    }

    public List<Game> getGame() {
        return game;
    }

    public void setGame(List<Game> game) {
        this.game = game;
    }

    public List<Tuple<String, Integer>> getGamelog() {
        return gamelog;
    }

    public void setGamelog(List<Tuple<String, Integer>> gamelog) {
        this.gamelog = gamelog;
    }

    public GameSet getGameset() {
        return gameset;
    }

    public void setGameset(GameSet gameset) {
        this.gameset = gameset;
    }

    public Map<Karte, Boolean> getCharacters() {
        return characters;
    }

    public void setCharacters(Map<Karte, Boolean> characters) {
        this.characters = characters;
    }

    public Group getWinninggroup() {
        return winninggroup;
    }

    public void setWinninggroup(Group winninggroup) {
        this.winninggroup = winninggroup;
    }

    public String getGametitle() {
        return gametitle;
    }

    public void setGametitle(String gametitle) {
        this.gametitle = gametitle;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String time;

    public GameResult(){
        //this.winningroup=game.get(0).
    }

    public Integer getMaxPoints(){
        Integer max=0;
        for(Game gam:game){
            if(max< gam.getPoints()){
                max= gam.getPoints();
            }
        }
        return max;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return gameid+"\n"+game;
    }

    @Override
    public boolean synchronize(GameElements elem) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * Generates an xml representation of this event.
     * @return The xml representation as String
     */
    @Override
    public String toXML() {
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.setOutput(writer);
            serializer.startTag("", "GameResult");
            serializer.attribute("","gameid", UUID.randomUUID().toString());
            //serializer.attribute("","players",this.cardlist.size()+"");
            serializer.attribute("","time",this.time);
            serializer.attribute("","gameid",this.gameid);
            serializer.attribute("","gametitle",this.gametitle);
            serializer.attribute("","gameset",this.gameset.getGamesetid());
            serializer.attribute("","rounds",this.rounds.toString());
            //serializer.attribute("","winninggroup",this.cardlist.get(0).getGroup().getGroupId());
            serializer.startTag("","gamelog");
            for(Tuple<String,Integer> log:this.gamelog){
                 serializer.startTag("","log");
                 serializer.attribute("","round",log.getTwo().toString());
                 serializer.text(log.getOne());
                 serializer.endTag("","log");
            }
            serializer.endTag("","gamelog");
            int i=0;
            /*for(Player winner:this.characters.keySet()){
                serializer.startTag("","game");
                serializer.attribute("", "character", winner.getName());
                serializer.attribute("","player",winner.getPlayerid());
                serializer.attribute("","points","");
                serializer.attribute("","won",this.characters.get(winner).toString());
                serializer.endTag("","game");
            }*/
            serializer.endTag("", "GameResult");
            serializer.flush();
            Log.e("GameResult: ", writer.toString());
            writer.toString();
            //this.openFileOutput("save-"+System.currentTimeMillis()+"",0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
