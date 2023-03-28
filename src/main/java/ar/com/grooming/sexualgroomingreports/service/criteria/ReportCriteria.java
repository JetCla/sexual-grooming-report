package ar.com.grooming.sexualgroomingreports.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link ar.com.grooming.sexualgroomingreports.domain.Report} entity. This class is used
 * in {@link ar.com.grooming.sexualgroomingreports.web.rest.ReportResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /reports?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class ReportCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter number;

    private LocalDateFilter date;

    private StringFilter officeAssigned;

    private StringFilter description;

    private StringFilter location;

    private LongFilter reportCommentsId;

    private LongFilter reportLogId;

    private LongFilter victimId;

    private StringFilter ownerId;

    private Boolean distinct;

    public ReportCriteria() {}

    public ReportCriteria(ReportCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.number = other.number == null ? null : other.number.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.officeAssigned = other.officeAssigned == null ? null : other.officeAssigned.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.location = other.location == null ? null : other.location.copy();
        this.reportCommentsId = other.reportCommentsId == null ? null : other.reportCommentsId.copy();
        this.reportLogId = other.reportLogId == null ? null : other.reportLogId.copy();
        this.victimId = other.victimId == null ? null : other.victimId.copy();
        this.ownerId = other.ownerId == null ? null : other.ownerId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ReportCriteria copy() {
        return new ReportCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNumber() {
        return number;
    }

    public StringFilter number() {
        if (number == null) {
            number = new StringFilter();
        }
        return number;
    }

    public void setNumber(StringFilter number) {
        this.number = number;
    }

    public LocalDateFilter getDate() {
        return date;
    }

    public LocalDateFilter date() {
        if (date == null) {
            date = new LocalDateFilter();
        }
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
    }

    public StringFilter getOfficeAssigned() {
        return officeAssigned;
    }

    public StringFilter officeAssigned() {
        if (officeAssigned == null) {
            officeAssigned = new StringFilter();
        }
        return officeAssigned;
    }

    public void setOfficeAssigned(StringFilter officeAssigned) {
        this.officeAssigned = officeAssigned;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getLocation() {
        return location;
    }

    public StringFilter location() {
        if (location == null) {
            location = new StringFilter();
        }
        return location;
    }

    public void setLocation(StringFilter location) {
        this.location = location;
    }

    public LongFilter getReportCommentsId() {
        return reportCommentsId;
    }

    public LongFilter reportCommentsId() {
        if (reportCommentsId == null) {
            reportCommentsId = new LongFilter();
        }
        return reportCommentsId;
    }

    public void setReportCommentsId(LongFilter reportCommentsId) {
        this.reportCommentsId = reportCommentsId;
    }

    public LongFilter getReportLogId() {
        return reportLogId;
    }

    public LongFilter reportLogId() {
        if (reportLogId == null) {
            reportLogId = new LongFilter();
        }
        return reportLogId;
    }

    public void setReportLogId(LongFilter reportLogId) {
        this.reportLogId = reportLogId;
    }

    public LongFilter getVictimId() {
        return victimId;
    }

    public LongFilter victimId() {
        if (victimId == null) {
            victimId = new LongFilter();
        }
        return victimId;
    }

    public void setVictimId(LongFilter victimId) {
        this.victimId = victimId;
    }

    public StringFilter getOwnerId() {
        return ownerId;
    }

    public StringFilter ownerId() {
        if (ownerId == null) {
            ownerId = new StringFilter();
        }
        return ownerId;
    }

    public void setOwnerId(StringFilter ownerId) {
        this.ownerId = ownerId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ReportCriteria that = (ReportCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(number, that.number) &&
            Objects.equals(date, that.date) &&
            Objects.equals(officeAssigned, that.officeAssigned) &&
            Objects.equals(description, that.description) &&
            Objects.equals(location, that.location) &&
            Objects.equals(reportCommentsId, that.reportCommentsId) &&
            Objects.equals(reportLogId, that.reportLogId) &&
            Objects.equals(victimId, that.victimId) &&
            Objects.equals(ownerId, that.ownerId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            number,
            date,
            officeAssigned,
            description,
            location,
            reportCommentsId,
            reportLogId,
            victimId,
            ownerId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (number != null ? "number=" + number + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (officeAssigned != null ? "officeAssigned=" + officeAssigned + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (location != null ? "location=" + location + ", " : "") +
            (reportCommentsId != null ? "reportCommentsId=" + reportCommentsId + ", " : "") +
            (reportLogId != null ? "reportLogId=" + reportLogId + ", " : "") +
            (victimId != null ? "victimId=" + victimId + ", " : "") +
            (ownerId != null ? "ownerId=" + ownerId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
