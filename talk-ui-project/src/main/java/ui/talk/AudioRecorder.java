package ui.talk;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFileFormat;

public class AudioRecorder {
    /**
     * AudioRecorder class
     * 
     * Main reference:
     * https://www.codejava.net/coding/capture-and-record-sound-into-wav-file-with-java-sound-api
     */

    // the line from which audio data is captured
    private TargetDataLine line = null;

    // path of the wav file
    private String audioFilePath = "resources/recording.wav";

    // default recording time limit
    private static final int DEFAULT_RECORD_TIME = 15;

    /**
     * Captures the sound and record into a WAV file
     */
    public void start() {
        try {
            AudioFormat format = new AudioFormat(16000.0f, 16, 1, true, true);
            line = AudioSystem.getTargetDataLine(format);
            line.open(format);
            line.start(); // start capturing

            // start recording
            AudioInputStream ais = new AudioInputStream(line);
            System.out.println("Start recording...");

            AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
            File audioFile = new File(audioFilePath);
            AudioSystem.write(ais, fileType, audioFile);

        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Closes the target data line to finish capturing and recording
     */
    public void finish() {
        line.stop();
        line.close();
        System.out.println("Finished");
    }

    /**
     * Record an audio for a certain period
     */
    public void record(int recordTimeInSeconds) {
        // creates a new thread that waits for a specified time before stopping
        Thread stopper = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(recordTimeInSeconds * 1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                finish();
            }
        });
        stopper.start();

        // start recording
        this.start();
    }

    public void record() {
        record(DEFAULT_RECORD_TIME);
    }

    /**
     * Getters and setters
     */
    public String getAudioFilePath() {
        return this.audioFilePath;
    }

    public AudioRecorder setAudioFilePath(String audioFilePath) {
        this.audioFilePath = audioFilePath;
        return this;
    }

    /**
     * Helper method: print all microphones available on the device
     */
    public static void findAllMicrophones() {
        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
        for (Mixer.Info info : mixerInfos) {
            Mixer m = AudioSystem.getMixer(info);
            Line.Info[] lineInfos = m.getTargetLineInfo();
            if (lineInfos.length >= 1 && lineInfos[0].getLineClass().equals(TargetDataLine.class)) {
                // Only prints out info if it is a Microphone
                System.out.println("Line Name: " + info.getName());// The name of the AudioDevice
                System.out.println("Line Description: " + info.getDescription());// The type of audio device
                for (Line.Info lineInfo : lineInfos) {
                    System.out.println("\t" + "---" + lineInfo);
                    Line line;
                    try {
                        line = m.getLine(lineInfo);
                    } catch (LineUnavailableException e) {
                        e.printStackTrace();
                        return;
                    }
                    System.out.println("\t-----" + line);
                }
            }
        }
    }
}