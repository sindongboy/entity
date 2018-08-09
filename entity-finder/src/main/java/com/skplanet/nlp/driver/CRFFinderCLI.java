package com.skplanet.nlp.driver;

import com.rakuten.cli.CommandLineInterface;
import com.skplanet.nlp.core.CRFFinder;
import com.skplanet.nlp.data.Entity;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Command Line Interface for CRF Finder
 * @author Donghun Shin, donghun.shin@sk.com
 * @date 10/6/14.
 */
public final class CRFFinderCLI {
    private static final Logger LOGGER = Logger.getLogger(CRFFinderCLI.class.getName());

    /**
     * Main Method
     * @param args argument list
     */
    public static void main(String[] args) throws IOException {
        // options
        CommandLineInterface cli = new CommandLineInterface();
        cli.addOption("h", "help", false, "show help", false);
        cli.addOption("d", "model", true, "model id", true);
        cli.addOption("m", "mode", true, "run type [ cli | file ]", true);
        cli.addOption("i", "input", true, "input file [ file mode only ]", false);
        cli.addOption("o", "output", true, "output file [ file mode only ]", false);
        cli.parseOptions(args);

        // set finder
        CRFFinder finder = new CRFFinder(cli.getOption("d"));

        String runtype = cli.getOption("m");
        List<Entity> entities;

        long btime, etime;

        String input;
        if (runtype.equals("file")) {
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
                } else {
                    writer.write(input);
                }
                writer.newLine();

            }
            reader.close();
            writer.close();
            etime = System.currentTimeMillis();
            LOGGER.info("Analysis Done in " + (etime - btime) + " msec.");
        } else {
            LOGGER.warn("run type not specified!");

            // find
            Scanner scan = new Scanner(System.in);
            System.out.print("INPUT:");
            try {
                while ((input = scan.nextLine()) != null) {
                    if (input == null) {
                        break;
                    }
                    if (input.trim().length() == 0) {
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
            } catch (NoSuchElementException e) {
                LOGGER.info("no more input", e);
            }
        }
    }

    private CRFFinderCLI() {

    }
}
