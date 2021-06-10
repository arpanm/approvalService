import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIndividualApprovalStatus } from '../individual-approval-status.model';
import { IndividualApprovalStatusService } from '../service/individual-approval-status.service';

@Component({
  templateUrl: './individual-approval-status-delete-dialog.component.html',
})
export class IndividualApprovalStatusDeleteDialogComponent {
  individualApprovalStatus?: IIndividualApprovalStatus;

  constructor(protected individualApprovalStatusService: IndividualApprovalStatusService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.individualApprovalStatusService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
