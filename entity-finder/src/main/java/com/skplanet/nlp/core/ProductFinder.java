package com.skplanet.nlp.core;

import com.skplanet.nlp.PROP;
import com.skplanet.nlp.config.Configuration;
import com.skplanet.nlp.data.Entity;
import com.skplanet.nlp.data.ProductEntity;
import com.skplanet.nlp.dict.Dictionary;
import com.skplanet.nlp.trie.Trie;
import com.skplanet.nlp.trie.TrieData;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity Finder specialized for Product
 *
 * @author Donghun Shin, donghun.shin@sk.com
 */
public final class ProductFinder extends AbstractFinder {
    private static final Logger LOGGER = Logger.getLogger(ProductFinder.class.getName());

    // symbols
    private static final String SYMBOLS = "[^a-zA-Z0-9가-힣 ]";
    // finder name
    public static final String NAME = "product";

    /**
     * constructor
     */
    public ProductFinder(Dictionary dictionary) {
        super(dictionary);
        Configuration config = Configuration.getInstance();
        try {
            config.loadProperties(PROP.MAIN_PROP_NAME);
        } catch (IOException e) {
            LOGGER.error("failed to load main properties: " + PROP.MAIN_PROP_NAME, e);
        }
    }

    /**
     * Find list of {@link com.skplanet.nlp.data.Entity} from the given input
     *
     * @param orgInput input sentence
     * @return list of {@link com.skplanet.nlp.data.Entity}
     */
    @Override
    public List<Entity> find(String orgInput, boolean isTitle) {
        String input = orgInput.toLowerCase().replaceAll(SYMBOLS, " ");

        if (input.trim().length() == 0) {
            LOGGER.warn("no input string");
            return null;
        }

        // result container
        ArrayList<Entity> result = new ArrayList<Entity>();

        Trie dict = this.dictionary.getDictionary();

        // find all matched object and store
        for (int i = 0; i < input.length(); i++) {

            i = i + findStartIndex(input, i);
            boolean found = false;
            // longest match
            TrieData res = dict.longestMatch(input.substring(i));
            if (res.getValue() != null) {
                ProductEntity e = new ProductEntity();
                e.setStartIndex(i);
                e.setEndIndex(i + res.getLength());
                String value = (String) res.getValue();
                String[] fields = value.split("\t");
                e.setName(res.getKey());
                e.setModel(fields[0]);
                e.setMaker(fields[1]);
                e.setBrand(fields[2]);
                found = true;

                result.add(e);
            }

            // prefix match
            /*
            res = dict.prefixMatch(input.substring(i));
            if (res.getValue() != null) {
                ProductEntity e = new ProductEntity();
                e.setStartIndex(i);
                e.setEndIndex(i + res.getLength());
                String value = (String) res.getValue();
                String[] fields = value.split("\t");
                e.setName(res.getKey());
                e.setModel(fields[0]);
                e.setMaker(fields[1]);
                e.setBrand(fields[2]);
                e.setIsTitle(isTitle);

                result.add(e);
            }
            */

            // relaxed match
            if (!found) {
                res = dict.matchRelaxed(input.substring(i));
                if (res.getValue() != null) {
                    ProductEntity e = new ProductEntity();
                    e.setStartIndex(i);
                    e.setEndIndex(i + res.getLength());
                    String value = (String) res.getValue();
                    String[] fields = value.split("\t");
                    e.setName(res.getKey());
                    e.setModel(fields[0]);
                    e.setMaker(fields[1]);
                    e.setBrand(fields[2]);

                    result.add(e);
                }
            }

        }

        if (result.size() == 0) {
            return null;
        }
        return result;
    }

    /**
     * Find start index for TRIE search
     *
     * @param input input string
     * @return start index
     */
    private int findStartIndex(String input, int index) {

        String newInput = input.substring(index).replaceAll(SYMBOLS, " ");

        int i = index;
        if(index > 0) {
            for (i = 0; i < newInput.length(); i++) {
                if (newInput.charAt(i) != ' ') {
                    continue;
                }
                break;
            }
        }
        for (; i < newInput.length(); i++) {
            if (newInput.charAt(i) == ' ') {
                continue;
            }
            break;
        }
        return i;
    }
}
