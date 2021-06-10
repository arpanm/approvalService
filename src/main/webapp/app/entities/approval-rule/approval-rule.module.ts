import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ApprovalRuleComponent } from './list/approval-rule.component';
import { ApprovalRuleDetailComponent } from './detail/approval-rule-detail.component';
import { ApprovalRuleUpdateComponent } from './update/approval-rule-update.component';
import { ApprovalRuleDeleteDialogComponent } from './delete/approval-rule-delete-dialog.component';
import { ApprovalRuleRoutingModule } from './route/approval-rule-routing.module';

@NgModule({
  imports: [SharedModule, ApprovalRuleRoutingModule],
  declarations: [ApprovalRuleComponent, ApprovalRuleDetailComponent, ApprovalRuleUpdateComponent, ApprovalRuleDeleteDialogComponent],
  entryComponents: [ApprovalRuleDeleteDialogComponent],
})
export class ApprovalRuleModule {}
