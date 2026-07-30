# Architecture

L'application applique MVC + DAO + Service Layer.

`FXML/CSS → Controller → Service → DAO/Repository → PostgreSQL`

Les contrôleurs reçoivent les actions JavaFX et délèguent la logique aux services. Les services appliquent les règles CEPE et ouvrent les transactions. Les DAO sont les seuls à exécuter des requêtes JDBC préparées, et les mappers transforment les résultats SQL en entités.

Règles : `moyenne = SUM(note × coef) / SUM(coef)` ; admis CEPE si moyenne `>= 9,75` ; admis en 6e si moyenne `> 12` ; une note est unique par année, élève et matière.
