package com.github.situx.mafiaapp.views;

/**
 * Created by timo on 30.04.14.
 */
public enum DialogEnum {


    ABOUT("About"),CHOOSECHARS("ChooseChars"),CHOOSEPLAYER("ChoosePlayer"),SHOWDEADCHARS("ShowDeadChars")
    ,CHOOSEDEADCHAR("ChooseDeadChar"),CHOOSEEXTRACARDS("ChooseExtraCards"), GAMELOG("GameLog"), SAVEPRESET("SavePreset");

    private String name;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    private DialogEnum(String name){
        this.name=name;

    }

    @Override
    public String toString() {
        return super.toString();
    }
}
