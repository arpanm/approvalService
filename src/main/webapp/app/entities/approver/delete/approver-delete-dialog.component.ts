import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IApprover } from '../approver.model';
import { ApproverService } from '../service/approver.service';

@Component({
  templateUrl: './approver-delete-dialog.component.html',
})
export class ApproverDeleteDialogComponent {
  approver?: IApprover;

  constructor(protected approverService: ApproverService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.approverService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
