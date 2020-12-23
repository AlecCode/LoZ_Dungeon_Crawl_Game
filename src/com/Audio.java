package com;

import javax.sound.sampled.*;
import java.io.File;

class Audio {
    private Clip sound;                 //sound holds the clip of sound to be played
    private boolean isPlaying = true;   //isPlaying holds whether or not the audio clip is playing

    public Audio(String f) {
        //This loads a .wav file
        try {
            File file = new File(f);
            AudioInputStream ais = AudioSystem.getAudioInputStream(file);
            this.sound = AudioSystem.getClip();
            this.sound.open(ais);
        }catch(Exception e){ System.out.println(e);}
    }

    //This method loops the audio clip
    public void loop() {
        this.sound.loop(Clip.LOOP_CONTINUOUSLY);
        isPlaying = true;
    }

    //This method plays the audio clip from the beginning
    public void play() {
        this.sound.setFramePosition(0);
        this.sound.start();
    }

    //This method plays/pauses the audio clip
    public void togglePause() {
        if(this.sound.isRunning()) {
            this.sound.stop();
            isPlaying = false;
        }
        else {
            this.sound.loop(Clip.LOOP_CONTINUOUSLY);
            isPlaying = true;
        }
    }

    //This method returns whether or not the audio clip is playing
    public boolean getIsPlaying() {
        return isPlaying;
    }
}