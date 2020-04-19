# Final Project: Talk UI

## Update Log

To test dialogflow:

1. Download API key
2. Set `export GOOGLE_APPLICATION_CREDENTIALS=/path/to/your-project-credentials.json`
3. Build the project by `mvn clean verify`
4. Run some samples: `mvn exec:java -DDetectIntentAudioBookARoom` or `mvn exec:java -DDetectIntentAudioMountainView`

## General Information

Current code organization in src/main/java:
```
ui
|___ toolkit
|       |___ behavior
|       |___ constraint
|       |___ graphics
|       |___ widget
|___ editor (my editor implemented out of the toolkit)
```

Main class is specified in line 78 in pom.xml. Command `mvn exec:java` will start running the main class.

READMEs for the package:

- Constraint: see hw3
- Behaviors: see hw4
- Widgets: see hw5

Implementations are subject to change if more features are needed.
