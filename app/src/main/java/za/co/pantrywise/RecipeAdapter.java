package za.co.pantrywise;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

/** Custom ListView adapter for the strictly matched recipes. */
public class RecipeAdapter extends BaseAdapter {
    private final Activity activity;
    private final List<Recipe> items;

    public RecipeAdapter(Activity activity, List<Recipe> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override public int getCount() { return items.size(); }
    @Override public Object getItem(int position) { return items.get(position); }
    @Override public long getItemId(int position) { return items.get(position).id; }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        Recipe recipe = items.get(position);
        LinearLayout row = Ui.card(activity);
        row.setPadding(Ui.dp(activity, 15), Ui.dp(activity, 13), Ui.dp(activity, 15), Ui.dp(activity, 13));
        TextView title = Ui.text(activity, recipe.name, 16, Ui.INK, true);
        TextView desc = Ui.text(activity, recipe.description, 13, Ui.MUTED, false);
        desc.setPadding(0, Ui.dp(activity, 4), 0, Ui.dp(activity, 7));
        desc.setLineSpacing(Ui.dp(activity, 1), 1.05f);
        TextView meta = Ui.text(activity, "◷  " + recipe.minutes + " min     ·     " + recipe.ingredientNames().size() + " ingredients", 12, Ui.GREEN, true);
        row.addView(title);
        row.addView(desc);
        row.addView(meta);
        return row;
    }
}
