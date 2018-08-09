package com.skplanet.nlp.entityExtractor.util;

import com.skplanet.nlp.NLPAPI;
import com.skplanet.nlp.NLPDoc;
import com.skplanet.nlp.morph.Morph;
import com.skplanet.nlp.morph.Morphs;
import com.skplanet.nlp.phrase.PhraseItem;
import com.skplanet.nlp.phrase.Phrases;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * NLP Wrapper, Make it as Singleton class.
 * <br>Created by Donghun Shin<br>
 * Contact: donghun.shin@sk.com, sindongboy@gmail.com<br>
 * Date: 7/2/13<br>
 */
public class NLP {
	private static Logger logger = Logger.getLogger(NLP.class.getName());

    private static final String configName = "nlp_api.properties";

    private static NLP instance = null;

    private NLPAPI nlp = null;

    /**
     * private class constructor
     */
    private NLP() {
        this.nlp = new NLPAPI(configName);
    }

    /**
     * Get instance of {@link NLP}
     * @return an instance of NLP class
     */
    public static NLP getInstance() {
        if (instance == null) {
            synchronized (NLP.class) {
                instance = new NLP();
            }
        }
        return instance;
    }

    /**
     * Get sentences from given text
     * @param text text to be sentence analyzed
     * @return array of sentence
     */
	public String[] getSentences(String text) {
		return nlp.doSegmenting(text);
	}

	/**
	 * Get Morph and POS {@link Pair} from the sentence given.
	 * @param sentence sentence to be pos analyzed.
	 * @return array of Morph and POS {@link Pair}
	 */
	public Pair[] getNLPResult(String sentence) {
		NLPDoc nlpDocs = this.nlp.doNLP(sentence);
		List<Pair> pos = new ArrayList<Pair>();

		Morphs morphs = nlpDocs.getMorphs();
		for (Morph m : morphs.getMorphs()) {
			if (m == null) {
				break;
			}
			Pair item = new Pair(m.getTextStr(), m.getPosStr());
			pos.add(item);
		}
		return pos.toArray(new Pair[pos.size()]);
	}

	/**
	 * Get NER items
	 *
	 * @param sentence input sentence
	 * @return array of NE
	 */
	public List<String> getNER(String sentence) {
		List<String> result = new ArrayList<String>();
		NLPDoc nlpRes = this.nlp.doNLP(sentence);

		Phrases phrases = nlpRes.getPhrases();
		List<PhraseItem> pItems = phrases.getNeItems();
		for(PhraseItem p : pItems) {
			result.add(p.getKeyword());
		}
		return result;
	}

    public List<PhraseItem> getPhraseItem(String sentence) {
        Phrases phrases = this.nlp.doNLP(sentence).getPhrases();
        return phrases.getNeItems();
    }

}
