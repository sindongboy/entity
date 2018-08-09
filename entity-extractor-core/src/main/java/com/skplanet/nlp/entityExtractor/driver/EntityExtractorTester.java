package com.skplanet.nlp.entityExtractor.driver;

import com.skplanet.nlp.cli.CommandLineInterface;
import com.skplanet.nlp.entityExtractor.analyzer.EntityExtractor;
import com.skplanet.nlp.entityExtractor.config.EConfiguration;
import com.skplanet.nlp.entityExtractor.evaluation.EvalDoc;
import com.skplanet.nlp.entityExtractor.type.Entity;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * <br>Created by Donghun Shin<br>
 * Contact: donghun.shin@sk.com, sindongboy@gmail.com<br>
 * Date: 7/1/13<br>
 */
public class EntityExtractorTester {


    public static void main(String[] args) {

		// -----------------
		// commands
		// -----------------
        CommandLineInterface cmd = new CommandLineInterface();
        cmd.addOption("c", "cate", true, "category", true);
        //cmd.addOption("i", "input", true, "input file", true);
        cmd.parseOptions(args);

        EConfiguration config = new EConfiguration();


		// ----------------------------
        // analyzer loading.
		// ----------------------------
        EntityExtractor eTester = new EntityExtractor(cmd.getOption("c"));
        eTester.init(config);
		eTester.loadNLP();


		// ------------------
        // dic loading.
		// ------------------
        long time = System.currentTimeMillis();
        eTester.loadDictionary(config);
        time = System.currentTimeMillis() - time;
        System.err.println("Dictionary loading time : " + time / (double) 1000 + "seconds.");


		Scanner scan = new Scanner(System.in);
		String line;
		while((line = scan.nextLine())!=null) {
			if(line.trim().length() == 0) {
				continue;
			}

			List<Entity> result = eTester.find(line, EntityExtractor.SEN_LEVEL_ANAL);
			if(result != null && result.size() > 0) {
                int count = 0;
				for(Entity e : result) {
                    if (count == result.size() - 1) {
                        System.out.print(e.getName().replace(" ", ""));
                        continue;
                    }
                    System.out.print(e.getName().replace(" ", "") + " ");
                    count++;
                }
			} else {
				System.out.print("NONE");
			}
			System.out.println("=>" + line);
		}
		scan.close();

    }

    static int found(ArrayList<Entity> result, ArrayList<String> answer) {
        if (result == null || answer == null) {
            return 0;
        }
        int count = 0;
        String res;
        for (String a : answer) {
            a = a.trim().toLowerCase().replace(" ", "");
            for (Entity r : result) {
                res = r.getName().trim().toLowerCase().replace(" ", "");
                if (a.contains(res) || res.contains(a)) {
                    count++;
                    break;
                }

                if (a.startsWith(res) || res.startsWith(a)) {
                    count++;
                    break;
                }

                if (a.endsWith(res) || res.endsWith(a)) {
                    count++;
                    break;
                }
            }
        }

        return count;
    }

    static boolean match(ArrayList<String> answers, String target) {
        if (answers == null) {
            return false;
        }
        target = target.toLowerCase().replace(" ", "");
        for (String a : answers) {
            a = a.toLowerCase().replace(" ", "");
            if (a.contains(target) || target.contains(a)) {
                return true;
            }

            if (a.startsWith(target) || target.startsWith(a)) {
                return true;
            }

            if (a.endsWith(target) || target.endsWith(a)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Answer Set Loading
     * @param answer answer file
     * @return loaded documents
     */
    static HashMap<String, EvalDoc> evaluationSetLoading(File answer) {
        HashMap<String, EvalDoc> result = new HashMap<String, EvalDoc>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(answer));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#") || line.trim().length() == 0) {
                    continue;
                }
                String[] field = line.split("\t");
                if (field.length != 2) {
                    System.err.println("[Warning] answer file format is wrong : " + line);
                    continue;
                }

                if (result.containsKey(field[0])) {
                    result.get(field[0]).addSentence(field[1]);
                } else {
                    EvalDoc item = new EvalDoc();
                    item.setDocseq(field[0]);
                    item.addSentence(field[1]);
                    result.put(field[0], item);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
