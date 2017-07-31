package com.mp3.downloader.music.model;

/**
 * Created by AlexandrVolkov on 23.07.2017.
 */
public interface AudioManager {
    void addAudio(String url);

    void deleteAudio(AudioLink audioLink);
}
