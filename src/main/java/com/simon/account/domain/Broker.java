package com.simon.account.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Broker.
 */
@Entity
@Table(name = "broker")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Broker implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "broker_number", nullable = false)
    private String brokerNumber;

    @NotNull
    @Column(name = "broker_name", nullable = false)
    private String brokerName;

    @OneToOne
    @JoinColumn(unique = true)
    private Address address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrokerNumber() {
        return brokerNumber;
    }

    public Broker brokerNumber(String brokerNumber) {
        this.brokerNumber = brokerNumber;
        return this;
    }

    public void setBrokerNumber(String brokerNumber) {
        this.brokerNumber = brokerNumber;
    }

    public String getBrokerName() {
        return brokerName;
    }

    public Broker brokerName(String brokerName) {
        this.brokerName = brokerName;
        return this;
    }

    public void setBrokerName(String brokerName) {
        this.brokerName = brokerName;
    }

    public Address getAddress() {
        return address;
    }

    public Broker address(Address address) {
        this.address = address;
        return this;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Broker broker = (Broker) o;
        if(broker.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, broker.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Broker{" +
            "id=" + id +
            ", brokerNumber='" + brokerNumber + "'" +
            ", brokerName='" + brokerName + "'" +
            '}';
    }
}
