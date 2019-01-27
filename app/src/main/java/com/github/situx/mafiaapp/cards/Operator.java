package com.github.situx.mafiaapp.cards;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.List;

/**
 * Created by timo on 03.05.14.
 */
public class Operator implements Parcelable{
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Operator createFromParcel(Parcel in) {
            return new Operator(in);
        }

        public Operator[] newArray(int size) {
            return new Operator[size];
        }
    };

    private Boolean hascheck=false;
    private Boolean group;

    private Boolean not;

    private Boolean count;

    private Boolean direction;

    private Boolean ability;

    private Boolean usedonabb;

    private Boolean onlycheck;

    private Boolean valueOp;

    public Operator(String operatorstr){
        this.hascheck=true;
        count=operatorstr.contains("#(");
        group=operatorstr.substring(0,operatorstr.indexOf('(')).contains("g");
        ability=operatorstr.substring(0,operatorstr.indexOf('(')).contains("a");
        usedonabb=operatorstr.substring(0,operatorstr.indexOf('(')).contains("u");
        direction=operatorstr.substring(0,operatorstr.indexOf('(')).contains("d");
        onlycheck=operatorstr.contains("?");
        not=operatorstr.substring(0,operatorstr.indexOf('(')).contains("!");
        valueOp=group || count || not;
        this.value=operatorstr.substring(operatorstr.indexOf('(')+1,operatorstr.lastIndexOf(')'));
        Log.e("New Operator:", this.toString());
    }

    public Operator(){
        this.hascheck=false;
        this.count=false;
        this.group=false;
        this.value="";
        this.not=false;
        this.direction=false;
        this.onlycheck=false;
        this.ability=false;
        this.valueOp=false;
        this.usedonabb=false;
    }

    public Operator(Parcel in){
        this.count= in.readByte() != 0;
        this.group = in.readByte() != 0;
        this.hascheck = in.readByte() != 0;
        this.not = in.readByte() != 0;
        this.valueOp = in.readByte() != 0;
        this.value=in.readString();
        this.ability= in.readByte() != 0;
        this.direction=in.readByte() != 0;
        this.usedonabb=in.readByte() != 0;
        this.onlycheck=in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Boolean getOnlyCheck() {
        return this.onlycheck;
    }

    public Boolean hasCheck() {
        return this.hascheck;
    }

    @Override
    public void writeToParcel(final Parcel parcel, final int i) {
        parcel.writeByte((byte) (this.count ? 1 : 0));
        parcel.writeByte((byte) (this.group ? 1 : 0));
        parcel.writeByte((byte) (this.hascheck ? 1 : 0));
        parcel.writeByte((byte) (this.not ? 1 : 0));
        parcel.writeByte((byte) (this.valueOp ? 1 : 0));
         parcel.writeString(this.value);
        parcel.writeByte((byte) (this.ability ? 1 : 0));
        parcel.writeByte((byte) (this.direction ? 1 : 0));
        parcel.writeByte((byte) (this.usedonabb ? 1 : 0));
        parcel.writeByte((byte) (this.onlycheck ? 1 : 0));
    }

    public Integer evaluate(Karte comparecard, List<Karte> cards, List<Karte> deadcards){
        Integer result=0;
        Log.e("Evaluate Operator",this.value);
        Log.e("Count",this.count.toString());
        Log.e("Group",this.group.toString());
        Log.e("HasCheck",this.hascheck.toString());
        Log.e("Ability",this.ability.toString());
        Log.e("UsedOnAbb",this.ability.toString());
        Log.e("Direction",this.direction.toString());
        Log.e("OnlyCheck",this.onlycheck.toString());
        Log.e("Not",this.not.toString());
        Log.e("ValueOp",this.valueOp.toString());
        Log.e("Comparecard",comparecard.getCardid().toString());
        if(count){
            if(group){
                if(not){
                    for(Karte card:cards){
                        if(!card.getGroup().getGroupId().equals(this.value)){
                            result+=card.getCurrentamount();
                        }
                    }
                }else{
                    for(Karte card:cards){
                        if(card.getGroup().getGroupId().equals(this.value)){
                            result+=card.getCurrentamount();
                        }
                    }
                }
            }else{
                if(not){
                    for(Karte card:cards){
                        if(!card.getCardid().equals(this.value)){
                            result+=card.getCurrentamount();
                        }
                    }
                }else{
                    for(Karte card:cards){
                        if(card.getCardid().equals(this.value)){
                            result+=card.getCurrentamount();
                            break;
                        }
                    }
                }
            }

        }else{
            if(group) {
                if (not) {
                    result = !comparecard.getGroup().getGroupId().equals(this.value) ? 1 : 0;
                } else {
                    result = comparecard.getGroup().getGroupId().equals(this.value) ? 1 : 0;
                }
            }else if(!group && ability && !usedonabb){
                 result=comparecard.getUuidToAbility().containsKey(this.value)?1:0;
            }else if(!group && !ability && usedonabb){
                result=comparecard.getUuidToAbility().containsKey(this.value)?1:0;
            }else{
               if(not){
                           if(comparecard.getCardid().equals(this.value)){
                           result=comparecard.isdead()?1:0;
                   }

               }else{
                       if(comparecard.getCardid().equals(this.value)){
                           result=comparecard.isdead()?0:1;
                       }
               }
            }
        }
        Log.e("Result",result.toString());
        return result;
    }

    public Boolean getValueOp() {
        return valueOp;
    }

    public void setValueOp(final Boolean valueOp) {
        this.valueOp = valueOp;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    private String value;

    public Boolean getNot() {
        return not;
    }

    public void setNot(final Boolean not) {
        this.not = not;
    }

    public Boolean getGroup() {
        return group;
    }

    public void setGroup(final Boolean group) {
        this.group = group;
    }

    public Boolean getCount() {
        return count;
    }

    public void setCount(final Boolean count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return this.toXML();
    }

    public String toXML(){
        String result="";
        if(!hascheck){
            return result;
        }
        if(onlycheck){
            result+="?";
        }
        if(not){
            result+="!";
        }
        if(ability){
            result+="a";
        }
        if(direction){
            result+="d";
        }
        if(usedonabb){
            result+="u";
        }
        if(group){
            result+="g";
        }else{
            result+="c";
        }
        if(count){
            result+="#";
        }
        result+="("+this.value+")";
        return result;
    }
}
