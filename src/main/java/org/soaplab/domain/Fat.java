package org.soaplab.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Fat.
 */
@Entity
@Table(name = "fat")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Fat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "inci")
    private String inci;

    @Column(name = "sap_naoh")
    private Double sapNaoh;

    @Column(name = "sap_koh")
    private Double sapKoh;

    @Column(name = "lauric")
    private Integer lauric;

    @Column(name = "myristic")
    private Integer myristic;

    @Column(name = "palmitic")
    private Integer palmitic;

    @Column(name = "stearic")
    private Integer stearic;

    @Column(name = "ricinoleic")
    private Integer ricinoleic;

    @Column(name = "oleic")
    private Integer oleic;

    @Column(name = "linoleic")
    private Integer linoleic;

    @Column(name = "linolenic")
    private Integer linolenic;

    @Column(name = "iodine")
    private Integer iodine;

    @Column(name = "ins")
    private Integer ins;

    @ManyToMany(mappedBy = "fats")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "fats" }, allowSetters = true)
    private Set<SoapReceipt> receipts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Fat id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Fat name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInci() {
        return this.inci;
    }

    public Fat inci(String inci) {
        this.inci = inci;
        return this;
    }

    public void setInci(String inci) {
        this.inci = inci;
    }

    public Double getSapNaoh() {
        return this.sapNaoh;
    }

    public Fat sapNaoh(Double sapNaoh) {
        this.sapNaoh = sapNaoh;
        return this;
    }

    public void setSapNaoh(Double sapNaoh) {
        this.sapNaoh = sapNaoh;
    }

    public Double getSapKoh() {
        return this.sapKoh;
    }

    public Fat sapKoh(Double sapKoh) {
        this.sapKoh = sapKoh;
        return this;
    }

    public void setSapKoh(Double sapKoh) {
        this.sapKoh = sapKoh;
    }

    public Integer getLauric() {
        return this.lauric;
    }

    public Fat lauric(Integer lauric) {
        this.lauric = lauric;
        return this;
    }

    public void setLauric(Integer lauric) {
        this.lauric = lauric;
    }

    public Integer getMyristic() {
        return this.myristic;
    }

    public Fat myristic(Integer myristic) {
        this.myristic = myristic;
        return this;
    }

    public void setMyristic(Integer myristic) {
        this.myristic = myristic;
    }

    public Integer getPalmitic() {
        return this.palmitic;
    }

    public Fat palmitic(Integer palmitic) {
        this.palmitic = palmitic;
        return this;
    }

    public void setPalmitic(Integer palmitic) {
        this.palmitic = palmitic;
    }

    public Integer getStearic() {
        return this.stearic;
    }

    public Fat stearic(Integer stearic) {
        this.stearic = stearic;
        return this;
    }

    public void setStearic(Integer stearic) {
        this.stearic = stearic;
    }

    public Integer getRicinoleic() {
        return this.ricinoleic;
    }

    public Fat ricinoleic(Integer ricinoleic) {
        this.ricinoleic = ricinoleic;
        return this;
    }

    public void setRicinoleic(Integer ricinoleic) {
        this.ricinoleic = ricinoleic;
    }

    public Integer getOleic() {
        return this.oleic;
    }

    public Fat oleic(Integer oleic) {
        this.oleic = oleic;
        return this;
    }

    public void setOleic(Integer oleic) {
        this.oleic = oleic;
    }

    public Integer getLinoleic() {
        return this.linoleic;
    }

    public Fat linoleic(Integer linoleic) {
        this.linoleic = linoleic;
        return this;
    }

    public void setLinoleic(Integer linoleic) {
        this.linoleic = linoleic;
    }

    public Integer getLinolenic() {
        return this.linolenic;
    }

    public Fat linolenic(Integer linolenic) {
        this.linolenic = linolenic;
        return this;
    }

    public void setLinolenic(Integer linolenic) {
        this.linolenic = linolenic;
    }

    public Integer getIodine() {
        return this.iodine;
    }

    public Fat iodine(Integer iodine) {
        this.iodine = iodine;
        return this;
    }

    public void setIodine(Integer iodine) {
        this.iodine = iodine;
    }

    public Integer getIns() {
        return this.ins;
    }

    public Fat ins(Integer ins) {
        this.ins = ins;
        return this;
    }

    public void setIns(Integer ins) {
        this.ins = ins;
    }

    public Set<SoapReceipt> getReceipts() {
        return this.receipts;
    }

    public Fat receipts(Set<SoapReceipt> soapReceipts) {
        this.setReceipts(soapReceipts);
        return this;
    }

    public Fat addReceipt(SoapReceipt soapReceipt) {
        this.receipts.add(soapReceipt);
        soapReceipt.getFats().add(this);
        return this;
    }

    public Fat removeReceipt(SoapReceipt soapReceipt) {
        this.receipts.remove(soapReceipt);
        soapReceipt.getFats().remove(this);
        return this;
    }

    public void setReceipts(Set<SoapReceipt> soapReceipts) {
        if (this.receipts != null) {
            this.receipts.forEach(i -> i.removeFats(this));
        }
        if (soapReceipts != null) {
            soapReceipts.forEach(i -> i.addFats(this));
        }
        this.receipts = soapReceipts;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fat)) {
            return false;
        }
        return id != null && id.equals(((Fat) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Fat{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", inci='" + getInci() + "'" +
            ", sapNaoh=" + getSapNaoh() +
            ", sapKoh=" + getSapKoh() +
            ", lauric=" + getLauric() +
            ", myristic=" + getMyristic() +
            ", palmitic=" + getPalmitic() +
            ", stearic=" + getStearic() +
            ", ricinoleic=" + getRicinoleic() +
            ", oleic=" + getOleic() +
            ", linoleic=" + getLinoleic() +
            ", linolenic=" + getLinolenic() +
            ", iodine=" + getIodine() +
            ", ins=" + getIns() +
            "}";
    }
}
