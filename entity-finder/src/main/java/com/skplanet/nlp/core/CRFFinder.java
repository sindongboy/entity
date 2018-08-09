package com.skplanet.nlp.core;

import com.skplanet.nlp.PROP;
import com.skplanet.nlp.config.Configuration;
import com.skplanet.nlp.data.Entity;
import com.skplanet.nlp.data.ProductEntity;
import com.skplanet.nlp.util.NLP;
import com.skplanet.nlp.util.Pair;
import org.apache.log4j.Logger;
import org.chasen.crfpp.Tagger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Conditional Random Field Entity Boundary Finder
 * @author Donghun Shin, donghun.shin@sk.com
 * @date 10/7/14.
 */
public class CRFFinder extends AbstractFinder {
    private static final Logger LOGGER = Logger.getLogger(CRFFinder.class.getName());

    private Tagger tagger = null;

    private NLP nlp = null;

    private String vLevel = null;
    private String nBest = null;
    private String model = null;

    /**
     * sole constructor called by sub-classes
     *
     * @param modelName model name
     */
    public CRFFinder(String modelName) {
        super(null);
        this.nlp = NLP.getInstance();
        Configuration config = Configuration.getInstance();
        try {
            config.loadProperties(PROP.MAIN_PROP_NAME);
        } catch (IOException e) {
            LOGGER.error("failed to load main properties: " + PROP.MAIN_PROP_NAME, e);
        }

        this.vLevel = config.readProperty(PROP.MAIN_PROP_NAME, PROP.VERBOSE_LEVEL);
        this.nBest = config.readProperty(PROP.MAIN_PROP_NAME, PROP.NBEST);
        this.model = config.getResource(modelName + PROP.MODEL_SUFFIX).getFile();

        this.tagger = new Tagger("-m " + this.model + " -v " + this.vLevel + " -n" + this.nBest);
        this.tagger.clear();
    }

    /**
     * Find list of {@link com.skplanet.nlp.data.Entity} from the given input
     *
     * @param input input sentence
     * @return list of {@link com.skplanet.nlp.data.Entity}
     */
    @Override
    public List<Entity> find(String input, boolean isTitle) {
        this.tagger.clear();
        // result
        List<Entity> result = new ArrayList<Entity>();

        // get nlp result
        Pair<String>[] nlpRes = this.nlp.getNLPResult(input);
        Map<Integer, String> tokenMap = new HashMap<Integer, String>();

        // add context
        int tokenCount = 0;
        for (Pair<String> p : nlpRes) {
            StringBuffer sb = new StringBuffer();
            // feature 1 [morph]
            sb.append(p.getFirst().split(NLP.DELIM_REGEX)[0]).append(" ");
            tokenMap.put(tokenCount, p.getFirst().split(NLP.DELIM_REGEX)[1] + NLP.DELIM + p.getFirst().split(NLP.DELIM_REGEX)[2]);
            // feature 2 [postag]
            sb.append(p.getSecond()).append(" ");
            // feature 3 [is title]
            if (isTitle) {
                sb.append("T").append(" ");
            } else {
                sb.append("F").append(" ");
            }
            // feature 4 [capitalized]
            // feature 5 [alphanumeric]
            tagger.add(sb.toString());
            tokenCount++;
        }

        //LOGGER.info("column size: " + tagger.xsize());
        //LOGGER.info("token size: " + tagger.size());
        //LOGGER.info("tag size: " + tagger.ysize());

        if (!tagger.parse()) {
            LOGGER.warn("tagger parsing error");
            return null;
        }

        LOGGER.debug("conditional prob=" + tagger.prob() + " log(Z)=" + tagger.Z());
        StringBuffer debugString = new StringBuffer();
        for (int i = 0; i < tagger.size(); ++i) {
            debugString.append(tagger.x(i, 0)).append(":").append(tagger.y2(i)).append(" ");
        }
        LOGGER.debug("tagged: " + debugString.toString().trim());

        for (int i = 0; i < tagger.size(); ++i) {

            // get crf result.
            //String startMorph = tagger.x(i, 0);
            //String startToken = tokenMap.get(i);
            //double prob = tagger.prob(i, 0);

            // extract entity info.
            String tag = tagger.y2(i);
            if (tag.equals("B")) {
                // find first 'O' tag occurred
                int j = i + 1;
                for (; j < tagger.size() && !tagger.y2(j).equals("O"); j++) {

                }
                j = j - 1;

                String startMorph = tagger.x(i, 0);
                String startTokenRaw = tokenMap.get(i);
                String startToken = startTokenRaw.replaceAll(NLP.DELIM_REGEX + "\\d+", "");
                int startPos = startToken.indexOf(startMorph);
                if (startPos < 0) {
                    // 만약 찾지 못했다면, 젤 처음이라고 가정 한다.
                    startPos = 0;
                }

                String endMorph = tagger.x(j, 0);
                String endTokenRaw = tokenMap.get(j);
                String endToken = endTokenRaw.replaceAll(NLP.DELIM_REGEX + "\\d+", "");
                int endPos = endToken.indexOf(endMorph) + endMorph.length();
                if (!endToken.contains(endMorph)) {
                    boolean found = false;
                    for (int x = j + 1; x < tagger.size(); x++) {
                        if (!tokenMap.get(x).equals(endTokenRaw)) {
                            break;
                        }
                        if (endToken.lastIndexOf(tagger.x(x, 0)) > 0) {
                            endPos = endToken.lastIndexOf(tagger.x(x, 0));
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        endPos = endToken.length();
                    }
                }

                if (tokenMap.get(i).equals(tokenMap.get(j))) {
                    startPos = calculateOffset(tokenMap, i, startPos);
                    endPos = calculateOffset(tokenMap, i, endPos);
                } else {
                    startPos = calculateOffset(tokenMap, i, startPos);
                    endPos = calculateOffset(tokenMap, j, endPos);
                }

                if (endPos > input.length()) {
                    endPos = input.length();
                }

                ProductEntity e = new ProductEntity();
                e.setName(input.substring(startPos, endPos));
                e.setStartIndex(startPos);
                e.setEndIndex(endPos - 1);
                e.setIsTitle(isTitle);

                result.add(e);
                i = j;
            }
        }

        return result;
    }

    /**
     * Calculate the offset
     * @param tokenMap token map
     * @param tokenIndex end token index
     * @return recovered raw sentence
     */
    private int calculateOffset(Map<Integer, String> tokenMap, int tokenIndex, int innerOffset) {
        StringBuffer sb = new StringBuffer();

        int preSize = 0;
        int i;
        for (i = 0; i <= tokenIndex; i++) {
            if (i > 0 && tokenMap.get(i).equals(tokenMap.get(i - 1))) {
                continue;
            }

            String[] data = tokenMap.get(i).split(NLP.DELIM_REGEX);
            String token = data[0];
            int tokenPosition = Integer.parseInt(data[1]);

            if (tokenMap.get(i).equals(tokenMap.get(tokenIndex))) {
                if (preSize < tokenPosition) {
                    sb.append(" ").append(token.substring(0, innerOffset));
                } else {
                    sb.append(token.substring(0, innerOffset));
                }
                break;
            } else {
                if (preSize < tokenPosition) {
                    sb.append(" ").append(token);
                    preSize++;
                } else {
                    sb.append(token);
                }
            }
            preSize += token.length();
        }
        return sb.toString().length();
    }

    //dynamic library linking
    static {
        try {
            System.loadLibrary("CRFPP");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Cannot load the example native code.\nMake sure your LD_LIBRARY_PATH contains \'.\'\n" + e);
            System.exit(1);
        }
    }



}
