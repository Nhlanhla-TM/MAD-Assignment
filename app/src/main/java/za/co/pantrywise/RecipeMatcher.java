package za.co.pantrywise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Strict recipe matching: every required ingredient must exist in the pantry
 * with a compatible unit and enough quantity. Partial matches never qualify.
 */
public class RecipeMatcher {
    public static List<Recipe> findStrictMatches(List<Recipe> recipes, List<Ingredient> pantry) {
        // Keep amounts separated by unit so grams are never accidentally added to pieces.
        Map<String, Map<String, Double>> pantryAmounts = new HashMap<>();
        for (Ingredient item : pantry) {
            String ingredientKey = normalize(item.name);
            String unitKey = normalizeUnit(item.unit);
            if (ingredientKey.isEmpty() || unitKey.isEmpty()) continue;
            Map<String, Double> unitAmounts = pantryAmounts.get(ingredientKey);
            if (unitAmounts == null) {
                unitAmounts = new HashMap<>();
                pantryAmounts.put(ingredientKey, unitAmounts);
            }
            double current = unitAmounts.containsKey(unitKey) ? unitAmounts.get(unitKey) : 0.0;
            unitAmounts.put(unitKey, current + item.quantity);
        }

        List<Recipe> matches = new ArrayList<>();
        for (Recipe recipe : recipes) {
            List<String> names = recipe.ingredientNames();
            List<String> quantities = recipe.ingredientQuantities();
            boolean qualifies = true;
            for (int i = 0; i < names.size(); i++) {
                String ingredientKey = normalize(names.get(i));
                Map<String, Double> availableUnits = pantryAmounts.get(ingredientKey);
                if (availableUnits == null) { qualifies = false; break; }

                String[] required = quantities.get(i).trim().split("\\s+", 2);
                double requiredAmount;
                try { requiredAmount = Double.parseDouble(required[0]); }
                catch (Exception ex) { qualifies = false; break; }
                String requiredUnit = required.length > 1 ? normalizeUnit(required[1]) : "";
                double available = availableUnits.containsKey(requiredUnit) ? availableUnits.get(requiredUnit) : 0.0;
                if (requiredUnit.isEmpty() || available < requiredAmount) {
                    qualifies = false;
                    break;
                }
            }
            if (qualifies) matches.add(recipe);
        }
        return matches;
    }

    private static String normalizeUnit(String unit) {
        String value = unit == null ? "" : unit.trim().toLowerCase(Locale.ROOT);
        if (value.equals("piece") || value.equals("pieces") || value.equals("pc") || value.equals("pcs")) return "pcs";
        if (value.equals("gram") || value.equals("grams") || value.equals("g")) return "g";
        if (value.equals("millilitre") || value.equals("millilitres") || value.equals("milliliter") || value.equals("milliliters") || value.equals("ml")) return "ml";
        if (value.equals("slice") || value.equals("slices")) return "slices";
        if (value.equals("kilogram") || value.equals("kilograms") || value.equals("kg")) return "kg";
        if (value.equals("litre") || value.equals("litres") || value.equals("liter") || value.equals("liters") || value.equals("l")) return "l";
        return value;
    }

    /** Normalises case and common singular/plural variants (tomatoes -> tomato). */
    public static String normalize(String name) {
        String value = name == null ? "" : name.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
        if (value.endsWith("ies") && value.length() > 4) value = value.substring(0, value.length() - 3) + "y";
        else if (value.endsWith("oes") && value.length() > 4) value = value.substring(0, value.length() - 2);
        else if (value.endsWith("s") && !value.endsWith("ss") && value.length() > 3) value = value.substring(0, value.length() - 1);
        return value;
    }
}
