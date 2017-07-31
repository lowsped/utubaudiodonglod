package com.mp3.downloader.music.db;

import com.mp3.downloader.music.model.Audio;
import com.mp3.downloader.music.model.AudioLink;

import java.util.List;

/**
 * Created by AlexandrVolkov on 27.06.2017.
 */
public interface DBService {
    long addAudio(Audio audio);

    void deleteAudio(Audio audio);

    Audio getAudioById(long id);

    long addLink(AudioLink audioLink);

    List<AudioLink> getAllLink();

    void deleteLink(AudioLink audioLink);

    boolean isAlreadyAdded(AudioLink audioLink);
}
