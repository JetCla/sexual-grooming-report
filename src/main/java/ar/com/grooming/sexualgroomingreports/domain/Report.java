package ar.com.grooming.sexualgroomingreports.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Report.
 */
@Entity
@Table(name = "report")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "number", nullable = false)
    private String number;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "office_assigned")
    private String officeAssigned;

    @Column(name = "description")
    private String description;

    @Column(name = "location")
    private String location;

    @OneToMany(mappedBy = "reportComments")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "reportComments", "commentedBy" }, allowSetters = true)
    private Set<ReportComments> reportComments = new HashSet<>();

    @OneToMany(mappedBy = "reportLogs")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "reportLogs", "updatedBy" }, allowSetters = true)
    private Set<ReportLog> reportLogs = new HashSet<>();

    @ManyToOne
    private Victim victim;

    @ManyToOne
    private User owner;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Report id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return this.number;
    }

    public Report number(String number) {
        this.setNumber(number);
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Report date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getOfficeAssigned() {
        return this.officeAssigned;
    }

    public Report officeAssigned(String officeAssigned) {
        this.setOfficeAssigned(officeAssigned);
        return this;
    }

    public void setOfficeAssigned(String officeAssigned) {
        this.officeAssigned = officeAssigned;
    }

    public String getDescription() {
        return this.description;
    }

    public Report description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return this.location;
    }

    public Report location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Set<ReportComments> getReportComments() {
        return this.reportComments;
    }

    public void setReportComments(Set<ReportComments> reportComments) {
        if (this.reportComments != null) {
            this.reportComments.forEach(i -> i.setReportComments(null));
        }
        if (reportComments != null) {
            reportComments.forEach(i -> i.setReportComments(this));
        }
        this.reportComments = reportComments;
    }

    public Report reportComments(Set<ReportComments> reportComments) {
        this.setReportComments(reportComments);
        return this;
    }

    public Report addReportComments(ReportComments reportComments) {
        this.reportComments.add(reportComments);
        reportComments.setReportComments(this);
        return this;
    }

    public Report removeReportComments(ReportComments reportComments) {
        this.reportComments.remove(reportComments);
        reportComments.setReportComments(null);
        return this;
    }

    public Set<ReportLog> getReportLogs() {
        return this.reportLogs;
    }

    public void setReportLogs(Set<ReportLog> reportLogs) {
        if (this.reportLogs != null) {
            this.reportLogs.forEach(i -> i.setReportLogs(null));
        }
        if (reportLogs != null) {
            reportLogs.forEach(i -> i.setReportLogs(this));
        }
        this.reportLogs = reportLogs;
    }

    public Report reportLogs(Set<ReportLog> reportLogs) {
        this.setReportLogs(reportLogs);
        return this;
    }

    public Report addReportLog(ReportLog reportLog) {
        this.reportLogs.add(reportLog);
        reportLog.setReportLogs(this);
        return this;
    }

    public Report removeReportLog(ReportLog reportLog) {
        this.reportLogs.remove(reportLog);
        reportLog.setReportLogs(null);
        return this;
    }

    public Victim getVictim() {
        return this.victim;
    }

    public void setVictim(Victim victim) {
        this.victim = victim;
    }

    public Report victim(Victim victim) {
        this.setVictim(victim);
        return this;
    }

    public User getOwner() {
        return this.owner;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public Report owner(User user) {
        this.setOwner(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Report)) {
            return false;
        }
        return id != null && id.equals(((Report) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Report{" +
            "id=" + getId() +
            ", number='" + getNumber() + "'" +
            ", date='" + getDate() + "'" +
            ", officeAssigned='" + getOfficeAssigned() + "'" +
            ", description='" + getDescription() + "'" +
            ", location='" + getLocation() + "'" +
            "}";
    }
}
