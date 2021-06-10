import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ApprovalLevelStatusComponent } from './list/approval-level-status.component';
import { ApprovalLevelStatusDetailComponent } from './detail/approval-level-status-detail.component';
import { ApprovalLevelStatusUpdateComponent } from './update/approval-level-status-update.component';
import { ApprovalLevelStatusDeleteDialogComponent } from './delete/approval-level-status-delete-dialog.component';
import { ApprovalLevelStatusRoutingModule } from './route/approval-level-status-routing.module';

@NgModule({
  imports: [SharedModule, ApprovalLevelStatusRoutingModule],
  declarations: [
    ApprovalLevelStatusComponent,
    ApprovalLevelStatusDetailComponent,
    ApprovalLevelStatusUpdateComponent,
    ApprovalLevelStatusDeleteDialogComponent,
  ],
  entryComponents: [ApprovalLevelStatusDeleteDialogComponent],
})
export class ApprovalLevelStatusModule {}
