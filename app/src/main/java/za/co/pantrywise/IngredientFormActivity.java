package za.co.pantrywise;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

/** Add and edit form with keyboard types, date picker and validation. */
public class IngredientFormActivity extends Activity {
    private DatabaseHelper db;
    private long ingredientId = -1;
    private EditText nameInput, quantityInput, unitInput, expiryInput;

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        Ui.applySystemBars(this);
        db = new DatabaseHelper(this);
        ingredientId = getIntent().getLongExtra("ingredient_id", -1);
        buildScreen();
        if (ingredientId != -1) loadExisting();
    }

    private void buildScreen() {
        LinearLayout root = Ui.page(this);
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        LinearLayout content = Ui.column(this);

        Button back = Ui.button(this, "←  Back to pantry", false);
        back.setOnClickListener(v -> finish());
        content.addView(back, Ui.matchWrap(this));
        Ui.gap(content, this, 18);
        content.addView(Ui.eyebrow(this, "MY PANTRY"));
        content.addView(Ui.heading(this, ingredientId == -1 ? "Add an item" : "Edit item"));
        content.addView(Ui.subheading(this, "Record the amount you have at home. Accurate quantities help PantryWise suggest only recipes you can really make."));

        LinearLayout form = Ui.card(this);
        form.setPadding(Ui.dp(this, 16), Ui.dp(this, 8), Ui.dp(this, 16), Ui.dp(this, 16));
        nameInput = addField(form, "Ingredient name", "For example, tomatoes", InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        quantityInput = addField(form, "Quantity available", "For example, 3 or 250", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        unitInput = addField(form, "Unit of measurement", "Use pcs, g, ml or slices", InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        TextView expiryLabel = Ui.text(this, "Expiry date (optional)", 13, Ui.INK, true);
        expiryLabel.setPadding(0, Ui.dp(this, 14), 0, Ui.dp(this, 6));
        form.addView(expiryLabel);
        LinearLayout expiryRow = new LinearLayout(this);
        expiryRow.setGravity(Gravity.CENTER_VERTICAL);
        expiryInput = Ui.input(this, "Choose a date", InputType.TYPE_CLASS_TEXT);
        expiryInput.setFocusable(false);
        expiryInput.setClickable(true);
        expiryInput.setOnClickListener(v -> showDatePicker());
        expiryRow.addView(expiryInput, new LinearLayout.LayoutParams(0, -2, 1));
        Button clearDate = Ui.smallButton(this, "Clear", false);
        LinearLayout.LayoutParams clearParams = new LinearLayout.LayoutParams(-2, -2);
        clearParams.leftMargin = Ui.dp(this, 8);
        expiryRow.addView(clearDate, clearParams);
        clearDate.setOnClickListener(v -> expiryInput.setText(""));
        form.addView(expiryRow);
        content.addView(form);
        Ui.gap(content, this, 14);

        LinearLayout note = Ui.card(this);
        note.setBackground(Ui.tint(Ui.PALE, 14, Ui.PALE));
        note.addView(Ui.text(this, "QUANTITY TIP", 11, Ui.GREEN, true));
        note.addView(Ui.text(this, "Use consistent units for each ingredient. For example, record cheese in grams and milk in millilitres so recipe matching can compare amounts correctly.", 13, Ui.MUTED, false));
        content.addView(note);
        Ui.gap(content, this, 16);

        Button save = Ui.button(this, ingredientId == -1 ? "Save ingredient" : "Save changes", true);
        save.setOnClickListener(v -> saveIngredient());
        content.addView(save, Ui.matchWrap(this));
        if (ingredientId != -1) {
            Ui.gap(content, this, 8);
            Button cancel = Ui.button(this, "Cancel", false);
            cancel.setOnClickListener(v -> finish());
            content.addView(cancel, Ui.matchWrap(this));
        }
        scroll.addView(content);
        root.addView(scroll, new LinearLayout.LayoutParams(-1, -1));
        setContentView(root);
    }

    private EditText addField(LinearLayout parent, String label, String hint, int inputType) {
        TextView labelView = Ui.text(this, label, 13, Ui.INK, true);
        labelView.setPadding(0, Ui.dp(this, 14), 0, Ui.dp(this, 6));
        parent.addView(labelView);
        EditText input = Ui.input(this, hint, inputType);
        parent.addView(input, Ui.matchWrap(this));
        return input;
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (picker, year, month, day) ->
                expiryInput.setText(String.format(Locale.ROOT, "%04d-%02d-%02d", year, month + 1, day)),
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void loadExisting() {
        Ingredient item = db.getIngredient(ingredientId);
        if (item == null) {
            Toast.makeText(this, "This pantry item could not be found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        nameInput.setText(item.name);
        quantityInput.setText(formatQuantity(item.quantity));
        unitInput.setText(item.unit);
        expiryInput.setText(item.expiryDate == null ? "" : item.expiryDate);
    }

    private void saveIngredient() {
        String name = nameInput.getText().toString().trim();
        String qtyText = quantityInput.getText().toString().trim().replace(',', '.');
        String unit = unitInput.getText().toString().trim();
        String expiry = expiryInput.getText().toString().trim();

        if (name.isEmpty()) { nameInput.setError("Enter an ingredient name"); nameInput.requestFocus(); return; }
        if (qtyText.isEmpty()) { quantityInput.setError("Enter the amount you have"); quantityInput.requestFocus(); return; }
        if (unit.isEmpty()) { unitInput.setError("Enter a unit, e.g. pcs, g or ml"); unitInput.requestFocus(); return; }

        double qty;
        try { qty = Double.parseDouble(qtyText); }
        catch (NumberFormatException ex) { quantityInput.setError("Enter a valid number"); quantityInput.requestFocus(); return; }
        if (Double.isNaN(qty) || Double.isInfinite(qty) || qty <= 0) {
            quantityInput.setError("Quantity must be greater than zero"); quantityInput.requestFocus(); return;
        }
        if (!expiry.isEmpty() && !isValidDate(expiry)) {
            expiryInput.setError("Choose a valid date using the date picker"); return;
        }

        long result;
        if (ingredientId == -1) result = db.addIngredient(name, qty, unit, expiry);
        else result = db.updateIngredient(ingredientId, name, qty, unit, expiry);
        if (result > 0) {
            Toast.makeText(this, ingredientId == -1 ? "Ingredient added to your pantry." : "Pantry item updated.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "The item could not be saved. Please try again.", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isValidDate(String value) {
        if (!value.matches("\\d{4}-\\d{2}-\\d{2}")) return false;
        try {
            String[] parts = value.split("-");
            Calendar calendar = Calendar.getInstance();
            calendar.setLenient(false);
            calendar.clear();
            calendar.set(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]) - 1, Integer.parseInt(parts[2]));
            calendar.getTime();
            return true;
        } catch (Exception ex) { return false; }
    }

    private String formatQuantity(double quantity) {
        return quantity == Math.rint(quantity) ? String.valueOf((long) quantity) : String.valueOf(quantity);
    }
}
