package com.skplanet.nlp.entityExtractor.analyzer;

import com.skplanet.nlp.entityExtractor.config.EConfiguration;
import com.skplanet.nlp.entityExtractor.knowledge.Dictionary;
import com.skplanet.nlp.entityExtractor.type.Entity;
import com.skplanet.nlp.entityExtractor.util.NLP;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Find Objects (Entities) for a given text.
 * it works either document and sentence level
 *
 * <br>Each Extractor only works for single category</br>
 *
 * <br>Created by Donghun Shin<br>
 * Contact: donghun.shin@sk.com, sindongboy@gmail.com<br>
 * Date: 7/1/13<br>
 */
public class EntityExtractor {
    private static Logger logger = Logger.getLogger(EntityExtractor.class.getName());

    // define analysis type.
    public static final int DOC_LEVEL_ANAL = 0;
    public static final int SEN_LEVEL_ANAL = 1; // by default

    // entity dictionary
    private Dictionary eDict = null;

    // entity dictionary path.
    private String dicPath = null;

    // entity dictionary suffix
    private String dicSuffix = null;

    // analysis type
    private int anal_type = -1;

    // category
    private String category = null;

    // NLP
    private NLP nlp = null;

    /**
     * <br>Constructor w/o Category</br>
     */
    @Deprecated
    public EntityExtractor() {
        this.eDict = new Dictionary();
        // category isn't specified.
    }

    /**
     * <br>Constructor w/ Category</br>
     * @param category category number
     */
    public EntityExtractor(String category) {
        this.eDict = new Dictionary();
        this.eDict.setCategory(category);
        this.category = category;
    }

    /**
     * <br>Init. the extractor</br>
     * <br>1. Set Dictionary path</br>
     * <br>2. Set Analysis type</br>
     * @param eConfig configuration for the extractor
     */
    public void init(EConfiguration eConfig) {
        this.dicPath = eConfig.getDicPath();
        this.anal_type = eConfig.getAnalyType();
        if (anal_type == -1) {
            // can't figure out the analysis type.
            throw new NullPointerException();
        }
        this.dicSuffix = eConfig.getDictionarySuffix();
    }

    /**
     * Load NLP (nlp-indexterm)
     *
     */
    public void loadNLP() {
        this.nlp = NLP.getInstance();
    }

