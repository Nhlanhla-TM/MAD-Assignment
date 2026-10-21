package za.co.pantrywise;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/** Custom adapter displaying pantry records and visible edit/delete actions. */
public class IngredientAdapter extends BaseAdapter {
    public interface Actions {
        void onEdit(Ingredient item);
        void onDelete(Ingredient item);
    }

    private final Activity activity;
    private final Actions actions;
    private final List<Ingredient> items = new ArrayList<>();

    public IngredientAdapter(Activity activity, List<Ingredient> items, Actions actions) {
        this.activity = activity;
        this.actions = actions;
        setItems(items);
    }

    public void setItems(List<Ingredient> updatedItems) {
        items.clear();
        items.addAll(updatedItems);
        notifyDataSetChanged();
    }

    @Override public int getCount() { return items.size(); }
    @Override public Object getItem(int position) { return items.get(position); }
    @Override public long getItemId(int position) { return items.get(position).id; }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        Ingredient item = items.get(position);
        LinearLayout row = Ui.card(activity);
        row.setPadding(Ui.dp(activity, 13), Ui.dp(activity, 11), Ui.dp(activity, 13), Ui.dp(activity, 10));

        LinearLayout titleRow = new LinearLayout(activity);
        titleRow.setOrientation(LinearLayout.HORIZONTAL);
        titleRow.setGravity(android.view.Gravity.CENTER_VERTICAL);
        TextView name = Ui.text(activity, item.name, 16, Ui.INK, true);
        titleRow.addView(name, new LinearLayout.LayoutParams(0, -2, 1));
        TextView quantity = Ui.text(activity, formatQuantity(item.quantity) + " " + item.unit, 13, Ui.GREEN, true);
        quantity.setGravity(android.view.Gravity.END | android.view.Gravity.CENTER_VERTICAL);
        titleRow.addView(quantity);
        row.addView(titleRow);

        boolean showExpiry = activity.getSharedPreferences("pantrywise_settings", 0)
                .getBoolean("show_expiry_dates", true);
        if (showExpiry && item.expiryDate != null && !item.expiryDate.trim().isEmpty()) {
            TextView expiry = Ui.text(activity, "Best before  ·  " + item.expiryDate, 12, Ui.MUTED, false);
            expiry.setPadding(0, Ui.dp(activity, 3), 0, 0);
            row.addView(expiry);
        }

        LinearLayout actionsRow = new LinearLayout(activity);
        actionsRow.setGravity(android.view.Gravity.END | android.view.Gravity.CENTER_VERTICAL);
        Button edit = Ui.smallButton(activity, "Edit", false);
        Button delete = Ui.smallButton(activity, "Delete", true);
        LinearLayout.LayoutParams actionParams = new LinearLayout.LayoutParams(-2, -2);
        actionParams.leftMargin = Ui.dp(activity, 7);
        actionsRow.addView(edit, actionParams);
        actionsRow.addView(delete, actionParams);
        edit.setOnClickListener(v -> actions.onEdit(item));
        delete.setOnClickListener(v -> actions.onDelete(item));
        row.addView(actionsRow);
        return row;
    }

    private String formatQuantity(double quantity) {
        if (quantity == Math.rint(quantity)) return String.valueOf((long) quantity);
        return String.valueOf(quantity);
    }
}
