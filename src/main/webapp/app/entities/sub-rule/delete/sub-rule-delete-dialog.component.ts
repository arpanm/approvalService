import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISubRule } from '../sub-rule.model';
import { SubRuleService } from '../service/sub-rule.service';

@Component({
  templateUrl: './sub-rule-delete-dialog.component.html',
})
export class SubRuleDeleteDialogComponent {
  subRule?: ISubRule;

  constructor(protected subRuleService: SubRuleService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.subRuleService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
