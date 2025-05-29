package com.example.kitchenwhiz.Model;

public class UserFavoriteRequest {
    String recipe_id;

    public UserFavoriteRequest(String recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getRecipe_id() {
        return recipe_id;
    }
}
