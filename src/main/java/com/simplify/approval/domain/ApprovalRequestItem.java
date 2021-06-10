package com.simplify.approval.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.simplify.approval.domain.enumeration.DataType;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ApprovalRequestItem.
 */
@Entity
@Table(name = "approval_request_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ApprovalRequestItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "field_name", nullable = false)
    private String fieldName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "data_type", nullable = false)
    private DataType dataType;

    @Column(name = "str_value")
    private String strValue;

    @Column(name = "dec_value")
    private Float decValue;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "approvalRequestItems", "individualApprovalStatuses", "approvalLevelStatuses", "rule" },
        allowSetters = true
    )
    private ApprovalRequest request;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApprovalRequestItem id(Long id) {
        this.id = id;
        return this;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public ApprovalRequestItem fieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public DataType getDataType() {
        return this.dataType;
    }

    public ApprovalRequestItem dataType(DataType dataType) {
        this.dataType = dataType;
        return this;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public String getStrValue() {
        return this.strValue;
    }

    public ApprovalRequestItem strValue(String strValue) {
        this.strValue = strValue;
        return this;
    }

    public void setStrValue(String strValue) {
        this.strValue = strValue;
    }

    public Float getDecValue() {
        return this.decValue;
    }

    public ApprovalRequestItem decValue(Float decValue) {
        this.decValue = decValue;
        return this;
    }

    public void setDecValue(Float decValue) {
        this.decValue = decValue;
    }

    public ApprovalRequest getRequest() {
        return this.request;
    }

    public ApprovalRequestItem request(ApprovalRequest approvalRequest) {
        this.setRequest(approvalRequest);
        return this;
    }

    public void setRequest(ApprovalRequest approvalRequest) {
        this.request = approvalRequest;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApprovalRequestItem)) {
            return false;
        }
        return id != null && id.equals(((ApprovalRequestItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApprovalRequestItem{" +
            "id=" + getId() +
            ", fieldName='" + getFieldName() + "'" +
            ", dataType='" + getDataType() + "'" +
            ", strValue='" + getStrValue() + "'" +
            ", decValue=" + getDecValue() +
            "}";
    }
}
