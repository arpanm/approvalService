import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ApprovalRequestItemComponent } from './list/approval-request-item.component';
import { ApprovalRequestItemDetailComponent } from './detail/approval-request-item-detail.component';
import { ApprovalRequestItemUpdateComponent } from './update/approval-request-item-update.component';
import { ApprovalRequestItemDeleteDialogComponent } from './delete/approval-request-item-delete-dialog.component';
import { ApprovalRequestItemRoutingModule } from './route/approval-request-item-routing.module';

@NgModule({
  imports: [SharedModule, ApprovalRequestItemRoutingModule],
  declarations: [
    ApprovalRequestItemComponent,
    ApprovalRequestItemDetailComponent,
    ApprovalRequestItemUpdateComponent,
    ApprovalRequestItemDeleteDialogComponent,
  ],
  entryComponents: [ApprovalRequestItemDeleteDialogComponent],
})
export class ApprovalRequestItemModule {}
