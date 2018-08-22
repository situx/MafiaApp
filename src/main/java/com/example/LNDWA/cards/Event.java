package com.example.LNDWA.cards;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.util.Xml;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by timo on 17.01.14.
 */
public class Event implements Parcelable, GameElements,Comparable<Event> {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
    /**
     * Title of the event.
     */
    private String title;
    /**
     * Event id.
     */
    private Integer id;
    /**
     * Description of the event.
     */
    private String description;
    /**
     * Probability of the event to occur.
     */
    private Integer probability;
    /**
     * Indicates if the event is active.
     */
    private Boolean active;

    /**
     * Empty constructor for event using default values.
     */
    public Event() {
        this.title = "";
        this.description = "";
        this.probability = 100;
        this.active = false;
    }

    /**
     * Parcel constructor for Event creating it from a received parcel.
     * @param in the received parcel
     */
    public Event(final Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
        this.probability = in.readInt();
        this.active = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Gets the active state of this event.
     * @return the state as Boolean
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * Sets the active state of this event.
     * @param active the active state as Boolean
     */
    public void setActive(final Boolean active) {
        this.active = active;
    }

    /**
     * Gets the description of this event.
     * @return the description as String
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of this event.
     * @param description the description as String
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Gets the id of this event.
     * @return the id as Integer
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the id of this event.
     * @param id the id as Integer
     */
    public void setId(final Integer id) {
        this.id = id;
    }

    /**
     * Gets the probability of this event.
     * @return the probability as Integer
     */
    public Integer getProbability() {
        return probability;
    }

    /**
     * Sets the probability of this event.
     * @param probability the probability as Integer
     */
    public void setProbability(final Integer probability) {
        this.probability = probability;
    }

    /**
     * Gets the title of this event.
     * @return the title as String
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of this event.
     * @param title the title as Stirng
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    @Override
    public boolean synchronize(final GameElements elem) {
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
            serializer.startTag("", "event");
            serializer.attribute("", "active", this.active.toString());
            serializer.attribute("", "id", this.id.toString());
            serializer.attribute("", "probability", this.probability.toString());
            serializer.attribute("", "title", this.title);
            serializer.flush();
            serializer.startTag("", "description");
            serializer.text(this.description);
            serializer.endTag("", "description");
            serializer.endTag("", "event");
            serializer.flush();
            Log.e("Event: ", writer.toString());
            return writer.toString()+"\n";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void writeToParcel(final Parcel parcel, final int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeInt(probability);
        parcel.writeByte((byte) (this.active ? 1 : 0));
    }

    @Override
    public int compareTo(Event o) {
        return this.title.compareTo(o.title);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Event && this.id.equals(((Event) obj).id);
    }
}
