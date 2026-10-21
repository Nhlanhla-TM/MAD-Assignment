package za.co.pantrywise;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

// Local SQLite database: pantry records persist after the app is closed.
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "pantrywise.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE pantry (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, quantity REAL NOT NULL, unit TEXT NOT NULL, expiry_date TEXT)");
        db.execSQL("CREATE TABLE recipes (" +
                "id INTEGER PRIMARY KEY, name TEXT NOT NULL, description TEXT, " +
                "ingredients TEXT NOT NULL, quantities TEXT NOT NULL, steps TEXT NOT NULL, minutes INTEGER NOT NULL)");
        seedRecipes(db);
        seedPantry(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Keep this simple for the assignment; a production app would migrate schema versions.
    }

    private void seedPantry(SQLiteDatabase db) {
        insertPantry(db, "Eggs", 6, "pcs", "");
        insertPantry(db, "Tomato", 3, "pcs", "");
        insertPantry(db, "Bread", 4, "slices", "");
        insertPantry(db, "Cheese", 100, "g", "");
        insertPantry(db, "Onion", 2, "pcs", "");
        insertPantry(db, "Rice", 250, "g", "");
        insertPantry(db, "Milk", 500, "ml", "");
    }

    private void insertPantry(SQLiteDatabase db, String name, double quantity, String unit, String expiry) {
        ContentValues v = new ContentValues();
        v.put("name", name); v.put("quantity", quantity); v.put("unit", unit); v.put("expiry_date", expiry);
        db.insert("pantry", null, v);
    }

    private void seedRecipes(SQLiteDatabase db) {
        String[][] data = {
            {"1","Cheesy Tomato Toast","A warm, quick toast with melted cheese.","bread|tomato|cheese","2 slices|1 pcs|30 g","1. Toast the bread lightly.\n2. Slice the tomato and place on the bread.\n3. Add cheese and grill until melted.", "10"},
            {"2","Simple Cheese Omelette","A soft omelette for any time of day.","eggs|cheese","2 pcs|25 g","1. Beat the eggs.\n2. Pour into a lightly heated non-stick pan.\n3. Add cheese, fold and cook through.", "8"},
            {"3","Tomato Egg Scramble","Eggs scrambled with fresh tomato.","eggs|tomato","2 pcs|1 pcs","1. Chop the tomato.\n2. Cook in a pan until softened.\n3. Add beaten eggs and stir until set.", "10"},
            {"4","Onion Fried Rice","A simple rice dish with onion.","rice|onion","200 g|1 pcs","1. Cook the rice if not already cooked.\n2. Chop and soften the onion in a pan.\n3. Add rice and stir until hot.", "20"},
            {"5","Cheesy Egg Toast","Crisp toast topped with egg and cheese.","bread|eggs|cheese","2 slices|1 pcs|20 g","1. Toast the bread.\n2. Cook the egg.\n3. Place egg on toast and finish with cheese.", "12"},
            {"6","Tomato Cheese Melt","A quick melted cheese and tomato snack.","bread|tomato|cheese","2 slices|1 pcs|30 g","1. Layer sliced tomato and cheese on bread.\n2. Grill until the cheese melts.\n3. Serve warm.", "7"},
            {"7","Milk Scrambled Eggs","Creamy eggs with a splash of milk.","eggs|milk","2 pcs|20 ml","1. Beat eggs with milk.\n2. Cook slowly in a non-stick pan, stirring.\n3. Remove while still soft.", "8"},
            {"8","Tomato Onion Rice","Rice with a simple tomato-onion base.","rice|tomato|onion","200 g|1 pcs|1 pcs","1. Cook rice.\n2. Chop tomato and onion and soften in a pan.\n3. Stir through the rice and heat well.", "22"},
            {"9","Cheese Omelette Toast","An omelette served on toast.","eggs|cheese|bread","2 pcs|25 g|2 slices","1. Make a cheese omelette.\n2. Toast the bread.\n3. Serve the omelette on the toast.", "12"},
            {"10","Tomato Egg Rice Bowl","A comforting bowl of rice, egg and tomato.","rice|eggs|tomato","200 g|1 pcs|1 pcs","1. Cook rice.\n2. Cook chopped tomato in a pan.\n3. Add egg and cook through, then serve over rice.", "18"},
            {"11","Cheese and Onion Toast","A savoury toasted sandwich filling.","bread|cheese|onion","2 slices|30 g|0.5 pcs","1. Slice onion thinly.\n2. Add onion and cheese to bread.\n3. Toast until golden and melted.", "10"},
            {"12","Milk French Toast","Golden eggy bread made with pantry staples.","bread|eggs|milk","2 slices|1 pcs|50 ml","1. Whisk egg and milk together.\n2. Dip bread into the mixture.\n3. Cook both sides in a lightly greased pan.", "12"},
            {"13","Tomato Cheese Rice","Rice topped with tomato and melted cheese.","rice|tomato|cheese","200 g|1 pcs|25 g","1. Cook rice.\n2. Warm chopped tomato in a pan.\n3. Mix with rice and top with cheese.", "18"},
            {"14","Onion Omelette","A simple omelette with onion.","eggs|onion","2 pcs|0.5 pcs","1. Finely chop onion.\n2. Beat eggs and stir in onion.\n3. Cook in a pan until set.", "10"},
            {"15","Tomato Milk Soup","A gentle tomato soup with a creamy finish.","tomato|milk|onion","2 pcs|150 ml|0.5 pcs","1. Chop tomato and onion.\n2. Simmer with a little water until soft.\n3. Stir in milk and heat gently without boiling.", "18"},
            {"16","Cheesy Rice Cakes","Pan-crisped rice cakes with cheese.","rice|cheese|eggs","200 g|30 g|1 pcs","1. Mix cooked rice, cheese and beaten egg.\n2. Shape into small patties.\n3. Pan-cook both sides until firm and golden.", "15"},
            {"17","Egg and Tomato Sandwich","A fresh, easy sandwich.","bread|eggs|tomato","2 slices|1 pcs|1 pcs","1. Cook the egg.\n2. Slice the tomato.\n3. Layer on bread and serve.", "9"},
            {"18","Onion Cheese Rice","A savoury rice bowl with onion and cheese.","rice|onion|cheese","200 g|0.5 pcs|25 g","1. Cook rice.\n2. Soften chopped onion in a pan.\n3. Stir in rice and finish with cheese.", "18"},
            {"19","Creamy Egg Toast","Soft eggs with milk on toast.","bread|eggs|milk","2 slices|2 pcs|30 ml","1. Whisk eggs and milk.\n2. Scramble gently in a pan.\n3. Serve over toasted bread.", "10"},
            {"20","Tomato Onion Omelette","A colourful omelette with two vegetables.","eggs|tomato|onion","2 pcs|1 pcs|0.5 pcs","1. Chop tomato and onion.\n2. Beat eggs and combine with vegetables.\n3. Cook in a pan until set.", "12"}
        };
        for (String[] r : data) {
            ContentValues v = new ContentValues();
            v.put("id", Integer.parseInt(r[0])); v.put("name", r[1]); v.put("description", r[2]);
            v.put("ingredients", r[3]); v.put("quantities", r[4]); v.put("steps", r[5]);
            v.put("minutes", Integer.parseInt(r[6]));
            db.insert("recipes", null, v);
        }
    }

    public long addIngredient(String name, double quantity, String unit, String expiry) {
        ContentValues v = new ContentValues();
        v.put("name", name.trim()); v.put("quantity", quantity); v.put("unit", unit.trim()); v.put("expiry_date", expiry);
        return getWritableDatabase().insert("pantry", null, v);
    }

    public int updateIngredient(long id, String name, double quantity, String unit, String expiry) {
        ContentValues v = new ContentValues();
        v.put("name", name.trim()); v.put("quantity", quantity); v.put("unit", unit.trim()); v.put("expiry_date", expiry);
        return getWritableDatabase().update("pantry", v, "id=?", new String[]{String.valueOf(id)});
    }

    public int deleteIngredient(long id) {
        return getWritableDatabase().delete("pantry", "id=?", new String[]{String.valueOf(id)});
    }

    public List<Ingredient> getIngredients() {
        List<Ingredient> list = new ArrayList<>();
        try (Cursor c = getReadableDatabase().rawQuery("SELECT id,name,quantity,unit,expiry_date FROM pantry ORDER BY name COLLATE NOCASE", null)) {
            while (c.moveToNext()) list.add(new Ingredient(c.getLong(0), c.getString(1), c.getDouble(2), c.getString(3), c.getString(4)));
        }
        return list;
    }

    public Ingredient getIngredient(long id) {
        try (Cursor c = getReadableDatabase().rawQuery("SELECT id,name,quantity,unit,expiry_date FROM pantry WHERE id=?", new String[]{String.valueOf(id)})) {
            if (c.moveToFirst()) return new Ingredient(c.getLong(0), c.getString(1), c.getDouble(2), c.getString(3), c.getString(4));
        }
        return null;
    }

    public List<Recipe> getRecipes() {
        List<Recipe> list = new ArrayList<>();
        try (Cursor c = getReadableDatabase().rawQuery("SELECT id,name,description,ingredients,quantities,steps,minutes FROM recipes ORDER BY name", null)) {
            while (c.moveToNext()) list.add(new Recipe(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getInt(6)));
        }
        return list;
    }

    public Recipe getRecipe(int id) {
        try (Cursor c = getReadableDatabase().rawQuery("SELECT id,name,description,ingredients,quantities,steps,minutes FROM recipes WHERE id=?", new String[]{String.valueOf(id)})) {
            if (c.moveToFirst()) return new Recipe(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getInt(6));
        }
        return null;
    }
}
