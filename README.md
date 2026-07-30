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

## Structure et documentation

Voir `docs/project-structure.md` pour le rôle des dossiers et `docs/architecture.md` pour les couches, règles et flux d'exécution.

## Auteurs et licence

Projet universitaire, équipe à compléter. Licence [MIT](LICENSE).
