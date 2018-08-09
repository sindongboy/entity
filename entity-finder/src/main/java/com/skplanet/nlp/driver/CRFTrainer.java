package com.skplanet.nlp.driver;

import com.rakuten.cli.CommandLineInterface;
import com.skplanet.nlp.core.ProductFinder;
import com.skplanet.nlp.data.Entity;
import com.skplanet.nlp.dict.Dictionary;
import com.skplanet.nlp.dict.ProductDictionary;
import com.skplanet.nlp.util.NLP;
import com.skplanet.nlp.util.Pair;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.List;

/**
 * Create Train Set from the tagged document for CRF Finder
 * @author Donghun Shin, donghun.shin@sk.com
 * @date 10/9/14.
 */
public final class CRFTrainer {
    private static final Logger LOGGER = Logger.getLogger(CRFTrainer.class.getName());

    public static void main(String[] args) throws IOException {
        CommandLineInterface cli = new CommandLineInterface();
        cli.addOption("d", "dict", true, "dictionary file", true);
        cli.addOption("i", "input", true, "input file to be tagged", true);
        cli.addOption("o", "output", true, "output path", true);
        cli.addOption("r", "overwrite", false, "overwrite existing model", false);
        cli.parseOptions(args);

        File inputFile = new File(cli.getOption("i"));
        BufferedReader reader;
        File outputFile = new File(cli.getOption("o"));
        BufferedWriter writer;

        Dictionary dict = ProductDictionary.getInstance();
        dict.load(cli.getOption("d"));
        ProductFinder finder = new ProductFinder(dict);

        NLP nlp = NLP.getInstance();

        try {

            reader = new BufferedReader(new FileReader(inputFile));
            writer = new BufferedWriter(new FileWriter(outputFile));
            String line;
            int docnum = 0;

            while ((line = reader.readLine()) != null) {
                // skip empty line
                if (line.trim().length() == 0) {
                    writer.newLine();
                    continue;
                }
                // add new line between documents
                if (docnum != 0) {
                    if (line.startsWith("^TITLE:")) {
                        writer.newLine();
                    }
                }

                List<Entity> entities;
                boolean isTitle = false;
                if (line.startsWith("^TITLE:")) {
                    line = line.replace("^TITLE: ", "");
                    isTitle = true;
                    entities = finder.find(line, true);
                } else {
                    entities = finder.find(line, false);
                }

                String taggedLine;
                if (entities != null) {
                    taggedLine = finder.tagging(line, entities, ">>>", "<<<");
                } else {
                    taggedLine = line;
                }

                Pair<String>[] nlpPair = nlp.getNLPResult(taggedLine);

                boolean start = false;
                int startCount = 0;
                for (int i = 0; i < nlpPair.length; i++) {
                    if (nlpPair[i].getFirst().split(NLP.DELIM_REGEX)[0].equals(">")) {
                        if (i + 3 < nlpPair.length &&
                                nlpPair[i + 1].getFirst().split(NLP.DELIM_REGEX)[0].equals(">") &&
                                nlpPair[i + 2].getFirst().split(NLP.DELIM_REGEX)[0].equals(">") &&
                                !nlpPair[i+3].getFirst().split(NLP.DELIM_REGEX)[0].equals(">")) {
                            start = true;
                            startCount = 0;
                            i += 2;
                            continue;
                        }
                    }

                    if (nlpPair[i].getFirst().split(NLP.DELIM_REGEX)[0].equals("<")) {
                        if (i + 2 < nlpPair.length &&
                                nlpPair[i + 1].getFirst().split(NLP.DELIM_REGEX)[0].equals("<") &&
                                nlpPair[i + 2].getFirst().split(NLP.DELIM_REGEX)[0].equals("<")) {
                            start = false;
                            i += 2;
                            continue;
                        }
                    }

                    if (start) {
                        if (startCount > 0) {
                            writer.write(nlpPair[i].getFirst().split(NLP.DELIM_REGEX)[0].replaceAll(" ", "") + " " + nlpPair[i].getSecond());
                            //System.out.print(nlpPair[i].getFirst().split(NLP.DELIM_REGEX)[0].replaceAll(" ", "") + " " + nlpPair[i].getSecond());
                            if (isTitle) {
                                writer.write(" T");
                                //System.out.print(" T");
                            } else {
                                writer.write(" F");
                                //System.out.print(" F");
                            }
                            writer.write(" I");
                            //System.out.println(" I");
                            writer.newLine();
                        } else {
                            writer.write(nlpPair[i].getFirst().split(NLP.DELIM_REGEX)[0].replaceAll(" ", "") + " " + nlpPair[i].getSecond());
                            //System.out.print(nlpPair[i].getFirst().split(NLP.DELIM_REGEX)[0].replaceAll(" ", "") + " " + nlpPair[i].getSecond());
                            if (isTitle) {
                                writer.write(" T");
                                //System.out.print(" T");
                            } else {
                                writer.write(" F");
                                //System.out.print(" F");
                            }
                            writer.write(" B");
                            //System.out.println(" B");
                            writer.newLine();
                        }
                        startCount++;
                    } else {
                        writer.write(nlpPair[i].getFirst().split(NLP.DELIM_REGEX)[0].replaceAll(" ", "") + " " + nlpPair[i].getSecond());
                        //System.out.print(nlpPair[i].getFirst().split(NLP.DELIM_REGEX)[0].replaceAll(" ", "") + " " + nlpPair[i].getSecond());
                        if (isTitle) {
                            writer.write(" T");
                            //System.out.print(" T");
                        } else {
                            writer.write(" F");
                            //System.out.print(" F");
                        }
                        writer.write(" O");
                        //System.out.println(" O");
                        writer.newLine();
                    }
                }

            }

            reader.close();
            writer.close();
        } catch (IOException e) {
            LOGGER.error("output file not found: " + cli.getOption("i"), e);
        }

    }

    private CRFTrainer() {

    }
}
