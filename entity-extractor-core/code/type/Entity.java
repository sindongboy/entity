package com.skplanet.nlp.entityExtractor.type;

import org.apache.log4j.Logger;


/**
 * <br>Created by Donghun Shin<br>
 * Contact: donghun.shin@sk.com, sindongboy@gmail.com<br>
 * Date: 7/1/13<br>
 */
public class Entity implements Comparable<Entity>{

	private static Logger logger = Logger.getLogger(Entity.class.getName());

    // offset
    private int sIdx = 0;
    private int eIdx = 0;

    // entity info
    private String name = null;
    private String subInfo = null;
    private int sentseq = 0;


    /**
     * Constructor
     * @param sidx start index
     * @param eidx endi index
     * @param name full name of the entity found.
     * @param subInfo any sub-information related with the entity
     */
    public Entity(int sidx, int eidx, String name, String subInfo) {
        this.sIdx = sidx;
        this.eIdx = eidx;
        this.name = name;
        this.subInfo = subInfo;
    }

    /**
     * Constructor
     * @param sidx start index
     * @param eidx end index
     * @param name full name of the entity found.
     */
    public Entity(int sidx, int eidx, String name) {
        this.sIdx = sidx;
        this.eIdx = eidx;
        this.name = name;
    }

    /**
     * Constructor
     * @param name full name of the entity found.
     */
    public Entity(String name) {
        this.name = name;
    }

    //-------------------//
    // Getter & Setter
    //-------------------//

    /**
     * Get Sentence Sequence
     * @return sentence sequence number
     */
    public int getSentseq() {
        return this.sentseq;
    }

    /**
     * Set Sentence Sequence
     * @param seq sentence sequence number to be set
     */
    public void setSentseq(int seq) {
        this.sentseq = seq;
    }

    /**
     * Set Start index
     * @param sidx start index
     */
    public void setStartIndex(int sidx) {
        this.sIdx = sidx;
    }

    /**
     * Get Start index
     * @return start index
     */
    public int getStartIndex() {
        return this.sIdx;
    }

    /**
     * Set End Index
     * @param eidx end index
     */
    public void setEndIndex(int eidx) {
        this.eIdx = eidx;
    }

    /**
     * Get End Index
     * @return end index
     */
    public int getEndIndex() {
        return this.eIdx;
    }

    /**
     * Set the name of the entity.
     * @param name name of the entity
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the name of the entity
     * @return name of the entity
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the sub-information for the entity
     * @param info sub-information for the entity
     */
    public void setInfo(String info) {
        this.subInfo = info;
    }

    /**
     * Get the sub-information for the entity
     * @return sub-information for the entity
     */
    public String getInfo() {
        return this.subInfo;
    }


    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     * <p/>
     * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)
     * <p/>
     * <p>The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     * <p/>
     * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.
     * <p/>
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     * <p/>
     * <p>In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of
     * <i>expression</i> is negative, zero or positive.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     *         is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(Entity o) {
        String src = this.name;
        String dst = o.getName();

        if (src.equals(dst)) {
            return 1;
        }

        return 0;
    }
}
