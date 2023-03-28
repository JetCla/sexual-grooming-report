package ar.com.grooming.sexualgroomingreports.service;

import ar.com.grooming.sexualgroomingreports.domain.ReportComments;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ReportComments}.
 */
public interface ReportCommentsService {
    /**
     * Save a reportComments.
     *
     * @param reportComments the entity to save.
     * @return the persisted entity.
     */
    ReportComments save(ReportComments reportComments);

    /**
     * Updates a reportComments.
     *
     * @param reportComments the entity to update.
     * @return the persisted entity.
     */
    ReportComments update(ReportComments reportComments);

    /**
     * Partially updates a reportComments.
     *
     * @param reportComments the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReportComments> partialUpdate(ReportComments reportComments);

    /**
     * Get all the reportComments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReportComments> findAll(Pageable pageable);

    /**
     * Get the "id" reportComments.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReportComments> findOne(Long id);

    /**
     * Delete the "id" reportComments.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
