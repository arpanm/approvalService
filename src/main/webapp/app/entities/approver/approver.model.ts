import { IIndividualApprovalStatus } from 'app/entities/individual-approval-status/individual-approval-status.model';
import { IApprovalLevelStatus } from 'app/entities/approval-level-status/approval-level-status.model';
import { IApprovalRule } from 'app/entities/approval-rule/approval-rule.model';

export interface IApprover {
  id?: number;
  programUserId?: string;
  email?: string;
  level?: number;
  individualApprovalStatuses?: IIndividualApprovalStatus[] | null;
  approvalLevelStatuses?: IApprovalLevelStatus[] | null;
  rule?: IApprovalRule | null;
}

export class Approver implements IApprover {
  constructor(
    public id?: number,
    public programUserId?: string,
    public email?: string,
    public level?: number,
    public individualApprovalStatuses?: IIndividualApprovalStatus[] | null,
    public approvalLevelStatuses?: IApprovalLevelStatus[] | null,
    public rule?: IApprovalRule | null
  ) {}
}

export function getApproverIdentifier(approver: IApprover): number | undefined {
  return approver.id;
}
