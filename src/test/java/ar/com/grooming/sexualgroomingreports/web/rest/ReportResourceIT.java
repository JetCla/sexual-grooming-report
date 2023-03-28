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
import ar.com.grooming.sexualgroomingreports.domain.Report;
import ar.com.grooming.sexualgroomingreports.domain.ReportComments;
import ar.com.grooming.sexualgroomingreports.domain.ReportLog;
import ar.com.grooming.sexualgroomingreports.domain.User;
import ar.com.grooming.sexualgroomingreports.domain.Victim;
import ar.com.grooming.sexualgroomingreports.repository.ReportRepository;
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
 * Integration tests for the {@link ReportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReportResourceIT {

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_OFFICE_ASSIGNED = "AAAAAAAAAA";
    private static final String UPDATED_OFFICE_ASSIGNED = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReportMockMvc;

    private Report report;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Report createEntity(EntityManager em) {
        Report report = new Report()
            .number(DEFAULT_NUMBER)
            .date(DEFAULT_DATE)
            .officeAssigned(DEFAULT_OFFICE_ASSIGNED)
            .description(DEFAULT_DESCRIPTION)
            .location(DEFAULT_LOCATION);
        return report;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Report createUpdatedEntity(EntityManager em) {
        Report report = new Report()
            .number(UPDATED_NUMBER)
            .date(UPDATED_DATE)
            .officeAssigned(UPDATED_OFFICE_ASSIGNED)
            .description(UPDATED_DESCRIPTION)
            .location(UPDATED_LOCATION);
        return report;
    }

    @BeforeEach
    public void initTest() {
        report = createEntity(em);
    }

    @Test
    @Transactional
    void createReport() throws Exception {
        int databaseSizeBeforeCreate = reportRepository.findAll().size();
        // Create the Report
        restReportMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(report))
            )
            .andExpect(status().isCreated());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeCreate + 1);
        Report testReport = reportList.get(reportList.size() - 1);
        assertThat(testReport.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testReport.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testReport.getOfficeAssigned()).isEqualTo(DEFAULT_OFFICE_ASSIGNED);
        assertThat(testReport.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testReport.getLocation()).isEqualTo(DEFAULT_LOCATION);
    }

    @Test
    @Transactional
    void createReportWithExistingId() throws Exception {
        // Create the Report with an existing ID
        report.setId(1L);

        int databaseSizeBeforeCreate = reportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(report))
            )
            .andExpect(status().isBadRequest());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportRepository.findAll().size();
        // set the field null
        report.setNumber(null);

        // Create the Report, which fails.

        restReportMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(report))
            )
            .andExpect(status().isBadRequest());

        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = reportRepository.findAll().size();
        // set the field null
        report.setDate(null);

        // Create the Report, which fails.

        restReportMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(report))
            )
            .andExpect(status().isBadRequest());

        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReports() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList
        restReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(report.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].officeAssigned").value(hasItem(DEFAULT_OFFICE_ASSIGNED)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)));
    }

    @Test
    @Transactional
    void getReport() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get the report
        restReportMockMvc
            .perform(get(ENTITY_API_URL_ID, report.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(report.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.officeAssigned").value(DEFAULT_OFFICE_ASSIGNED))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION));
    }

    @Test
    @Transactional
    void getReportsByIdFiltering() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        Long id = report.getId();

        defaultReportShouldBeFound("id.equals=" + id);
        defaultReportShouldNotBeFound("id.notEquals=" + id);

        defaultReportShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultReportShouldNotBeFound("id.greaterThan=" + id);

        defaultReportShouldBeFound("id.lessThanOrEqual=" + id);
        defaultReportShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReportsByNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where number equals to DEFAULT_NUMBER
        defaultReportShouldBeFound("number.equals=" + DEFAULT_NUMBER);

        // Get all the reportList where number equals to UPDATED_NUMBER
        defaultReportShouldNotBeFound("number.equals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllReportsByNumberIsInShouldWork() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where number in DEFAULT_NUMBER or UPDATED_NUMBER
        defaultReportShouldBeFound("number.in=" + DEFAULT_NUMBER + "," + UPDATED_NUMBER);

        // Get all the reportList where number equals to UPDATED_NUMBER
        defaultReportShouldNotBeFound("number.in=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllReportsByNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where number is not null
        defaultReportShouldBeFound("number.specified=true");

        // Get all the reportList where number is null
        defaultReportShouldNotBeFound("number.specified=false");
    }

    @Test
    @Transactional
    void getAllReportsByNumberContainsSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where number contains DEFAULT_NUMBER
        defaultReportShouldBeFound("number.contains=" + DEFAULT_NUMBER);

        // Get all the reportList where number contains UPDATED_NUMBER
        defaultReportShouldNotBeFound("number.contains=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllReportsByNumberNotContainsSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where number does not contain DEFAULT_NUMBER
        defaultReportShouldNotBeFound("number.doesNotContain=" + DEFAULT_NUMBER);

        // Get all the reportList where number does not contain UPDATED_NUMBER
        defaultReportShouldBeFound("number.doesNotContain=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    void getAllReportsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where date equals to DEFAULT_DATE
        defaultReportShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the reportList where date equals to UPDATED_DATE
        defaultReportShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllReportsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where date in DEFAULT_DATE or UPDATED_DATE
        defaultReportShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the reportList where date equals to UPDATED_DATE
        defaultReportShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllReportsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where date is not null
        defaultReportShouldBeFound("date.specified=true");

        // Get all the reportList where date is null
        defaultReportShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllReportsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where date is greater than or equal to DEFAULT_DATE
        defaultReportShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the reportList where date is greater than or equal to UPDATED_DATE
        defaultReportShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllReportsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where date is less than or equal to DEFAULT_DATE
        defaultReportShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the reportList where date is less than or equal to SMALLER_DATE
        defaultReportShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllReportsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where date is less than DEFAULT_DATE
        defaultReportShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the reportList where date is less than UPDATED_DATE
        defaultReportShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllReportsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where date is greater than DEFAULT_DATE
        defaultReportShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the reportList where date is greater than SMALLER_DATE
        defaultReportShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllReportsByOfficeAssignedIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where officeAssigned equals to DEFAULT_OFFICE_ASSIGNED
        defaultReportShouldBeFound("officeAssigned.equals=" + DEFAULT_OFFICE_ASSIGNED);

        // Get all the reportList where officeAssigned equals to UPDATED_OFFICE_ASSIGNED
        defaultReportShouldNotBeFound("officeAssigned.equals=" + UPDATED_OFFICE_ASSIGNED);
    }

    @Test
    @Transactional
    void getAllReportsByOfficeAssignedIsInShouldWork() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where officeAssigned in DEFAULT_OFFICE_ASSIGNED or UPDATED_OFFICE_ASSIGNED
        defaultReportShouldBeFound("officeAssigned.in=" + DEFAULT_OFFICE_ASSIGNED + "," + UPDATED_OFFICE_ASSIGNED);

        // Get all the reportList where officeAssigned equals to UPDATED_OFFICE_ASSIGNED
        defaultReportShouldNotBeFound("officeAssigned.in=" + UPDATED_OFFICE_ASSIGNED);
    }

    @Test
    @Transactional
    void getAllReportsByOfficeAssignedIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where officeAssigned is not null
        defaultReportShouldBeFound("officeAssigned.specified=true");

        // Get all the reportList where officeAssigned is null
        defaultReportShouldNotBeFound("officeAssigned.specified=false");
    }

    @Test
    @Transactional
    void getAllReportsByOfficeAssignedContainsSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where officeAssigned contains DEFAULT_OFFICE_ASSIGNED
        defaultReportShouldBeFound("officeAssigned.contains=" + DEFAULT_OFFICE_ASSIGNED);

        // Get all the reportList where officeAssigned contains UPDATED_OFFICE_ASSIGNED
        defaultReportShouldNotBeFound("officeAssigned.contains=" + UPDATED_OFFICE_ASSIGNED);
    }

    @Test
    @Transactional
    void getAllReportsByOfficeAssignedNotContainsSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where officeAssigned does not contain DEFAULT_OFFICE_ASSIGNED
        defaultReportShouldNotBeFound("officeAssigned.doesNotContain=" + DEFAULT_OFFICE_ASSIGNED);

        // Get all the reportList where officeAssigned does not contain UPDATED_OFFICE_ASSIGNED
        defaultReportShouldBeFound("officeAssigned.doesNotContain=" + UPDATED_OFFICE_ASSIGNED);
    }

    @Test
    @Transactional
    void getAllReportsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where description equals to DEFAULT_DESCRIPTION
        defaultReportShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the reportList where description equals to UPDATED_DESCRIPTION
        defaultReportShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllReportsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultReportShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the reportList where description equals to UPDATED_DESCRIPTION
        defaultReportShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllReportsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where description is not null
        defaultReportShouldBeFound("description.specified=true");

        // Get all the reportList where description is null
        defaultReportShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllReportsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where description contains DEFAULT_DESCRIPTION
        defaultReportShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the reportList where description contains UPDATED_DESCRIPTION
        defaultReportShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllReportsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where description does not contain DEFAULT_DESCRIPTION
        defaultReportShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the reportList where description does not contain UPDATED_DESCRIPTION
        defaultReportShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllReportsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where location equals to DEFAULT_LOCATION
        defaultReportShouldBeFound("location.equals=" + DEFAULT_LOCATION);

        // Get all the reportList where location equals to UPDATED_LOCATION
        defaultReportShouldNotBeFound("location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllReportsByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where location in DEFAULT_LOCATION or UPDATED_LOCATION
        defaultReportShouldBeFound("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION);

        // Get all the reportList where location equals to UPDATED_LOCATION
        defaultReportShouldNotBeFound("location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllReportsByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where location is not null
        defaultReportShouldBeFound("location.specified=true");

        // Get all the reportList where location is null
        defaultReportShouldNotBeFound("location.specified=false");
    }

    @Test
    @Transactional
    void getAllReportsByLocationContainsSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where location contains DEFAULT_LOCATION
        defaultReportShouldBeFound("location.contains=" + DEFAULT_LOCATION);

        // Get all the reportList where location contains UPDATED_LOCATION
        defaultReportShouldNotBeFound("location.contains=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllReportsByLocationNotContainsSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where location does not contain DEFAULT_LOCATION
        defaultReportShouldNotBeFound("location.doesNotContain=" + DEFAULT_LOCATION);

        // Get all the reportList where location does not contain UPDATED_LOCATION
        defaultReportShouldBeFound("location.doesNotContain=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllReportsByReportCommentsIsEqualToSomething() throws Exception {
        ReportComments reportComments;
        if (TestUtil.findAll(em, ReportComments.class).isEmpty()) {
            reportRepository.saveAndFlush(report);
            reportComments = ReportCommentsResourceIT.createEntity(em);
        } else {
            reportComments = TestUtil.findAll(em, ReportComments.class).get(0);
        }
        em.persist(reportComments);
        em.flush();
        report.addReportComments(reportComments);
        reportRepository.saveAndFlush(report);
        Long reportCommentsId = reportComments.getId();

        // Get all the reportList where reportComments equals to reportCommentsId
        defaultReportShouldBeFound("reportCommentsId.equals=" + reportCommentsId);

        // Get all the reportList where reportComments equals to (reportCommentsId + 1)
        defaultReportShouldNotBeFound("reportCommentsId.equals=" + (reportCommentsId + 1));
    }

    @Test
    @Transactional
    void getAllReportsByReportLogIsEqualToSomething() throws Exception {
        ReportLog reportLog;
        if (TestUtil.findAll(em, ReportLog.class).isEmpty()) {
            reportRepository.saveAndFlush(report);
            reportLog = ReportLogResourceIT.createEntity(em);
        } else {
            reportLog = TestUtil.findAll(em, ReportLog.class).get(0);
        }
        em.persist(reportLog);
        em.flush();
        report.addReportLog(reportLog);
        reportRepository.saveAndFlush(report);
        Long reportLogId = reportLog.getId();

        // Get all the reportList where reportLog equals to reportLogId
        defaultReportShouldBeFound("reportLogId.equals=" + reportLogId);

        // Get all the reportList where reportLog equals to (reportLogId + 1)
        defaultReportShouldNotBeFound("reportLogId.equals=" + (reportLogId + 1));
    }

    @Test
    @Transactional
    void getAllReportsByVictimIsEqualToSomething() throws Exception {
        Victim victim;
        if (TestUtil.findAll(em, Victim.class).isEmpty()) {
            reportRepository.saveAndFlush(report);
            victim = VictimResourceIT.createEntity(em);
        } else {
            victim = TestUtil.findAll(em, Victim.class).get(0);
        }
        em.persist(victim);
        em.flush();
        report.setVictim(victim);
        reportRepository.saveAndFlush(report);
        Long victimId = victim.getId();

        // Get all the reportList where victim equals to victimId
        defaultReportShouldBeFound("victimId.equals=" + victimId);

        // Get all the reportList where victim equals to (victimId + 1)
        defaultReportShouldNotBeFound("victimId.equals=" + (victimId + 1));
    }

    @Test
    @Transactional
    void getAllReportsByOwnerIsEqualToSomething() throws Exception {
        User owner;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            reportRepository.saveAndFlush(report);
            owner = UserResourceIT.createEntity(em);
        } else {
            owner = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(owner);
        em.flush();
        report.setOwner(owner);
        reportRepository.saveAndFlush(report);
        String ownerId = owner.getId();

        // Get all the reportList where owner equals to ownerId
        defaultReportShouldBeFound("ownerId.equals=" + ownerId);

        // Get all the reportList where owner equals to "invalid-id"
        defaultReportShouldNotBeFound("ownerId.equals=" + "invalid-id");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReportShouldBeFound(String filter) throws Exception {
        restReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(report.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].officeAssigned").value(hasItem(DEFAULT_OFFICE_ASSIGNED)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)));

        // Check, that the count call also returns 1
        restReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReportShouldNotBeFound(String filter) throws Exception {
        restReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReport() throws Exception {
        // Get the report
        restReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReport() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        int databaseSizeBeforeUpdate = reportRepository.findAll().size();

        // Update the report
        Report updatedReport = reportRepository.findById(report.getId()).get();
        // Disconnect from session so that the updates on updatedReport are not directly saved in db
        em.detach(updatedReport);
        updatedReport
            .number(UPDATED_NUMBER)
            .date(UPDATED_DATE)
            .officeAssigned(UPDATED_OFFICE_ASSIGNED)
            .description(UPDATED_DESCRIPTION)
            .location(UPDATED_LOCATION);

        restReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReport.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedReport))
            )
            .andExpect(status().isOk());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
        Report testReport = reportList.get(reportList.size() - 1);
        assertThat(testReport.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testReport.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testReport.getOfficeAssigned()).isEqualTo(UPDATED_OFFICE_ASSIGNED);
        assertThat(testReport.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testReport.getLocation()).isEqualTo(UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void putNonExistingReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().size();
        report.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, report.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(report))
            )
            .andExpect(status().isBadRequest());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().size();
        report.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(report))
            )
            .andExpect(status().isBadRequest());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().size();
        report.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(report))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReportWithPatch() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        int databaseSizeBeforeUpdate = reportRepository.findAll().size();

        // Update the report using partial update
        Report partialUpdatedReport = new Report();
        partialUpdatedReport.setId(report.getId());

        partialUpdatedReport.date(UPDATED_DATE).officeAssigned(UPDATED_OFFICE_ASSIGNED).description(UPDATED_DESCRIPTION);

        restReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReport.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReport))
            )
            .andExpect(status().isOk());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
        Report testReport = reportList.get(reportList.size() - 1);
        assertThat(testReport.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testReport.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testReport.getOfficeAssigned()).isEqualTo(UPDATED_OFFICE_ASSIGNED);
        assertThat(testReport.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testReport.getLocation()).isEqualTo(DEFAULT_LOCATION);
    }

    @Test
    @Transactional
    void fullUpdateReportWithPatch() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        int databaseSizeBeforeUpdate = reportRepository.findAll().size();

        // Update the report using partial update
        Report partialUpdatedReport = new Report();
        partialUpdatedReport.setId(report.getId());

        partialUpdatedReport
            .number(UPDATED_NUMBER)
            .date(UPDATED_DATE)
            .officeAssigned(UPDATED_OFFICE_ASSIGNED)
            .description(UPDATED_DESCRIPTION)
            .location(UPDATED_LOCATION);

        restReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReport.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReport))
            )
            .andExpect(status().isOk());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
        Report testReport = reportList.get(reportList.size() - 1);
        assertThat(testReport.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testReport.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testReport.getOfficeAssigned()).isEqualTo(UPDATED_OFFICE_ASSIGNED);
        assertThat(testReport.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testReport.getLocation()).isEqualTo(UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void patchNonExistingReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().size();
        report.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, report.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(report))
            )
            .andExpect(status().isBadRequest());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().size();
        report.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(report))
            )
            .andExpect(status().isBadRequest());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().size();
        report.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(report))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReport() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        int databaseSizeBeforeDelete = reportRepository.findAll().size();

        // Delete the report
        restReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, report.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
