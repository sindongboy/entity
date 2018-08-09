package com.skplanet.nlp.data;

/**
 * Product Entity Object
 *
 * @author Donghun Shin, donghun.shin@sk.com
 * @date 10/6/14.
 */
public class ProductEntity extends AbstractEntity {

    // start index
    private int startIndex = -1;
    // end index
    private int endIndex = -1;
    // matched string
    private String name = null;
    // model name
    private String model = null;
    // brand name
    private String brand = null;
    // maker name
    private String maker = null;
    // category name
    private String category = null;

    /**
     * constructor
     */
    public ProductEntity() {

    }

    /**
     * set start index
     *
     * @param startIndex start index
     */
    @Override
    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    /**
     * get start index
     *
     * @return start index
     */
    @Override
    public int getStartIndex() {
        return this.startIndex;
    }

    /**
     * set end index
     *
     * @param endIndex end index
     */
    @Override
    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    /**
     * get end index
     *
     * @return end index
     */
    @Override
    public int getEndIndex() {
        return this.endIndex;
    }

    /**
     * get model name
     *
     * @return model name
     */
    public String getModel() {
        return model;
    }

    /**
     * set model name
     *
     * @param model model name
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * get brand name
     *
     * @return brand name
     */
    public String getBrand() {
        return brand;
    }

    /**
     * set brand name
     *
     * @param brand brand name
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * get maker name
     *
     * @return maker name
     */
    public String getMaker() {
        return maker;
    }

    /**
     * set maker name
     *
     * @param maker maker name
     */
    public void setMaker(String maker) {
        this.maker = maker;
    }

    /**
     * get category name
     *
     * @return category name
     */
    public String getCategory() {
        return category;
    }

    /**
     * set category name
     *
     * @param category category name
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * get entity name
     *
     * @return entity name
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * set entity name
     *
     * @param name entity name
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * is Entity from title?
     *
     * @param isTitle true if the entity is from title, otherwise false;
     */
    @Override
    public void setIsTitle(boolean isTitle) {
        this.isTitle = isTitle;
    }

    /**
     * check if the entity extracted from title
     *
     * @return true if the entity extracted from title
     */
    @Override
    public boolean isTitle() {
        return this.isTitle;
    }
}
