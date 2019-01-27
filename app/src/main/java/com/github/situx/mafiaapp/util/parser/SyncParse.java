package com.github.situx.mafiaapp.util.parser;

import com.github.situx.mafiaapp.cards.GameSet;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by timo on 15.03.14.
 */
public class SyncParse extends DefaultHandler {

    private List<String> gamesetlist;

    private GameSet gameSet;

    private boolean set=false;

    private boolean error=false;

    private String errormessage="";

    public SyncParse(){
        this.gamesetlist=new LinkedList<>();
    }

    public void setError(final boolean error) {
        this.error = error;
    }

    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        switch(qName){
            case "error": error=true;break;
            case "gamesetlist":break;
            case "set": this.set=true;break;
        }
    }

    public String getErrormessage() {
        return errormessage;
    }

    public boolean isError() {
        return error;
    }


    @Override
    public void characters(final char[] ch, final int start, final int length) throws SAXException {
        super.characters(ch, start, length);
        if(this.set){
            this.gamesetlist.add(new String(ch,start,length));
        }
        if(this.error){
            this.errormessage=new String(ch,start,length);

        }
    }

    @Override
    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        switch(qName){
            case "gamesetlist":break;
            case "set": this.set=false;break;
        }
    }

    public List<String> parseGameSetList(final String filestring) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory.newInstance().newSAXParser().parse(new InputSource(new StringReader(filestring)),this);
        Collections.sort(this.gamesetlist);
        return this.gamesetlist;
    }
}
