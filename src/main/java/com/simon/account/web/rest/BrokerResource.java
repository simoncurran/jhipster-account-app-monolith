package com.simon.account.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.simon.account.domain.Broker;

import com.simon.account.repository.BrokerRepository;
import com.simon.account.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Broker.
 */
@RestController
@RequestMapping("/api")
public class BrokerResource {

    private final Logger log = LoggerFactory.getLogger(BrokerResource.class);
        
    @Inject
    private BrokerRepository brokerRepository;

    /**
     * POST  /brokers : Create a new broker.
     *
     * @param broker the broker to create
     * @return the ResponseEntity with status 201 (Created) and with body the new broker, or with status 400 (Bad Request) if the broker has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/brokers")
    @Timed
    public ResponseEntity<Broker> createBroker(@Valid @RequestBody Broker broker) throws URISyntaxException {
        log.debug("REST request to save Broker : {}", broker);
        if (broker.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("broker", "idexists", "A new broker cannot already have an ID")).body(null);
        }
        Broker result = brokerRepository.save(broker);
        return ResponseEntity.created(new URI("/api/brokers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("broker", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /brokers : Updates an existing broker.
     *
     * @param broker the broker to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated broker,
     * or with status 400 (Bad Request) if the broker is not valid,
     * or with status 500 (Internal Server Error) if the broker couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/brokers")
    @Timed
    public ResponseEntity<Broker> updateBroker(@Valid @RequestBody Broker broker) throws URISyntaxException {
        log.debug("REST request to update Broker : {}", broker);
        if (broker.getId() == null) {
            return createBroker(broker);
        }
        Broker result = brokerRepository.save(broker);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("broker", broker.getId().toString()))
            .body(result);
    }

    /**
     * GET  /brokers : get all the brokers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of brokers in body
     */
    @GetMapping("/brokers")
    @Timed
    public List<Broker> getAllBrokers() {
        log.debug("REST request to get all Brokers");
        List<Broker> brokers = brokerRepository.findAll();
        return brokers;
    }

    /**
     * GET  /brokers/:id : get the "id" broker.
     *
     * @param id the id of the broker to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the broker, or with status 404 (Not Found)
     */
    @GetMapping("/brokers/{id}")
    @Timed
    public ResponseEntity<Broker> getBroker(@PathVariable Long id) {
        log.debug("REST request to get Broker : {}", id);
        Broker broker = brokerRepository.findOne(id);
        return Optional.ofNullable(broker)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /brokers/:id : delete the "id" broker.
     *
     * @param id the id of the broker to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/brokers/{id}")
    @Timed
    public ResponseEntity<Void> deleteBroker(@PathVariable Long id) {
        log.debug("REST request to delete Broker : {}", id);
        brokerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("broker", id.toString())).build();
    }

}
