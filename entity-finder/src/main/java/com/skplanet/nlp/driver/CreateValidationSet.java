package com.skplanet.nlp.driver;

import com.rakuten.cli.CommandLineInterface;
import org.apache.log4j.Logger;

import java.io.*;

/**
 * Create Validation Set from training set
 * @author Donghun Shin, donghun.shin@sk.com
 * @date 10/12/14.
 */
public final class CreateValidationSet {
    public static final Logger LOGGER = Logger.getLogger(CreateValidationSet.class.getName());

    public static void main(String[] args) throws IOException {
        CommandLineInterface cli = new CommandLineInterface();
        cli.addOption("i", "input", true, "training set", true);
        cli.addOption("o", "output path", true, "output path", true);
        cli.addOption("n", "fold", true, "n-fold", true);
        cli.parseOptions(args);

        File trainFile = new File(cli.getOption("i"));
        BufferedReader reader = new BufferedReader(new FileReader(trainFile));
        char[] cbuf = new char[(int) trainFile.length()];
        String sbuf;
        while (!reader.ready()) {

        }
        reader.read(cbuf);
        reader.close();
        sbuf = String.valueOf(cbuf);

        int offset = 0;
        int count = 0;
        while ((offset = sbuf.indexOf("\n\n", offset)) > 0) {
            count++;
            offset++;
        }
        int singleSetSize = count / Integer.parseInt(cli.getOption("n"));

        BufferedWriter writer;
        int setCount = 0;
        String outputBase = cli.getOption("o");
        int startOffset = 0;
        count = 0;
        offset = 0;
        while ((offset = sbuf.indexOf("\n\n", offset)) > 0) {
            if (count % singleSetSize == singleSetSize - 1) {
                writer = new BufferedWriter(new FileWriter(new File(outputBase + "/" + setCount + ".val")));
                writer.write(sbuf.substring(startOffset, offset));
                writer.close();
                startOffset = offset + 2;
                setCount++;
            }
            count++;
            offset++;
        }

    }

    private CreateValidationSet() {

    }
}
