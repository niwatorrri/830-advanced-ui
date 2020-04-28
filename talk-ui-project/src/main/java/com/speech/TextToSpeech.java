package com.speech;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class TextToSpeech {
    private Voice voice;
    private static final String DEFAULT_VOICE_NAME = "kevin16";
    private static final String FREETTS_VOICES_PROPERTY =
            "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory";

    public TextToSpeech(String voiceName) {
        System.setProperty("freetts.voices", FREETTS_VOICES_PROPERTY);
        VoiceManager voiceManager = VoiceManager.getInstance();
        Voice voice = voiceManager.getVoice(voiceName);
        voice.allocate();
        this.voice = voice;
    }

    public TextToSpeech() {
        this(DEFAULT_VOICE_NAME);
    }

    public void speak(String text) {
        voice.speak(text);
    }
}