import * as dayjs from 'dayjs';
import { IApprovalRequest } from 'app/entities/approval-request/approval-request.model';
import { IApprover } from 'app/entities/approver/approver.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface IApprovalLevelStatus {
  id?: number;
  status?: Status;
  level?: number;
  clientTime?: string;
  createdBy?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updatedBy?: string | null;
  updatedAt?: dayjs.Dayjs | null;
  request?: IApprovalRequest | null;
  approver?: IApprover | null;
}

export class ApprovalLevelStatus implements IApprovalLevelStatus {
  constructor(
    public id?: number,
    public status?: Status,
    public level?: number,
    public clientTime?: string,
    public createdBy?: string | null,
    public createdAt?: dayjs.Dayjs | null,
    public updatedBy?: string | null,
    public updatedAt?: dayjs.Dayjs | null,
    public request?: IApprovalRequest | null,
    public approver?: IApprover | null
  ) {}
}

export function getApprovalLevelStatusIdentifier(approvalLevelStatus: IApprovalLevelStatus): number | undefined {
  return approvalLevelStatus.id;
}
