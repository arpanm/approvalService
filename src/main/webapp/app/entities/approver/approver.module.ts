import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ApproverComponent } from './list/approver.component';
import { ApproverDetailComponent } from './detail/approver-detail.component';
import { ApproverUpdateComponent } from './update/approver-update.component';
import { ApproverDeleteDialogComponent } from './delete/approver-delete-dialog.component';
import { ApproverRoutingModule } from './route/approver-routing.module';

@NgModule({
  imports: [SharedModule, ApproverRoutingModule],
  declarations: [ApproverComponent, ApproverDetailComponent, ApproverUpdateComponent, ApproverDeleteDialogComponent],
  entryComponents: [ApproverDeleteDialogComponent],
})
export class ApproverModule {}
