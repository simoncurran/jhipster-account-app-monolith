package com.simon.account.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Agreement.
 */
@Entity
@Table(name = "agreement")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Agreement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "agreement_number", nullable = false)
    private String agreementNumber;

    @NotNull
    @Column(name = "lob", nullable = false)
    private String lob;

    @Column(name = "premium")
    private Float premium;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Broker broker;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgreementNumber() {
        return agreementNumber;
    }

    public Agreement agreementNumber(String agreementNumber) {
        this.agreementNumber = agreementNumber;
        return this;
    }

    public void setAgreementNumber(String agreementNumber) {
        this.agreementNumber = agreementNumber;
    }

    public String getLob() {
        return lob;
    }

    public Agreement lob(String lob) {
        this.lob = lob;
        return this;
    }

    public void setLob(String lob) {
        this.lob = lob;
    }

    public Float getPremium() {
        return premium;
    }

    public Agreement premium(Float premium) {
        this.premium = premium;
        return this;
    }

    public void setPremium(Float premium) {
        this.premium = premium;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Agreement customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Broker getBroker() {
        return broker;
    }

    public Agreement broker(Broker broker) {
        this.broker = broker;
        return this;
    }

    public void setBroker(Broker broker) {
        this.broker = broker;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Agreement agreement = (Agreement) o;
        if(agreement.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, agreement.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Agreement{" +
            "id=" + id +
            ", agreementNumber='" + agreementNumber + "'" +
            ", lob='" + lob + "'" +
            ", premium='" + premium + "'" +
            '}';
    }
}
