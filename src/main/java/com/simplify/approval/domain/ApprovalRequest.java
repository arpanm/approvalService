package com.simplify.approval.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.simplify.approval.domain.enumeration.ApprovalType;
import com.simplify.approval.domain.enumeration.Status;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ApprovalRequest.
 */
@Entity
@Table(name = "approval_request")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ApprovalRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "program_id", nullable = false)
    private String programId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ApprovalType type;

    @Column(name = "approve_call_back_url")
    private String approveCallBackUrl;

    @Column(name = "reject_call_back_url")
    private String rejectCallBackUrl;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "final_status")
    private Status finalStatus;

    @OneToMany(mappedBy = "request")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "request" }, allowSetters = true)
    private Set<ApprovalRequestItem> approvalRequestItems = new HashSet<>();

    @OneToMany(mappedBy = "request")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "request", "approver" }, allowSetters = true)
    private Set<IndividualApprovalStatus> individualApprovalStatuses = new HashSet<>();

    @OneToMany(mappedBy = "request")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "request", "approver" }, allowSetters = true)
    private Set<ApprovalLevelStatus> approvalLevelStatuses = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "subRules", "approvers", "approvalRequests" }, allowSetters = true)
    private ApprovalRule rule;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApprovalRequest id(Long id) {
        this.id = id;
        return this;
    }

    public String getProgramId() {
        return this.programId;
    }

    public ApprovalRequest programId(String programId) {
        this.programId = programId;
        return this;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public ApprovalType getType() {
        return this.type;
    }

    public ApprovalRequest type(ApprovalType type) {
        this.type = type;
        return this;
    }

    public void setType(ApprovalType type) {
        this.type = type;
    }

    public String getApproveCallBackUrl() {
        return this.approveCallBackUrl;
    }

    public ApprovalRequest approveCallBackUrl(String approveCallBackUrl) {
        this.approveCallBackUrl = approveCallBackUrl;
        return this;
    }

    public void setApproveCallBackUrl(String approveCallBackUrl) {
        this.approveCallBackUrl = approveCallBackUrl;
    }

    public String getRejectCallBackUrl() {
        return this.rejectCallBackUrl;
    }

    public ApprovalRequest rejectCallBackUrl(String rejectCallBackUrl) {
        this.rejectCallBackUrl = rejectCallBackUrl;
        return this;
    }

    public void setRejectCallBackUrl(String rejectCallBackUrl) {
        this.rejectCallBackUrl = rejectCallBackUrl;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public ApprovalRequest createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public ApprovalRequest createdAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public ApprovalRequest updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDate getUpdatedAt() {
        return this.updatedAt;
    }

    public ApprovalRequest updatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Status getFinalStatus() {
        return this.finalStatus;
    }

    public ApprovalRequest finalStatus(Status finalStatus) {
        this.finalStatus = finalStatus;
        return this;
    }

    public void setFinalStatus(Status finalStatus) {
        this.finalStatus = finalStatus;
    }

    public Set<ApprovalRequestItem> getApprovalRequestItems() {
        return this.approvalRequestItems;
    }

    public ApprovalRequest approvalRequestItems(Set<ApprovalRequestItem> approvalRequestItems) {
        this.setApprovalRequestItems(approvalRequestItems);
        return this;
    }

    public ApprovalRequest addApprovalRequestItem(ApprovalRequestItem approvalRequestItem) {
        this.approvalRequestItems.add(approvalRequestItem);
        approvalRequestItem.setRequest(this);
        return this;
    }

    public ApprovalRequest removeApprovalRequestItem(ApprovalRequestItem approvalRequestItem) {
        this.approvalRequestItems.remove(approvalRequestItem);
        approvalRequestItem.setRequest(null);
        return this;
    }

    public void setApprovalRequestItems(Set<ApprovalRequestItem> approvalRequestItems) {
        if (this.approvalRequestItems != null) {
            this.approvalRequestItems.forEach(i -> i.setRequest(null));
        }
        if (approvalRequestItems != null) {
            approvalRequestItems.forEach(i -> i.setRequest(this));
        }
        this.approvalRequestItems = approvalRequestItems;
    }

    public Set<IndividualApprovalStatus> getIndividualApprovalStatuses() {
        return this.individualApprovalStatuses;
    }

    public ApprovalRequest individualApprovalStatuses(Set<IndividualApprovalStatus> individualApprovalStatuses) {
        this.setIndividualApprovalStatuses(individualApprovalStatuses);
        return this;
    }

    public ApprovalRequest addIndividualApprovalStatus(IndividualApprovalStatus individualApprovalStatus) {
        this.individualApprovalStatuses.add(individualApprovalStatus);
        individualApprovalStatus.setRequest(this);
        return this;
    }

    public ApprovalRequest removeIndividualApprovalStatus(IndividualApprovalStatus individualApprovalStatus) {
        this.individualApprovalStatuses.remove(individualApprovalStatus);
        individualApprovalStatus.setRequest(null);
        return this;
    }

    public void setIndividualApprovalStatuses(Set<IndividualApprovalStatus> individualApprovalStatuses) {
        if (this.individualApprovalStatuses != null) {
            this.individualApprovalStatuses.forEach(i -> i.setRequest(null));
        }
        if (individualApprovalStatuses != null) {
            individualApprovalStatuses.forEach(i -> i.setRequest(this));
        }
        this.individualApprovalStatuses = individualApprovalStatuses;
    }

    public Set<ApprovalLevelStatus> getApprovalLevelStatuses() {
        return this.approvalLevelStatuses;
    }

    public ApprovalRequest approvalLevelStatuses(Set<ApprovalLevelStatus> approvalLevelStatuses) {
        this.setApprovalLevelStatuses(approvalLevelStatuses);
        return this;
    }

    public ApprovalRequest addApprovalLevelStatus(ApprovalLevelStatus approvalLevelStatus) {
        this.approvalLevelStatuses.add(approvalLevelStatus);
        approvalLevelStatus.setRequest(this);
        return this;
    }

    public ApprovalRequest removeApprovalLevelStatus(ApprovalLevelStatus approvalLevelStatus) {
        this.approvalLevelStatuses.remove(approvalLevelStatus);
        approvalLevelStatus.setRequest(null);
        return this;
    }

    public void setApprovalLevelStatuses(Set<ApprovalLevelStatus> approvalLevelStatuses) {
        if (this.approvalLevelStatuses != null) {
            this.approvalLevelStatuses.forEach(i -> i.setRequest(null));
        }
        if (approvalLevelStatuses != null) {
            approvalLevelStatuses.forEach(i -> i.setRequest(this));
        }
        this.approvalLevelStatuses = approvalLevelStatuses;
    }

    public ApprovalRule getRule() {
        return this.rule;
    }

    public ApprovalRequest rule(ApprovalRule approvalRule) {
        this.setRule(approvalRule);
        return this;
    }

    public void setRule(ApprovalRule approvalRule) {
        this.rule = approvalRule;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApprovalRequest)) {
            return false;
        }
        return id != null && id.equals(((ApprovalRequest) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApprovalRequest{" +
            "id=" + getId() +
            ", programId='" + getProgramId() + "'" +
            ", type='" + getType() + "'" +
            ", approveCallBackUrl='" + getApproveCallBackUrl() + "'" +
            ", rejectCallBackUrl='" + getRejectCallBackUrl() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", finalStatus='" + getFinalStatus() + "'" +
            "}";
    }
}
