# Written Report Guide — PantryWise

Replace the bracketed details and insert screenshots captured from your own running application. Do not claim that a screen or feature works until you have tested it on your emulator or device.

## Cover Page
**Application:** PantryWise — Smart Pantry Manager  
**Student name:** [Your name]  
**Student number:** [Your ITS number]  
**Module:** Mobile App Development 700  
**Date:** [Submission date]  
**GitHub repository:** [Paste your repository URL]

## Table of Contents
1. Introduction
2. System Design
3. Implementation
4. Testing and Screenshots
5. Challenges and Solutions
6. Conclusion and Reflection
7. References

## 1. Introduction
PantryWise is a Java Android application designed to help users keep track of ingredients available at home and identify recipes that can be prepared without buying missing ingredients. Food waste is the practical problem addressed by the application. Its defining feature is strict recipe matching: a recipe is suggested only when every required ingredient is present in a sufficient quantity.

## 2. System Design
Describe the screen flow: Dashboard/Pantry → Add/Edit Ingredient; Dashboard → Suggested Recipes → Recipe Details; Dashboard → Settings.

**Database model:** `pantry(id, name, quantity, unit, expiry_date)` and `recipes(id, name, description, ingredients, quantities, steps, minutes)`. Recipe rows are preloaded on first database creation. Pantry rows are created, read, updated and deleted through SQLite.

## 3. Implementation
Explain in your own words:
- `DatabaseHelper` uses `SQLiteOpenHelper` to create tables and run CRUD statements.
- `IngredientAdapter` and `RecipeAdapter` extend `BaseAdapter` to display dynamic records in a `ListView`.
- `Intent` objects open different Activities and pass the selected ingredient or recipe ID.
- `RecipeMatcher.findStrictMatches()` checks every required ingredient and quantity; a missing item or insufficient quantity excludes the recipe.
- `IngredientFormActivity` validates required fields, numeric quantity and positive quantity.

## 4. Testing and Screenshots
Insert real screenshots from your running app with captions:
- Dashboard and pantry list
- Add ingredient form
- Validation message for missing/invalid values
- Updated pantry after editing an item
- Delete confirmation and pantry after deletion
- Suggested recipes with a full match
- A recipe excluded after removing a required ingredient
- Recipe detail and method
- Settings screen
- Persistence check after closing and reopening the app

Suggested test cases:
1. Save a valid pantry item — expected: it appears in the list.
2. Submit blank name/unit — expected: validation message, no record saved.
3. Enter non-numeric or zero quantity — expected: validation message.
4. Edit an item — expected: changed values appear after returning to the list.
5. Delete an item — expected: item disappears from the list.
6. Remove one ingredient required by a recipe — expected: that recipe is absent from strict suggestions.
7. Restore the missing ingredient with sufficient quantity — expected: recipe becomes eligible again.
8. Close and reopen the app — expected: pantry data remains.

## 5. Challenges and Solutions
Write about 2–3 challenges you genuinely encountered during implementation and how you resolved them. Possible areas to reflect on if they happened: configuring Gradle, handling invalid quantity input, and ensuring partial recipe matches are excluded.

## 6. Conclusion and Reflection
Reflect on what you learned about Java Activities, Intents, adapters, SQLite persistence and implementing business rules. Mention realistic future improvements such as unit conversion, scheduled expiry notifications and a larger recipe collection.

## 7. References
Add the sources you actually consulted using the Harvard referencing style required by the assignment. Useful primary documentation includes:
- Android Developers (n.d.) *Save data using SQLite*. Available at: https://developer.android.com/training/data-storage/sqlite
- Android Developers (n.d.) *Activities*. Available at: https://developer.android.com/guide/components/activities/intro-activities
- Android Developers (n.d.) *Intents and intent filters*. Available at: https://developer.android.com/guide/components/intents-filters
- Android Developers (n.d.) *Build your app*. Available at: https://developer.android.com/build

Access dates should reflect when you personally accessed each source. Do not include sources you did not consult.
