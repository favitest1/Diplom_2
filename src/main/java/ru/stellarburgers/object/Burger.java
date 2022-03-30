package ru.stellarburgers.object;

import java.util.ArrayList;

public class Burger {

    private ArrayList<Object> ingredients;

    private ArrayList<Ingredient> ingredientsData;

    public Burger(ArrayList<Object> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<Object> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Object> ingredients) {
        this.ingredients = ingredients;
    }
}
