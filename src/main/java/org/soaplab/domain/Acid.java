package org.soaplab.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Acid.
 */
@Entity
@Table(name = "acid")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Acid implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "inci")
    private String inci;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Acid id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Acid name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInci() {
        return this.inci;
    }

    public Acid inci(String inci) {
        this.inci = inci;
        return this;
    }

    public void setInci(String inci) {
        this.inci = inci;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Acid)) {
            return false;
        }
        return id != null && id.equals(((Acid) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Acid{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", inci='" + getInci() + "'" +
            "}";
    }
}
