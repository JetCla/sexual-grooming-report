package ar.com.grooming.sexualgroomingreports.service;

// for static metamodels
import ar.com.grooming.sexualgroomingreports.domain.Victim;
import ar.com.grooming.sexualgroomingreports.domain.Victim_;
import ar.com.grooming.sexualgroomingreports.repository.VictimRepository;
import ar.com.grooming.sexualgroomingreports.service.criteria.VictimCriteria;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Victim} entities in the database.
 * The main input is a {@link VictimCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Victim} or a {@link Page} of {@link Victim} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VictimQueryService extends QueryService<Victim> {

    private final Logger log = LoggerFactory.getLogger(VictimQueryService.class);

    private final VictimRepository victimRepository;

    public VictimQueryService(VictimRepository victimRepository) {
        this.victimRepository = victimRepository;
    }

    /**
     * Return a {@link List} of {@link Victim} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Victim> findByCriteria(VictimCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Victim> specification = createSpecification(criteria);
        return victimRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Victim} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Victim> findByCriteria(VictimCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Victim> specification = createSpecification(criteria);
        return victimRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VictimCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Victim> specification = createSpecification(criteria);
        return victimRepository.count(specification);
    }

    /**
     * Function to convert {@link VictimCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Victim> createSpecification(VictimCriteria criteria) {
        Specification<Victim> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Victim_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Victim_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Victim_.lastName));
            }
            if (criteria.getAge() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAge(), Victim_.age));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), Victim_.city));
            }
            if (criteria.getState() != null) {
                specification = specification.and(buildStringSpecification(criteria.getState(), Victim_.state));
            }
            if (criteria.getCountry() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountry(), Victim_.country));
            }
            if (criteria.getObservations() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObservations(), Victim_.observations));
            }
        }
        return specification;
    }
}
