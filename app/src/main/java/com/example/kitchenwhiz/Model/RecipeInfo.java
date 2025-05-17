package com.example.kitchenwhiz.Model;

import androidx.annotation.Nullable;

import java.util.List;

public class RecipeInfo {
    private String title;
    private String servings;
    private String readyInMinutes;
    private String summary;
    private List<Ingredients> instructions;

    public RecipeInfo(String title, String servings, String readyInMinutes, String summary, List<Ingredients> instructions) {
        this.title = title;
        this.servings = servings;
        this.readyInMinutes = readyInMinutes;
        this.summary = summary;
        this.instructions = instructions;
    }
    public List<Ingredients> getInstructions() {
        return instructions;
    }

    public String getReadyInMinutes() {
        return readyInMinutes;
    }

    public String getServings() {
        return servings;
    }

    public String getSummary() {
        return summary;
    }

    public String getTitle() {
        return title;
    }

    public void setInstructions(List<Ingredients> instructions) {
        this.instructions = instructions;
    }

    public void setReadyInMinutes(String readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
