package com.github.situx.mafiaapp.cards;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.util.Xml;

import com.github.situx.mafiaapp.cards.GameElements;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Set;
import java.util.UUID;

/**
 * Created by timo on 21.03.14.
 */
public class Group implements GameElements, Comparable<Group>, Parcelable {
    /**
     * Icon of this group.
     */
    private String groupIcon;
    /**
     * Unique identifier of this group.
     */
    private String groupId;
    /**
     * Short identifier of this group.
     */
    private String groupIdentifier;
    /**
     * Description of this group.
     */
    private String groupdescription;
    /**
     * The name of this group.
     */
    private String groupname;
    private Set<String> winsTogetherWith;
    /**
     * Indicates if this group can win the game.
     */
    private Boolean winsgame;

    private Boolean secondary;

    /**
     * Constructor for this class.
     *
     * @param groupname
     * @param winsgame
     * @param groupdescription
     * @param groupIcon
     * @param groupIdentifier
     */
    public Group(final String groupname, final Boolean winsgame, final String groupdescription, final String groupIcon, final String groupIdentifier,final Boolean secondary) {
        this.groupname = groupname;
        this.winsgame = winsgame;
        this.groupdescription = groupdescription;
        this.groupIcon = groupIcon;
        this.groupIdentifier = groupIdentifier;
        this.secondary=secondary;
    }

    public Group(){
        this.groupname="";
        this.groupIcon=" ";
        this.groupdescription="";
        this.groupIdentifier="";
        this.winsgame=false;
        this.secondary=false;
        this.groupId= UUID.randomUUID().toString();
    }
    public Group(final String name){
        this.groupname=name;
        this.groupIcon="";
        this.groupdescription=name;
        this.groupIdentifier=name;
        this.winsgame=false;
        this.secondary=false;
        this.groupId= UUID.randomUUID().toString();
    }

    public Group(Parcel in){
        this.winsgame=in.readByte()!=0;
        this.secondary=in.readByte()!=0;
        this.groupname=in.readString();
        this.groupId=in.readString();
        this.groupIdentifier=in.readString();
        this.groupdescription=in.readString();
        this.groupIcon=in.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    public Boolean getSecondary() {
        return secondary;
    }

    public Boolean imgexists() {
        return !" ".equals(this.groupIcon);
    }

    @Override
    public int compareTo(final Group group) {
        return this.groupIdentifier.compareTo(group.groupIdentifier);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getGroupIcon() {
        return groupIcon;
    }

    public void setGroupIcon(final String groupIcon) {
        this.groupIcon = groupIcon;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(final String groupId) {
        this.groupId = groupId;
    }

    public String getGroupIdentifier() {
        return groupIdentifier;
    }

    public void setGroupIdentifier(final String groupIdentifier) {
        this.groupIdentifier = groupIdentifier;
    }

    public String getGroupdescription() {
        return groupdescription;
    }

    public void setGroupdescription(final String groupdescription) {
        this.groupdescription = groupdescription;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(final String groupname) {
        this.groupname = groupname;
    }

    public Set<String> getWinsTogetherWith() {
        return winsTogetherWith;
    }

    public void setSecondary(final Boolean secondary) {
        this.secondary = secondary;
    }

    public void setWinsTogetherWith(final Set<String> winsTogetherWith) {
        this.winsTogetherWith = winsTogetherWith;
    }

    //TODO Define winning conditions for groups? Is this possible in XML?

    public Boolean getWinsgame() {
        return winsgame;
    }

    public void setWinsgame(final Boolean winsgame) {
        this.winsgame = winsgame;
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
            serializer.startTag("", "group");
            serializer.attribute("", "gid", this.groupIdentifier);
            serializer.attribute("", "name", this.groupname);
            serializer.attribute("", "id", this.groupId);
            serializer.attribute("", "icon", this.groupIcon);
            serializer.attribute("", "canwin", this.winsgame.toString());
            serializer.attribute("", "secondary", this.secondary.toString());
            serializer.startTag("", "description");
            serializer.text(this.groupdescription);
            serializer.endTag("", "description");
            serializer.endTag("","group");
            serializer.flush();
            Log.e("GroupOut", writer.toString());
            return writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void writeToParcel(final Parcel parcel, final int i) {
        parcel.writeByte((byte) (this.winsgame ? 1 : 0));
        parcel.writeByte((byte) (this.secondary ? 1 : 0));
        parcel.writeString(this.groupname);
        parcel.writeString(this.groupId);
        parcel.writeString(this.groupIdentifier);
        parcel.writeString(this.groupdescription);
        parcel.writeString(this.groupIcon);
    }

    @Override
    public String toString() {
        return groupIdentifier;
    }

    @Override
    public boolean equals(final Object o) {
        return o instanceof Group && this.groupIdentifier.equals(((Group) o).groupIdentifier);
    }
}
