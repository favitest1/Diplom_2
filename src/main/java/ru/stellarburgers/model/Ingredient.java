package ru.stellarburgers.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Ingredient {
    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("proteins")
    @Expose
    private String proteins;

    @SerializedName("fat")
    @Expose
    private String fat;

    @SerializedName("carbohydrates")
    @Expose
    private String carbohydrates;

    @SerializedName("calories")
    @Expose
    private String calories;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("image_mobile")
    @Expose
    private String imageMobile;

    @SerializedName("image_large")
    @Expose
    private String imageLarge;

    @SerializedName("__v")
    @Expose
    private String v;


    public Ingredient(String id, String name, String type, String proteins, String fat, String carbohydrates, String calories, String price, String image, String imageMobile, String imageLarge, String v) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.proteins = proteins;
        this.fat = fat;
        this.carbohydrates = carbohydrates;
        this.calories = calories;
        this.price = price;
        this.image = image;
        this.imageMobile = imageMobile;
        this.imageLarge = imageLarge;
        this.v = v;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProteins() {
        return proteins;
    }

    public void setProteins(String proteins) {
        this.proteins = proteins;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(String carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageMobile() {
        return imageMobile;
    }

    public void setImageMobile(String imageMobile) {
        this.imageMobile = imageMobile;
    }

    public String getImageLarge() {
        return imageLarge;
    }

    public void setImageLarge(String imageLarge) {
        this.imageLarge = imageLarge;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }
}
