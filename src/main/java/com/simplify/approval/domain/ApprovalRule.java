package com.simplify.approval.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.simplify.approval.domain.enumeration.ApprovalType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ApprovalRule.
 */
@Entity
@Table(name = "approval_rule")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ApprovalRule implements Serializable {

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

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @OneToMany(mappedBy = "rule")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "subRuleInListItems", "rule" }, allowSetters = true)
    private Set<SubRule> subRules = new HashSet<>();

    @OneToMany(mappedBy = "rule")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "individualApprovalStatuses", "approvalLevelStatuses", "rule" }, allowSetters = true)
    private Set<Approver> approvers = new HashSet<>();

    @OneToMany(mappedBy = "rule")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "approvalRequestItems", "individualApprovalStatuses", "approvalLevelStatuses", "rule" },
        allowSetters = true
    )
    private Set<ApprovalRequest> approvalRequests = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApprovalRule id(Long id) {
        this.id = id;
        return this;
    }

    public String getProgramId() {
        return this.programId;
    }

    public ApprovalRule programId(String programId) {
        this.programId = programId;
        return this;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public ApprovalType getType() {
        return this.type;
    }

    public ApprovalRule type(ApprovalType type) {
        this.type = type;
        return this;
    }

    public void setType(ApprovalType type) {
        this.type = type;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public ApprovalRule createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public ApprovalRule createdAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public ApprovalRule updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDate getUpdatedAt() {
        return this.updatedAt;
    }

    public ApprovalRule updatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<SubRule> getSubRules() {
        return this.subRules;
    }

    public ApprovalRule subRules(Set<SubRule> subRules) {
        this.setSubRules(subRules);
        return this;
    }

    public ApprovalRule addSubRule(SubRule subRule) {
        this.subRules.add(subRule);
        subRule.setRule(this);
        return this;
    }

    public ApprovalRule removeSubRule(SubRule subRule) {
        this.subRules.remove(subRule);
        subRule.setRule(null);
        return this;
    }

    public void setSubRules(Set<SubRule> subRules) {
        if (this.subRules != null) {
            this.subRules.forEach(i -> i.setRule(null));
        }
        if (subRules != null) {
            subRules.forEach(i -> i.setRule(this));
        }
        this.subRules = subRules;
    }

    public Set<Approver> getApprovers() {
        return this.approvers;
    }

    public ApprovalRule approvers(Set<Approver> approvers) {
        this.setApprovers(approvers);
        return this;
    }

    public ApprovalRule addApprover(Approver approver) {
        this.approvers.add(approver);
        approver.setRule(this);
        return this;
    }

    public ApprovalRule removeApprover(Approver approver) {
        this.approvers.remove(approver);
        approver.setRule(null);
        return this;
    }

    public void setApprovers(Set<Approver> approvers) {
        if (this.approvers != null) {
            this.approvers.forEach(i -> i.setRule(null));
        }
        if (approvers != null) {
            approvers.forEach(i -> i.setRule(this));
        }
        this.approvers = approvers;
    }

    public Set<ApprovalRequest> getApprovalRequests() {
        return this.approvalRequests;
    }

    public ApprovalRule approvalRequests(Set<ApprovalRequest> approvalRequests) {
        this.setApprovalRequests(approvalRequests);
        return this;
    }

    public ApprovalRule addApprovalRequest(ApprovalRequest approvalRequest) {
        this.approvalRequests.add(approvalRequest);
        approvalRequest.setRule(this);
        return this;
    }

    public ApprovalRule removeApprovalRequest(ApprovalRequest approvalRequest) {
        this.approvalRequests.remove(approvalRequest);
        approvalRequest.setRule(null);
        return this;
    }

    public void setApprovalRequests(Set<ApprovalRequest> approvalRequests) {
        if (this.approvalRequests != null) {
            this.approvalRequests.forEach(i -> i.setRule(null));
        }
        if (approvalRequests != null) {
            approvalRequests.forEach(i -> i.setRule(this));
        }
        this.approvalRequests = approvalRequests;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApprovalRule)) {
            return false;
        }
        return id != null && id.equals(((ApprovalRule) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApprovalRule{" +
            "id=" + getId() +
            ", programId='" + getProgramId() + "'" +
            ", type='" + getType() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
