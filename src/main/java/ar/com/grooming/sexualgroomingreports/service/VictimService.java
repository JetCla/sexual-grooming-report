package ar.com.grooming.sexualgroomingreports.service;

import ar.com.grooming.sexualgroomingreports.domain.Victim;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Victim}.
 */
public interface VictimService {
    /**
     * Save a victim.
     *
     * @param victim the entity to save.
     * @return the persisted entity.
     */
    Victim save(Victim victim);

    /**
     * Updates a victim.
     *
     * @param victim the entity to update.
     * @return the persisted entity.
     */
    Victim update(Victim victim);

    /**
     * Partially updates a victim.
     *
     * @param victim the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Victim> partialUpdate(Victim victim);

    /**
     * Get all the victims.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Victim> findAll(Pageable pageable);

    /**
     * Get the "id" victim.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Victim> findOne(Long id);

    /**
     * Delete the "id" victim.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
