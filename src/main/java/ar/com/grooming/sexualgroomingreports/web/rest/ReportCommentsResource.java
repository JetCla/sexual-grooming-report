package ar.com.grooming.sexualgroomingreports.web.rest;

import ar.com.grooming.sexualgroomingreports.domain.ReportComments;
import ar.com.grooming.sexualgroomingreports.repository.ReportCommentsRepository;
import ar.com.grooming.sexualgroomingreports.service.ReportCommentsService;
import ar.com.grooming.sexualgroomingreports.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ar.com.grooming.sexualgroomingreports.domain.ReportComments}.
 */
@RestController
@RequestMapping("/api")
public class ReportCommentsResource {

    private final Logger log = LoggerFactory.getLogger(ReportCommentsResource.class);

    private static final String ENTITY_NAME = "reportComments";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportCommentsService reportCommentsService;

    private final ReportCommentsRepository reportCommentsRepository;

    public ReportCommentsResource(ReportCommentsService reportCommentsService, ReportCommentsRepository reportCommentsRepository) {
        this.reportCommentsService = reportCommentsService;
        this.reportCommentsRepository = reportCommentsRepository;
    }

    /**
     * {@code POST  /report-comments} : Create a new reportComments.
     *
     * @param reportComments the reportComments to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportComments, or with status {@code 400 (Bad Request)} if the reportComments has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/report-comments")
    public ResponseEntity<ReportComments> createReportComments(@Valid @RequestBody ReportComments reportComments)
        throws URISyntaxException {
        log.debug("REST request to save ReportComments : {}", reportComments);
        if (reportComments.getId() != null) {
            throw new BadRequestAlertException("A new reportComments cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReportComments result = reportCommentsService.save(reportComments);
        return ResponseEntity
            .created(new URI("/api/report-comments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /report-comments/:id} : Updates an existing reportComments.
     *
     * @param id the id of the reportComments to save.
     * @param reportComments the reportComments to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportComments,
     * or with status {@code 400 (Bad Request)} if the reportComments is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportComments couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/report-comments/{id}")
    public ResponseEntity<ReportComments> updateReportComments(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReportComments reportComments
    ) throws URISyntaxException {
        log.debug("REST request to update ReportComments : {}, {}", id, reportComments);
        if (reportComments.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportComments.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportCommentsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReportComments result = reportCommentsService.update(reportComments);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportComments.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /report-comments/:id} : Partial updates given fields of an existing reportComments, field will ignore if it is null
     *
     * @param id the id of the reportComments to save.
     * @param reportComments the reportComments to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportComments,
     * or with status {@code 400 (Bad Request)} if the reportComments is not valid,
     * or with status {@code 404 (Not Found)} if the reportComments is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportComments couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/report-comments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReportComments> partialUpdateReportComments(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReportComments reportComments
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReportComments partially : {}, {}", id, reportComments);
        if (reportComments.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportComments.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportCommentsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReportComments> result = reportCommentsService.partialUpdate(reportComments);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportComments.getId().toString())
        );
    }

    /**
     * {@code GET  /report-comments} : get all the reportComments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reportComments in body.
     */
    @GetMapping("/report-comments")
    public ResponseEntity<List<ReportComments>> getAllReportComments(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ReportComments");
        Page<ReportComments> page = reportCommentsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /report-comments/:id} : get the "id" reportComments.
     *
     * @param id the id of the reportComments to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportComments, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/report-comments/{id}")
    public ResponseEntity<ReportComments> getReportComments(@PathVariable Long id) {
        log.debug("REST request to get ReportComments : {}", id);
        Optional<ReportComments> reportComments = reportCommentsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reportComments);
    }

    /**
     * {@code DELETE  /report-comments/:id} : delete the "id" reportComments.
     *
     * @param id the id of the reportComments to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/report-comments/{id}")
    public ResponseEntity<Void> deleteReportComments(@PathVariable Long id) {
        log.debug("REST request to delete ReportComments : {}", id);
        reportCommentsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
