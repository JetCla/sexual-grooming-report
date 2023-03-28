package ar.com.grooming.sexualgroomingreports.service.impl;

import ar.com.grooming.sexualgroomingreports.domain.Report;
import ar.com.grooming.sexualgroomingreports.repository.ReportRepository;
import ar.com.grooming.sexualgroomingreports.service.ReportService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Report}.
 */
@Service
@Transactional
public class ReportServiceImpl implements ReportService {

    private final Logger log = LoggerFactory.getLogger(ReportServiceImpl.class);

    private final ReportRepository reportRepository;

    public ReportServiceImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    public Report save(Report report) {
        log.debug("Request to save Report : {}", report);
        return reportRepository.save(report);
    }

    @Override
    public Report update(Report report) {
        log.debug("Request to update Report : {}", report);
        return reportRepository.save(report);
    }

    @Override
    public Optional<Report> partialUpdate(Report report) {
        log.debug("Request to partially update Report : {}", report);

        return reportRepository
            .findById(report.getId())
            .map(existingReport -> {
                if (report.getNumber() != null) {
                    existingReport.setNumber(report.getNumber());
                }
                if (report.getDate() != null) {
                    existingReport.setDate(report.getDate());
                }
                if (report.getOfficeAssigned() != null) {
                    existingReport.setOfficeAssigned(report.getOfficeAssigned());
                }
                if (report.getDescription() != null) {
                    existingReport.setDescription(report.getDescription());
                }
                if (report.getLocation() != null) {
                    existingReport.setLocation(report.getLocation());
                }

                return existingReport;
            })
            .map(reportRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Report> findAll(Pageable pageable) {
        log.debug("Request to get all Reports");
        return reportRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Report> findOne(Long id) {
        log.debug("Request to get Report : {}", id);
        return reportRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Report : {}", id);
        reportRepository.deleteById(id);
    }
}
