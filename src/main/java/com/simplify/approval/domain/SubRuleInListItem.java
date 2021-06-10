package com.simplify.approval.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SubRuleInListItem.
 */
@Entity
@Table(name = "sub_rule_in_list_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SubRuleInListItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "str_item")
    private String strItem;

    @Column(name = "dec_item")
    private Float decItem;

    @ManyToOne
    @JsonIgnoreProperties(value = { "subRuleInListItems", "rule" }, allowSetters = true)
    private SubRule subRule;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SubRuleInListItem id(Long id) {
        this.id = id;
        return this;
    }

    public String getStrItem() {
        return this.strItem;
    }

    public SubRuleInListItem strItem(String strItem) {
        this.strItem = strItem;
        return this;
    }

    public void setStrItem(String strItem) {
        this.strItem = strItem;
    }

    public Float getDecItem() {
        return this.decItem;
    }

    public SubRuleInListItem decItem(Float decItem) {
        this.decItem = decItem;
        return this;
    }

    public void setDecItem(Float decItem) {
        this.decItem = decItem;
    }

    public SubRule getSubRule() {
        return this.subRule;
    }

    public SubRuleInListItem subRule(SubRule subRule) {
        this.setSubRule(subRule);
        return this;
    }

    public void setSubRule(SubRule subRule) {
        this.subRule = subRule;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubRuleInListItem)) {
            return false;
        }
        return id != null && id.equals(((SubRuleInListItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubRuleInListItem{" +
            "id=" + getId() +
            ", strItem='" + getStrItem() + "'" +
            ", decItem=" + getDecItem() +
            "}";
    }
}
