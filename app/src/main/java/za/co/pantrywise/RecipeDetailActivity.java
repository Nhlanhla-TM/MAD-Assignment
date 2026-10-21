package za.co.pantrywise;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

/** Recipe detail page with ingredient quantities and numbered preparation steps. */
public class RecipeDetailActivity extends Activity {
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        Ui.applySystemBars(this);
        DatabaseHelper db = new DatabaseHelper(this);
        Recipe recipe = db.getRecipe(getIntent().getIntExtra("recipe_id", -1));

        LinearLayout root = Ui.page(this);
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        LinearLayout content = Ui.column(this);
        Button back = Ui.button(this, "←  Back to recipes", false);
        back.setOnClickListener(v -> finish());
        content.addView(back, Ui.matchWrap(this));
        Ui.gap(content, this, 18);

        if (recipe == null) {
            content.addView(Ui.heading(this, "Recipe unavailable"));
            content.addView(Ui.subheading(this, "This recipe could not be found in the local database. Return to suggestions and choose another recipe."));
        } else {
            content.addView(Ui.eyebrow(this, "YOUR RECIPE"));
            content.addView(Ui.heading(this, recipe.name));
            content.addView(Ui.subheading(this, recipe.description));

            LinearLayout meta = Ui.heroCard(this);
            meta.addView(Ui.text(this, "READY IN  " + recipe.minutes + " MINUTES", 13, Ui.WHITE, true));
            meta.addView(Ui.text(this, "Made from the ingredients in your pantry", 13, 0xFFE0ECE2, false));
            content.addView(meta);
            Ui.gap(content, this, 20);

            content.addView(Ui.sectionHeading(this, "Ingredients"));
            content.addView(Ui.subheading(this, "Check the quantities before you start cooking."));
            List<String> names = recipe.ingredientNames();
            List<String> quantities = recipe.ingredientQuantities();
            for (int i = 0; i < names.size(); i++) {
                LinearLayout row = Ui.card(this);
                row.setOrientation(LinearLayout.HORIZONTAL);
                TextView bullet = Ui.text(this, "✓", 16, Ui.GREEN, true);
                row.addView(bullet, new LinearLayout.LayoutParams(Ui.dp(this, 27), -2));
                TextView ingredient = Ui.text(this, names.get(i), 14, Ui.INK, true);
                row.addView(ingredient, new LinearLayout.LayoutParams(0, -2, 1));
                TextView amount = Ui.text(this, quantities.get(i), 13, Ui.GREEN, true);
                row.addView(amount);
                content.addView(row);
                Ui.gap(content, this, 7);
            }
            Ui.gap(content, this, 15);
            content.addView(Ui.sectionHeading(this, "Preparation"));
            content.addView(Ui.subheading(this, "Follow each step in order."));
            String[] steps = recipe.steps.split("\\n");
            int stepNumber = 1;
            for (String step : steps) {
                String cleanStep = step.replaceFirst("^\\s*\\d+\\.\\s*", "").trim();
                LinearLayout row = Ui.card(this);
                row.setOrientation(LinearLayout.HORIZONTAL);
                TextView number = Ui.text(this, String.valueOf(stepNumber), 14, Ui.WHITE, true);
                number.setGravity(android.view.Gravity.CENTER);
                number.setBackground(Ui.tint(Ui.GREEN, 12, Ui.GREEN));
                row.addView(number, new LinearLayout.LayoutParams(Ui.dp(this, 30), Ui.dp(this, 30)));
                TextView stepText = Ui.text(this, cleanStep, 14, Ui.INK, false);
                stepText.setGravity(android.view.Gravity.TOP);
                stepText.setPadding(Ui.dp(this, 10), Ui.dp(this, 3), 0, 0);
                row.addView(stepText, new LinearLayout.LayoutParams(0, -2, 1));
                content.addView(row);
                Ui.gap(content, this, 8);
                stepNumber++;
            }
        }

        scroll.addView(content);
        root.addView(scroll, new LinearLayout.LayoutParams(-1, -1));
        setContentView(root);
    }
}
