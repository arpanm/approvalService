import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { IndividualApprovalStatusComponent } from './list/individual-approval-status.component';
import { IndividualApprovalStatusDetailComponent } from './detail/individual-approval-status-detail.component';
import { IndividualApprovalStatusUpdateComponent } from './update/individual-approval-status-update.component';
import { IndividualApprovalStatusDeleteDialogComponent } from './delete/individual-approval-status-delete-dialog.component';
import { IndividualApprovalStatusRoutingModule } from './route/individual-approval-status-routing.module';

@NgModule({
  imports: [SharedModule, IndividualApprovalStatusRoutingModule],
  declarations: [
    IndividualApprovalStatusComponent,
    IndividualApprovalStatusDetailComponent,
    IndividualApprovalStatusUpdateComponent,
    IndividualApprovalStatusDeleteDialogComponent,
  ],
  entryComponents: [IndividualApprovalStatusDeleteDialogComponent],
})
export class IndividualApprovalStatusModule {}
