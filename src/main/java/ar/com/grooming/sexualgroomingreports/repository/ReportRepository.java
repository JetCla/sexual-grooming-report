package ar.com.grooming.sexualgroomingreports.repository;

import ar.com.grooming.sexualgroomingreports.domain.Report;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Spring Data JPA repository for the Report entity.
 */
public interface ReportRepository extends JpaRepository<Report, Long>, JpaSpecificationExecutor<Report> {
    @Query("select report from Report report where report.owner.login = ?#{principal.preferredUsername}")
    List<Report> findByOwnerIsCurrentUser();
}
