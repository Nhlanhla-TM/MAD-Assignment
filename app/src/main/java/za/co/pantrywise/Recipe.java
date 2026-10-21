package za.co.pantrywise;

import java.util.Arrays;
import java.util.List;

public class Recipe {
    public final int id;
    public final String name;
    public final String description;
    public final String ingredients;
    public final String quantities;
    public final String steps;
    public final int minutes;

    public Recipe(int id, String name, String description, String ingredients,
                  String quantities, String steps, int minutes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.quantities = quantities;
        this.steps = steps;
        this.minutes = minutes;
    }

    public List<String> ingredientNames() {
        return Arrays.asList(ingredients.split("\\|"));
    }

    public List<String> ingredientQuantities() {
        return Arrays.asList(quantities.split("\\|"));
    }
}
