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
 * A ApprovalLevelStatus.
 */
@Entity
@Table(name = "approval_level_status")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ApprovalLevelStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @NotNull
    @Column(name = "level", nullable = false)
    private Integer level;

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

    public ApprovalLevelStatus id(Long id) {
        this.id = id;
        return this;
    }

    public Status getStatus() {
        return this.status;
    }

    public ApprovalLevelStatus status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getLevel() {
        return this.level;
    }

    public ApprovalLevelStatus level(Integer level) {
        this.level = level;
        return this;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getClientTime() {
        return this.clientTime;
    }

    public ApprovalLevelStatus clientTime(String clientTime) {
        this.clientTime = clientTime;
        return this;
    }

    public void setClientTime(String clientTime) {
        this.clientTime = clientTime;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public ApprovalLevelStatus createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public ApprovalLevelStatus createdAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public ApprovalLevelStatus updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDate getUpdatedAt() {
        return this.updatedAt;
    }

    public ApprovalLevelStatus updatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ApprovalRequest getRequest() {
        return this.request;
    }

    public ApprovalLevelStatus request(ApprovalRequest approvalRequest) {
        this.setRequest(approvalRequest);
        return this;
    }

    public void setRequest(ApprovalRequest approvalRequest) {
        this.request = approvalRequest;
    }

    public Approver getApprover() {
        return this.approver;
    }

    public ApprovalLevelStatus approver(Approver approver) {
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
        if (!(o instanceof ApprovalLevelStatus)) {
            return false;
        }
        return id != null && id.equals(((ApprovalLevelStatus) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApprovalLevelStatus{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", level=" + getLevel() +
            ", clientTime='" + getClientTime() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
