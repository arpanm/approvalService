import * as dayjs from 'dayjs';
import { ISubRule } from 'app/entities/sub-rule/sub-rule.model';
import { IApprover } from 'app/entities/approver/approver.model';
import { IApprovalRequest } from 'app/entities/approval-request/approval-request.model';
import { ApprovalType } from 'app/entities/enumerations/approval-type.model';

export interface IApprovalRule {
  id?: number;
  programId?: string;
  type?: ApprovalType;
  createdBy?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updatedBy?: string | null;
  updatedAt?: dayjs.Dayjs | null;
  subRules?: ISubRule[] | null;
  approvers?: IApprover[] | null;
  approvalRequests?: IApprovalRequest[] | null;
}

export class ApprovalRule implements IApprovalRule {
  constructor(
    public id?: number,
    public programId?: string,
    public type?: ApprovalType,
    public createdBy?: string | null,
    public createdAt?: dayjs.Dayjs | null,
    public updatedBy?: string | null,
    public updatedAt?: dayjs.Dayjs | null,
    public subRules?: ISubRule[] | null,
    public approvers?: IApprover[] | null,
    public approvalRequests?: IApprovalRequest[] | null
  ) {}
}

export function getApprovalRuleIdentifier(approvalRule: IApprovalRule): number | undefined {
  return approvalRule.id;
}
