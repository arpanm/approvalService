package com.simplify.approval.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.simplify.approval.domain.enumeration.Status;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A IndividualApprovalStatus.
 */
@Entity
@Table(name = "individual_approval_status")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class IndividualApprovalStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @NotNull
    @Column(name = "client_time", nullable = false)
    private String clientTime;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "approvalRequestItems", "individualApprovalStatuses", "approvalLevelStatuses", "rule" },
        allowSetters = true
    )
    private ApprovalRequest request;

    @ManyToOne
    @JsonIgnoreProperties(value = { "individualApprovalStatuses", "approvalLevelStatuses", "rule" }, allowSetters = true)
    private Approver approver;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IndividualApprovalStatus id(Long id) {
        this.id = id;
        return this;
    }

    public Status getStatus() {
        return this.status;
    }

    public IndividualApprovalStatus status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getClientTime() {
        return this.clientTime;
    }

    public IndividualApprovalStatus clientTime(String clientTime) {
        this.clientTime = clientTime;
        return this;
    }

    public void setClientTime(String clientTime) {
        this.clientTime = clientTime;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public IndividualApprovalStatus createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public IndividualApprovalStatus createdAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public IndividualApprovalStatus updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDate getUpdatedAt() {
        return this.updatedAt;
    }

    public IndividualApprovalStatus updatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ApprovalRequest getRequest() {
        return this.request;
    }

    public IndividualApprovalStatus request(ApprovalRequest approvalRequest) {
        this.setRequest(approvalRequest);
        return this;
    }

    public void setRequest(ApprovalRequest approvalRequest) {
        this.request = approvalRequest;
    }

    public Approver getApprover() {
        return this.approver;
    }

    public IndividualApprovalStatus approver(Approver approver) {
        this.setApprover(approver);
        return this;
    }

    public void setApprover(Approver approver) {
        this.approver = approver;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IndividualApprovalStatus)) {
            return false;
        }
        return id != null && id.equals(((IndividualApprovalStatus) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IndividualApprovalStatus{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", clientTime='" + getClientTime() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
