package com.skplanet.nlp.driver;

import com.rakuten.cli.CommandLineInterface;
import com.skplanet.nlp.core.ProductFinder;
import com.skplanet.nlp.data.Entity;
import com.skplanet.nlp.dict.ProductDictionary;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.List;
import java.util.Scanner;

/**
 * Command Line Interface for Product Finder
 * @author Donghun Shin, donghun.shin@sk.com
 * @date 10/6/14.
 */
public final class ProductFinderCLI {
    private static final Logger LOGGER = Logger.getLogger(ProductFinderCLI.class.getName());

    /**
     * Main Method
     * @param args argument list
     */
    public static void main(String[] args) throws IOException {
        // options
        CommandLineInterface cli = new CommandLineInterface();
        cli.addOption("d", "dictionary", true, "category number", true);
        cli.addOption("m", "mode", true, "run type [ cli | file ]", true);
        cli.addOption("i", "input", true, "input file [ file mode only ]", false);
        cli.addOption("o", "output", true, "output file [ file mode only ]", false);
        cli.parseOptions(args);

        // set dictionary
        ProductDictionary dict = ProductDictionary.getInstance();
        dict.load(cli.getOption("d"));

        // set finder
        ProductFinder finder = new ProductFinder(dict);

        String mode = cli.getOption("m");

        long btime, etime;
        String input;
        List<Entity> entities;
        if (mode.equals("file")) {

            if (!cli.hasOption("i") || !cli.hasOption("o")) {
                LOGGER.error("run type is file mode, so input and output file path is needed", new FileNotFoundException());
            }

            BufferedReader reader = new BufferedReader(new FileReader(new File(cli.getOption("i"))));
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(cli.getOption("o"))));

            LOGGER.info("Analysis Start ....");
            btime = System.currentTimeMillis();
            while ((input = reader.readLine()) != null) {
                if (input.trim().length() == 0) {
                    writer.newLine();
                    continue;
                }

                entities = finder.find(input, false);
                if (entities != null) {
                    writer.write(finder.tagging(input, entities, "<<<", ">>>"));
                    writer.newLine();
                } else {
                    writer.write(input);
                    writer.newLine();
                }
            }
            reader.close();
            writer.close();
            etime = System.currentTimeMillis();
            LOGGER.info("Analysis Done in " + (etime - btime) + " msec.");
        } else {
            // find
            Scanner scan = new Scanner(System.in);
            System.out.print("INPUT:");
            while ((input = scan.nextLine()) != null) {
                if (input == null || input.trim().length() == 0) {
                    System.out.print("INPUT:");
                    continue;
                }
                LOGGER.info("Analysis Start ....");
                btime = System.currentTimeMillis();
                entities = finder.find(input, false);
                etime = System.currentTimeMillis();
                LOGGER.info("Analysis Done in " + (etime - btime) + " msec.");
                if (entities != null) {
                    System.out.println("RESULT:" + finder.tagging(input, entities, "<<<", ">>>"));
                } else {
                    System.out.println("RESULT:" + input);
                }

                System.out.print("INPUT:");
            }
        }
    }

    private ProductFinderCLI() {

    }
}
