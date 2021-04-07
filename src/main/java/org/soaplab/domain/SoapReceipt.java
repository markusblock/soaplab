package org.soaplab.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SoapReceipt.
 */
@Entity
@Table(name = "soap_receipt")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SoapReceipt implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "liquid")
    private Integer liquid;

    @Column(name = "superfat")
    private Integer superfat;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_soap_receipt__fats",
        joinColumns = @JoinColumn(name = "soap_receipt_id"),
        inverseJoinColumns = @JoinColumn(name = "fats_id")
    )
    @JsonIgnoreProperties(value = { "receipts" }, allowSetters = true)
    private Set<Fat> fats = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SoapReceipt id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public SoapReceipt name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLiquid() {
        return this.liquid;
    }

    public SoapReceipt liquid(Integer liquid) {
        this.liquid = liquid;
        return this;
    }

    public void setLiquid(Integer liquid) {
        this.liquid = liquid;
    }

    public Integer getSuperfat() {
        return this.superfat;
    }

    public SoapReceipt superfat(Integer superfat) {
        this.superfat = superfat;
        return this;
    }

    public void setSuperfat(Integer superfat) {
        this.superfat = superfat;
    }

    public Set<Fat> getFats() {
        return this.fats;
    }

    public SoapReceipt fats(Set<Fat> fats) {
        this.setFats(fats);
        return this;
    }

    public SoapReceipt addFats(Fat fat) {
        this.fats.add(fat);
        fat.getReceipts().add(this);
        return this;
    }

    public SoapReceipt removeFats(Fat fat) {
        this.fats.remove(fat);
        fat.getReceipts().remove(this);
        return this;
    }

    public void setFats(Set<Fat> fats) {
        this.fats = fats;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SoapReceipt)) {
            return false;
        }
        return id != null && id.equals(((SoapReceipt) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SoapReceipt{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", liquid=" + getLiquid() +
            ", superfat=" + getSuperfat() +
            "}";
    }
}
