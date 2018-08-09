package com.skplanet.nlp.entityExtractor.knowledge;

import com.skplanet.nlp.entityExtractor.type.Entity;
import com.skplanet.nlp.entityExtractor.util.Offset;
import com.skplanet.nlp.trie.Trie;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * <br>Created by Donghun Shin<br>
 * Contact: donghun.shin@sk.com, sindongboy@gmail.com<br>
 * Date: 6/28/13<br>
 */
public class Dictionary {
    private static Logger logger = Logger.getLogger(Dictionary.class.getName());

    // Trie (char-based) dictionary
    private Trie dict = null;

    // category
    private String category = null;

    // item count
    private int num = 0;

    // Maker Dict
    private HashMap<String, String> makerMap = null;

    // Brand Dict
    private HashMap<String, String> brandMap = null;

    // Category Map Dict
    private HashMap<String, String> categoryMap = null;

    /**
     * Constructor
     */
    public Dictionary() {
        this.dict = new Trie();
        this.makerMap = new HashMap<String, String>();
        this.brandMap = new HashMap<String, String>();
        this.categoryMap = new HashMap<String, String>();
    }

    /**
     * Constructor
     * @param category category number
     */
    public Dictionary(String category) {
        this.dict = new Trie();
        this.category = category;
        this.makerMap = new HashMap<String, String>();
        this.brandMap = new HashMap<String, String>();
        this.categoryMap = new HashMap<String, String>();

    }

    public void loadCategoryMap(String path) {
        File categoryMapFile = new File(path);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(categoryMapFile));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().length() == 0 || line.startsWith("#")) {
                    continue;
                }
                String[] fields = line.split("\t");
                if (fields.length != 2) {
                    logger.debug("wrong entry : " + line);
                    continue;
                }
                this.categoryMap.put(fields[0], fields[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadMakerMap(String path) {
        File makerMapFile = new File(path);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(makerMapFile));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().length() == 0 || line.startsWith("#")) {
                    continue;
                }
                String[] fields = line.split("\t");
                if (fields.length != 2) {
                    logger.debug("wrong entry : " + line);
                    continue;
                }
                this.makerMap.put(fields[0], fields[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void loadBrandMap(String path) {
        File brandMapFile = new File(path);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(brandMapFile));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().length() == 0 || line.startsWith("#")) {
                    continue;
                }
                String[] fields = line.split("\t");
                if (fields.length != 2) {
                    logger.debug("wrong entry : " + line);
                    continue;
                }
                this.brandMap.put(fields[0], fields[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the category number
     * @return category number
     */
    public String getCategory() {
        return this.category;
    }

    /**
     * Set the category
     * @param category category number to be set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Get Maker Name
     * @param makerId Category name mapped ID
     * @return maker name
     */
    public String getMakerName(String makerId) {
        return this.makerMap.get(makerId);
    }

    /**
     * Get Brand Name
     * @param brandId Brand Name mapped ID
     * @return brand name
     */
    public String getBrandName(String brandId) {
        return this.brandMap.get(brandId);
    }

    /**
     * Get the number of item.
     * @return item number loaded.
     */
    public int size() {
        return this.num;
    }

    /**
     * Add dictionary item
     * @param key key
     * @param val value
     */
    public void add(String key, String val) {
        this.dict.put(key, val);
    }


    /**
     * Add dictionary item with maker , brand, model name separately
     * @param maker maker id
     * @param brand brand id
     * @param model model name
     * @param sCategory Sub-Category id
     * @param key expression
     */
    public void add(String maker, String brand, String model, String sCategory, String key) {
        String m = getMakerName(maker);
        String b = getBrandName(brand);
        String val = m + "\t" + b + "\t" + model + "\t" + sCategory;
        this.dict.put(key, val);
    }

    /**
     * check if given string is in the dictionary or not.
     * @param key string to be looked up.
     * @return true if exist, otherwise false.
     */
    public boolean contains(String key) {
        return !(this.dict == null || key == null) && key.trim().length() != 0 && !this.dict.contains(key);
    }


    /**
     * Find all matched item with their offset
     * @param key text to be looked up
     * @return array of {@link Offset} if match found.
     */
    public ArrayList<Entity> find(String key) {
        // -----------------------------
        // check input and dictionary
        if (this.dict == null || key == null) {
            return null;
        }
        if (key.trim().length() == 0) {
            return null;
        }

        // result container
        ArrayList<Entity> result = new ArrayList<Entity>();


        // find all matched object and store
        int offset;
        String str;
        for (int i = 0; i < key.length(); i++) {
            if (key.charAt(i) == ' ') {
                continue;
            }
            str = key.substring(i);
            if (i == 0 || key.charAt(i - 1) == ' ' ||
                    key.charAt(i-1) == '\'' ||
                    key.charAt(i-1) == '\"' ||
                    key.charAt(i-1) == '“' ||
                    key.charAt(i-1) == '=' ||
                    key.charAt(i-1) == '-' ||
                    key.charAt(i-1) == '{' ||
                    key.charAt(i-1) == '<' ||
                    key.charAt(i-1) == '(' ||
                    key.charAt(i-1) == '[' ||
                    key.charAt(i-1) == '}' ||
                    key.charAt(i-1) == '>' ||
                    key.charAt(i-1) == ')' ||
                    key.charAt(i-1) == ']'
            ) {

                offset = this.dict.prefixMatch(key.substring(i));
                if (offset > 0) { // partially matched
                    str = key.substring(i, i + offset);
                    String tRes = (String) this.dict.match(key.substring(i, i + offset));
                    if (tRes != null) {
                        Entity e = new Entity(i, i + offset, key.substring(i, i + offset), tRes);
                        result.add(e);
                    }
                    i += offset - 1;
                }
            }
        }

        if (result.size() == 0) {
            return null;
        }
        return result;
    }
}
