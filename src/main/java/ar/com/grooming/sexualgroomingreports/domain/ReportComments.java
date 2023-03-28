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
 * A ReportComments.
 */
@Entity
@Table(name = "report_comments")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ReportComments implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne
    @JsonIgnoreProperties(value = { "reportComments", "reportLogs", "victim", "owner" }, allowSetters = true)
    private Report reportComments;

    @ManyToOne
    private User commentedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReportComments id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public ReportComments date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Report getReportComments() {
        return this.reportComments;
    }

    public void setReportComments(Report report) {
        this.reportComments = report;
    }

    public ReportComments reportComments(Report report) {
        this.setReportComments(report);
        return this;
    }

    public User getCommentedBy() {
        return this.commentedBy;
    }

    public void setCommentedBy(User user) {
        this.commentedBy = user;
    }

    public ReportComments commentedBy(User user) {
        this.setCommentedBy(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportComments)) {
            return false;
        }
        return id != null && id.equals(((ReportComments) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportComments{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            "}";
    }
}
