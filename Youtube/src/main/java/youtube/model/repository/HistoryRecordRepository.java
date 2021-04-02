package youtube.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import youtube.model.pojo.HistoryRecord;
import youtube.model.pojo.Video;

import java.util.List;

@Repository
public interface HistoryRecordRepository extends JpaRepository<HistoryRecord, Integer> {
    List<HistoryRecord> findAllByWatchedVideo(Video video);
}
