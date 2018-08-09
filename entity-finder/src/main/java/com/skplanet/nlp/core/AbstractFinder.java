package com.skplanet.nlp.core;

import com.skplanet.nlp.data.Entity;
import com.skplanet.nlp.dict.Dictionary;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Abstract Finder Class
 * @author Donghun Shin, donghun.shin@sk.com
 */
public abstract class AbstractFinder implements Finder {

    // list of entities found
    protected List<Entity> entities = null;

    // dictionary
    protected Dictionary dictionary;

    /**
     * sole constructor called by sub-classes
     */
    protected AbstractFinder(Dictionary dictionary) {
        this.dictionary = dictionary;
        this.entities = new ArrayList<Entity>();
    }

    /**
     * Entity Boundary Tagging
     * @param line string line to be tagged
     * @param entities entities found
     * @param startTag start tag
     * @param endTag end tag
     * @return entity boundary tagged string line
     */
    public String tagging(String line, List<Entity> entities, String startTag, String endTag) {
        Set<Integer> starts = new HashSet<Integer>();
        Set<Integer> ends = new HashSet<Integer>();

        // get start and end index
        for (Entity e : entities) {
            starts.add(e.getStartIndex());
            ends.add(e.getEndIndex());
        }

        StringBuffer sb = new StringBuffer();
        // tagging
        boolean started = false;
        for (int i = 0; i < line.length(); i++) {
            if (starts.contains(i)) {
                if (!started) {
                    sb.append(startTag).append(line.charAt(i));
                    started = true;
                } else {
                    sb.append(line.charAt(i));
                }
                continue;
            }

            if (ends.contains(i)) {
                if (started) {
                    sb.append(line.charAt(i)).append(endTag);
                    started = false;
                }
                continue;
            }

            sb.append(line.charAt(i));
        }

        return sb.toString().replaceAll(endTag + " " + startTag, " ");
    }
}
