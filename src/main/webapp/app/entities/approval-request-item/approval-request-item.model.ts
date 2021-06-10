import { IApprovalRequest } from 'app/entities/approval-request/approval-request.model';
import { DataType } from 'app/entities/enumerations/data-type.model';

export interface IApprovalRequestItem {
  id?: number;
  fieldName?: string;
  dataType?: DataType;
  strValue?: string | null;
  decValue?: number | null;
  request?: IApprovalRequest | null;
}

export class ApprovalRequestItem implements IApprovalRequestItem {
  constructor(
    public id?: number,
    public fieldName?: string,
    public dataType?: DataType,
    public strValue?: string | null,
    public decValue?: number | null,
    public request?: IApprovalRequest | null
  ) {}
}

export function getApprovalRequestItemIdentifier(approvalRequestItem: IApprovalRequestItem): number | undefined {
  return approvalRequestItem.id;
}
