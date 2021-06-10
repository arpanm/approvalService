import { ISubRuleInListItem } from 'app/entities/sub-rule-in-list-item/sub-rule-in-list-item.model';
import { IApprovalRule } from 'app/entities/approval-rule/approval-rule.model';
import { DataType } from 'app/entities/enumerations/data-type.model';
import { Condition } from 'app/entities/enumerations/condition.model';
import { AppendType } from 'app/entities/enumerations/append-type.model';

export interface ISubRule {
  id?: number;
  fieldName?: string;
  dataType?: DataType;
  condition?: Condition;
  rangeMinValue?: number | null;
  rangeMaxValue?: number | null;
  equalStrValue?: string | null;
  equalDecValue?: number | null;
  appendType?: AppendType;
  subRuleInListItems?: ISubRuleInListItem[] | null;
  rule?: IApprovalRule | null;
}

export class SubRule implements ISubRule {
  constructor(
    public id?: number,
    public fieldName?: string,
    public dataType?: DataType,
    public condition?: Condition,
    public rangeMinValue?: number | null,
    public rangeMaxValue?: number | null,
    public equalStrValue?: string | null,
    public equalDecValue?: number | null,
    public appendType?: AppendType,
    public subRuleInListItems?: ISubRuleInListItem[] | null,
    public rule?: IApprovalRule | null
  ) {}
}

export function getSubRuleIdentifier(subRule: ISubRule): number | undefined {
  return subRule.id;
}
