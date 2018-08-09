package com.skplanet.nlp.dict;

import com.skplanet.nlp.config.Configuration;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


/**
 * Product Entity Dictionary
 * @author Donghun Shin, donghun.shin@sk.com
 * @date 10/6/14.
 */
public final class ProductDictionary extends AbstractDictionary {
    // logger
    private static final Logger LOGGER = Logger.getLogger(ProductDictionary.class.getName());

    private static ProductDictionary instance = null;

    /**
     * constructor
     */
    private ProductDictionary() {
        super();
    }

    /**
     * Get instance of {@link com.skplanet.nlp.dict.ProductDictionary}
     * @return an instance of NLP class
     */
    public static ProductDictionary getInstance() {
        if (instance == null) {
            synchronized (ProductDictionary.class) {
                instance = new ProductDictionary();
            }
        }
        return instance;
    }

    /**
     * Load Entity Dictionary
     *
     * @param dictionary dictionary name
     * @return entity dictionary loaded {@link com.skplanet.nlp.trie.Trie}
     */
    @Override
    public void load(String dictionary) {
        long btime, etime;
        // get dictionary file
        Configuration config = Configuration.getInstance();
        String dictName = config.getResource(dictionary).getFile();
        File dictFile = new File(dictName);
        BufferedReader reader;
        String line;

        this.id = dictionary.substring(0, dictionary.lastIndexOf("."));
        LOGGER.info("entity dictionary loading ....");
        btime = System.currentTimeMillis();
        try {
            reader = new BufferedReader(new FileReader(dictFile));
            while ((line = reader.readLine()) != null) {
                if (line.trim().length() == 0 || line.startsWith("#")) {
                    continue;
                }
                String[] fields = line.split("\t");
                if (fields.length != 5) {
                    LOGGER.warn("wrong format : " + line);
                    continue;
                }

                // 0 : expression
                // 1 : model
                // 2 : maker
                // 3 : brand
                // 4 : category path
                // 0 -> key, 1~4 -> value
                this.dictionary.put(fields[0].trim().toLowerCase(),
                        fields[1] + "\t"
                        + fields[2] + "\t"
                        + fields[3] + "\t"
                        + fields[4]
                );
            }
            reader.close();

        } catch (IOException e) {
            LOGGER.error("failed to read entity dictionary: " + dictionary, e);
        }
        etime = System.currentTimeMillis();
        LOGGER.info("entity dictionary (" + dictionary + ") loading done in " + (etime - btime) + " msec.");
    }

    /**
     * Get Category id
     *
     * @return category id
     */
    @Override
    public String getCategoryId() {
        return this.id;
    }
}
