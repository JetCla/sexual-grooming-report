package ar.com.grooming.sexualgroomingreports.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ar.com.grooming.sexualgroomingreports.IntegrationTest;
import ar.com.grooming.sexualgroomingreports.domain.ReportLog;
import ar.com.grooming.sexualgroomingreports.repository.ReportLogRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ReportLogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReportLogResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CHANGE = "AAAAAAAAAA";
    private static final String UPDATED_CHANGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/report-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReportLogRepository reportLogRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReportLogMockMvc;

    private ReportLog reportLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportLog createEntity(EntityManager em) {
        ReportLog reportLog = new ReportLog().date(DEFAULT_DATE).change(DEFAULT_CHANGE);
        return reportLog;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportLog createUpdatedEntity(EntityManager em) {
        ReportLog reportLog = new ReportLog().date(UPDATED_DATE).change(UPDATED_CHANGE);
        return reportLog;
    }

    @BeforeEach
    public void initTest() {
        reportLog = createEntity(em);
    }

    @Test
    @Transactional
    void createReportLog() throws Exception {
        int databaseSizeBeforeCreate = reportLogRepository.findAll().size();
        // Create the ReportLog
        restReportLogMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportLog))
            )
            .andExpect(status().isCreated());

        // Validate the ReportLog in the database
        List<ReportLog> reportLogList = reportLogRepository.findAll();
        assertThat(reportLogList).hasSize(databaseSizeBeforeCreate + 1);
        ReportLog testReportLog = reportLogList.get(reportLogList.size() - 1);
        assertThat(testReportLog.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testReportLog.getChange()).isEqualTo(DEFAULT_CHANGE);
    }

    @Test
    @Transactional
    void createReportLogWithExistingId() throws Exception {
        // Create the ReportLog with an existing ID
        reportLog.setId(1L);

        int databaseSizeBeforeCreate = reportLogRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportLogMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportLog in the database
        List<ReportLog> reportLogList = reportLogRepository.findAll();
        assertThat(reportLogList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportLogRepository.findAll().size();
        // set the field null
        reportLog.setDate(null);

        // Create the ReportLog, which fails.

        restReportLogMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportLog))
            )
            .andExpect(status().isBadRequest());

        List<ReportLog> reportLogList = reportLogRepository.findAll();
        assertThat(reportLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReportLogs() throws Exception {
        // Initialize the database
        reportLogRepository.saveAndFlush(reportLog);

        // Get all the reportLogList
        restReportLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].change").value(hasItem(DEFAULT_CHANGE)));
    }

    @Test
    @Transactional
    void getReportLog() throws Exception {
        // Initialize the database
        reportLogRepository.saveAndFlush(reportLog);

        // Get the reportLog
        restReportLogMockMvc
            .perform(get(ENTITY_API_URL_ID, reportLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reportLog.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.change").value(DEFAULT_CHANGE));
    }

    @Test
    @Transactional
    void getNonExistingReportLog() throws Exception {
        // Get the reportLog
        restReportLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReportLog() throws Exception {
        // Initialize the database
        reportLogRepository.saveAndFlush(reportLog);

        int databaseSizeBeforeUpdate = reportLogRepository.findAll().size();

        // Update the reportLog
        ReportLog updatedReportLog = reportLogRepository.findById(reportLog.getId()).get();
        // Disconnect from session so that the updates on updatedReportLog are not directly saved in db
        em.detach(updatedReportLog);
        updatedReportLog.date(UPDATED_DATE).change(UPDATED_CHANGE);

        restReportLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReportLog.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedReportLog))
            )
            .andExpect(status().isOk());

        // Validate the ReportLog in the database
        List<ReportLog> reportLogList = reportLogRepository.findAll();
        assertThat(reportLogList).hasSize(databaseSizeBeforeUpdate);
        ReportLog testReportLog = reportLogList.get(reportLogList.size() - 1);
        assertThat(testReportLog.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testReportLog.getChange()).isEqualTo(UPDATED_CHANGE);
    }

    @Test
    @Transactional
    void putNonExistingReportLog() throws Exception {
        int databaseSizeBeforeUpdate = reportLogRepository.findAll().size();
        reportLog.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportLog.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportLog in the database
        List<ReportLog> reportLogList = reportLogRepository.findAll();
        assertThat(reportLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReportLog() throws Exception {
        int databaseSizeBeforeUpdate = reportLogRepository.findAll().size();
        reportLog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportLog in the database
        List<ReportLog> reportLogList = reportLogRepository.findAll();
        assertThat(reportLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReportLog() throws Exception {
        int databaseSizeBeforeUpdate = reportLogRepository.findAll().size();
        reportLog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportLogMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportLog))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportLog in the database
        List<ReportLog> reportLogList = reportLogRepository.findAll();
        assertThat(reportLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReportLogWithPatch() throws Exception {
        // Initialize the database
        reportLogRepository.saveAndFlush(reportLog);

        int databaseSizeBeforeUpdate = reportLogRepository.findAll().size();

        // Update the reportLog using partial update
        ReportLog partialUpdatedReportLog = new ReportLog();
        partialUpdatedReportLog.setId(reportLog.getId());

        partialUpdatedReportLog.date(UPDATED_DATE).change(UPDATED_CHANGE);

        restReportLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportLog.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReportLog))
            )
            .andExpect(status().isOk());

        // Validate the ReportLog in the database
        List<ReportLog> reportLogList = reportLogRepository.findAll();
        assertThat(reportLogList).hasSize(databaseSizeBeforeUpdate);
        ReportLog testReportLog = reportLogList.get(reportLogList.size() - 1);
        assertThat(testReportLog.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testReportLog.getChange()).isEqualTo(UPDATED_CHANGE);
    }

    @Test
    @Transactional
    void fullUpdateReportLogWithPatch() throws Exception {
        // Initialize the database
        reportLogRepository.saveAndFlush(reportLog);

        int databaseSizeBeforeUpdate = reportLogRepository.findAll().size();

        // Update the reportLog using partial update
        ReportLog partialUpdatedReportLog = new ReportLog();
        partialUpdatedReportLog.setId(reportLog.getId());

        partialUpdatedReportLog.date(UPDATED_DATE).change(UPDATED_CHANGE);

        restReportLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportLog.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReportLog))
            )
            .andExpect(status().isOk());

        // Validate the ReportLog in the database
        List<ReportLog> reportLogList = reportLogRepository.findAll();
        assertThat(reportLogList).hasSize(databaseSizeBeforeUpdate);
        ReportLog testReportLog = reportLogList.get(reportLogList.size() - 1);
        assertThat(testReportLog.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testReportLog.getChange()).isEqualTo(UPDATED_CHANGE);
    }

    @Test
    @Transactional
    void patchNonExistingReportLog() throws Exception {
        int databaseSizeBeforeUpdate = reportLogRepository.findAll().size();
        reportLog.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reportLog.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportLog in the database
        List<ReportLog> reportLogList = reportLogRepository.findAll();
        assertThat(reportLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReportLog() throws Exception {
        int databaseSizeBeforeUpdate = reportLogRepository.findAll().size();
        reportLog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportLog in the database
        List<ReportLog> reportLogList = reportLogRepository.findAll();
        assertThat(reportLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReportLog() throws Exception {
        int databaseSizeBeforeUpdate = reportLogRepository.findAll().size();
        reportLog.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportLogMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportLog))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportLog in the database
        List<ReportLog> reportLogList = reportLogRepository.findAll();
        assertThat(reportLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReportLog() throws Exception {
        // Initialize the database
        reportLogRepository.saveAndFlush(reportLog);

        int databaseSizeBeforeDelete = reportLogRepository.findAll().size();

        // Delete the reportLog
        restReportLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, reportLog.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ReportLog> reportLogList = reportLogRepository.findAll();
        assertThat(reportLogList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
