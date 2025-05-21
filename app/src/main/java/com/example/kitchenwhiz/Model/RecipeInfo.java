package com.example.kitchenwhiz.Model;

import androidx.annotation.Nullable;

import java.util.List;

public class RecipeInfo {
    private String title;
    private int servings;
    private int ready_in_minutes;
    private String summary;
    private String instructions;
    private List<Ingredients> ingredients;

    public RecipeInfo(String title, int servings, int readyInMinutes, String summary, String instructions, List<Ingredients> ingredients) {
        this.title = title;
        this.servings = servings;
        this.ready_in_minutes = readyInMinutes;
        this.summary = summary;
        this.instructions = instructions;
        this.ingredients = ingredients;
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

    public String getSummary() {
        return summary;
    }

    public String getTitle() {
        return title;
    }

    public List<Ingredients> getIngredients() {
        return ingredients;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setIngredients(List<Ingredients> ingredients) {
        this.ingredients = ingredients;
    }

    public void setReadyInMinutes(int readyInMinutes) {
        this.ready_in_minutes = readyInMinutes;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
