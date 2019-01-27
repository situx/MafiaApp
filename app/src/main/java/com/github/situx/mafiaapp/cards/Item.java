package com.github.situx.mafiaapp.cards;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by timo on 18.01.14.
 */
public class Item implements Parcelable{

    private String description;

    private Integer id;

    private String image;

    private String name;

    private Integer probability;

    private Integer round;

    public Integer getRound() {
        return round;
    }

    public void setRound(final Integer round) {
        this.round = round;
    }

    public Integer getProbability() {
        return probability;
    }

    public Boolean imgexists() {
        return this.image != null;
    }

    public void setProbability(final Integer probability) {
        this.probability = probability;
    }

    public Integer getValid() {
        return valid;
    }

    public void setValid(final Integer valid) {
        this.valid = valid;
    }

    private Integer valid;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel parcel, final int i) {
       parcel.writeString(description);
        parcel.writeInt(id);
        parcel.writeString(image);
        parcel.writeString(name);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(final String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Item() {

    }

    public Item(Parcel in) {
        this.description=in.readString();
        this.id=in.readInt();
        this.image=in.readString();
        this.name=in.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
