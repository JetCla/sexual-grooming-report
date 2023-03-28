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
import ar.com.grooming.sexualgroomingreports.domain.Victim;
import ar.com.grooming.sexualgroomingreports.repository.VictimRepository;
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
 * Integration tests for the {@link VictimResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VictimResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;
    private static final Integer SMALLER_AGE = 1 - 1;

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVATIONS = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATIONS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/victims";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VictimRepository victimRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVictimMockMvc;

    private Victim victim;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Victim createEntity(EntityManager em) {
        Victim victim = new Victim()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .age(DEFAULT_AGE)
            .city(DEFAULT_CITY)
            .state(DEFAULT_STATE)
            .country(DEFAULT_COUNTRY)
            .observations(DEFAULT_OBSERVATIONS);
        return victim;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Victim createUpdatedEntity(EntityManager em) {
        Victim victim = new Victim()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .age(UPDATED_AGE)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .country(UPDATED_COUNTRY)
            .observations(UPDATED_OBSERVATIONS);
        return victim;
    }

    @BeforeEach
    public void initTest() {
        victim = createEntity(em);
    }

    @Test
    @Transactional
    void createVictim() throws Exception {
        int databaseSizeBeforeCreate = victimRepository.findAll().size();
        // Create the Victim
        restVictimMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(victim))
            )
            .andExpect(status().isCreated());

        // Validate the Victim in the database
        List<Victim> victimList = victimRepository.findAll();
        assertThat(victimList).hasSize(databaseSizeBeforeCreate + 1);
        Victim testVictim = victimList.get(victimList.size() - 1);
        assertThat(testVictim.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testVictim.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testVictim.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testVictim.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testVictim.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testVictim.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testVictim.getObservations()).isEqualTo(DEFAULT_OBSERVATIONS);
    }

    @Test
    @Transactional
    void createVictimWithExistingId() throws Exception {
        // Create the Victim with an existing ID
        victim.setId(1L);

        int databaseSizeBeforeCreate = victimRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVictimMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(victim))
            )
            .andExpect(status().isBadRequest());

        // Validate the Victim in the database
        List<Victim> victimList = victimRepository.findAll();
        assertThat(victimList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVictims() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        // Get all the victimList
        restVictimMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(victim.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].observations").value(hasItem(DEFAULT_OBSERVATIONS)));
    }

    @Test
    @Transactional
    void getVictim() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        // Get the victim
        restVictimMockMvc
            .perform(get(ENTITY_API_URL_ID, victim.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(victim.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.observations").value(DEFAULT_OBSERVATIONS));
    }

    @Test
    @Transactional
    void getVictimsByIdFiltering() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        Long id = victim.getId();

        defaultVictimShouldBeFound("id.equals=" + id);
        defaultVictimShouldNotBeFound("id.notEquals=" + id);

        defaultVictimShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVictimShouldNotBeFound("id.greaterThan=" + id);

        defaultVictimShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVictimShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVictimsByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        // Get all the victimList where firstName equals to DEFAULT_FIRST_NAME
        defaultVictimShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the victimList where firstName equals to UPDATED_FIRST_NAME
        defaultVictimShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllVictimsByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        // Get all the victimList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultVictimShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the victimList where firstName equals to UPDATED_FIRST_NAME
        defaultVictimShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllVictimsByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        // Get all the victimList where firstName is not null
        defaultVictimShouldBeFound("firstName.specified=true");

        // Get all the victimList where firstName is null
        defaultVictimShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllVictimsByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        // Get all the victimList where firstName contains DEFAULT_FIRST_NAME
        defaultVictimShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the victimList where firstName contains UPDATED_FIRST_NAME
        defaultVictimShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllVictimsByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        // Get all the victimList where firstName does not contain DEFAULT_FIRST_NAME
        defaultVictimShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the victimList where firstName does not contain UPDATED_FIRST_NAME
        defaultVictimShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllVictimsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        // Get all the victimList where lastName equals to DEFAULT_LAST_NAME
        defaultVictimShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the victimList where lastName equals to UPDATED_LAST_NAME
        defaultVictimShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllVictimsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        // Get all the victimList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultVictimShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the victimList where lastName equals to UPDATED_LAST_NAME
        defaultVictimShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllVictimsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        // Get all the victimList where lastName is not null
        defaultVictimShouldBeFound("lastName.specified=true");

        // Get all the victimList where lastName is null
        defaultVictimShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllVictimsByLastNameContainsSomething() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        // Get all the victimList where lastName contains DEFAULT_LAST_NAME
        defaultVictimShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the victimList where lastName contains UPDATED_LAST_NAME
        defaultVictimShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllVictimsByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        // Get all the victimList where lastName does not contain DEFAULT_LAST_NAME
        defaultVictimShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the victimList where lastName does not contain UPDATED_LAST_NAME
        defaultVictimShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllVictimsByAgeIsEqualToSomething() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        // Get all the victimList where age equals to DEFAULT_AGE
        defaultVictimShouldBeFound("age.equals=" + DEFAULT_AGE);

        // Get all the victimList where age equals to UPDATED_AGE
        defaultVictimShouldNotBeFound("age.equals=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    void getAllVictimsByAgeIsInShouldWork() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        // Get all the victimList where age in DEFAULT_AGE or UPDATED_AGE
        defaultVictimShouldBeFound("age.in=" + DEFAULT_AGE + "," + UPDATED_AGE);

        // Get all the victimList where age equals to UPDATED_AGE
        defaultVictimShouldNotBeFound("age.in=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    void getAllVictimsByAgeIsNullOrNotNull() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        // Get all the victimList where age is not null
        defaultVictimShouldBeFound("age.specified=true");

        // Get all the victimList where age is null
        defaultVictimShouldNotBeFound("age.specified=false");
    }

    @Test
    @Transactional
    void getAllVictimsByAgeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        // Get all the victimList where age is greater than or equal to DEFAULT_AGE
        defaultVictimShouldBeFound("age.greaterThanOrEqual=" + DEFAULT_AGE);

        // Get all the victimList where age is greater than or equal to UPDATED_AGE
        defaultVictimShouldNotBeFound("age.greaterThanOrEqual=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    void getAllVictimsByAgeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        // Get all the victimList where age is less than or equal to DEFAULT_AGE
        defaultVictimShouldBeFound("age.lessThanOrEqual=" + DEFAULT_AGE);

        // Get all the victimList where age is less than or equal to SMALLER_AGE
        defaultVictimShouldNotBeFound("age.lessThanOrEqual=" + SMALLER_AGE);
    }

    @Test
    @Transactional
    void getAllVictimsByAgeIsLessThanSomething() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        // Get all the victimList where age is less than DEFAULT_AGE
        defaultVictimShouldNotBeFound("age.lessThan=" + DEFAULT_AGE);

        // Get all the victimList where age is less than UPDATED_AGE
        defaultVictimShouldBeFound("age.lessThan=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    void getAllVictimsByAgeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        // Get all the victimList where age is greater than DEFAULT_AGE
        defaultVictimShouldNotBeFound("age.greaterThan=" + DEFAULT_AGE);

        // Get all the victimList where age is greater than SMALLER_AGE
        defaultVictimShouldBeFound("age.greaterThan=" + SMALLER_AGE);
    }

    @Test
    @Transactional
    void getAllVictimsByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        // Get all the victimList where city equals to DEFAULT_CITY
        defaultVictimShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the victimList where city equals to UPDATED_CITY
        defaultVictimShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllVictimsByCityIsInShouldWork() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        // Get all the victimList where city in DEFAULT_CITY or UPDATED_CITY
        defaultVictimShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the victimList where city equals to UPDATED_CITY
        defaultVictimShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllVictimsByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        // Get all the victimList where city is not null
        defaultVictimShouldBeFound("city.specified=true");

        // Get all the victimList where city is null
        defaultVictimShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    void getAllVictimsByCityContainsSomething() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        // Get all the victimList where city contains DEFAULT_CITY
        defaultVictimShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the victimList where city contains UPDATED_CITY
        defaultVictimShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllVictimsByCityNotContainsSomething() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        // Get all the victimList where city does not contain DEFAULT_CITY
        defaultVictimShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the victimList where city does not contain UPDATED_CITY
        defaultVictimShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllVictimsByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        // Get all the victimList where state equals to DEFAULT_STATE
        defaultVictimShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the victimList where state equals to UPDATED_STATE
        defaultVictimShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllVictimsByStateIsInShouldWork() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        // Get all the victimList where state in DEFAULT_STATE or UPDATED_STATE
        defaultVictimShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the victimList where state equals to UPDATED_STATE
        defaultVictimShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllVictimsByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        // Get all the victimList where state is not null
        defaultVictimShouldBeFound("state.specified=true");

        // Get all the victimList where state is null
        defaultVictimShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    void getAllVictimsByStateContainsSomething() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        // Get all the victimList where state contains DEFAULT_STATE
        defaultVictimShouldBeFound("state.contains=" + DEFAULT_STATE);

        // Get all the victimList where state contains UPDATED_STATE
        defaultVictimShouldNotBeFound("state.contains=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllVictimsByStateNotContainsSomething() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        // Get all the victimList where state does not contain DEFAULT_STATE
        defaultVictimShouldNotBeFound("state.doesNotContain=" + DEFAULT_STATE);

        // Get all the victimList where state does not contain UPDATED_STATE
        defaultVictimShouldBeFound("state.doesNotContain=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllVictimsByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        // Get all the victimList where country equals to DEFAULT_COUNTRY
        defaultVictimShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the victimList where country equals to UPDATED_COUNTRY
        defaultVictimShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllVictimsByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        // Get all the victimList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultVictimShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the victimList where country equals to UPDATED_COUNTRY
        defaultVictimShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllVictimsByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        // Get all the victimList where country is not null
        defaultVictimShouldBeFound("country.specified=true");

        // Get all the victimList where country is null
        defaultVictimShouldNotBeFound("country.specified=false");
    }

    @Test
    @Transactional
    void getAllVictimsByCountryContainsSomething() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        // Get all the victimList where country contains DEFAULT_COUNTRY
        defaultVictimShouldBeFound("country.contains=" + DEFAULT_COUNTRY);

        // Get all the victimList where country contains UPDATED_COUNTRY
        defaultVictimShouldNotBeFound("country.contains=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllVictimsByCountryNotContainsSomething() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        // Get all the victimList where country does not contain DEFAULT_COUNTRY
        defaultVictimShouldNotBeFound("country.doesNotContain=" + DEFAULT_COUNTRY);

        // Get all the victimList where country does not contain UPDATED_COUNTRY
        defaultVictimShouldBeFound("country.doesNotContain=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllVictimsByObservationsIsEqualToSomething() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        // Get all the victimList where observations equals to DEFAULT_OBSERVATIONS
        defaultVictimShouldBeFound("observations.equals=" + DEFAULT_OBSERVATIONS);

        // Get all the victimList where observations equals to UPDATED_OBSERVATIONS
        defaultVictimShouldNotBeFound("observations.equals=" + UPDATED_OBSERVATIONS);
    }

    @Test
    @Transactional
    void getAllVictimsByObservationsIsInShouldWork() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        // Get all the victimList where observations in DEFAULT_OBSERVATIONS or UPDATED_OBSERVATIONS
        defaultVictimShouldBeFound("observations.in=" + DEFAULT_OBSERVATIONS + "," + UPDATED_OBSERVATIONS);

        // Get all the victimList where observations equals to UPDATED_OBSERVATIONS
        defaultVictimShouldNotBeFound("observations.in=" + UPDATED_OBSERVATIONS);
    }

    @Test
    @Transactional
    void getAllVictimsByObservationsIsNullOrNotNull() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        // Get all the victimList where observations is not null
        defaultVictimShouldBeFound("observations.specified=true");

        // Get all the victimList where observations is null
        defaultVictimShouldNotBeFound("observations.specified=false");
    }

    @Test
    @Transactional
    void getAllVictimsByObservationsContainsSomething() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        // Get all the victimList where observations contains DEFAULT_OBSERVATIONS
        defaultVictimShouldBeFound("observations.contains=" + DEFAULT_OBSERVATIONS);

        // Get all the victimList where observations contains UPDATED_OBSERVATIONS
        defaultVictimShouldNotBeFound("observations.contains=" + UPDATED_OBSERVATIONS);
    }

    @Test
    @Transactional
    void getAllVictimsByObservationsNotContainsSomething() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        // Get all the victimList where observations does not contain DEFAULT_OBSERVATIONS
        defaultVictimShouldNotBeFound("observations.doesNotContain=" + DEFAULT_OBSERVATIONS);

        // Get all the victimList where observations does not contain UPDATED_OBSERVATIONS
        defaultVictimShouldBeFound("observations.doesNotContain=" + UPDATED_OBSERVATIONS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVictimShouldBeFound(String filter) throws Exception {
        restVictimMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(victim.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].observations").value(hasItem(DEFAULT_OBSERVATIONS)));

        // Check, that the count call also returns 1
        restVictimMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVictimShouldNotBeFound(String filter) throws Exception {
        restVictimMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVictimMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVictim() throws Exception {
        // Get the victim
        restVictimMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVictim() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        int databaseSizeBeforeUpdate = victimRepository.findAll().size();

        // Update the victim
        Victim updatedVictim = victimRepository.findById(victim.getId()).get();
        // Disconnect from session so that the updates on updatedVictim are not directly saved in db
        em.detach(updatedVictim);
        updatedVictim
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .age(UPDATED_AGE)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .country(UPDATED_COUNTRY)
            .observations(UPDATED_OBSERVATIONS);

        restVictimMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVictim.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVictim))
            )
            .andExpect(status().isOk());

        // Validate the Victim in the database
        List<Victim> victimList = victimRepository.findAll();
        assertThat(victimList).hasSize(databaseSizeBeforeUpdate);
        Victim testVictim = victimList.get(victimList.size() - 1);
        assertThat(testVictim.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testVictim.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testVictim.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testVictim.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testVictim.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testVictim.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testVictim.getObservations()).isEqualTo(UPDATED_OBSERVATIONS);
    }

    @Test
    @Transactional
    void putNonExistingVictim() throws Exception {
        int databaseSizeBeforeUpdate = victimRepository.findAll().size();
        victim.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVictimMockMvc
            .perform(
                put(ENTITY_API_URL_ID, victim.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(victim))
            )
            .andExpect(status().isBadRequest());

        // Validate the Victim in the database
        List<Victim> victimList = victimRepository.findAll();
        assertThat(victimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVictim() throws Exception {
        int databaseSizeBeforeUpdate = victimRepository.findAll().size();
        victim.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVictimMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(victim))
            )
            .andExpect(status().isBadRequest());

        // Validate the Victim in the database
        List<Victim> victimList = victimRepository.findAll();
        assertThat(victimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVictim() throws Exception {
        int databaseSizeBeforeUpdate = victimRepository.findAll().size();
        victim.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVictimMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(victim))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Victim in the database
        List<Victim> victimList = victimRepository.findAll();
        assertThat(victimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVictimWithPatch() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        int databaseSizeBeforeUpdate = victimRepository.findAll().size();

        // Update the victim using partial update
        Victim partialUpdatedVictim = new Victim();
        partialUpdatedVictim.setId(victim.getId());

        partialUpdatedVictim.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME).age(UPDATED_AGE).country(UPDATED_COUNTRY);

        restVictimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVictim.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVictim))
            )
            .andExpect(status().isOk());

        // Validate the Victim in the database
        List<Victim> victimList = victimRepository.findAll();
        assertThat(victimList).hasSize(databaseSizeBeforeUpdate);
        Victim testVictim = victimList.get(victimList.size() - 1);
        assertThat(testVictim.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testVictim.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testVictim.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testVictim.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testVictim.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testVictim.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testVictim.getObservations()).isEqualTo(DEFAULT_OBSERVATIONS);
    }

    @Test
    @Transactional
    void fullUpdateVictimWithPatch() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        int databaseSizeBeforeUpdate = victimRepository.findAll().size();

        // Update the victim using partial update
        Victim partialUpdatedVictim = new Victim();
        partialUpdatedVictim.setId(victim.getId());

        partialUpdatedVictim
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .age(UPDATED_AGE)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .country(UPDATED_COUNTRY)
            .observations(UPDATED_OBSERVATIONS);

        restVictimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVictim.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVictim))
            )
            .andExpect(status().isOk());

        // Validate the Victim in the database
        List<Victim> victimList = victimRepository.findAll();
        assertThat(victimList).hasSize(databaseSizeBeforeUpdate);
        Victim testVictim = victimList.get(victimList.size() - 1);
        assertThat(testVictim.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testVictim.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testVictim.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testVictim.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testVictim.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testVictim.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testVictim.getObservations()).isEqualTo(UPDATED_OBSERVATIONS);
    }

    @Test
    @Transactional
    void patchNonExistingVictim() throws Exception {
        int databaseSizeBeforeUpdate = victimRepository.findAll().size();
        victim.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVictimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, victim.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(victim))
            )
            .andExpect(status().isBadRequest());

        // Validate the Victim in the database
        List<Victim> victimList = victimRepository.findAll();
        assertThat(victimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVictim() throws Exception {
        int databaseSizeBeforeUpdate = victimRepository.findAll().size();
        victim.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVictimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(victim))
            )
            .andExpect(status().isBadRequest());

        // Validate the Victim in the database
        List<Victim> victimList = victimRepository.findAll();
        assertThat(victimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVictim() throws Exception {
        int databaseSizeBeforeUpdate = victimRepository.findAll().size();
        victim.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVictimMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(victim))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Victim in the database
        List<Victim> victimList = victimRepository.findAll();
        assertThat(victimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVictim() throws Exception {
        // Initialize the database
        victimRepository.saveAndFlush(victim);

        int databaseSizeBeforeDelete = victimRepository.findAll().size();

        // Delete the victim
        restVictimMockMvc
            .perform(delete(ENTITY_API_URL_ID, victim.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Victim> victimList = victimRepository.findAll();
        assertThat(victimList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
