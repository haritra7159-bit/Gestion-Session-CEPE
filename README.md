# Gestion d'une session de CEPE

Application JavaFX de gestion d'une session du Certificat d'Étude Primaire Élémentaire. Elle gère écoles, élèves, matières et notes, puis automatise délibération, classement et relevés PDF.

## Objectifs

- Fiabiliser la saisie des résultats CEPE.
- Calculer les moyennes pondérées et les décisions.
- Produire listes d'admission, classement et relevés PDF.

## Aperçus

Les captures d'écran seront ajoutées dans `docs/screenshots/` : tableau de bord, CRUD, délibération et relevé PDF.

## Technologies

Java 21, JavaFX, CSS JavaFX, PostgreSQL, JDBC, Maven, JUnit 5, SLF4J, OpenPDF et Apache POI.

## Architecture

Architecture **MVC + DAO + Service Layer** : FXML/CSS → contrôleurs → services → DAO JDBC → PostgreSQL. Voir `docs/architecture.md`.

## Installation

1. Installer Java 21, Maven 3.9+ et PostgreSQL 16+.
2. Exécuter `psql -U postgres -f database/01_create_database.sql`.
3. Exécuter `psql -U postgres -d cepe -f database/02_schema_and_seed.sql`.
4. Adapter `src/main/resources/application.properties` avec les identifiants locaux.

## Commandes Maven

`mvn clean test` · `mvn javafx:run` · `mvn clean package`

## Développement avec VS Code

Ouvrez directement le dossier du projet dans Visual Studio Code et installez les extensions recommandées. Les tâches `CEPE: compiler et tester`, `CEPE: démarrer JavaFX` et `CEPE: générer le package` sont disponibles depuis `Terminal > Run Task`. Le guide complet est dans `docs/vscode-setup.md`.

## Structure et documentation

Voir `docs/project-structure.md` pour le rôle des dossiers, `docs/architecture.md` pour les couches et règles, et `docs/database.md` pour le dictionnaire de données.

## Auteurs et licence

Projet universitaire en L2GB à l'ENI Fianarantsoa, équipe composé de Fy Haritra et de Rianala. Licence [MIT](LICENSE).
