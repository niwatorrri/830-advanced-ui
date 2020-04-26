# Final Project: Talk UI

## Update Log

Note that the projectID in pom.xml has been changed! Get the correct service account credentials to work.

(04/21/2020) A skeleton of speech to graphics

1. Added ui.talk.TalkUI and changed main class to this
2. The audio will be recorded and saved to `resources/recording.wav`, which is then uploaded to detect intent
3. The query result returned will be used to render graphics
4. Caveat: if you are using VSCode, you might not be prompted to grant mic permission and the recording may not work

(04/19/2020) To test dialogflow:

1. Download API key
2. Set `export GOOGLE_APPLICATION_CREDENTIALS=/path/to/your-project-credentials.json`
3. Build the project by `mvn clean verify`
4. Run some samples: `mvn exec:java -DDetectIntentAudioBookARoom` or `mvn exec:java -DDetectIntentAudioMountainView`

## General Information

Current code organization in src/main/java:
```
ui
|__  talk (Talk UI main interface)
|___ toolkit
|       |___ behavior
|       |___ constraint
|       |___ graphics
|       |___ widget
|___ editor (my editor implemented out of the toolkit)
```

Command `mvn exec:java` will start running the main class.

READMEs for the package:

- Constraint: see hw3
- Behaviors: see hw4
- Widgets: see hw5

Implementations are subject to change if more features are needed.
