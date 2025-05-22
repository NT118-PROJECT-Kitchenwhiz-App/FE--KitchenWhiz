package com.example.kitchenwhiz.Model;

import java.util.List;

public class RecipeModel {
    String _id;
    String title;
    String image;
    int servings;

    String summary;
    String instructions;
    int ready_in_minutes;
    List<Ingredients> ingredients;

    public RecipeModel() {
        this._id = "";
        this.title = "Default Recipe";
        this.image = "";
        this.servings = 0;
        this.ready_in_minutes = 0;
        this.ingredients = null;
        this.instructions = "";
    }

    public RecipeModel(String _id, String title, String image, int servings, int readyInMinutes, List<Ingredients> ingredients, String instructions) {
        this._id = _id;
        this.title = title;
        this.image = image;
        this.servings = servings;
        this.ready_in_minutes = readyInMinutes;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }
    public String getImage() {
        return image;
    }

    public String getId() {
        return _id;
    }

    public String getTitle() {
        return title;
    }

    public List<Ingredients> getIngredients() {
        return ingredients;
    }

    public String getSummary() {
        return summary;
    }

    public String getInstructions() {
        return instructions;
    }

    public int getReadyInMinutes() {
        return ready_in_minutes;
    }

    public int getServings() {
        return servings;
    }

}
