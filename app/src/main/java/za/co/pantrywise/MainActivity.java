package za.co.pantrywise;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Pantry dashboard: database-backed list, search, add/edit/delete and recipe navigation. */
public class MainActivity extends Activity {
    private DatabaseHelper db;
    private LinearLayout root;
    private List<Ingredient> ingredients = new ArrayList<>();
    private IngredientAdapter adapter;
    private EditText searchInput;
    private ListView pantryList;
    private TextView countText;
    private TextView emptyText;

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        Ui.applySystemBars(this);
        db = new DatabaseHelper(this);
        buildScreen();
    }

    @Override protected void onResume() {
        super.onResume();
        if (db != null && root != null) refreshPantry();
    }

    private void buildScreen() {
        ingredients = db.getIngredients();
        root = Ui.page(this);
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        LinearLayout content = Ui.column(this);

        LinearLayout brandRow = new LinearLayout(this);
        brandRow.setGravity(Gravity.CENTER_VERTICAL);
        TextView mark = Ui.text(this, "P", 22, Ui.WHITE, true);
        mark.setGravity(Gravity.CENTER);
        mark.setBackground(Ui.tint(Ui.GREEN, 13, Ui.GREEN));
        brandRow.addView(mark, new LinearLayout.LayoutParams(Ui.dp(this, 42), Ui.dp(this, 42)));
        LinearLayout brandText = Ui.column(this);
        brandText.setPadding(Ui.dp(this, 10), 0, 0, 0);
        brandText.addView(Ui.text(this, "PantryWise", 18, Ui.INK, true));
        brandText.addView(Ui.text(this, "SMART PANTRY MANAGER", 10, Ui.MUTED, true));
        brandRow.addView(brandText, new LinearLayout.LayoutParams(0, -2, 1));
        Button settings = Ui.smallButton(this, "Settings  ⚙", false);
        settings.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
        brandRow.addView(settings);
        content.addView(brandRow);
        Ui.gap(content, this, 18);

        LinearLayout hero = Ui.heroCard(this);
        TextView welcome = Ui.text(this, "MAKE MORE OF\nWHAT YOU HAVE.", 25, Ui.WHITE, true);
        welcome.setLineSpacing(Ui.dp(this, 2), 1.0f);
        hero.addView(welcome);
        TextView heroText = Ui.text(this, "Find simple recipes using the ingredients already in your kitchen.", 14, 0xFFE0ECE2, false);
        heroText.setLineSpacing(Ui.dp(this, 2), 1.05f);
        heroText.setPadding(0, Ui.dp(this, 8), 0, Ui.dp(this, 14));
        hero.addView(heroText);
        Button suggest = Ui.button(this, "Find recipes I can make   →", true);
        suggest.setTextColor(Ui.GREEN_DARK);
        suggest.setBackground(Ui.tint(0xFFE7F1E6, 13, 0xFFE7F1E6));
        suggest.setOnClickListener(v -> startActivity(new Intent(this, SuggestedRecipesActivity.class)));
        hero.addView(suggest);
        content.addView(hero);
        Ui.gap(content, this, 14);

        LinearLayout stats = new LinearLayout(this);
        stats.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout statCard = Ui.card(this);
        statCard.addView(Ui.eyebrow(this, "Your pantry"));
        countText = Ui.text(this, ingredients.size() + " ingredients", 20, Ui.GREEN, true);
        statCard.addView(countText);
        statCard.addView(Ui.text(this, "Saved on this device", 12, Ui.MUTED, false));
        stats.addView(statCard, new LinearLayout.LayoutParams(0, -2, 1));
        LinearLayout statInfo = Ui.card(this);
        statInfo.addView(Ui.eyebrow(this, "Your goal"));
        statInfo.addView(Ui.text(this, "Waste less", 20, Ui.GREEN, true));
        statInfo.addView(Ui.text(this, "Cook with what you have", 12, Ui.MUTED, false));
        LinearLayout.LayoutParams second = new LinearLayout.LayoutParams(0, -2, 1);
        second.leftMargin = Ui.dp(this, 9);
        stats.addView(statInfo, second);
        content.addView(stats);
        Ui.gap(content, this, 20);

        LinearLayout headingRow = new LinearLayout(this);
        headingRow.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout headingTexts = Ui.column(this);
        headingTexts.addView(Ui.sectionHeading(this, "My pantry"));
        headingTexts.addView(Ui.text(this, "Keep your ingredients up to date", 12, Ui.MUTED, false));
        headingRow.addView(headingTexts, new LinearLayout.LayoutParams(0, -2, 1));
        Button add = Ui.button(this, "+ Add item", true);
        add.setMinHeight(Ui.dp(this, 40));
        add.setOnClickListener(v -> startActivity(new Intent(this, IngredientFormActivity.class)));
        headingRow.addView(add);
        content.addView(headingRow);
        Ui.gap(content, this, 10);

        searchInput = Ui.input(this, "Search ingredients...", android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        content.addView(searchInput, Ui.matchWrap(this));
        Ui.gap(content, this, 10);

        pantryList = new ListView(this);
        pantryList.setDivider(null);
        pantryList.setDividerHeight(Ui.dp(this, 9));
        pantryList.setBackgroundColor(Ui.CREAM);
        pantryList.setCacheColorHint(Ui.CREAM);
        adapter = new IngredientAdapter(this, ingredients, new IngredientAdapter.Actions() {
            @Override public void onEdit(Ingredient item) {
                Intent intent = new Intent(MainActivity.this, IngredientFormActivity.class);
                intent.putExtra("ingredient_id", item.id);
                startActivity(intent);
            }
            @Override public void onDelete(Ingredient item) { confirmDelete(item); }
        });
        pantryList.setAdapter(adapter);
        content.addView(pantryList, new LinearLayout.LayoutParams(-1, Ui.dp(this, Math.max(125, Math.min(ingredients.size(), 5) * 118))));
        emptyText = Ui.text(this, "Your pantry is empty. Add your first ingredient to get started.", 13, Ui.MUTED, false);
        emptyText.setGravity(Gravity.CENTER);
        emptyText.setPadding(Ui.dp(this, 12), Ui.dp(this, 12), Ui.dp(this, 12), Ui.dp(this, 12));
        emptyText.setBackground(Ui.tint(Ui.WHITE, 14, Ui.BORDER));
        content.addView(emptyText, Ui.matchWrap(this));
        TextView emptyHint = Ui.text(this, "Tip: use Edit to update an item, or Delete to remove it.", 12, Ui.MUTED, false);
        emptyHint.setPadding(0, Ui.dp(this, 8), 0, Ui.dp(this, 4));
        content.addView(emptyHint);

        TextView footer = Ui.text(this, "PLAN SMARTER  ·  COOK SIMPLER  ·  WASTE LESS", 10, Ui.MUTED, true);
        footer.setGravity(Gravity.CENTER);
        footer.setLetterSpacing(0.04f);
        footer.setPadding(0, Ui.dp(this, 18), 0, Ui.dp(this, 8));
        content.addView(footer);

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { filterPantry(s.toString()); }
            @Override public void afterTextChanged(Editable s) { }
        });

        scroll.addView(content);
        root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));
        setContentView(root);
    }

    private void refreshPantry() {
        ingredients = db.getIngredients();
        if (countText != null) countText.setText(ingredients.size() + (ingredients.size() == 1 ? " ingredient" : " ingredients"));
        filterPantry(searchInput == null ? "" : searchInput.getText().toString());
    }

    private void filterPantry(String query) {
        if (adapter == null) return;
        String q = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);
        List<Ingredient> filtered = new ArrayList<>();
        for (Ingredient item : ingredients) {
            if (item.name.toLowerCase(Locale.ROOT).contains(q)) filtered.add(item);
        }
        adapter.setItems(filtered);
        if (pantryList != null && pantryList.getLayoutParams() != null) {
            int rowCount = filtered.size();
            pantryList.getLayoutParams().height =
                    Ui.dp(this, rowCount * 118);
            pantryList.requestLayout();
        }
        if (emptyText != null) {
            if (filtered.isEmpty()) {
                emptyText.setText(q.isEmpty()
                        ? "Your pantry is empty. Add your first ingredient to get started."
                        : "No ingredients found for “" + query.trim() + "”. Try another search.");
                emptyText.setVisibility(View.VISIBLE);
            } else {
                emptyText.setVisibility(View.GONE);
            }
        }
    }

    private void confirmDelete(Ingredient item) {
        new AlertDialog.Builder(this)
                .setTitle("Delete " + item.name + "?")
                .setMessage("This will remove the ingredient from your saved pantry. You can add it again later.")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", (dialog, which) -> {
                    db.deleteIngredient(item.id);
                    Toast.makeText(this, item.name + " deleted", Toast.LENGTH_SHORT).show();
                    refreshPantry();
                }).show();
    }
}
