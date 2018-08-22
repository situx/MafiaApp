package com.example.LNDWA.cards;

/**
 * Created by timo on 09.03.14.
 */
public interface GameElements {
    /**
     * Synchronizes a game element with another game element.
     * @param elem
     */
    public boolean synchronize(GameElements elem);
    /**
     * Creates a XML String representation of the current class.
     * @return the representation as String
     */
    public String toXML();
}
