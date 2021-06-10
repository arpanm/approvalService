import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { SubRuleInListItemComponent } from './list/sub-rule-in-list-item.component';
import { SubRuleInListItemDetailComponent } from './detail/sub-rule-in-list-item-detail.component';
import { SubRuleInListItemUpdateComponent } from './update/sub-rule-in-list-item-update.component';
import { SubRuleInListItemDeleteDialogComponent } from './delete/sub-rule-in-list-item-delete-dialog.component';
import { SubRuleInListItemRoutingModule } from './route/sub-rule-in-list-item-routing.module';

@NgModule({
  imports: [SharedModule, SubRuleInListItemRoutingModule],
  declarations: [
    SubRuleInListItemComponent,
    SubRuleInListItemDetailComponent,
    SubRuleInListItemUpdateComponent,
    SubRuleInListItemDeleteDialogComponent,
  ],
  entryComponents: [SubRuleInListItemDeleteDialogComponent],
})
export class SubRuleInListItemModule {}
