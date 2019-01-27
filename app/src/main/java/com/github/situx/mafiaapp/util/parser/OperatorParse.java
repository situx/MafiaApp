package com.github.situx.mafiaapp.util.parser;

import com.github.situx.mafiaapp.cards.Karte;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by timo on 17.04.14.
 */
public class OperatorParse {

    private final List<Karte> deadcards;
    private String operatorstr;

    private final Map<String, Karte> cardlist;

    private final Map<String,Integer> groupmap;

    public OperatorParse(Map<String, Karte> card, Map<String,Integer> groupmap, List<Karte> deadcards){
         this.cardlist=card;
        this.groupmap=groupmap;
        this.deadcards=deadcards;
    }

    public Integer evaluateOperator(String operatorstr){
        String cardid,groupid;
        if(operatorstr.matches("-?\\d+")){
            return Integer.valueOf(operatorstr);
        }
        if(operatorstr.contains("!c#(")){
            cardid=operatorstr.substring(3,operatorstr.length()-1);
            return Collections.frequency(this.deadcards,this.cardlist.get(cardid));
        }else if(operatorstr.contains("!g#(")){
            groupid=operatorstr.substring(3,operatorstr.length()-1);
            Integer result=0;
             for(Karte card:this.deadcards){
                 if(card.getGroup().getGroupIdentifier().equals(groupid)){
                    result++;
                 }
             }
            return result;
        }else if(operatorstr.contains("g#(")){
            groupid=operatorstr.substring(3,operatorstr.length()-1);
            return this.groupmap.get(groupid);
        }else if(operatorstr.contains("c#(")){
            cardid=operatorstr.substring(3,operatorstr.length()-1);
            return this.cardlist.get(cardid).getCurrentamount();
        }
        return -1;
    }
}
