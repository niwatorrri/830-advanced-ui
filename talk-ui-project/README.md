# Final Project: Talk UI

To run the Talk UI system, first execute the following line:

```bash
export GOOGLE_APPLICATION_CREDENTIALS=talkui-dialogflow-credentials.json && mvn clean package
```

The DialogFlow credentials `talkui-dialogflow-credentials.json` will not be released to anyone outside the project.

After executing the command above, run `mvn exec:java` to start the interface.

The user audio will be recorded and saved to `resources/recording.wav`, which is then uploaded to detect intent, and the query result returned will then be used to render graphics.

Caveat: if you are not prompted to grant mic permission, it is likely that the recording won't work.

Demo video can be found [here](https://www.youtube.com/watch?v=T39Fwn_G0DI&list=LLUql4h3NdtT7ylpL5a4emig&index=5&t=1s).

## General Information

Current code organization in src/main/java:
```
/
|___ ui
|    |___ talk __________________ Talk UI main interface
|    |___ toolkit _______________ self-implemented UI toolkit
|    |       |___ behavior
|    |       |___ constraint
|    |       |___ graphics
|    |       |___ widget
|    |___ editor ________________ node editor in hw5 (ignore this)
|___ com
     |___ example/dialogflow ____ example codes to interact with DialogFlow
     |___ speech ________________ utilities to capture audio and text-to-speech
```

Change log of the base toolkit:

- Implementation of `behavior.InteractiveWindowGroup` changed completely to solve previous bugs where children's behaviors cannot propagate up to top group
