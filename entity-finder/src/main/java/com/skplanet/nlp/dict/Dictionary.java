package com.skplanet.nlp.dict;


import com.skplanet.nlp.trie.Trie;

/**
 * @author Donghun Shin, donghun.shin@sk.com
 * @date 10/6/14.
 */
public interface Dictionary {

    /**
     * Load Entity Dictionary
     * @param category category number
     * @return entity dictionary loaded {@link com.skplanet.nlp.trie.Trie}
     */
    public void load(String category);

    /**
     * Get Dictionary
     * @return dictionary {@link com.skplanet.nlp.trie.Trie}
     */
    public Trie getDictionary();

    /**
     * Get Category id
     * @return category id
     */
    public String getCategoryId();
}
