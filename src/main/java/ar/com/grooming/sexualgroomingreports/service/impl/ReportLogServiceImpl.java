package ar.com.grooming.sexualgroomingreports.service.impl;

import ar.com.grooming.sexualgroomingreports.domain.ReportLog;
import ar.com.grooming.sexualgroomingreports.repository.ReportLogRepository;
import ar.com.grooming.sexualgroomingreports.service.ReportLogService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ReportLog}.
 */
@Service
@Transactional
public class ReportLogServiceImpl implements ReportLogService {

    private final Logger log = LoggerFactory.getLogger(ReportLogServiceImpl.class);

    private final ReportLogRepository reportLogRepository;

    public ReportLogServiceImpl(ReportLogRepository reportLogRepository) {
        this.reportLogRepository = reportLogRepository;
    }

    @Override
    public ReportLog save(ReportLog reportLog) {
        log.debug("Request to save ReportLog : {}", reportLog);
        return reportLogRepository.save(reportLog);
    }

    @Override
    public ReportLog update(ReportLog reportLog) {
        log.debug("Request to update ReportLog : {}", reportLog);
        return reportLogRepository.save(reportLog);
    }

    @Override
    public Optional<ReportLog> partialUpdate(ReportLog reportLog) {
        log.debug("Request to partially update ReportLog : {}", reportLog);

        return reportLogRepository
            .findById(reportLog.getId())
            .map(existingReportLog -> {
                if (reportLog.getDate() != null) {
                    existingReportLog.setDate(reportLog.getDate());
                }
                if (reportLog.getChange() != null) {
                    existingReportLog.setChange(reportLog.getChange());
                }

                return existingReportLog;
            })
            .map(reportLogRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReportLog> findAll(Pageable pageable) {
        log.debug("Request to get all ReportLogs");
        return reportLogRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReportLog> findOne(Long id) {
        log.debug("Request to get ReportLog : {}", id);
        return reportLogRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ReportLog : {}", id);
        reportLogRepository.deleteById(id);
    }
}
