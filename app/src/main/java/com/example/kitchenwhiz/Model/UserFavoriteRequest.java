package com.example.kitchenwhiz.Model;

public class UserFavoriteRequest {
    String user_id;
    String recipe_id;

    public UserFavoriteRequest(String user_id, String recipe_id) {
        this.user_id = user_id;
        this.recipe_id = recipe_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getRecipe_id() {
        return recipe_id;
    }
}
