package ar.com.grooming.sexualgroomingreports.service;

import ar.com.grooming.sexualgroomingreports.domain.ReportLog;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ReportLog}.
 */
public interface ReportLogService {
    /**
     * Save a reportLog.
     *
     * @param reportLog the entity to save.
     * @return the persisted entity.
     */
    ReportLog save(ReportLog reportLog);

    /**
     * Updates a reportLog.
     *
     * @param reportLog the entity to update.
     * @return the persisted entity.
     */
    ReportLog update(ReportLog reportLog);

    /**
     * Partially updates a reportLog.
     *
     * @param reportLog the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReportLog> partialUpdate(ReportLog reportLog);

    /**
     * Get all the reportLogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReportLog> findAll(Pageable pageable);

    /**
     * Get the "id" reportLog.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReportLog> findOne(Long id);

    /**
     * Delete the "id" reportLog.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
