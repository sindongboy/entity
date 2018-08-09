package com.skplanet.nlp.entityExtractor.evaluation;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * Evaluation Document Template for Entity Extraction
 * <br>
 * <br>
 * Created by Donghun Shin
 * <br>
 * Contact: donghun.shin@sk.com, sindongboy@gmail.com
 * <br>
 * Date: 7/17/13
 * <br>
 */
public class EvalDoc {
	private static Logger logger = Logger.getLogger(EvalDoc.class.getName());
    public static final String none = "NONE";

    private String docseq = null;
    private ArrayList<String> sentences = null;
    private HashMap<String, ArrayList<String>> object = null;

    private int objectSize = 0;

    /**
     * Constructor
     */
    public EvalDoc() {
        sentences = new ArrayList<String>();
        object = new HashMap<String, ArrayList<String>>();
    }

    /**
     * get doc. sequence
     * @return document sequence
     */
    public String getDocseq() {
        return docseq;
    }

    /**
     * set doc. sequence
     * @param docseq document sequence number to be set
     */
    public void setDocseq(String docseq) {
        this.docseq = docseq;
    }

    /**
     * get sentences
     * @return array of sentences
     */
    public ArrayList<String> getSentences() {
        return sentences;
    }

    /**
     * set sentences
     * @param sentences array of sentences to be set
     */
    public void setSentences(ArrayList<String> sentences) {
        this.sentences = sentences;
    }

    /**
     * Add sentence
     * @param sentence sentence to be added
     */
    public void addSentence(String sentence) {
        this.addObject(sentence);
        this.sentences.add(sentence);
    }

    public ArrayList<String> getObjects(String sentseq) {
        return this.object.get(sentseq);
    }

    private void addObject(String sentence) {
        int sidx = 0;
        int eidx = 0;

        sidx = sentence.indexOf("<<<");
        while (sidx > -1) {
            eidx = sentence.indexOf(">>>", sidx);
            if (eidx > -1) {
                this.objectSize++;
                if (this.object.containsKey(this.sentences.size() + "")) {
                    this.object.get(this.sentences.size() + "").add(sentence.substring(sidx + 3, eidx));
                } else {
                    ArrayList<String> items = new ArrayList<String>();
                    items.add(sentence.substring(sidx + 3, eidx));
                    this.object.put(this.sentences.size() + "", items);
                }
            } else {
                break;
            }
            sidx = sentence.indexOf("<<<", eidx);
        }

    }

    public int getObjectSize() {
        return this.objectSize;
    }
}
