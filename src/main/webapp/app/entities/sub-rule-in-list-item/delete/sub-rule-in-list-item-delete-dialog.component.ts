import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISubRuleInListItem } from '../sub-rule-in-list-item.model';
import { SubRuleInListItemService } from '../service/sub-rule-in-list-item.service';

@Component({
  templateUrl: './sub-rule-in-list-item-delete-dialog.component.html',
})
export class SubRuleInListItemDeleteDialogComponent {
  subRuleInListItem?: ISubRuleInListItem;

  constructor(protected subRuleInListItemService: SubRuleInListItemService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.subRuleInListItemService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
