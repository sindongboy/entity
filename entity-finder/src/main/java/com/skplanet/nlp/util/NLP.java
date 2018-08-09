package com.skplanet.nlp.util;


import com.skplanet.nlp.NLPAPI;
import com.skplanet.nlp.NLPDoc;
import com.skplanet.nlp.config.Configuration;
import com.skplanet.nlp.morph.Morph;
import com.skplanet.nlp.morph.Morphs;
import com.skplanet.nlp.phrase.PhraseItem;
import com.skplanet.nlp.phrase.Phrases;
import com.skplanet.nlp.token.Tokens;

import java.util.ArrayList;
import java.util.List;

/**
 * NLP Wrapper, Make it as Singleton class.
 * <br>Created by Donghun Shin<br>
 * Contact: donghun.shin@sk.com, sindongboy@gmail.com<br>
 * Date: 7/2/13<br>
 */
public final class NLP {

    private static final String CONFIG_NAME = "nlp_api.properties";
    private static NLP instance = null;

    private NLPAPI nlp = new NLPAPI(CONFIG_NAME, Configuration.CLASSPATH_LOAD );
    public static final String DELIM = "$#$";
    public static final String DELIM_REGEX = "\\$#\\$";

    /**
     * private class constructor
     */
    private NLP() {
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
     * return example : Pair[morph:token:tokenNum][postag]
     *
	 * @param sentence sentence to be pos analyzed.
	 * @return array of Morph and POS {@link Pair}
	 */
	public Pair<String>[] getNLPResult(String sentence) {
		NLPDoc nlpDocs = this.nlp.doNLP(sentence);
		List<Pair> pos = new ArrayList<Pair>();

		Morphs morphs = nlpDocs.getMorphs();
        Tokens tokens = nlpDocs.getTokens();
        for (Morph m : morphs.getMorphs()) {
			if (m == null) {
				break;
			}
            int tokenPosition = tokens.getToken(m.getTokenNumber()).getPosition();
            String token = tokens.getToken(m.getTokenNumber()).getTextStr();
			Pair<String> item = new Pair<String>(m.getTextStr() + DELIM + token + DELIM + tokenPosition, m.getPosStr());
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
