package com.skplanet.nlp.data;

/**
 * @author Donghun Shin, donghun.shin@sk.com
 * @date 10/6/14.
 */
public interface Entity {

    /**
     * get entity name
     * @return entity name
     */
    public String getName();

    /**
     * set entity name
     * @param name entity name
     */
    public void setName(String name);

    /**
     * set start index
     * @param startIndex start index
     */
    public void setStartIndex(int startIndex);

    /**
     * get start index
     * @return start index
     */
    public int getStartIndex();

    /**
     * set end index
     * @param endIndex end index
     */
    public void setEndIndex(int endIndex);

    /**
     * get end index
     * @return end index
     */
    public int getEndIndex();

    /**
     * check if the entity extracted from title
     * @return true if the entity extracted from title
     */
    public boolean isTitle();

}
