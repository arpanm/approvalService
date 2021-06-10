package com.simplify.approval.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.simplify.approval.domain.enumeration.AppendType;
import com.simplify.approval.domain.enumeration.Condition;
import com.simplify.approval.domain.enumeration.DataType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SubRule.
 */
@Entity
@Table(name = "sub_rule")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SubRule implements Serializable {

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

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_condition", nullable = false)
    private Condition condition;

    @Column(name = "range_min_value")
    private Float rangeMinValue;

    @Column(name = "range_max_value")
    private Float rangeMaxValue;

    @Column(name = "equal_str_value")
    private String equalStrValue;

    @Column(name = "equal_dec_value")
    private Float equalDecValue;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "append_type", nullable = false)
    private AppendType appendType;

    @OneToMany(mappedBy = "subRule")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "subRule" }, allowSetters = true)
    private Set<SubRuleInListItem> subRuleInListItems = new HashSet<>();

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

    public SubRule id(Long id) {
        this.id = id;
        return this;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public SubRule fieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public DataType getDataType() {
        return this.dataType;
    }

    public SubRule dataType(DataType dataType) {
        this.dataType = dataType;
        return this;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public Condition getCondition() {
        return this.condition;
    }

    public SubRule condition(Condition condition) {
        this.condition = condition;
        return this;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Float getRangeMinValue() {
        return this.rangeMinValue;
    }

    public SubRule rangeMinValue(Float rangeMinValue) {
        this.rangeMinValue = rangeMinValue;
        return this;
    }

    public void setRangeMinValue(Float rangeMinValue) {
        this.rangeMinValue = rangeMinValue;
    }

    public Float getRangeMaxValue() {
        return this.rangeMaxValue;
    }

    public SubRule rangeMaxValue(Float rangeMaxValue) {
        this.rangeMaxValue = rangeMaxValue;
        return this;
    }

    public void setRangeMaxValue(Float rangeMaxValue) {
        this.rangeMaxValue = rangeMaxValue;
    }

    public String getEqualStrValue() {
        return this.equalStrValue;
    }

    public SubRule equalStrValue(String equalStrValue) {
        this.equalStrValue = equalStrValue;
        return this;
    }

    public void setEqualStrValue(String equalStrValue) {
        this.equalStrValue = equalStrValue;
    }

    public Float getEqualDecValue() {
        return this.equalDecValue;
    }

    public SubRule equalDecValue(Float equalDecValue) {
        this.equalDecValue = equalDecValue;
        return this;
    }

    public void setEqualDecValue(Float equalDecValue) {
        this.equalDecValue = equalDecValue;
    }

    public AppendType getAppendType() {
        return this.appendType;
    }

    public SubRule appendType(AppendType appendType) {
        this.appendType = appendType;
        return this;
    }

    public void setAppendType(AppendType appendType) {
        this.appendType = appendType;
    }

    public Set<SubRuleInListItem> getSubRuleInListItems() {
        return this.subRuleInListItems;
    }

    public SubRule subRuleInListItems(Set<SubRuleInListItem> subRuleInListItems) {
        this.setSubRuleInListItems(subRuleInListItems);
        return this;
    }

    public SubRule addSubRuleInListItem(SubRuleInListItem subRuleInListItem) {
        this.subRuleInListItems.add(subRuleInListItem);
        subRuleInListItem.setSubRule(this);
        return this;
    }

    public SubRule removeSubRuleInListItem(SubRuleInListItem subRuleInListItem) {
        this.subRuleInListItems.remove(subRuleInListItem);
        subRuleInListItem.setSubRule(null);
        return this;
    }

    public void setSubRuleInListItems(Set<SubRuleInListItem> subRuleInListItems) {
        if (this.subRuleInListItems != null) {
            this.subRuleInListItems.forEach(i -> i.setSubRule(null));
        }
        if (subRuleInListItems != null) {
            subRuleInListItems.forEach(i -> i.setSubRule(this));
        }
        this.subRuleInListItems = subRuleInListItems;
    }

    public ApprovalRule getRule() {
        return this.rule;
    }

    public SubRule rule(ApprovalRule approvalRule) {
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
        if (!(o instanceof SubRule)) {
            return false;
        }
        return id != null && id.equals(((SubRule) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubRule{" +
            "id=" + getId() +
            ", fieldName='" + getFieldName() + "'" +
            ", dataType='" + getDataType() + "'" +
            ", condition='" + getCondition() + "'" +
            ", rangeMinValue=" + getRangeMinValue() +
            ", rangeMaxValue=" + getRangeMaxValue() +
            ", equalStrValue='" + getEqualStrValue() + "'" +
            ", equalDecValue=" + getEqualDecValue() +
            ", appendType='" + getAppendType() + "'" +
            "}";
    }
}
