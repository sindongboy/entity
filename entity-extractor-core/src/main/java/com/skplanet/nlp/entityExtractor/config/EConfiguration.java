package com.skplanet.nlp.entityExtractor.config;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.skplanet.nlp.config.Configuration;
import com.skplanet.nlp.entityExtractor.analyzer.EntityExtractor;

/**
 * Configuration for Entity Extractor<br>
 *
 * <br>Created by Donghun Shin<br>
 * Contact: donghun.shin@sk.com, sindongboy@gmail.com<br>
 * Date: 6/28/13<br>
 */
public class EConfiguration {
	private static Logger logger = Logger.getLogger(EConfiguration.class.getName());

    private static final String CONFIG_FILE_NAME = "entityExtractor.properties";


    //--------------------------------------
    // FIELDS
    private String DIC_PATH = "DIC_PATH";
    private String ANAL_TYPE = "ANAL_TYPE";
    private String DIC_SUFFIX = "DIC_SUFFIX";
    private String CATEGORY_MAP = "CATEGORY_MAP";
    private String MAKER_MAP = "MAKER_MAP";
    private String BRAND_MAP = "BRAND_MAP";
    private String NLP = "NLP";

    //--------------------------------------

    private Configuration config = null;

    /**
     * Constructor
     * Set & Load Configuration file
     */
    public EConfiguration() {
        config = Configuration.getInstance();
        try {
            config.loadProperties(CONFIG_FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get Object Extractor Dictionary Path
     * @return the path where all the object dictionaries are located.
     */
    public String getDicPath() {
        if (this.config != null) {
            return this.config.getProperties(CONFIG_FILE_NAME).getProperty(DIC_PATH);
        } else {
            throw new NullPointerException();
        }
    }

    /**
     * Get Category Map Dict Path
     * @return category map (full) path
     */
    public String getCategoryMapPath() {
        if (this.config != null) {
            return this.config.getProperties(CONFIG_FILE_NAME).getProperty(CATEGORY_MAP);
        } else {
            throw new NullPointerException();
        }
    }

    /**
     * Get Maker Map Dict Path
     * @return maker map (full) path
     */
    public String getMakerMapPath() {
        if (this.config != null) {
            return this.config.getProperties(CONFIG_FILE_NAME).getProperty(MAKER_MAP);
        } else {
            throw new NullPointerException();
        }
    }

    /**
     * Get Brand Map Dict Path
     * @return brand map (full) path
     */
    public String getBrandMapPath() {
        if (this.config != null) {
            return this.config.getProperties(CONFIG_FILE_NAME).getProperty(BRAND_MAP);
        } else {
            throw new NullPointerException();
        }
    }


    /**
     * Get the Analysis type, either document level or sentence level
     * @return 0 for document level, 1 for sentence level
     */
    public int getAnalyType() {
        if (this.config != null) {
            String type = this.config.getProperties(CONFIG_FILE_NAME).getProperty(ANAL_TYPE);
            if (type.equals("SEN")) { // sentence level
                return EntityExtractor.SEN_LEVEL_ANAL;
            } else if (type.equals("DOC")) { //document level
                return EntityExtractor.DOC_LEVEL_ANAL;
            } else { // wrong.
                logger.error("wrong analysis type: " + type);
                return -1;
            }
        } else {
            throw new NullPointerException();
        }
    }

    /**
     * Get the suffix for dictionary file
     * @return suffix for dictionary file
     */
    public String getDictionarySuffix() {
        if (this.config != null) {
            return this.config.getProperties(CONFIG_FILE_NAME).getProperty(DIC_SUFFIX);
        } else {
            throw new NullPointerException();
        }
    }

    public boolean useNLP() {
        if (this.config != null) {
            String type = this.config.getProperties(CONFIG_FILE_NAME).getProperty(DIC_SUFFIX);
            if (type.equals("true")) {
                return true;
            } else {
                return false;
            }
        } else {
            throw new NullPointerException();
        }
    }

}
