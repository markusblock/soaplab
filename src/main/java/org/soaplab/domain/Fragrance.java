package org.soaplab.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.soaplab.domain.enumeration.FragranceType;

/**
 * A Fragrance.
 */
@Entity
@Table(name = "fragrance")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Fragrance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "inci")
    private String inci;

    @Enumerated(EnumType.STRING)
    @Column(name = "typ")
    private FragranceType typ;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Fragrance id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Fragrance name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInci() {
        return this.inci;
    }

    public Fragrance inci(String inci) {
        this.inci = inci;
        return this;
    }

    public void setInci(String inci) {
        this.inci = inci;
    }

    public FragranceType getTyp() {
        return this.typ;
    }

    public Fragrance typ(FragranceType typ) {
        this.typ = typ;
        return this;
    }

    public void setTyp(FragranceType typ) {
        this.typ = typ;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fragrance)) {
            return false;
        }
        return id != null && id.equals(((Fragrance) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Fragrance{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", inci='" + getInci() + "'" +
            ", typ='" + getTyp() + "'" +
            "}";
    }
}
