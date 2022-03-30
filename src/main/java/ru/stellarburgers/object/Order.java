package ru.stellarburgers.object;

import java.util.ArrayList;
import java.util.List;

public class Order {
    List <Ingredient> ingredients = new ArrayList<>();


    public Order(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
