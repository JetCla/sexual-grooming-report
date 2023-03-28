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
import ar.com.grooming.sexualgroomingreports.domain.ReportComments;
import ar.com.grooming.sexualgroomingreports.repository.ReportCommentsRepository;
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
 * Integration tests for the {@link ReportCommentsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReportCommentsResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/report-comments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReportCommentsRepository reportCommentsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReportCommentsMockMvc;

    private ReportComments reportComments;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportComments createEntity(EntityManager em) {
        ReportComments reportComments = new ReportComments().date(DEFAULT_DATE);
        return reportComments;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportComments createUpdatedEntity(EntityManager em) {
        ReportComments reportComments = new ReportComments().date(UPDATED_DATE);
        return reportComments;
    }

    @BeforeEach
    public void initTest() {
        reportComments = createEntity(em);
    }

    @Test
    @Transactional
    void createReportComments() throws Exception {
        int databaseSizeBeforeCreate = reportCommentsRepository.findAll().size();
        // Create the ReportComments
        restReportCommentsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportComments))
            )
            .andExpect(status().isCreated());

        // Validate the ReportComments in the database
        List<ReportComments> reportCommentsList = reportCommentsRepository.findAll();
        assertThat(reportCommentsList).hasSize(databaseSizeBeforeCreate + 1);
        ReportComments testReportComments = reportCommentsList.get(reportCommentsList.size() - 1);
        assertThat(testReportComments.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createReportCommentsWithExistingId() throws Exception {
        // Create the ReportComments with an existing ID
        reportComments.setId(1L);

        int databaseSizeBeforeCreate = reportCommentsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportCommentsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportComments))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportComments in the database
        List<ReportComments> reportCommentsList = reportCommentsRepository.findAll();
        assertThat(reportCommentsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportCommentsRepository.findAll().size();
        // set the field null
        reportComments.setDate(null);

        // Create the ReportComments, which fails.

        restReportCommentsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportComments))
            )
            .andExpect(status().isBadRequest());

        List<ReportComments> reportCommentsList = reportCommentsRepository.findAll();
        assertThat(reportCommentsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReportComments() throws Exception {
        // Initialize the database
        reportCommentsRepository.saveAndFlush(reportComments);

        // Get all the reportCommentsList
        restReportCommentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportComments.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    void getReportComments() throws Exception {
        // Initialize the database
        reportCommentsRepository.saveAndFlush(reportComments);

        // Get the reportComments
        restReportCommentsMockMvc
            .perform(get(ENTITY_API_URL_ID, reportComments.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reportComments.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingReportComments() throws Exception {
        // Get the reportComments
        restReportCommentsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReportComments() throws Exception {
        // Initialize the database
        reportCommentsRepository.saveAndFlush(reportComments);

        int databaseSizeBeforeUpdate = reportCommentsRepository.findAll().size();

        // Update the reportComments
        ReportComments updatedReportComments = reportCommentsRepository.findById(reportComments.getId()).get();
        // Disconnect from session so that the updates on updatedReportComments are not directly saved in db
        em.detach(updatedReportComments);
        updatedReportComments.date(UPDATED_DATE);

        restReportCommentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReportComments.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedReportComments))
            )
            .andExpect(status().isOk());

        // Validate the ReportComments in the database
        List<ReportComments> reportCommentsList = reportCommentsRepository.findAll();
        assertThat(reportCommentsList).hasSize(databaseSizeBeforeUpdate);
        ReportComments testReportComments = reportCommentsList.get(reportCommentsList.size() - 1);
        assertThat(testReportComments.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingReportComments() throws Exception {
        int databaseSizeBeforeUpdate = reportCommentsRepository.findAll().size();
        reportComments.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportCommentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportComments.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportComments))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportComments in the database
        List<ReportComments> reportCommentsList = reportCommentsRepository.findAll();
        assertThat(reportCommentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReportComments() throws Exception {
        int databaseSizeBeforeUpdate = reportCommentsRepository.findAll().size();
        reportComments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportCommentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportComments))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportComments in the database
        List<ReportComments> reportCommentsList = reportCommentsRepository.findAll();
        assertThat(reportCommentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReportComments() throws Exception {
        int databaseSizeBeforeUpdate = reportCommentsRepository.findAll().size();
        reportComments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportCommentsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportComments))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportComments in the database
        List<ReportComments> reportCommentsList = reportCommentsRepository.findAll();
        assertThat(reportCommentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReportCommentsWithPatch() throws Exception {
        // Initialize the database
        reportCommentsRepository.saveAndFlush(reportComments);

        int databaseSizeBeforeUpdate = reportCommentsRepository.findAll().size();

        // Update the reportComments using partial update
        ReportComments partialUpdatedReportComments = new ReportComments();
        partialUpdatedReportComments.setId(reportComments.getId());

        restReportCommentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportComments.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReportComments))
            )
            .andExpect(status().isOk());

        // Validate the ReportComments in the database
        List<ReportComments> reportCommentsList = reportCommentsRepository.findAll();
        assertThat(reportCommentsList).hasSize(databaseSizeBeforeUpdate);
        ReportComments testReportComments = reportCommentsList.get(reportCommentsList.size() - 1);
        assertThat(testReportComments.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void fullUpdateReportCommentsWithPatch() throws Exception {
        // Initialize the database
        reportCommentsRepository.saveAndFlush(reportComments);

        int databaseSizeBeforeUpdate = reportCommentsRepository.findAll().size();

        // Update the reportComments using partial update
        ReportComments partialUpdatedReportComments = new ReportComments();
        partialUpdatedReportComments.setId(reportComments.getId());

        partialUpdatedReportComments.date(UPDATED_DATE);

        restReportCommentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportComments.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReportComments))
            )
            .andExpect(status().isOk());

        // Validate the ReportComments in the database
        List<ReportComments> reportCommentsList = reportCommentsRepository.findAll();
        assertThat(reportCommentsList).hasSize(databaseSizeBeforeUpdate);
        ReportComments testReportComments = reportCommentsList.get(reportCommentsList.size() - 1);
        assertThat(testReportComments.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingReportComments() throws Exception {
        int databaseSizeBeforeUpdate = reportCommentsRepository.findAll().size();
        reportComments.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportCommentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reportComments.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportComments))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportComments in the database
        List<ReportComments> reportCommentsList = reportCommentsRepository.findAll();
        assertThat(reportCommentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReportComments() throws Exception {
        int databaseSizeBeforeUpdate = reportCommentsRepository.findAll().size();
        reportComments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportCommentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportComments))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportComments in the database
        List<ReportComments> reportCommentsList = reportCommentsRepository.findAll();
        assertThat(reportCommentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReportComments() throws Exception {
        int databaseSizeBeforeUpdate = reportCommentsRepository.findAll().size();
        reportComments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportCommentsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportComments))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportComments in the database
        List<ReportComments> reportCommentsList = reportCommentsRepository.findAll();
        assertThat(reportCommentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReportComments() throws Exception {
        // Initialize the database
        reportCommentsRepository.saveAndFlush(reportComments);

        int databaseSizeBeforeDelete = reportCommentsRepository.findAll().size();

        // Delete the reportComments
        restReportCommentsMockMvc
            .perform(delete(ENTITY_API_URL_ID, reportComments.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ReportComments> reportCommentsList = reportCommentsRepository.findAll();
        assertThat(reportCommentsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
