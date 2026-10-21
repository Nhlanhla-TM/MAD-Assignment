package za.co.pantrywise;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/** Settings are persisted locally using SharedPreferences. */
public class SettingsActivity extends Activity {
    private SharedPreferences preferences;

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        Ui.applySystemBars(this);
        preferences = getSharedPreferences("pantrywise_settings", MODE_PRIVATE);
        buildScreen();
    }

    private void buildScreen() {
        LinearLayout root = Ui.page(this);
        android.widget.ScrollView scroll = new android.widget.ScrollView(this);
        LinearLayout content = Ui.column(this);
        Button back = Ui.button(this, "←  Back to pantry", false);
        back.setOnClickListener(v -> finish());
        content.addView(back, Ui.matchWrap(this));
        Ui.gap(content, this, 18);
        content.addView(Ui.eyebrow(this, "PERSONALISE YOUR APP"));
        content.addView(Ui.heading(this, "Settings"));
        content.addView(Ui.subheading(this, "Manage how pantry information is displayed on this device."));

        LinearLayout displayCard = Ui.card(this);
        displayCard.addView(Ui.text(this, "Pantry display", 17, Ui.INK, true));
        TextView description = Ui.text(this, "Show the best-before date beneath ingredients that have an expiry date.", 13, Ui.MUTED, false);
        description.setPadding(0, Ui.dp(this, 4), 0, Ui.dp(this, 10));
        displayCard.addView(description);
        Switch expirySwitch = new Switch(this);
        expirySwitch.setText("Show expiry dates in pantry");
        expirySwitch.setTextSize(14);
        expirySwitch.setTextColor(Ui.INK);
        expirySwitch.setChecked(preferences.getBoolean("show_expiry_dates", true));
        expirySwitch.setOnCheckedChangeListener((button, checked) -> {
            preferences.edit().putBoolean("show_expiry_dates", checked).apply();
            Toast.makeText(this, checked ? "Expiry dates will be shown." : "Expiry dates are hidden from the list.", Toast.LENGTH_SHORT).show();
        });
        displayCard.addView(expirySwitch);
        content.addView(displayCard);
        Ui.gap(content, this, 12);

        LinearLayout storageCard = Ui.card(this);
        storageCard.addView(Ui.text(this, "Your data and privacy", 17, Ui.INK, true));
        storageCard.addView(Ui.text(this, "Pantry items are stored in a local SQLite database on this phone. Your saved ingredients remain available when you close and reopen the app. Uninstalling the app or clearing its storage may remove local data.", 13, Ui.MUTED, false));
        content.addView(storageCard);
        Ui.gap(content, this, 12);

        LinearLayout aboutCard = Ui.card(this);
        aboutCard.addView(Ui.text(this, "About PantryWise", 17, Ui.INK, true));
        aboutCard.addView(Ui.text(this, "Smart Pantry Manager", 14, Ui.GREEN, true));
        aboutCard.addView(Ui.text(this, "Mobile App Development 700\nBuilt with Java and SQLite\nVersion 1.0", 13, Ui.MUTED, false));
        content.addView(aboutCard);
        Ui.gap(content, this, 18);
        TextView footer = Ui.text(this, "PLAN SMARTER. WASTE LESS.", 11, Ui.MUTED, true);
        footer.setGravity(Gravity.CENTER);
        content.addView(footer);

        scroll.addView(content);
        root.addView(scroll, new LinearLayout.LayoutParams(-1, -1));
        setContentView(root);
    }
}
