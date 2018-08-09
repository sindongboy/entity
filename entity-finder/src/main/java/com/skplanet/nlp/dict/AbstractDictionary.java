package com.skplanet.nlp.dict;

import com.skplanet.nlp.trie.Trie;


/**
 * Abstract Dictionary class
 * @author Donghun Shin, donghun.shin@sk.com
 * @date 10/6/14.
 */
public abstract class AbstractDictionary implements Dictionary {

    // category id
    protected String id = null;
    // dictionary
    protected Trie dictionary = null;

    /**
     * Sole constructor called by sub-classes
     */
    protected AbstractDictionary() {
        dictionary = new Trie();
    }

    /**
     * Load Entity Dictionary
     *
     * @param category category number
     * @return entity dictionary loaded {@link com.skplanet.nlp.trie.Trie}
     */
    @Override
    public abstract void load(String category);

    /**
     * Get Dictionary
     *
     * @return dictionary {@link com.skplanet.nlp.trie.Trie}
     */
    @Override
    public Trie getDictionary() {
        return this.dictionary;
    }

}
