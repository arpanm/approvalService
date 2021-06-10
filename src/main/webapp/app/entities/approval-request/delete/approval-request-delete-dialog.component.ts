import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IApprovalRequest } from '../approval-request.model';
import { ApprovalRequestService } from '../service/approval-request.service';

@Component({
  templateUrl: './approval-request-delete-dialog.component.html',
})
export class ApprovalRequestDeleteDialogComponent {
  approvalRequest?: IApprovalRequest;

  constructor(protected approvalRequestService: ApprovalRequestService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.approvalRequestService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
