package com.skplanet.nlp.core;

import com.skplanet.nlp.data.Entity;

import java.util.List;

/**
 * Finder Interface
 * @author Donghun Shin, donghun.shin@sk.com
 * @date 10/6/14.
 */
public interface Finder {

    /**
     * Find list of {@link Entity} from the given input
     * @param input input sentence
     * @return list of {@link Entity}
     */
    public List<Entity> find(String input, boolean isTitle);

}
