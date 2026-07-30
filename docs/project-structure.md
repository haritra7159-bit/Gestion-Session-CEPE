# Organisation des dossiers

| Dossier | Rôle, classes et dépendances |
|---|---|
| `application` | Démarrage JavaFX et navigation ; dépend de JavaFX et contrôleurs. |
| `config` | Propriétés, constantes et connexion JDBC ; dépend de JDBC. |
| `controller` | Contrôleurs FXML ; dépend des services, jamais des DAO. |
| `entity` | Entités persistées `Ecole`, `Eleve`, `Matiere`, `Note` ; sans dépendance de couche. |
| `model` | DTO, résultats de délibération et statistiques ; dépend des entités. |
| `dao` et `dao/impl` | Contrats et CRUD JDBC ; dépend de config, mapper et JDBC. |
| `repository` | Lectures complexes (résultats, classement) ; dépend de JDBC et mapper. |
| `service` et `service/impl` | Règles métier JavaDoc ; dépend de DAO, validateurs et exceptions. |
| `mapper` | Transformation `ResultSet` en objets ; dépend de JDBC et entités. |
| `pdf`, `validator`, `util`, `exception` | Relevés, validation, outils et erreurs techniques/métier. |
| `resources/fxml`, `css`, `images`, `fonts` | Vues, thème et ressources visuelles ; dépend de JavaFX. |
| `resources/sql`, `reports` | Scripts côté application et modèles d'édition. |
| `database` | Création de base, contraintes, index et données de démonstration PostgreSQL. |
| `docs`, `scripts`, `tests` | Documentation, automatisation et ressources de test. |
