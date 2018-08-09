package com.skplanet.nlp.data;

/**
 * @author Donghun Shin, donghun.shin@sk.com
 * @date 10/6/14.
 */
public abstract class AbstractEntity implements Entity {

    protected boolean isTitle = false;

    /**
     * is Entity from title?
     * @param isTitle true if the entity is from title, otherwise false;
     */
    public abstract void setIsTitle(boolean isTitle);

    /**
     * check if the entity extracted from title
     *
     * @return true if the entity extracted from title
     */
    @Override
    public abstract boolean isTitle();
}
