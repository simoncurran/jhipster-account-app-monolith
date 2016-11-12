package com.simon.account.web.rest;

import com.simon.account.AccountsApp;

import com.simon.account.domain.Broker;
import com.simon.account.repository.BrokerRepository;

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
 * Test class for the BrokerResource REST controller.
 *
 * @see BrokerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountsApp.class)
public class BrokerResourceIntTest {

    private static final String DEFAULT_BROKER_NUMBER = "AAAAA";
    private static final String UPDATED_BROKER_NUMBER = "BBBBB";

    private static final String DEFAULT_BROKER_NAME = "AAAAA";
    private static final String UPDATED_BROKER_NAME = "BBBBB";

    @Inject
    private BrokerRepository brokerRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restBrokerMockMvc;

    private Broker broker;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BrokerResource brokerResource = new BrokerResource();
        ReflectionTestUtils.setField(brokerResource, "brokerRepository", brokerRepository);
        this.restBrokerMockMvc = MockMvcBuilders.standaloneSetup(brokerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Broker createEntity(EntityManager em) {
        Broker broker = new Broker()
                .brokerNumber(DEFAULT_BROKER_NUMBER)
                .brokerName(DEFAULT_BROKER_NAME);
        return broker;
    }

    @Before
    public void initTest() {
        broker = createEntity(em);
    }

    @Test
    @Transactional
    public void createBroker() throws Exception {
        int databaseSizeBeforeCreate = brokerRepository.findAll().size();

        // Create the Broker

        restBrokerMockMvc.perform(post("/api/brokers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(broker)))
                .andExpect(status().isCreated());

        // Validate the Broker in the database
        List<Broker> brokers = brokerRepository.findAll();
        assertThat(brokers).hasSize(databaseSizeBeforeCreate + 1);
        Broker testBroker = brokers.get(brokers.size() - 1);
        assertThat(testBroker.getBrokerNumber()).isEqualTo(DEFAULT_BROKER_NUMBER);
        assertThat(testBroker.getBrokerName()).isEqualTo(DEFAULT_BROKER_NAME);
    }

    @Test
    @Transactional
    public void checkBrokerNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = brokerRepository.findAll().size();
        // set the field null
        broker.setBrokerNumber(null);

        // Create the Broker, which fails.

        restBrokerMockMvc.perform(post("/api/brokers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(broker)))
                .andExpect(status().isBadRequest());

        List<Broker> brokers = brokerRepository.findAll();
        assertThat(brokers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBrokerNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = brokerRepository.findAll().size();
        // set the field null
        broker.setBrokerName(null);

        // Create the Broker, which fails.

        restBrokerMockMvc.perform(post("/api/brokers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(broker)))
                .andExpect(status().isBadRequest());

        List<Broker> brokers = brokerRepository.findAll();
        assertThat(brokers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBrokers() throws Exception {
        // Initialize the database
        brokerRepository.saveAndFlush(broker);

        // Get all the brokers
        restBrokerMockMvc.perform(get("/api/brokers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(broker.getId().intValue())))
                .andExpect(jsonPath("$.[*].brokerNumber").value(hasItem(DEFAULT_BROKER_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].brokerName").value(hasItem(DEFAULT_BROKER_NAME.toString())));
    }

    @Test
    @Transactional
    public void getBroker() throws Exception {
        // Initialize the database
        brokerRepository.saveAndFlush(broker);

        // Get the broker
        restBrokerMockMvc.perform(get("/api/brokers/{id}", broker.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(broker.getId().intValue()))
            .andExpect(jsonPath("$.brokerNumber").value(DEFAULT_BROKER_NUMBER.toString()))
            .andExpect(jsonPath("$.brokerName").value(DEFAULT_BROKER_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBroker() throws Exception {
        // Get the broker
        restBrokerMockMvc.perform(get("/api/brokers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBroker() throws Exception {
        // Initialize the database
        brokerRepository.saveAndFlush(broker);
        int databaseSizeBeforeUpdate = brokerRepository.findAll().size();

        // Update the broker
        Broker updatedBroker = brokerRepository.findOne(broker.getId());
        updatedBroker
                .brokerNumber(UPDATED_BROKER_NUMBER)
                .brokerName(UPDATED_BROKER_NAME);

        restBrokerMockMvc.perform(put("/api/brokers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBroker)))
                .andExpect(status().isOk());

        // Validate the Broker in the database
        List<Broker> brokers = brokerRepository.findAll();
        assertThat(brokers).hasSize(databaseSizeBeforeUpdate);
        Broker testBroker = brokers.get(brokers.size() - 1);
        assertThat(testBroker.getBrokerNumber()).isEqualTo(UPDATED_BROKER_NUMBER);
        assertThat(testBroker.getBrokerName()).isEqualTo(UPDATED_BROKER_NAME);
    }

    @Test
    @Transactional
    public void deleteBroker() throws Exception {
        // Initialize the database
        brokerRepository.saveAndFlush(broker);
        int databaseSizeBeforeDelete = brokerRepository.findAll().size();

        // Get the broker
        restBrokerMockMvc.perform(delete("/api/brokers/{id}", broker.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Broker> brokers = brokerRepository.findAll();
        assertThat(brokers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
