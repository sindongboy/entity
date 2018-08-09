package com.skplanet.nlp.entityExtractor.driver;

import com.skplanet.nlp.entityExtractor.util.NLP;
import com.skplanet.nlp.entityExtractor.util.Pair;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Scanner;

final class NLPTester {
	private static Logger logger = Logger.getLogger(NLPTester.class.getName());
	public static void main(String [] args) {
		logger.info("nlp init ....");
		NLP nlp = NLP.getInstance();
		logger.info("nlp init done"); 
		Scanner scan = new Scanner(System.in);
		String line;
		while((line = scan.nextLine())!=null) {
			if(line.trim().length() == 0) {
				continue;
			}
			String [] sentences = nlp.getSentences(line);
			int sentCount = 0;
			for(String sent : sentences) {

				// --> processing
				Pair[] nlpRes = nlp.getNLPResult(sent);
                List<String> ners = nlp.getNER(sent);
				// --> processing
				
				System.out.print("> ");
                for (Pair nlpRe : nlpRes) {
                    System.out.print(nlpRe.getFirst() + "/" + nlpRe.getSecond() + " ");
                }
				System.out.println();

                int count = 0;
                for (String ne : ners) {
                    System.out.println("NE" + count + ": " + ne);
                    count++;
                }
			}
		}
		scan.close();

	}
}
