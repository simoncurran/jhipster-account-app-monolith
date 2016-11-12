package com.simon.account.web.rest;

import com.simon.account.AccountsApp;

import com.simon.account.domain.Agreement;
import com.simon.account.repository.AgreementRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AgreementResource REST controller.
 *
 * @see AgreementResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountsApp.class)
public class AgreementResourceIntTest {

    private static final String DEFAULT_AGREEMENT_NUMBER = "AAAAA";
    private static final String UPDATED_AGREEMENT_NUMBER = "BBBBB";

    private static final String DEFAULT_LOB = "AAAAA";
    private static final String UPDATED_LOB = "BBBBB";

    private static final Float DEFAULT_PREMIUM = 1F;
    private static final Float UPDATED_PREMIUM = 2F;

    @Inject
    private AgreementRepository agreementRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAgreementMockMvc;

    private Agreement agreement;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AgreementResource agreementResource = new AgreementResource();
        ReflectionTestUtils.setField(agreementResource, "agreementRepository", agreementRepository);
        this.restAgreementMockMvc = MockMvcBuilders.standaloneSetup(agreementResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Agreement createEntity(EntityManager em) {
        Agreement agreement = new Agreement()
                .agreementNumber(DEFAULT_AGREEMENT_NUMBER)
                .lob(DEFAULT_LOB)
                .premium(DEFAULT_PREMIUM);
        return agreement;
    }

    @Before
    public void initTest() {
        agreement = createEntity(em);
    }

    @Test
    @Transactional
    public void createAgreement() throws Exception {
        int databaseSizeBeforeCreate = agreementRepository.findAll().size();

        // Create the Agreement

        restAgreementMockMvc.perform(post("/api/agreements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(agreement)))
                .andExpect(status().isCreated());

        // Validate the Agreement in the database
        List<Agreement> agreements = agreementRepository.findAll();
        assertThat(agreements).hasSize(databaseSizeBeforeCreate + 1);
        Agreement testAgreement = agreements.get(agreements.size() - 1);
        assertThat(testAgreement.getAgreementNumber()).isEqualTo(DEFAULT_AGREEMENT_NUMBER);
        assertThat(testAgreement.getLob()).isEqualTo(DEFAULT_LOB);
        assertThat(testAgreement.getPremium()).isEqualTo(DEFAULT_PREMIUM);
    }

    @Test
    @Transactional
    public void checkAgreementNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = agreementRepository.findAll().size();
        // set the field null
        agreement.setAgreementNumber(null);

        // Create the Agreement, which fails.

        restAgreementMockMvc.perform(post("/api/agreements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(agreement)))
                .andExpect(status().isBadRequest());

        List<Agreement> agreements = agreementRepository.findAll();
        assertThat(agreements).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLobIsRequired() throws Exception {
        int databaseSizeBeforeTest = agreementRepository.findAll().size();
        // set the field null
        agreement.setLob(null);

        // Create the Agreement, which fails.

        restAgreementMockMvc.perform(post("/api/agreements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(agreement)))
                .andExpect(status().isBadRequest());

        List<Agreement> agreements = agreementRepository.findAll();
        assertThat(agreements).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAgreements() throws Exception {
        // Initialize the database
        agreementRepository.saveAndFlush(agreement);

        // Get all the agreements
        restAgreementMockMvc.perform(get("/api/agreements?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(agreement.getId().intValue())))
                .andExpect(jsonPath("$.[*].agreementNumber").value(hasItem(DEFAULT_AGREEMENT_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].lob").value(hasItem(DEFAULT_LOB.toString())))
                .andExpect(jsonPath("$.[*].premium").value(hasItem(DEFAULT_PREMIUM.doubleValue())));
    }

    @Test
    @Transactional
    public void getAgreement() throws Exception {
        // Initialize the database
        agreementRepository.saveAndFlush(agreement);

        // Get the agreement
        restAgreementMockMvc.perform(get("/api/agreements/{id}", agreement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(agreement.getId().intValue()))
            .andExpect(jsonPath("$.agreementNumber").value(DEFAULT_AGREEMENT_NUMBER.toString()))
            .andExpect(jsonPath("$.lob").value(DEFAULT_LOB.toString()))
            .andExpect(jsonPath("$.premium").value(DEFAULT_PREMIUM.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAgreement() throws Exception {
        // Get the agreement
        restAgreementMockMvc.perform(get("/api/agreements/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAgreement() throws Exception {
        // Initialize the database
        agreementRepository.saveAndFlush(agreement);
        int databaseSizeBeforeUpdate = agreementRepository.findAll().size();

        // Update the agreement
        Agreement updatedAgreement = agreementRepository.findOne(agreement.getId());
        updatedAgreement
                .agreementNumber(UPDATED_AGREEMENT_NUMBER)
                .lob(UPDATED_LOB)
                .premium(UPDATED_PREMIUM);

        restAgreementMockMvc.perform(put("/api/agreements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAgreement)))
                .andExpect(status().isOk());

        // Validate the Agreement in the database
        List<Agreement> agreements = agreementRepository.findAll();
        assertThat(agreements).hasSize(databaseSizeBeforeUpdate);
        Agreement testAgreement = agreements.get(agreements.size() - 1);
        assertThat(testAgreement.getAgreementNumber()).isEqualTo(UPDATED_AGREEMENT_NUMBER);
        assertThat(testAgreement.getLob()).isEqualTo(UPDATED_LOB);
        assertThat(testAgreement.getPremium()).isEqualTo(UPDATED_PREMIUM);
    }

    @Test
    @Transactional
    public void deleteAgreement() throws Exception {
        // Initialize the database
        agreementRepository.saveAndFlush(agreement);
        int databaseSizeBeforeDelete = agreementRepository.findAll().size();

        // Get the agreement
        restAgreementMockMvc.perform(delete("/api/agreements/{id}", agreement.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Agreement> agreements = agreementRepository.findAll();
        assertThat(agreements).hasSize(databaseSizeBeforeDelete - 1);
    }
}
