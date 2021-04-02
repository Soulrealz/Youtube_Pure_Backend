package youtube.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import youtube.model.pojo.HistoryRecord;

@Repository
public interface HistoryRecordRepository extends JpaRepository<HistoryRecord, Integer> {

}
