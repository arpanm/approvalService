import * as dayjs from 'dayjs';
import { IApprovalRequestItem } from 'app/entities/approval-request-item/approval-request-item.model';
import { IIndividualApprovalStatus } from 'app/entities/individual-approval-status/individual-approval-status.model';
import { IApprovalLevelStatus } from 'app/entities/approval-level-status/approval-level-status.model';
import { IApprovalRule } from 'app/entities/approval-rule/approval-rule.model';
import { ApprovalType } from 'app/entities/enumerations/approval-type.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface IApprovalRequest {
  id?: number;
  programId?: string;
  type?: ApprovalType;
  approveCallBackUrl?: string | null;
  rejectCallBackUrl?: string | null;
  createdBy?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updatedBy?: string | null;
  updatedAt?: dayjs.Dayjs | null;
  finalStatus?: Status | null;
  approvalRequestItems?: IApprovalRequestItem[] | null;
  individualApprovalStatuses?: IIndividualApprovalStatus[] | null;
  approvalLevelStatuses?: IApprovalLevelStatus[] | null;
  rule?: IApprovalRule | null;
}

export class ApprovalRequest implements IApprovalRequest {
  constructor(
    public id?: number,
    public programId?: string,
    public type?: ApprovalType,
    public approveCallBackUrl?: string | null,
    public rejectCallBackUrl?: string | null,
    public createdBy?: string | null,
    public createdAt?: dayjs.Dayjs | null,
    public updatedBy?: string | null,
    public updatedAt?: dayjs.Dayjs | null,
    public finalStatus?: Status | null,
    public approvalRequestItems?: IApprovalRequestItem[] | null,
    public individualApprovalStatuses?: IIndividualApprovalStatus[] | null,
    public approvalLevelStatuses?: IApprovalLevelStatus[] | null,
    public rule?: IApprovalRule | null
  ) {}
}

export function getApprovalRequestIdentifier(approvalRequest: IApprovalRequest): number | undefined {
  return approvalRequest.id;
}
