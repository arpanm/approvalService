package com.simplify.approval.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Approver.
 */
@Entity
@Table(name = "approver")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Approver implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "program_user_id", nullable = false)
    private String programUserId;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "level", nullable = false)
    private Integer level;

    @OneToMany(mappedBy = "approver")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "request", "approver" }, allowSetters = true)
    private Set<IndividualApprovalStatus> individualApprovalStatuses = new HashSet<>();

    @OneToMany(mappedBy = "approver")
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

    public Approver id(Long id) {
        this.id = id;
        return this;
    }

    public String getProgramUserId() {
        return this.programUserId;
    }

    public Approver programUserId(String programUserId) {
        this.programUserId = programUserId;
        return this;
    }

    public void setProgramUserId(String programUserId) {
        this.programUserId = programUserId;
    }

    public String getEmail() {
        return this.email;
    }

    public Approver email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getLevel() {
        return this.level;
    }

    public Approver level(Integer level) {
        this.level = level;
        return this;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Set<IndividualApprovalStatus> getIndividualApprovalStatuses() {
        return this.individualApprovalStatuses;
    }

    public Approver individualApprovalStatuses(Set<IndividualApprovalStatus> individualApprovalStatuses) {
        this.setIndividualApprovalStatuses(individualApprovalStatuses);
        return this;
    }

    public Approver addIndividualApprovalStatus(IndividualApprovalStatus individualApprovalStatus) {
        this.individualApprovalStatuses.add(individualApprovalStatus);
        individualApprovalStatus.setApprover(this);
        return this;
    }

    public Approver removeIndividualApprovalStatus(IndividualApprovalStatus individualApprovalStatus) {
        this.individualApprovalStatuses.remove(individualApprovalStatus);
        individualApprovalStatus.setApprover(null);
        return this;
    }

    public void setIndividualApprovalStatuses(Set<IndividualApprovalStatus> individualApprovalStatuses) {
        if (this.individualApprovalStatuses != null) {
            this.individualApprovalStatuses.forEach(i -> i.setApprover(null));
        }
        if (individualApprovalStatuses != null) {
            individualApprovalStatuses.forEach(i -> i.setApprover(this));
        }
        this.individualApprovalStatuses = individualApprovalStatuses;
    }

    public Set<ApprovalLevelStatus> getApprovalLevelStatuses() {
        return this.approvalLevelStatuses;
    }

    public Approver approvalLevelStatuses(Set<ApprovalLevelStatus> approvalLevelStatuses) {
        this.setApprovalLevelStatuses(approvalLevelStatuses);
        return this;
    }

    public Approver addApprovalLevelStatus(ApprovalLevelStatus approvalLevelStatus) {
        this.approvalLevelStatuses.add(approvalLevelStatus);
        approvalLevelStatus.setApprover(this);
        return this;
    }

    public Approver removeApprovalLevelStatus(ApprovalLevelStatus approvalLevelStatus) {
        this.approvalLevelStatuses.remove(approvalLevelStatus);
        approvalLevelStatus.setApprover(null);
        return this;
    }

    public void setApprovalLevelStatuses(Set<ApprovalLevelStatus> approvalLevelStatuses) {
        if (this.approvalLevelStatuses != null) {
            this.approvalLevelStatuses.forEach(i -> i.setApprover(null));
        }
        if (approvalLevelStatuses != null) {
            approvalLevelStatuses.forEach(i -> i.setApprover(this));
        }
        this.approvalLevelStatuses = approvalLevelStatuses;
    }

    public ApprovalRule getRule() {
        return this.rule;
    }

    public Approver rule(ApprovalRule approvalRule) {
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
        if (!(o instanceof Approver)) {
            return false;
        }
        return id != null && id.equals(((Approver) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Approver{" +
            "id=" + getId() +
            ", programUserId='" + getProgramUserId() + "'" +
            ", email='" + getEmail() + "'" +
            ", level=" + getLevel() +
            "}";
    }
}
