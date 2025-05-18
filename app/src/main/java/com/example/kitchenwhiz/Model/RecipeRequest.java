package com.example.kitchenwhiz.Model;

public class RecipeRequest {
    private String image;
    private String recipeInfo;

    public RecipeRequest(String image, String recipeInfo) {
        this.image = image;
        this.recipeInfo = recipeInfo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRecipeInfo() {
        return recipeInfo;
    }

    public void setRecipeInfo(String recipeInfo) {
        this.recipeInfo = recipeInfo;
    }
}
