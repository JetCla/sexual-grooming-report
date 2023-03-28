package ar.com.grooming.sexualgroomingreports.service.impl;

import ar.com.grooming.sexualgroomingreports.domain.ReportComments;
import ar.com.grooming.sexualgroomingreports.repository.ReportCommentsRepository;
import ar.com.grooming.sexualgroomingreports.service.ReportCommentsService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ReportComments}.
 */
@Service
@Transactional
public class ReportCommentsServiceImpl implements ReportCommentsService {

    private final Logger log = LoggerFactory.getLogger(ReportCommentsServiceImpl.class);

    private final ReportCommentsRepository reportCommentsRepository;

    public ReportCommentsServiceImpl(ReportCommentsRepository reportCommentsRepository) {
        this.reportCommentsRepository = reportCommentsRepository;
    }

    @Override
    public ReportComments save(ReportComments reportComments) {
        log.debug("Request to save ReportComments : {}", reportComments);
        return reportCommentsRepository.save(reportComments);
    }

    @Override
    public ReportComments update(ReportComments reportComments) {
        log.debug("Request to update ReportComments : {}", reportComments);
        return reportCommentsRepository.save(reportComments);
    }

    @Override
    public Optional<ReportComments> partialUpdate(ReportComments reportComments) {
        log.debug("Request to partially update ReportComments : {}", reportComments);

        return reportCommentsRepository
            .findById(reportComments.getId())
            .map(existingReportComments -> {
                if (reportComments.getDate() != null) {
                    existingReportComments.setDate(reportComments.getDate());
                }

                return existingReportComments;
            })
            .map(reportCommentsRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReportComments> findAll(Pageable pageable) {
        log.debug("Request to get all ReportComments");
        return reportCommentsRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReportComments> findOne(Long id) {
        log.debug("Request to get ReportComments : {}", id);
        return reportCommentsRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ReportComments : {}", id);
        reportCommentsRepository.deleteById(id);
    }
}
