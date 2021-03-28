package youtube.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import youtube.exceptions.NotFoundException;
import youtube.model.dto.VideoWithoutIDDTO;
import youtube.model.pojo.Video;
import youtube.model.repository.VideoRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class VideoService {
    @Autowired
    private VideoRepository videoRepository;

    /*
    public VideoWithoutIDDTO getVideoByID(int id) {
        Optional<Video> video = videoRepository.findById(id);
        if (video.isEmpty()) {
            throw new NotFoundException("No such video exists");
        }
        return new VideoWithoutIDDTO(video.get());
    }
    */


    public List<VideoWithoutIDDTO> getAllVideos(String title) {
        // Building a list of Videos to return Videos without ID
        List<VideoWithoutIDDTO> ls = new LinkedList<>();
        var v = videoRepository.findAllByTitle(title);
        for (Video video : v) {
            ls.add(new VideoWithoutIDDTO(video));
        }

        return ls;
    }

    public VideoWithoutIDDTO getByName(String title) {
        if(videoRepository.findByTitle(title) == null) {
            throw new NotFoundException("There is no video with that name.");
        }

        Video video = videoRepository.findByTitle(title);
        return new VideoWithoutIDDTO(video);
    }
}
