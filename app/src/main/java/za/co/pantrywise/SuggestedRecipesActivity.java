package za.co.pantrywise;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

/** Displays only recipes that pass the strict ingredient-and-quantity matcher. */
public class SuggestedRecipesActivity extends Activity {
    private DatabaseHelper db;
    private List<Recipe> matches;

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        Ui.applySystemBars(this);
        db = new DatabaseHelper(this);
        buildScreen();
    }

    @Override protected void onResume() {
        super.onResume();
        if (db != null) buildScreen();
    }

    private void buildScreen() {
        matches = RecipeMatcher.findStrictMatches(db.getRecipes(), db.getIngredients());
        LinearLayout root = Ui.page(this);
        Button back = Ui.button(this, "←  Back to pantry", false);
        back.setOnClickListener(v -> finish());
        root.addView(back, Ui.matchWrap(this));
        Ui.gap(root, this, 16);
        root.addView(Ui.eyebrow(this, "COOK WITH WHAT YOU HAVE"));
        root.addView(Ui.heading(this, "Suggested recipes"));
        root.addView(Ui.subheading(this, "Every recipe shown here has all its required ingredients and quantities in your pantry. Partial matches are excluded."));

        LinearLayout summary = Ui.card(this);
        summary.setOrientation(LinearLayout.HORIZONTAL);
        summary.setGravity(Gravity.CENTER_VERTICAL);
        TextView number = Ui.text(this, String.valueOf(matches.size()), 30, Ui.GREEN, true);
        summary.addView(number, new LinearLayout.LayoutParams(Ui.dp(this, 54), -2));
        LinearLayout details = Ui.column(this);
        details.addView(Ui.text(this, matches.size() == 1 ? "recipe ready to make" : "recipes ready to make", 16, Ui.INK, true));
        details.addView(Ui.text(this, "Strict matching is switched on", 12, Ui.MUTED, false));
        summary.addView(details);
        root.addView(summary);
        Ui.gap(root, this, 12);

        if (matches.isEmpty()) {
            LinearLayout empty = Ui.card(this);
            empty.addView(Ui.text(this, "Nothing to cook just yet", 19, Ui.INK, true));
            empty.addView(Ui.text(this, "No recipe matches every ingredient and required quantity. Add more ingredients or update a quantity in My Pantry, then check again.", 14, Ui.MUTED, false));
            Ui.gap(empty, this, 10);
            Button add = Ui.button(this, "+ Add ingredients", true);
            add.setOnClickListener(v -> startActivity(new Intent(this, IngredientFormActivity.class)));
            empty.addView(add);
            root.addView(empty);
        } else {
            TextView hint = Ui.text(this, "Select a recipe to view ingredients and cooking steps.", 12, Ui.MUTED, false);
            root.addView(hint);
            Ui.gap(root, this, 7);
            ListView list = new ListView(this);
            list.setDivider(null);
            list.setDividerHeight(Ui.dp(this, 9));
            list.setBackgroundColor(Ui.CREAM);
            list.setCacheColorHint(Ui.CREAM);
            list.setAdapter(new RecipeAdapter(this, matches));
            list.setOnItemClickListener((p, v, position, id) -> {
                Intent i = new Intent(this, RecipeDetailActivity.class);
                i.putExtra("recipe_id", matches.get(position).id);
                startActivity(i);
            });
            root.addView(list, new LinearLayout.LayoutParams(-1, 0, 1));
        }
        setContentView(root);
    }
}
