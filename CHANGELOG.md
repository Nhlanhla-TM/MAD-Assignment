# PantryWise UI and functionality update

## Professional interface update
- Refined shared UI components, typography, spacing, cards, buttons and colour palette.
- Redesigned dashboard, ingredient form, suggested recipes, recipe details and settings screens.
- Added a PantryWise launcher icon and app theme colours.

## Functionality improvements
- Added pantry search by ingredient name.
- Added visible Edit and Delete controls on each pantry item.
- Improved ingredient validation and clearer inline field errors.
- Persisted the expiry-date display setting using SharedPreferences.
- Added explicit empty states for an empty pantry, empty searches and no strict recipe matches.
- Improved strict matching to avoid combining quantities recorded in different units and to recognise common unit synonyms.
- Kept SQLite persistence and the seeded 20-recipe collection.
