package com.example.kitchenwhiz.Model;

import java.util.Date;
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
    int likes;
    Date view_at;

    public RecipeModel() {
        this._id = "";
        this.title = "Default Recipe";
        this.image = "";
        this.servings = 0;
        this.summary = "";
        this.ready_in_minutes = 0;
        this.ingredients = null;
        this.instructions = "";
        this.likes = 0;

    }

    public RecipeModel(String _id, String title, String image, int servings, int readyInMinutes, List<Ingredients> ingredients, String instructions, int liked, Date view_at, String summary) {
        this._id = _id;
        this.title = title;
        this.image = image;
        this.servings = servings;
        this.ready_in_minutes = readyInMinutes;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.likes = liked;
        this.view_at = view_at;
        this.summary = summary;
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

    public Date getView_at() {
        return view_at;
    }

    public int getLikes() {
        return likes;
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
