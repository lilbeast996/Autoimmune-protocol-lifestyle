package com.healthy_lifestyle.aip_lifestyle;

import java.util.List;

public class RecipeModel {
    private String title, description, image,  recipe, prep;
    private List<String> ingredients;
    private String  recipeKey, favouriteKey;

    public RecipeModel(String title, String description, String image, String recipe, String prep, List<String> ingredients, String recipeKey, String favouriteKey) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.recipe = recipe;
        this.prep = prep;
        this.ingredients = ingredients;
        this.recipeKey = recipeKey;
        this.favouriteKey = favouriteKey;
    }

    public RecipeModel(String title, String description, String recipe, String prep, List<String> ingredients, String recipeKey) {
        this.title = title;
        this.description = description;
        this.recipe = recipe;
        this.prep = prep;
        this.ingredients = ingredients;
        this.recipeKey = recipeKey;
    }

    public RecipeModel(String title, String description, String image, String recipe, String prep, List<String> ingredients) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.recipe = recipe;
        this.prep = prep;
        this.ingredients = ingredients;
    }

    public RecipeModel(String title, String description, String image, String recipe, String prep, List<String> ingredients, String recipeKey) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.recipe = recipe;
        this.prep = prep;
        this.ingredients = ingredients;
        this.recipeKey = recipeKey;
    }

    public RecipeModel(String title, String description, String recipe, String prep, List<String> ingredients) {
        this.title = title;
        this.description = description;
        this.recipe = recipe;
        this.prep = prep;
        this.ingredients = ingredients;
    }

    public RecipeModel() {
    }


    public String getFavouriteKey() {
        return favouriteKey;
    }

    public void setFavouriteKey(String favouriteKey) {
        this.favouriteKey = favouriteKey;
    }

    public String getRecipeKey() {
        return recipeKey;
    }

    public void setRecipeKey(String recipeKey) {
        this.recipeKey = recipeKey;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrep() {
        return prep;
    }

    public void setPrep(String prep) {
        this.prep = prep;
    }
}
