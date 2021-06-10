import { ISubRule } from 'app/entities/sub-rule/sub-rule.model';

export interface ISubRuleInListItem {
  id?: number;
  strItem?: string | null;
  decItem?: number | null;
  subRule?: ISubRule | null;
}

export class SubRuleInListItem implements ISubRuleInListItem {
  constructor(public id?: number, public strItem?: string | null, public decItem?: number | null, public subRule?: ISubRule | null) {}
}

export function getSubRuleInListItemIdentifier(subRuleInListItem: ISubRuleInListItem): number | undefined {
  return subRuleInListItem.id;
}
