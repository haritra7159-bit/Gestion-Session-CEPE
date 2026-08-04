# 🎓 Gestion Session CEPE

Application de bureau JavaFX pour la gestion complète d'une session du **Certificat d'Étude Primaire Élémentaire (CEPE)**.

---

## 🛠 Technologies

| Couche | Technologie |
|--------|-------------|
| Langage | Java 25 |
| UI | JavaFX 25 + FXML + CSS |
| Base de données | PostgreSQL 16 |
| Connexion JDBC | HikariCP (pool) |
| Build | Maven 3.9+ |
| Tests | JUnit 5 + Mockito |
| PDF | OpenPDF |
| Logs | SLF4J + Logback |

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
psql -U postgres -d cepe -f database/migration/insert_demo_data.sql

# 3. Configurer la connexion DB
# Modifier src/main/resources/application.properties

# 4. Compiler et lancer
mvn clean javafx:run

| Module              | Description                                                 |
| ------------------- | ----------------------------------------------------------- |
| 🏫 **Écoles**       | Gestion des écoles participantes                            |
| 🎓 **Élèves**       | Inscription, modification, suppression + recherche intégrée |
| 📝 **Notes**        | Gestion des notes par élève et par matière (dialog intégré) |
| 📚 **Matières**     | Définition des matières et coefficients                     |
| ⚖️ **Délibération** | Calcul automatique des moyennes pondérées et décisions      |
| 🏆 **Classement**   | Classement par mérite des admis et des échoués              |
| 📄 **Relevés PDF**  | Génération de relevés de notes formatés en PDF              |

📁 Organisation
plain
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
🎯 Architecture
MVC + Service Layer + DAO
plain
FXML/CSS (View)
    ↓
Controller
    ↓
Service (métier)
    ↓
DAO (JDBC)
    ↓
PostgreSQL
📜 Commandes utiles
Table
Commande	Action
mvn clean compile	Compiler
mvn javafx:run	Lancer l'application
mvn clean test	Exécuter les tests
mvn clean package	Générer le JAR
📝 Licence
Projet universitaire – L2GB Fianarantsoa
