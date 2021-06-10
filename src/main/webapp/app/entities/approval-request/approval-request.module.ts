import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ApprovalRequestComponent } from './list/approval-request.component';
import { ApprovalRequestDetailComponent } from './detail/approval-request-detail.component';
import { ApprovalRequestUpdateComponent } from './update/approval-request-update.component';
import { ApprovalRequestDeleteDialogComponent } from './delete/approval-request-delete-dialog.component';
import { ApprovalRequestRoutingModule } from './route/approval-request-routing.module';

@NgModule({
  imports: [SharedModule, ApprovalRequestRoutingModule],
  declarations: [
    ApprovalRequestComponent,
    ApprovalRequestDetailComponent,
    ApprovalRequestUpdateComponent,
    ApprovalRequestDeleteDialogComponent,
  ],
  entryComponents: [ApprovalRequestDeleteDialogComponent],
})
export class ApprovalRequestModule {}
