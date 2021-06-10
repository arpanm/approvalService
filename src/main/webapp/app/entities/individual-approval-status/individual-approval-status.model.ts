import * as dayjs from 'dayjs';
import { IApprovalRequest } from 'app/entities/approval-request/approval-request.model';
import { IApprover } from 'app/entities/approver/approver.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface IIndividualApprovalStatus {
  id?: number;
  status?: Status;
  clientTime?: string;
  createdBy?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updatedBy?: string | null;
  updatedAt?: dayjs.Dayjs | null;
  request?: IApprovalRequest | null;
  approver?: IApprover | null;
}

export class IndividualApprovalStatus implements IIndividualApprovalStatus {
  constructor(
    public id?: number,
    public status?: Status,
    public clientTime?: string,
    public createdBy?: string | null,
    public createdAt?: dayjs.Dayjs | null,
    public updatedBy?: string | null,
    public updatedAt?: dayjs.Dayjs | null,
    public request?: IApprovalRequest | null,
    public approver?: IApprover | null
  ) {}
}

export function getIndividualApprovalStatusIdentifier(individualApprovalStatus: IIndividualApprovalStatus): number | undefined {
  return individualApprovalStatus.id;
}
