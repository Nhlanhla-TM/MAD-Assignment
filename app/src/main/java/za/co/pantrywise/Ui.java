package za.co.pantrywise;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.Window;

/** Shared visual components keep PantryWise screens consistent and easy to maintain. */
public final class Ui {
    private Ui() { }

    public static final int GREEN = 0xFF245642;
    public static final int GREEN_DARK = 0xFF173D2F;
    public static final int PALE = 0xFFE7F0E8;
    public static final int CREAM = 0xFFF6F7F2;
    public static final int INK = 0xFF1D2922;
    public static final int MUTED = 0xFF66746A;
    public static final int WHITE = 0xFFFFFFFF;
    public static final int BORDER = 0xFFDCE5DC;
    public static final int ORANGE = 0xFF9A5B16;
    public static final int PALE_ORANGE = 0xFFFFF1DE;
    public static final int RED = 0xFFB23B35;


    public static void applySystemBars(Activity a) {
        Window window = a.getWindow();
        window.setStatusBarColor(GREEN_DARK);
        window.setNavigationBarColor(CREAM);
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
    }

    public static LinearLayout page(Activity a) {
        LinearLayout root = new LinearLayout(a);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(CREAM);
        root.setPadding(dp(a, 18), dp(a, 12), dp(a, 18), dp(a, 14));
        return root;
    }

    public static LinearLayout column(Activity a) {
        LinearLayout layout = new LinearLayout(a);
        layout.setOrientation(LinearLayout.VERTICAL);
        return layout;
    }

    public static TextView text(Activity a, String value, int size, int color, boolean bold) {
        TextView t = new TextView(a);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setGravity(Gravity.CENTER_VERTICAL);
        t.setIncludeFontPadding(true);
        if (bold) t.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        else t.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
        return t;
    }

    public static TextView eyebrow(Activity a, String value) {
        TextView t = text(a, value.toUpperCase(java.util.Locale.ROOT), 11, GREEN, true);
        t.setLetterSpacing(0.08f);
        t.setPadding(0, dp(a, 2), 0, dp(a, 5));
        return t;
    }

    public static TextView heading(Activity a, String value) {
        TextView t = text(a, value, 27, INK, true);
        t.setPadding(0, dp(a, 2), 0, dp(a, 5));
        return t;
    }

    public static TextView sectionHeading(Activity a, String value) {
        TextView t = text(a, value, 19, INK, true);
        t.setPadding(0, dp(a, 2), 0, dp(a, 4));
        return t;
    }

    public static TextView subheading(Activity a, String value) {
        TextView t = text(a, value, 14, MUTED, false);
        t.setGravity(Gravity.TOP);
        t.setLineSpacing(dp(a, 2), 1.05f);
        t.setPadding(0, 0, 0, dp(a, 12));
        return t;
    }

    public static Button button(Activity a, String label, boolean primary) {
        Button b = new Button(a);
        b.setText(label);
        b.setAllCaps(false);
        b.setTextSize(14);
        b.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        b.setTextColor(primary ? WHITE : GREEN);
        b.setMinHeight(dp(a, 44));
        b.setMinimumHeight(dp(a, 44));
        b.setPadding(dp(a, 14), 0, dp(a, 14), 0);
        b.setStateListAnimator(null);
        b.setBackground(tint(primary ? GREEN : PALE, 14, primary ? GREEN : BORDER));
        if (android.os.Build.VERSION.SDK_INT >= 21) b.setElevation(dp(a, primary ? 2 : 0));
        return b;
    }

    public static Button smallButton(Activity a, String label, boolean danger) {
        Button b = new Button(a);
        b.setText(label);
        b.setAllCaps(false);
        b.setTextSize(12);
        b.setTextColor(danger ? RED : GREEN);
        b.setMinHeight(dp(a, 36));
        b.setMinimumHeight(dp(a, 36));
        b.setPadding(dp(a, 7), 0, dp(a, 7), 0);
        b.setStateListAnimator(null);
        b.setBackground(tint(danger ? 0xFFFFE9E7 : PALE, 10, danger ? 0xFFF3D0CD : BORDER));
        return b;
    }

    public static EditText input(Activity a, String hint, int inputType) {
        EditText e = new EditText(a);
        e.setSingleLine(true);
        e.setTextSize(15);
        e.setTextColor(INK);
        e.setHintTextColor(0xFF89958C);
        e.setHint(hint);
        e.setInputType(inputType);
        e.setPadding(dp(a, 14), dp(a, 10), dp(a, 14), dp(a, 10));
        e.setBackground(tint(WHITE, 12, BORDER));
        e.setSelectAllOnFocus(false);
        return e;
    }

    public static LinearLayout card(Activity a) {
        LinearLayout c = new LinearLayout(a);
        c.setOrientation(LinearLayout.VERTICAL);
        c.setPadding(dp(a, 15), dp(a, 13), dp(a, 15), dp(a, 13));
        c.setBackground(tint(WHITE, 17, BORDER));
        if (android.os.Build.VERSION.SDK_INT >= 21) c.setElevation(dp(a, 1));
        return c;
    }

    public static LinearLayout heroCard(Activity a) {
        LinearLayout c = new LinearLayout(a);
        c.setOrientation(LinearLayout.VERTICAL);
        c.setPadding(dp(a, 18), dp(a, 18), dp(a, 18), dp(a, 18));
        c.setBackground(tint(GREEN_DARK, 20, GREEN_DARK));
        return c;
    }

    public static View divider(Activity a) {
        View v = new View(a);
        v.setBackgroundColor(BORDER);
        v.setLayoutParams(new LinearLayout.LayoutParams(-1, dp(a, 1)));
        return v;
    }

    public static void gap(LinearLayout parent, Activity a, int height) {
        View v = new View(a);
        parent.addView(v, new LinearLayout.LayoutParams(1, dp(a, height)));
    }

    public static int dp(Activity a, int value) {
        return (int) (value * a.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static LinearLayout.LayoutParams matchWrap(Activity a) {
        return new LinearLayout.LayoutParams(-1, -2);
    }

    public static LinearLayout.LayoutParams weighted() {
        return new LinearLayout.LayoutParams(0, -2, 1);
    }

    public static GradientDrawable tint(int color, int radius, int stroke) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(dpRadius(radius));
        if (stroke != color) d.setStroke(1, stroke);
        return d;
    }

    private static float dpRadius(int radius) { return radius * 2.0f; }
}
