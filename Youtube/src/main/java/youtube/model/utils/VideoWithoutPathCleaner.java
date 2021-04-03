package youtube.model.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import youtube.model.pojo.Video;
import youtube.model.repository.VideoRepository;
import java.util.List;


@Component
@EnableScheduling
public class VideoWithoutPathCleaner {
    private static final int ID = 0;
    private static final int SLEEP_PERIOD = 1000*60*60*24;
    @Autowired
    VideoRepository videoRepository;

    @Scheduled(cron = "0 0 4 * * *")
    public void deleteVideosWithoutMedia(){
        List<Video> toDeleteVideos = videoRepository.findAllByIdGreaterThan(ID);

        for(Video video: toDeleteVideos) {
            if(video.getPath() == null) {
                videoRepository.delete(video);
            }
        }
    }
}
