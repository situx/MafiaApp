package com.github.situx.mafiaapp.util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created with IntelliJ IDEA.
 * Generic tuple class representing a tuple of two comparable types.
 */
public class Tuple<T extends Comparable<T>,T2 extends Comparable<T2>> implements Comparable<Tuple<T,T2>>,Parcelable {
    /**Tuple value of type one.*/
    private T one;
    /**Tuple value of type two.*/
    private T2 two;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Tuple createFromParcel(Parcel in) {
            return new Tuple(in);
        }

        public Tuple[] newArray(int size) {
            return new Tuple[size];
        }
    };

    /**
     * Constructor for this class.
     * @param one value one as type one
     * @param two value two as type two
     */
    public Tuple(T one, T2 two){
        this.one=one;
        this.two=two;
    }

    /**
     * Constructor for this class.
     */
    public Tuple(Parcel in){
        /*if(this.one instanceof String){

        }*/
        this.one=(T)(Object)in.readString();
        this.two=(T2)(Object)in.readString();
    }

    @Override
    public int compareTo(final Tuple<T, T2> o) {

        int cmp = this.one.compareTo(o.one);
        if (cmp == 0)
            cmp = this.two.compareTo(o.two);
        return cmp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel parcel, final int i) {
         parcel.writeString(this.one.toString());
         parcel.writeString(this.two.toString());
    }

    /**
     * Gets the first value of type one.
     * @return the value
     */
    public T getOne(){
        return one;
    }

    /**
     * Gets the second value of type two.
     * @return the value
     */
    public T2 getTwo(){
        return two;
    }

    /**
     * Sets the first value of type one.
     * @param one the value to set
     */
    public void setOne(T one){ this.one=one;}

    /**
     * Sets the second value of type two.
     * @param two the value to set
     */
    public void setTwo(T2 two){this.two=two;}

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Tuple && this.one.equals(((Tuple) obj).one) && this.two.equals(((Tuple) obj).two);
    }


    @Override
    public String toString() {
        return "["+this.one+","+this.two+"]";
    }
}
