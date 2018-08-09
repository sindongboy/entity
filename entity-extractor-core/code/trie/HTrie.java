package com.skplanet.nlp.entityExtractor.trie;

import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Created by Donghun Shin
 * Contact: donghun.shin@sk.com, sindongboy@gmail.com
 * Date: 6/13/13
 */
public class HTrie {

	private static Logger logger  = Logger.getLogger(HTrie.class.getName());

    private static final String symbols = "',.-*";

    /**
     * Exact Trie Search
     * @param root root of the dictionary
     * @param item item to be matched
     * @return object in data node if exist, or null
     */
    public static Object hTrieSearch(Map<Object, HTrieNode> root, char[] item) {

        if (root == null) {
            return null;
        }
        Object result = null;

        HTrieNode node = root.get(Character.toLowerCase(item[0]));
        for (int i = 1; i < item.length; i++) {

            if (node != null) {
                if (node.isLeafObject()) {
                    result = node.getValuesObject();
                }
            } else {
                return null;
            }
            node = node.getNode(Character.toLowerCase(item[i]));
        }

        if (node != null) {
            if (node.isLeafObject()) {
                result = node.getValuesObject();
            }
        }
        return result;
    }
    /**
     * Relaxed Trie Search
     * @param root root of the dictionary
     * @param item item to be matched
     * @return object in data node if exist, or null
     */
    public static Object hTrieSearchRelaxed(Map<Object, HTrieNode> root, char[] item) {

        if (root == null) {
            return null; // abnormal return.
        }

        // get the root
        HTrieNode node = root.get(Character.toLowerCase(item[0]));
        HTrieNode pNode = null;

        Object result = null;

        // loop through item[]
        for (int i = 1; i < item.length; i++) {

            //----------------------------------
            // Match Failed !
            //----------------------------------
            if (node == null) {

                // Symbol subtraction //
                if (symbols.indexOf(Character.toLowerCase(item[i - 1])) > 0) {// symbol
                    if (pNode == null) {
                        node = root.get(Character.toLowerCase(item[i]));
                        pNode = node;
                    } else {
                        node = pNode.getNode(Character.toLowerCase(item[i]));
                        pNode = node;
                    }
                    continue;
                }

                for (char c : symbols.toCharArray()) {
                    if (pNode == null) {
                        node = root.get(Character.toLowerCase(c));
                    } else {
                        node = pNode.getNode(c);
                    }

                    if (node != null) {
                        break;
                    }
                }
                if (node != null) {
                    if (i - 2 >= 0) {
                        i = i - 2;
                    } else {
                        i = i - 1;
                    }
                    continue;
                }

                break;

            } else {
                if (node.isLeafObject()) {
                    result = node.getValuesObject();
                }
            }
            pNode = node;
            node = node.getNode(Character.toLowerCase(item[i]));
        }

        // 끝까지 match
        if (node != null) {
            if (node.isLeafObject()) {
                result = node.getValuesObject();
            }
        } else {
            if (symbols.indexOf(item[item.length - 1]) > 0 && pNode != null) {
               if(pNode.isLeafObject())
                   result = pNode.getValuesObject();
            }
        }

        return result;
    }



    /**
     * Longest (Relaxed) Trie Match
     * @param root root of the dictionary
     * @param item item to be matched
     * @return char. offset at the matched position.
     */
    public static int hTrieLongestMatchRelaxed(Map<Object, HTrieNode> root, char[] item) {

        if (root == null) {
            return -1; // abnormal return.
        }

        // get the root
        HTrieNode node = root.get(Character.toLowerCase(item[0]));
        HTrieNode pNode = null;
        // loop through item[]
        for (int i = 1; i < item.length; i++) {
            if (node == null) {

                // Symbol subtraction //
                if (symbols.indexOf(Character.toLowerCase(item[i - 1])) > 0) {// symbol
                    if (pNode == null) {
                        node = root.get(Character.toLowerCase(item[i]));
                        pNode = node;
                    } else {
                        node = pNode.getNode(Character.toLowerCase(item[i]));
                        pNode = node;
                    }
                    continue;
                }

                // Symbol addition //
                for (char c : symbols.toCharArray()) {
                    if (pNode == null) {
                        node = root.get(Character.toLowerCase(c));
                    } else {
                        node = pNode.getNode(c);
                    }

                    if (node != null) {
                        break;
                    }
                }
                if (node != null) {
                    if (i - 2 >= 0) {
                        i = i - 2;
                    } else {
                        i = i - 1;
                    }
                    continue;
                }

                return i - 1;
            }
            pNode = node;
            node = node.getNode(Character.toLowerCase(item[i]));
        }

        // 끝까지 match
        if (node != null) {
            return item.length;
        }

        if (symbols.indexOf(item[item.length - 1]) > 0) {
            return item.length;
        }

        // 마지막에 match failed.
        return item.length - 1;
    }


    /**
     * Longest (Exact) Trie Match
     * @param root root of the dictionary
     * @param item item to be matched
     * @return char. offset at the matched position.
     */
    public static int hTrieLongestMatch(Map<Object, HTrieNode> root, char[] item) {

        if (root == null) {
            return -1; // abnormal return.
        }

        // get the root
        HTrieNode node = root.get(Character.toLowerCase(item[0]));

        // loop through item[]
        // if loop ended and final node is not null, then return true,
        // otherwise return null;
        for (int i = 1; i < item.length; i++) {

            if (node != null) {
                if (node.isLeafObject()) {
                    return i;
                }
            } else {
                return i-1;
            }
            node = node.getNode(Character.toLowerCase(item[i]));
        }

        if (node != null) {
            return item.length;
        }
        return item.length - 1;
    }


}
