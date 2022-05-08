package com.healthy_lifestyle.aip_lifestyle.foodList;

public class FoodListModel {
    private String type;
    private String representatives;
    private String image;

    public FoodListModel() {
    }

    public FoodListModel(String type, String representatives, String image) {
        this.type = type;
        this.representatives = representatives;
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRepresentatives() {
        return representatives;
    }

    public void setRepresentatives(String representatives) {
        this.representatives = representatives;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
