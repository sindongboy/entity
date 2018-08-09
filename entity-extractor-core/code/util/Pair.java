package com.skplanet.nlp.entityExtractor.util;

import org.apache.log4j.Logger;

/**
 * Simple Pair Class implementation
 *
 * Created by Donghun Shin
 * Contact: donghun.shin@sk.com, sindongboy@gmail.com
 * Date: 12/20/12
 */
public class Pair {
	private static Logger logger = Logger.getLogger(Pair.class.getName());
    private String first;
    private String second;

    public Pair(String first, String second) {
        super();
        this.first = first;
        this.second = second;
    }

    /**
     *  new hash code for pair class
     * @return new hashCode
     */
    public int hashCode() {

        int hashFirst = first != null ? first.hashCode() : 0;
        int hashSecond = second != null ? second.hashCode() : 0;

        // new hashCode created.
        return (hashFirst + hashSecond) * hashSecond + hashFirst;
    }

    /**
     * Compare classes
     * @param other target class
     * @return true if both classes are same
     */
    public boolean equals(Object other) {
        if (other instanceof Pair)
        {
            Pair otherPair = (Pair) other;
            return
                    ((  this.first == otherPair.getFirst() || // 첫번째 element
                            ( this.first != null && otherPair.getFirst() != null  &&
                                    this.first.equals(otherPair.getFirst()))) // string compare
                     &&

                     (	this.second == otherPair.getSecond() || // 두번째 element
                            ( this.second != null && otherPair.getSecond() != null &&
                                    this.second.equals(otherPair.getSecond())))  // string compare
                    );
        }
        return false;
    }

    /**
     * get first item of {@link Pair}
     * @return first item of the class
     */
    public String getFirst() {
        return first;
    }

    /**
     * set first item of {@link Pair}
     * @param first
     */
    public void setFirst(String first) {
        this.first = first;
    }

    /**
     * get second item of {@link Pair}
     * @return second item of the class
     */
    public String getSecond() {
        return second;
    }

    /**
     * set second item of {@link Pair}
     * @param second
     */
    public void setSecond(String second) {
        this.second = second;
    }
}

