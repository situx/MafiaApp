package com.github.situx.mafiaapp.util.parser;

import android.text.Html;
import com.github.situx.mafiaapp.cards.Event;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by timo on 17.01.14.
 */
public class EventParse extends DefaultHandler{

        private static final String DESCRIPTION="description";
        private static final String TITLE="title";
        private static final String EVENTID="id";
        private static final String ACTIVE="active";
        private static final String PROBABILITY="probability";
        private static final String EVENT="event";
        /**The event to parse.*/
        private Event event;
        /**The list of event as result.*/
        private List<Event> eventList;

        private String tempdescription;
        /**Status booleans.*/
        private Boolean play=false,description=false;
        /**Random implementation.*/
        private Random random;

    /**
     * Constructor for this class.
     */
        public EventParse(){
            this.eventList=new LinkedList<>();
            this.random=new Random(System.currentTimeMillis());
        }

    /**
     * Parses the eventfile and returns an event list.
     * @param file the file to parse
     * @return the list of events
     */
    public List<Event> parseEvents(final File file){
        try {
            SAXParserFactory.newInstance().newSAXParser().parse(file, this);
        } catch (SAXException e) {
            e.toString();
        } catch (IOException e) {
            e.toString();
        } catch (ParserConfigurationException e) {
            e.toString();
        }
        return this.eventList;
    }

    /**The parsing function for the characters.
     * @param fileName the name of the file to be parsed.
     * @return the list of characters.**/
    public List<Event> parseEvents(final InputSource fileName){
        try {
            SAXParserFactory.newInstance().newSAXParser().parse(fileName, this);
        } catch (SAXException e) {
            e.toString();
        } catch (IOException e) {
            e.toString();
        } catch (ParserConfigurationException e) {
            e.toString();
        }
        return this.eventList;
    }

        @Override
        public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
            switch(qName){
                case EVENT: this.event=new Event();
                    this.event.setId(Integer.valueOf(attributes.getValue(EVENTID)));
                    this.event.setTitle(attributes.getValue(TITLE));
                    if(attributes.getValue(PROBABILITY)==null){
                        this.event.setProbability(100);
                    }else{
                        this.event.setProbability(Integer.valueOf(attributes.getValue(PROBABILITY)));
                    }
                    if(attributes.getValue(ACTIVE)==null){
                        this.event.setActive(false);
                    }else{
                        this.event.setActive(Boolean.valueOf(attributes.getValue(ACTIVE)));
                    }
                    this.play=true;break;
                case DESCRIPTION: if(play){
                    this.description=true;this.tempdescription="";
                }break;
                default:
            }
        }

    @Override
    public void characters(final char[] ch, final int start, final int length) throws SAXException {
        if(this.description)
            this.tempdescription+=new String(ch,start,length);
    }

    @Override
        public void endElement(final String uri, final String localName, final String qName) throws SAXException {
            switch(qName){
                case DESCRIPTION: if(play){
                    this.event.setDescription(Html.fromHtml(this.tempdescription).toString());
                this.description=false;}break;
                case EVENT: this.eventList.add(this.event);this.play=false;break;
                default:
            }
        }

    /**
     * Gets the list of events.
     * @return the list of events
     */
        public List<Event> getEvents() {
            return this.eventList;
        }

    /**
     * Gets a random event out of the list of events.
     * @return the random event
     */
    public Event getRandomEvent(){
       return this.eventList.get(this.random.nextInt()%this.eventList.size());
    }

}
