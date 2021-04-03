package youtube.model.utils;

import youtube.model.pojo.Video;

public class PairVideoInt {
    Video video;
    int likes;

    public PairVideoInt(Video video, int likes) {
        this.video = video;
        this.likes = likes;
    }

    public Video getVideo() { return video; }
    public int getLikes() { return likes; }
}
