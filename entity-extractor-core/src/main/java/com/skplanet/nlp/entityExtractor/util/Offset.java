package com.skplanet.nlp.entityExtractor.util;

import org.apache.log4j.Logger;

/**
 * Utility for encapsulating offset and related object.
 *
 * Created by Donghun Shin
 * Contact: donghun.shin@sk.com, sindongboy@gmail.com
 * Date: 2/14/13
 */
public class Offset {
	private static Logger logger = Logger.getLogger(Offset.class.getName());

    private int start;
    private int end;

    private Object value;

    public Offset() {
        this.start = 0;
        this.end = 0;
        this.value = null;
    }

    public Offset(int start, int end, Object value) {
        if (start < 0 || end <0)
            throw new IllegalArgumentException("start and end index must be zero or greater!");

        if (start > end)
            throw new IllegalArgumentException("start index must not be larger than end index!");

        this.start = start;
        this.end = end;
        this.value = value;
    }

    public int getStart() {
        return this.start;
    }

    public int getEnd() {
        return this.end;
    }

    public Object getValue() {
        return this.value;
    }

    public void setStart(int i) {
        this.start = i;
    }

    public void setEnd(int i) {
        this.end = i;
    }

    public void setValue(Object val) {
        this.value = val;
    }

    public int length() {
        return this.end - this.start;
    }

}
