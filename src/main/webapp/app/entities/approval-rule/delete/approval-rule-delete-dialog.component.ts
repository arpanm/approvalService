import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IApprovalRule } from '../approval-rule.model';
import { ApprovalRuleService } from '../service/approval-rule.service';

@Component({
  templateUrl: './approval-rule-delete-dialog.component.html',
})
export class ApprovalRuleDeleteDialogComponent {
  approvalRule?: IApprovalRule;

  constructor(protected approvalRuleService: ApprovalRuleService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.approvalRuleService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