    /**
     * Load Entity Dictionary
     * @return true for success, otherwise false
     */
    public boolean loadDictionary(EConfiguration config) {

        // dicPath must be set before loading.
        if (this.dicPath == null) {
            logger.error("dictionary path required");
            return false;
        }

        // load category, maker, brand dic
        eDict.loadCategoryMap(config.getCategoryMapPath());
        eDict.loadMakerMap(config.getMakerMapPath());
        eDict.loadBrandMap(config.getBrandMapPath());

        // set dictionary file.
        File dicFile = new File(dicPath + "/" + category + dicSuffix);

        char[] cbuf = new char[(int) dicFile.length()];
        String sbuf;
        BufferedReader reader;
        try {

            //-------------------
            // read file
            reader = new BufferedReader(new FileReader(dicFile));
            while (!reader.ready()) {  }
            reader.read(cbuf);
            reader.close();
            sbuf = String.valueOf(cbuf);
            //-------------------

            if (sbuf.trim().length() == 0) {
                return false;
            }
            // TODO: dic header 를 없앨것이다.
            sbuf = sbuf.substring(sbuf.indexOf("\n\n") + 2).trim();

            int sidx = 0;
            int eidx = 0;


            int entryCount = 0;
            while (eidx > -1) {
                entryCount++;
                if (entryCount % 100 == 0) {
                    logger.info("dictionary loading: " + entryCount);
                }

                eidx = nextItem(sbuf, sidx);

                String entry;

                if (eidx < 0) {
                    entry = sbuf.substring(sidx);
                } else {
                    entry = sbuf.substring(sidx, eidx);
                }
                String[] fields = entry.split("\t");

                if(fields.length != 5) {
                    logger.error("wrong entry: " + entry);
                    sidx = eidx + 1;
                    continue;
                }

                // 0 : exp
                // 1 : model
                // 2 : company
                // 3 : brand
                // 4 : sub-category
                // 0 - key, 1~4 - value
                // TODO: 좀 더 일반적인 폼에 유연하게 작동하게 만들고 싶다.
                //this.eDict.add(fields[0].toLowerCase(), fields[1] + "\t" +  fields[2] + "\t" + fields[3] + "\t" + fields[4]);
                this.eDict.add(fields[2].trim(), fields[3].trim(), fields[1].trim(), fields[4].trim(), fields[0].toLowerCase());
                sidx = eidx + 1;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



        return true;
    }

    /**
     * Find entities from given text
     * @param text target sentence
     * @param analType analysis type
     * @return array of {@link Entity} if exist otherwise null
     */
    public List<Entity> find(String text, int analType) {
        if (analType == DOC_LEVEL_ANAL) {
            return findFromDocument(text);
        } else if (analType == SEN_LEVEL_ANAL) {
            return findFromSentence(text);
        } else {
            logger.error("Wrong Anal Type : " + analType);
            return null;
        }
    }

    /**
     * Find entities from given text, sentence level analysis
     * @param text target sentence
     * @return array of {@link Entity} if exist otherwise null
     */
    private ArrayList<Entity> findFromSentence(String text) {
        logger.info("input : " + text);

        ArrayList<Entity> result = new ArrayList<Entity>();

        // get all matched items and their offset information
        ArrayList<Entity> candidates;
        candidates = this.eDict.find(text.toLowerCase());
        if(candidates == null) {
            logger.info("no candidates");
            return result;
        }
        //result = candidates;

        // -----------------------------------------------------------
        // NER 정보를 수집한다.
        List<String> ners = nlp.getNER(text);
        // candidates  에서 NER결과들로 filtering 한다.
        for(Entity e : candidates) {
            if(ners.size() == 0) {
                result.add(e);
                continue;
            }
            for(String s : ners) {
                logger.debug("compare: " + e.getName() + " - " + s);
                if(equalsEntity(e.getName(), s)) {
                    logger.info("matched: " + e.getName() + " - " + s);
                    result.add(e);
                }
            }
        }
        // -----------------------------------------------------------

        return result;
    }

    /**
     * Check if both entity found is same entity or not
     *
     * @param org first entity candidates
     * @param dst seconde entity candidates
     * @return true if both entities are the same entity otherwise return false
     */
    private static boolean equalsEntity(String org, String dst) {
        // 전처리
        // remove symbols, space, to lower case
        org = org.toLowerCase().replaceAll("[\\s!-.,\\(\\)\\[\\]\\{\\}:;<>~]+", "");
        dst = dst.toLowerCase().replaceAll("[\\s!-.,\\(\\)\\[\\]\\{\\}:;<>~]+", "");
        return org.equals(dst) || org.contains(dst) || dst.contains(org);

    }

    /** * Find entities from given text, document level analysis
     * @param text target sentence
     * @return array of {@link Entity} if exist otherwise null
     */
    private List<Entity> findFromDocument(String text) {
        List<Entity> result = new ArrayList<Entity>();

        //get sentences
        String[] sentences = this.nlp.getSentences(text);
        List<Entity> candidates = null;
        int sentseq = 0;
        for (String sent : sentences) {
            candidates = this.eDict.find(sent);
            if (candidates != null) {
                for (Entity e : candidates) {
                    e.setSentseq(sentseq);
                    result.add(e);
                }
            }
            sentseq++;
        }
        return result;
    }

    /**
     * get the end index of next dictionary item
     * @param sbuf dictionary text
     * @param sidx start index of next dictionary item
     * @return end index of next dictionary item
     */
    private static int nextItem(String sbuf, int sidx) {
        int eidx = sbuf.indexOf("\n", sidx);
        return eidx;
    }

    //---------------------------//
    // Getter & Setter
    //---------------------------//

    /**
     * Set the category for the extractor
     * @param category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Get the category for the extractor
     * @return category for the extractor
     */
    public String getCategory() {
        return this.category;
    }



}
