package com.example.LNDWA.util.parser;

import com.example.LNDWA.cards.Game;
import com.example.LNDWA.cards.Player;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: timo
 * Date: 25.11.13
 * Time: 18:02
 * Parses a list of players_config.
 */
public class PlayerParse extends DefaultHandler {
    /**Temp player for parsing a player list.*/
    private Player player;
    /**List of player to be parsed.*/
    private List<Player> playerList;

    private Boolean play=false,error=false;

    private String errormessage="";

    /**
     * Constructor for this class.
     */
    public PlayerParse(){
        this.playerList=new LinkedList<>();
    }

    public void setError(final Boolean error) {
        this.error = error;
    }

    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
        switch(qName){
            case "error": error=true;break;
            case "player": this.player=new Player(attributes.getValue("name"),attributes.getValue("firstname"));
                this.player.setTotal(Integer.valueOf(attributes.getValue("total")));
                if(attributes.getValue("playerid")==null){
                    this.player.setPlayerid(UUID.randomUUID().toString());
                }else{
                    this.player.setPlayerid(attributes.getValue("playerid"));
                }
                this.play=true;break;
            case "game": if(play){
                this.player.addGame(new Game(attributes.getValue("character"),Integer.valueOf(attributes.getValue("points")),attributes.getValue("id")));
            }
            default:
        }
    }

    public Boolean getError() {
        return error;
    }

    public String getErrormessage() {

        return errormessage;
    }

    @Override
    public void characters(final char[] ch, final int start, final int length) throws SAXException {
        if(error){
            this.errormessage=new String(ch,start,length);

        }
    }

    @Override
    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        switch(qName){
            case "player": this.playerList.add(this.player);this.play=false;break;
            default:
        }
    }

    /**
     * Returns the parsed list of players_config.
     * @return the list of players_config
     */
    public List<Player> getPlayers() {
        return this.playerList;
    }

    /**
     * Parses a game file and fills the corresponding fields in this class.
     *
     * @param file the file to parse
     */
    public List<Player> parsePlayer(final File file) {
        try {
            SAXParserFactory.newInstance().newSAXParser().parse(file, this);
        } catch (SAXException e) {
            e.toString();
        } catch (IOException e) {
            e.toString();
        } catch (ParserConfigurationException e) {
            e.toString();
        }
        return this.playerList;
    }

    /**The parsing function for the characters.
     * @param fileName the name of the file to be parsed.
     * @return the list of characters.**/
    public List<Player> parsePlayer(final InputSource fileName){
        try {
            SAXParserFactory.newInstance().newSAXParser().parse(fileName, this);
        } catch (SAXException e) {
            e.toString();
        } catch (IOException e) {
            e.toString();
        } catch (ParserConfigurationException e) {
            e.toString();
        }
        return this.playerList;
    }

    /**The parsing function for the characters.
     * @param fileName the name of the file to be parsed.
     * @return the list of characters.**/
    public List<Player> parsePlayer(final InputStream fileName){
        try {
            SAXParserFactory.newInstance().newSAXParser().parse(fileName, this);
        } catch (SAXException e) {
            e.toString();
        } catch (IOException e) {
            e.toString();
        } catch (ParserConfigurationException e) {
            e.toString();
        }
        return this.playerList;
    }
}
