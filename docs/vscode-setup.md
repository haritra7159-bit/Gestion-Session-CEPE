# Développer avec Visual Studio Code

## Extensions requises

Ouvrez le dossier racine du projet dans VS Code. L'éditeur proposera les extensions listées dans `.vscode/extensions.json` : Java Extension Pack, Maven for Java, Language Support for Java, PostgreSQL et GitLens.

## JDK et Maven

VS Code doit utiliser un JDK 21. Après installation, ouvrez la palette de commandes avec `Ctrl+Shift+P`, lancez `Java: Configure Java Runtime`, puis choisissez le JDK 21 pour le projet. Dans le terminal intégré, vérifiez `java -version`, `javac -version` et `mvn -version`.

## Ouverture du projet

1. Démarrez VS Code.
2. Choisissez `File > Open Folder`.
3. Sélectionnez `D:\STUDIES\L2_GB_Grp1\JAVA_Mme_Volatiana\PROJET`.
4. Acceptez l'import Maven lorsque VS Code le propose.
5. Attendez la fin de l'indexation Java avant de lancer une tâche.

## Tâches disponibles

Utilisez `Terminal > Run Task` :

- `CEPE: compiler et tester` exécute `mvn clean test`.
- `CEPE: démarrer JavaFX` vérifie le projet puis exécute `mvn javafx:run`.
- `CEPE: générer le package` produit le livrable Maven.

Le débogage peut être lancé via `Run and Debug > Déboguer CEPE`. Pour JavaFX, la tâche Maven reste l'option de lancement la plus fiable jusqu'à l'ajout éventuel de modules Java.

## PostgreSQL

Exécutez les scripts SQL dans le terminal intégré, puis renseignez les paramètres locaux dans `src/main/resources/application.properties`. N'ajoutez jamais de mot de passe réel dans un commit.
