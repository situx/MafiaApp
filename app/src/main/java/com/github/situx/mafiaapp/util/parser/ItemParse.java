package com.github.situx.mafiaapp.util.parser;

import com.github.situx.mafiaapp.cards.Item;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by timo on 18.01.14.
 */
public class ItemParse extends DefaultHandler{


    private Item item;

    private List<Item> itemList;

    private String tempdescription;

    private Boolean play=false,description=false;

    public ItemParse(){
        this.itemList=new LinkedList<>();
        final Random random = new Random(System.currentTimeMillis());
    }

    public void parseEvents(InputSource fileName){
        try {
            SAXParserFactory.newInstance().newSAXParser().parse(fileName, this);
        } catch (SAXException e) {
            e.toString();
        } catch (IOException e) {
            e.toString();
        } catch (ParserConfigurationException e) {
            e.toString();
        }
    }

    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
        switch(qName){
            case "item": this.item=new Item();this.item.setId(Integer.valueOf(attributes.getValue("id")));this.item.setName(attributes.getValue("name"));this.play=true;break;
            case "description": if(play){
                this.description=true;this.tempdescription="";
            }break;
            default:
        }
    }

    @Override
    public void characters(final char[] ch, final int start, final int length) throws SAXException {
        if(this.description)
            this.tempdescription+=new String(ch,start,length)+"\n";
    }

    @Override
    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        switch(qName){
            case "description": this.item.setDescription(this.tempdescription);this.description=false;break;
            case "item": this.itemList.add(this.item);this.play=false;break;
            default:
        }
    }

    public List<Item> getItems() {
        return this.itemList;
    }

}
