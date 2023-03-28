package ar.com.grooming.sexualgroomingreports.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ReportLog.
 */
@Entity
@Table(name = "report_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ReportLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "change")
    private String change;

    @ManyToOne
    @JsonIgnoreProperties(value = { "reportComments", "reportLogs", "victim", "owner" }, allowSetters = true)
    private Report reportLogs;

    @ManyToOne
    private User updatedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReportLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public ReportLog date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getChange() {
        return this.change;
    }

    public ReportLog change(String change) {
        this.setChange(change);
        return this;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public Report getReportLogs() {
        return this.reportLogs;
    }

    public void setReportLogs(Report report) {
        this.reportLogs = report;
    }

    public ReportLog reportLogs(Report report) {
        this.setReportLogs(report);
        return this;
    }

    public User getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(User user) {
        this.updatedBy = user;
    }

    public ReportLog updatedBy(User user) {
        this.setUpdatedBy(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportLog)) {
            return false;
        }
        return id != null && id.equals(((ReportLog) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportLog{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", change='" + getChange() + "'" +
            "}";
    }
}
