package com.github.situx.mafiaapp.util;

/**
 * Created with IntelliJ IDEA.
 * Generic tuple class representing a tuple of two comparable types.
 */
public class Triple<T extends Comparable<T>,T2 extends Comparable<T2>,T3 extends Comparable<T3>> implements Comparable<Triple<T,T2,T3>> {
    /**Tuple value of type one.*/
    private T one;
    /**Tuple value of type two.*/
    private T2 two;

    private T3 three;

    /**
     * Constructor for this class.
     * @param one value one as type one
     * @param two value two as type two
     */
    public Triple(T one, T2 two,T3 three){
        this.one=one;
        this.two=two;
        this.three=three;
    }

    @Override
    public int compareTo(final Triple<T, T2,T3> o) {
        Triple t=(Triple) o;

        int cmp = this.getOne().compareTo((T) t.getOne());
        if (cmp == 0)
            cmp = this.getTwo().compareTo((T2) t.getTwo());
        if (cmp == 0)
            cmp = this.getThree().compareTo((T3) t.getThree());
        return cmp;
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

    public T3 getThree() {
        return three;
    }

    public void setThree(final T3 three) {
        this.three = three;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Tuple && this.one.equals(((Triple) obj).one) && this.two.equals(((Triple) obj).two) && this.three.equals(((Triple) obj).three);
    }


    @Override
    public String toString() {
        return "["+this.one+","+this.two+","+this.three+"]";
    }
}
