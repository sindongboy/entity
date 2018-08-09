package com.skplanet.nlp.entityExtractor.driver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import com.skplanet.nlp.entityExtractor.config.EConfiguration;
import com.skplanet.nlp.entityExtractor.knowledge.Dictionary;

/**
 * Dictionary Loading Tester
 * <br></br>
 * <br></br>
 * Created by Donghun Shin
 * <br></br>
 * Contact: donghun.shin@sk.com, sindongboy@gmail.com
 * <br></br>
 * Date: 6/28/13
 * <br></br>
 */
public class DictionaryTester {

    public static void main(String[] args) {

        // config
        EConfiguration config = new EConfiguration();

        // collection of dictionary
        HashMap<String, Dictionary> dicts = new HashMap<String, Dictionary>();

        // dictionary files
        File[] dicFiles = new File(config.getDicPath()).listFiles();
        long time = System.currentTimeMillis();

        // read and load dictionary
        BufferedReader read;
        assert dicFiles != null;
        for (File f : dicFiles) {
            Dictionary dict = new Dictionary();
            dict.setCategory(f.getName());
            System.out.println("loading : " + f.getName());
            try {
                read = new BufferedReader(new FileReader(f));
                String line;
                int count = 0;
                while ((line = read.readLine()) != null) {
                    /*
                    if (count % 1000 == 0) {
                        System.out.println("line: " + count);
                    }
                    */
                    count++;
                    if (line.startsWith("#") || line.trim().length() == 0) {
                        continue;
                    }
                    String[] field = line.split("\t");
                    dict.add(field[0], field[0]);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            dicts.put(f.getName(), dict);
        }
        time = System.currentTimeMillis() - time;

        System.out.println("Time: " + time/(double)1000);

    }
}
