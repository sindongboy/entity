package com.skplanet.nlp.entityExtractor.driver;

import com.skplanet.hnlp.crf.transliteration.transliterationAPI;

import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * <br>
 * <br>
 * Created by Donghun Shin
 * <br>
 * Contact: donghun.shin@sk.com, sindongboy@gmail.com
 * <br>
 * Date: 11/6/13
 * <br>
 */
public class TransliterationTester {

    static transliterationAPI transliterator = null;

    public static void main(final String[] args) {
        String rscPath = null;

        for(int i=0; i<args.length; i++) {
            if(args[i].charAt(0) == '-') {
                final char option = args[i].charAt(1);
                switch (option) {
                    case 'r':
                        i++; rscPath = args[i]; break;
                }
            }
        }

        if(rscPath == null) { System.out.println("use -r [resource path]"); return; }
        transliterator = new transliterationAPI(rscPath);
        if( transliterator.initialize() == false ) { /* model & tlDic loading */
            System.err.println("transliterator initialization failed!"); return;
        }

        // Process input file
        final Scanner is = new Scanner(new InputStreamReader(System.in));
        while(is.hasNextLine()) {
            final String srcStr = is.nextLine();
            printTransliteratedString(srcStr);
        }
    }
//          final String srcStr = "Hasanuddin"; //knuth, knowledge, baldwin, alex, georgia, Hasanuddin
//          printTransliteratedString(srcStr);

    static void printTransliteratedString(final String srcStr) {
        String targetStr = "";
        int resVal = transliterator.detect(srcStr);
        if( resVal == 0 ) { /* fail */
            System.err.println("transliterator detect failed!"); return;
        } else if( resVal == 1 ) { /* userDic */
            targetStr = transliterator.getCached(srcStr);
        } else { /* crf decoding */
            targetStr = transliterator.getOutput();
        }

        final String outputStr = srcStr + "-" + targetStr;
        System.out.print(outputStr + "\n");
        /* n-best 및 확률 값 출력 => for debugging
        final double[] outputProb = transliterator.getOutputProb();
        for(int i=0; i < output.length; i++) {
            System.out.println( transliterator.getFeatureList().get(i) + "\t" + output[i] + "\t" + outputProb[i] );
        }
        System.out.println();
        */
    }

}
