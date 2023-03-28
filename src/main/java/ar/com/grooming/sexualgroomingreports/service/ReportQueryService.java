package ar.com.grooming.sexualgroomingreports.service;

// for static metamodels
import ar.com.grooming.sexualgroomingreports.domain.Report;
import ar.com.grooming.sexualgroomingreports.domain.ReportComments_;
import ar.com.grooming.sexualgroomingreports.domain.ReportLog_;
import ar.com.grooming.sexualgroomingreports.domain.Report_;
import ar.com.grooming.sexualgroomingreports.domain.User_;
import ar.com.grooming.sexualgroomingreports.domain.Victim_;
import ar.com.grooming.sexualgroomingreports.repository.ReportRepository;
import ar.com.grooming.sexualgroomingreports.service.criteria.ReportCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Report} entities in the database.
 * The main input is a {@link ReportCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Report} or a {@link Page} of {@link Report} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReportQueryService extends QueryService<Report> {

    private final Logger log = LoggerFactory.getLogger(ReportQueryService.class);

    private final ReportRepository reportRepository;

    public ReportQueryService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    /**
     * Return a {@link List} of {@link Report} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Report> findByCriteria(ReportCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Report> specification = createSpecification(criteria);
        return reportRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Report} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Report> findByCriteria(ReportCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Report> specification = createSpecification(criteria);
        return reportRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReportCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Report> specification = createSpecification(criteria);
        return reportRepository.count(specification);
    }

    /**
     * Function to convert {@link ReportCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Report> createSpecification(ReportCriteria criteria) {
        Specification<Report> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Report_.id));
            }
            if (criteria.getNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumber(), Report_.number));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Report_.date));
            }
            if (criteria.getOfficeAssigned() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOfficeAssigned(), Report_.officeAssigned));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Report_.description));
            }
            if (criteria.getLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocation(), Report_.location));
            }
            if (criteria.getReportCommentsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getReportCommentsId(),
                            root -> root.join(Report_.reportComments, JoinType.LEFT).get(ReportComments_.id)
                        )
                    );
            }
            if (criteria.getReportLogId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getReportLogId(),
                            root -> root.join(Report_.reportLogs, JoinType.LEFT).get(ReportLog_.id)
                        )
                    );
            }
            if (criteria.getVictimId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getVictimId(), root -> root.join(Report_.victim, JoinType.LEFT).get(Victim_.id))
                    );
            }
            if (criteria.getOwnerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getOwnerId(), root -> root.join(Report_.owner, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
