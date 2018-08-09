package com.skplanet.nlp.entityExtractor.evaluation;

import com.skplanet.nlp.cli.CommandLineInterface;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Donghun Shin
 * Contact: donghun.shin@sk.com, sindongboy@gmail.com
 * Date: 10/19/13
 */
public class Evaluation {
    private static Logger logger = Logger.getLogger(Evaluation.class.getName());
    public static void main(String[] args) {

        CommandLineInterface cli = new CommandLineInterface();
        cli.addOption("a", "answer", true, "answer file", true);
        cli.addOption("t", "test", true, "test file", true);
        cli.parseOptions(args);

        File aFile = new File(cli.getOption("a"));
        File tFile = new File(cli.getOption("t"));
        BufferedReader reader;
        String line;

        ArrayList<String> answers = new ArrayList<String>();
        ArrayList<String> answerSentences = new ArrayList<String>();
        ArrayList<String> results = new ArrayList<String>();
        ArrayList<String> resultSentences = new ArrayList<String>();

        // load answers and test resutls
        try {
            // afile
            reader = new BufferedReader(new FileReader(aFile));
            while ((line = reader.readLine()) != null) {
                if (line.trim().length() == 0) {
                    continue;
                }
                String field[] = line.split("=>");

                if (field.length != 2) {
                    logger.error("wrong format: " + line);
                    continue;
                }
                answers.add(field[0].trim().toLowerCase());
                answerSentences.add(field[1].trim());
            }
            reader.close();


            reader = new BufferedReader(new FileReader(tFile));
            while ((line = reader.readLine()) != null) {
                if (line.trim().length() == 0) {
                    continue;
                }
                String field[] = line.split("=>");

                if (field.length != 2) {
                    logger.error("wrong format: " + line);
                    continue;
                }
                results.add(field[0].trim().toLowerCase());
                resultSentences.add(field[1].trim());
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int docnum = answerSentences.size();
        System.out.println("Total number of sentence loaded: " + docnum);

        // total numbers
        int answerNumTotal = getTotal(answers);
        System.out.println("Total number of answer found: " + answerNumTotal);
        int resultNumTotal = getTotal(results);
        System.out.println("Total number of result found: " + resultNumTotal);

        // precision
        double precision = 0.0;
        int matched = 0;
        for (int i = 0; i < docnum; i++) {
            matched += matchCount(answers.get(i), results.get(i));
        }
        precision = (double) matched / (double) resultNumTotal;

        // recall
        double recall = 0.0;
        matched = 0;
        for (int i = 0; i < docnum; i++) {
            int tmpCount = matchCount(results.get(i), answers.get(i));
            if (tmpCount == 0) {
                System.out.println("NO_MATCH: " + answerSentences.get(i));
                System.out.println("ANSWER: " + answers.get(i));
                System.out.println("RESULT: " + results.get(i));
            }
            matched += tmpCount;

        }
        recall = (double) matched / (double) answerNumTotal;

        // f-score
        double fscore = 2 * ( (precision * recall)/( precision + recall ) );

        System.out.format("Precision: %.3f%n", precision);
        System.out.format("Recall: %.3f%n", recall);
        System.out.format("F-Score: %.3f%n", fscore);

    }

    static int matchCount(String answers, String results) {
        String[] aTokens = answers.split(" ");
        String[] rTokens = results.split(" ");
        int mCount = 0;

        for (String r : rTokens) {
            for (String a : aTokens) {
                if (r.equals(a) || r.contains(a) || a.contains(r)) {
                    mCount++;
                    break;
                }
            }
        }

        return mCount;
    }

    static int getTotal(ArrayList<String> items) {
        int count = 0;
        for (String item : items) {
            count += item.split(" ").length;
        }
        return count;
    }

}
