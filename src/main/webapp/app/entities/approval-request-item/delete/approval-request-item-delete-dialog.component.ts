import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IApprovalRequestItem } from '../approval-request-item.model';
import { ApprovalRequestItemService } from '../service/approval-request-item.service';

@Component({
  templateUrl: './approval-request-item-delete-dialog.component.html',
})
export class ApprovalRequestItemDeleteDialogComponent {
  approvalRequestItem?: IApprovalRequestItem;

  constructor(protected approvalRequestItemService: ApprovalRequestItemService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.approvalRequestItemService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
