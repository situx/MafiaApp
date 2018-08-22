package com.example.LNDWA.util.parser;

import android.util.Log;
import com.example.LNDWA.cards.Preset;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by timo on 19.02.14.
 */
public class PresetParse extends DefaultHandler {

    private boolean card=false,preset=false;

    private Integer cardamount=0;

    private List<Preset> result;

    private Preset temp;

    public PresetParse(){
        this.result=new LinkedList<>();
    }

    public List<Preset> parsePreset(File file) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory.newInstance().newSAXParser().parse(file,this);
        Collections.sort(result, new Comparator<Preset>() {
            public int compare(Preset s1, Preset s2) {
                int compareval = s1.getPlayer().compareTo(s2.getPlayer());
                if (compareval == 0) {
                    return s1.getPresetName().compareTo(s2.getPresetName());
                }
                return compareval;
            }
        });
        return this.result;
    }

    /**The parsing function for the characters.
     * @param fileName the name of the file to be parsed.
     * @return the list of characters.**/
    public List<Preset> parsePreset(final InputSource fileName){
        try {
            SAXParserFactory.newInstance().newSAXParser().parse(fileName, this);
        } catch (SAXException e) {
            e.toString();
        } catch (IOException e) {
            e.toString();
        } catch (ParserConfigurationException e) {
            e.toString();
        }
        return this.result;
    }

    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
        switch(qName){
            case "preset": temp=new Preset(); temp.setPresetName(attributes.getValue("name"));
                Log.e("Preset", attributes.getValue("name"));
                temp.setGamesetid(attributes.getValue("gamesetid"));
                temp.setPlayer(Integer.valueOf(attributes.getValue("player"))); preset=true;break;
            case "card": if(preset){card=true;cardamount=Integer.valueOf(attributes.getValue("amount"));}break;
            default:
        }
    }

    @Override
    public void characters(final char[] ch, final int start, final int length) throws SAXException {
        if(card && preset){
           temp.getCardlist().put(new String(ch, start, length), cardamount);
        }
    }

    @Override
    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        switch(qName){
            case "preset": this.result.add(temp);preset=false;break;
            case "card": card=false;break;
            default:
        }
    }


}
