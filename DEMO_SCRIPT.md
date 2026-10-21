# 5–7 Minute Demonstration Plan

Use your own voice throughout the recording. Only describe functionality that you have personally tested.

- **0:00–1:00 — GitHub:** Show the public repository, README, and actual incremental commit history.
- **1:00–3:30 — Live application:** Open the app; add an ingredient; edit it; delete an item; open recipe suggestions; open a recipe detail; close/reopen the app to demonstrate persistence.
- **3:30–5:45 — Explain code:** Point to `RecipeMatcher.findStrictMatches`, `DatabaseHelper`, and the custom adapter. Explain how the recipe is excluded when one ingredient is missing.
- **5:45–6:30 — Database choice:** Explain that SQLite is local, offline, lightweight, and suitable for persistent CRUD in this application.

Suggested strict-match test: identify a recipe that currently appears, remove one required ingredient from the pantry, revisit suggestions and show that the recipe disappears. Restore the ingredient with enough quantity and show that the recipe qualifies again.
