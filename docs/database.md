# Dictionnaire de données

Le schéma de référence est `database/02_schema_and_seed.sql`. Les noms ci-dessous sont ceux utilisés par le sujet et par les futures requêtes JDBC.

| Table | Attribut | Type PostgreSQL | Rôle |
|---|---|---|---|
| `ecole` | `numEcole` | `VARCHAR(20)` | Identifiant unique de l'école. |
| `ecole` | `design` | `VARCHAR(150)` | Désignation de l'école. |
| `ecole` | `adresse` | `VARCHAR(255)` | Adresse de l'école. |
| `eleve` | `numEleve` | `VARCHAR(20)` | Identifiant unique de l'élève. |
| `eleve` | `numEcole` | `VARCHAR(20)` | Référence vers l'école de l'élève. |
| `eleve` | `nom` | `VARCHAR(100)` | Nom de famille. |
| `eleve` | `prenom` | `VARCHAR(150)` | Prénom(s). |
| `eleve` | `date_naissance` | `DATE` | Date de naissance, requise pour le relevé PDF. |
| `matiere` | `numMat` | `VARCHAR(20)` | Identifiant unique de la matière. |
| `matiere` | `designMat` | `VARCHAR(100)` | Désignation de la matière. |
| `matiere` | `coef` | `SMALLINT` | Coefficient, compris entre 1 et 10. |
| `note` | `annee_scolaire` | `VARCHAR(9)` | Année au format `YYYY-YYYY`. |
| `note` | `numEleve` | `VARCHAR(20)` | Référence vers l'élève. |
| `note` | `numMat` | `VARCHAR(20)` | Référence vers la matière. |
| `note` | `note` | `NUMERIC(4,2)` | Valeur comprise entre 0 et 20. |

## Clés et relations

- `ecole.numEcole` est la clé primaire de `ecole`.
- `eleve.numEleve` est la clé primaire de `eleve` et `eleve.numEcole` référence `ecole.numEcole`.
- `matiere.numMat` est la clé primaire de `matiere`.
- La clé primaire de `note` est composée de `annee_scolaire`, `numEleve` et `numMat`.

PostgreSQL convertit les identifiants non quotés en minuscules. Les DAO utiliseront donc des requêtes non quotées et des alias explicites pour garder des noms Java cohérents, par exemple `numecole AS "numEcole"`.
