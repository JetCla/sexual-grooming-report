package ar.com.grooming.sexualgroomingreports.repository;

import ar.com.grooming.sexualgroomingreports.domain.ReportLog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Spring Data JPA repository for the ReportLog entity.
 */
public interface ReportLogRepository extends JpaRepository<ReportLog, Long> {
    @Query("select reportLog from ReportLog reportLog where reportLog.updatedBy.login = ?#{principal.preferredUsername}")
    List<ReportLog> findByUpdatedByIsCurrentUser();
}
