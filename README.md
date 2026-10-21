# PantryWise — Smart Pantry Manager

PantryWise is a Java Android application for tracking ingredients at home and finding recipes that can be made using only the ingredients and quantities already available. The strict matching rule excludes recipes if even one required ingredient or quantity is missing.

## Features

- Dashboard with pantry summary and quick navigation.
- Add, view, edit and delete pantry ingredients.
- Ingredient name, positive quantity, unit and optional expiry date.
- Date picker for expiry dates and validation for required fields, quantities and dates.
- Search the pantry by ingredient name.
- SQLite storage for pantry items and a seeded collection of 20 recipes.
- Strict recipe matching by ingredient, quantity and compatible unit; partial matches are excluded.
- Recipe details with required quantities and preparation steps.
- Settings preference to show or hide expiry dates in the pantry list.
- Consistent green-and-cream interface, custom ListView adapters and a PantryWise launcher icon.

## Technology

- Java (Android)
- Android Activities and Intents
- ListView with custom adapters
- SQLiteOpenHelper for persistent local data
- SharedPreferences for the display setting

## Open and run

1. Extract the complete project folder from the ZIP.
2. Open Android Studio and choose **Open**.
3. Select the folder containing `settings.gradle` (the project root).
4. Allow Gradle sync to finish. If Android Studio asks to install a missing SDK component, follow the prompt.
5. Connect an Android phone with USB debugging enabled, select it in the device dropdown and press **Run**.

Recommended environment: Android Studio with JDK 17 and Android SDK Platform 35 installed. The project uses Android Gradle Plugin 8.7.3 and Java 11 source compatibility.

## Database notes

The pantry and recipe data are stored locally in `pantrywise.db`. Pantry changes persist between app sessions. Recipe records are seeded on first database creation. The app does not require a login or internet connection.

## Strict matching behaviour

Every ingredient required by a recipe must be present with a compatible unit and at least the required quantity. Common plural forms and unit synonyms are normalised. Amounts recorded in different units are not combined. A recipe with missing ingredients or insufficient quantities is not shown in Suggested Recipes.

## Suggested demonstration checklist

1. Open the dashboard and explain the pantry summary.
2. Search for an ingredient.
3. Add an ingredient and include an expiry date.
4. Edit its quantity and save the changes.
5. Delete an ingredient and confirm the prompt.
6. Open Suggested Recipes and explain that only complete matches appear.
7. Open a recipe to show its quantities and method.
8. Change the expiry-date display setting and return to the pantry.
9. Close and reopen the app to demonstrate SQLite persistence.

## Important for assessment

This project is a working starter implementation, not a substitute for understanding and demonstrating your own work. Review the Java classes, test every feature on your phone, maintain your own meaningful Git history, and capture screenshots from your actual running application for the report. Follow the assignment's requirements for the GitHub repository, demonstration video and final ZIP submission.
