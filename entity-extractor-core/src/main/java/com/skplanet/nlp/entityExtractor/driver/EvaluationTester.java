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
import java.util.Set;

/**
 * <br>Created by Donghun Shin<br>
 * Contact: donghun.shin@sk.com, sindongboy@gmail.com<br>
 * Date: 7/1/13<br>
 */
public class EvaluationTester {

    public static void main(String[] args) {

		// -----------------
		// commands
		// -----------------
        CommandLineInterface cmd = new CommandLineInterface();
        cmd.addOption("c", "cate", true, "category", true);
        cmd.addOption("i", "input", true, "input file", true);
        cmd.parseOptions(args);

        EConfiguration config = new EConfiguration();


		// ----------------------------
        // analyzer loading.
		// ----------------------------
        EntityExtractor eTester = new EntityExtractor(cmd.getOption("c"));
        eTester.init(config);


        // dic loading.
        long time = System.currentTimeMillis();
        eTester.loadDictionary(config);
        time = System.currentTimeMillis() - time;
        System.out.println("Dictionary loading time : " + time / (double) 1000 + "seconds.");


        // analysis
        List<Entity> result;
        File file = new File(cmd.getOption("i") + "/" + cmd.getOption("c") + ".ans");
        // load answer set.
        HashMap<String, EvalDoc> answers = evaluationSetLoading(file);
        int answerCount = 0;
        int foundCount = 0;
        int resultCount = 0;
        int correctCount = 0;
        Set<String> ansKey = answers.keySet();
        for (String key : ansKey) {
            answerCount += answers.get(key).getObjectSize();
        }

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            int sentid = 0;
            String docid = "";
            while ((line = reader.readLine()) != null) {
                if (line.trim().length() == 0) {
                    continue;
                }

                System.out.println(line);
                String[] field = line.split("\t");
                if (field[0].equals(docid)) {
                    sentid++;
                } else {
                    docid = field[0];
                    sentid = 0;
                }
                result = eTester.find(field[1].replace("<<<", "").replace(">>>", ""), EntityExtractor.SEN_LEVEL_ANAL);
                ArrayList<String> objects = answers.get(field[0]).getObjects(sentid + "");
                foundCount += found(result, objects);

                if (result != null) {
                    for (Entity e : result) {
                        resultCount++;
                        String modelNameFound = e.getInfo().split("\t")[0];
                        System.out.println("found: " + e.getInfo() + " : " + e.getName() + " (" + e.getStartIndex() + ", " + e.getEndIndex() + ")");
                        if (match(objects, modelNameFound)) {
                            correctCount++;
                            System.out.println("matched");
                        } else {
                            System.out.print("not matched: ");
                            if (objects != null) {
                                for (String obj : objects) {
                                    System.out.print(obj + " ");
                                }
                                System.out.println();
                            } else {
                                System.out.println();
                            }
                        }
                    }
                } else {
                    if (objects != null) {
                        System.out.print("not found : ");
                        for (String obj : objects) {
                            System.out.print(obj + ", ");
                        }
                        System.out.println();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println("result total: " + resultCount);
        System.out.println("matched: " + correctCount);
        System.out.println("answer total: " + answerCount);
        System.out.println("found total: " + foundCount);
        double precision = (double) correctCount / (double) resultCount;
        double recall = (double) foundCount / (double) answerCount;
        System.out.println("precision: " + precision);
        System.out.println("recall: " + recall);
        double fscore = 2 * ( (precision * recall)/( precision + recall ) );
        System.out.println("f-score: " + fscore);

    }

    static int found(List<Entity> result, ArrayList<String> answer) {
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
