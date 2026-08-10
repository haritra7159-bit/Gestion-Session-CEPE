# 🎓 Gestion Session CEPE

Application de bureau JavaFX pour la gestion complète d'une session du **Certificat d'Étude Primaire Élémentaire (CEPE)**.

---

## 🛠 Technologies

| Couche          | Technologie            |
| --------------- | ---------------------- |
| Langage         | Java 25                |
| UI              | JavaFX 25 + FXML + CSS |
| Base de données | PostgreSQL 16          |
| Connexion JDBC  | HikariCP (pool)        |
| Build           | Maven 3.9+             |
| Tests           | JUnit 5 + Mockito      |
| PDF             | OpenPDF                |
| Logs            | SLF4J + Logback        |

---

## ⚡ Prérequis

- Java 25
- Maven 3.9+
- PostgreSQL 16+

---

## 🚀 Installation

```bash
# 1. Cloner le dépôt
git clone https://github.com/haritra7159-bit/Gestion-Session-CEPE.git
cd Gestion-Session-CEPE

# 2. Créer la base de données
psql -U postgres -f database/migration/create_cepe_schema.sql
psql -U postgres -d cepe -f database/migration/insertion_donnees_demo.sql

# 3. Configurer la connexion DB
# Modifier resources/application.properties
# Exemple :
# db.url=jdbc:postgresql://localhost:5432/cepe
# db.user=postgres
# db.password=mdp1706

# 4. Compiler et lancer
./mvnw.cmd clean javafx:run
```

### Modules

| Module              | Description                                                 |
| ------------------- | ----------------------------------------------------------- |
| 🏫 **Écoles**       | Gestion des écoles participantes                            |
| 🎓 **Élèves**       | Inscription, modification, suppression + recherche intégrée |
| 📝 **Notes**        | Gestion des notes par élève et par matière (dialog intégré) |
| 📚 **Matières**     | Définition des matières et coefficients                     |
| ⚖️ **Délibération** | Calcul automatique des moyennes pondérées et décisions      |
| 🏆 **Classement**   | Classement par mérite des admis et des échoués              |
| 📄 **Relevés PDF**  | Génération de relevés de notes formatés en PDF              |

### Organisation

```text
Gestion-Session-CEPE/
├── java/mg/cepe/gestion/
│   ├── application/      # Point d'entrée JavaFX
│   ├── controller/       # Contrôleurs FXML (MVC)
│   ├── service/          # Logique métier + implémentations
│   ├── dao/              # Accès données JDBC + implémentations
│   ├── model/            # Entités (POJO)
│   ├── pdf/              # Générateur PDF
│   └── util/             # Utilitaires
├── resources/
│   ├── fxml/             # Vues JavaFX
│   ├── css/              # Feuilles de style
│   └── application.properties
├── database/migration/   # Scripts SQL
└── test/                 # Tests unitaires
```

### Architecture

```text
MVC + Service Layer + DAO
FXML/CSS (View)
    ↓
Controller
    ↓
Service (métier)
    ↓
DAO (JDBC)
    ↓
PostgreSQL
```

### Commandes utiles

| Commande                 | Action               |
| ------------------------ | -------------------- |
| ./mvnw.cmd clean compile | Compiler             |
| ./mvnw.cmd javafx:run    | Lancer l'application |
| ./mvnw.cmd clean test    | Exécuter les tests   |
| ./mvnw.cmd clean package | Générer le JAR       |

### Licence

Projet universitaire – L2GB Fianarantsoa
Contributeurs : Fy Haritra, Rianala
