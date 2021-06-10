import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IApprovalLevelStatus } from '../approval-level-status.model';
import { ApprovalLevelStatusService } from '../service/approval-level-status.service';

@Component({
  templateUrl: './approval-level-status-delete-dialog.component.html',
})
export class ApprovalLevelStatusDeleteDialogComponent {
  approvalLevelStatus?: IApprovalLevelStatus;

  constructor(protected approvalLevelStatusService: ApprovalLevelStatusService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.approvalLevelStatusService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
